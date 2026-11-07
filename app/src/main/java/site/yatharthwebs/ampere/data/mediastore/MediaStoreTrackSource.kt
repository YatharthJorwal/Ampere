package site.yatharthwebs.ampere.data.mediastore

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import site.yatharthwebs.ampere.domain.Track
import site.yatharthwebs.ampere.domain.TrackSource

/** Reads music straight from MediaStore. Room-backed caching arrives with playlists (docs/DECISIONS.md ADR-0022). */
class MediaStoreTrackSource(
    context: Context,
) : TrackSource {
    private val appContext = context.applicationContext

    override suspend fun loadTracks(): List<Track> =
        withContext(Dispatchers.IO) {
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            val projection =
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.TRACK,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.MIME_TYPE,
                )
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} > 0"
            val tracks = ArrayList<Track>()
            appContext.contentResolver.query(collection, projection, selection, null, null)?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                val addedCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val displayName = cursor.getString(nameCol).orEmpty().substringBeforeLast('.')
                    val title = cursor.getString(titleCol).orEmpty().ifBlank { displayName }
                    tracks +=
                        Track(
                            id = id,
                            uri = ContentUris.withAppendedId(collection, id).toString(),
                            title = title,
                            artist = knownOrEmpty(cursor.getString(artistCol)),
                            album = knownOrEmpty(cursor.getString(albumCol)),
                            durationMs = cursor.getLong(durationCol),
                            // MediaStore packs disc and track as disc * 1000 + track.
                            trackNumber = cursor.getInt(trackCol) % 1000,
                            dateAddedSec = cursor.getLong(addedCol),
                            mimeType = cursor.getString(mimeCol).orEmpty(),
                        )
                }
            }
            tracks
        }

    private fun knownOrEmpty(value: String?): String =
        if (value == null ||
            value == MediaStore.UNKNOWN_STRING
        ) {
            ""
        } else {
            value
        }
}
