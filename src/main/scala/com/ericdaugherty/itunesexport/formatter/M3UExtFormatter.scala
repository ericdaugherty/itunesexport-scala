package com.ericdaugherty.itunesexport.formatter

import java.io.{File, PrintWriter}
import java.text.MessageFormat.format

import com.ericdaugherty.itunesexport.parser.Playlist


/**
 * Formats a given playlist as an m3u playlist file.
 *
 * M3UExt Files are described here:
 * http://hanna.pyxidis.org/tech/m3u.html
 * http://en.wikipedia.org/wiki/M3U
 * http://www.assistanttools.com/articles/m3u_playlist_format.shtml
 *
 * @author Eric Daugherty
 */
class M3UExtFormatter(settings: FormatterSettings) extends Formatter(settings) {

  def writePlaylist(playlist: Playlist) {
    // Write out each track using a PrintWriter
    val extension = if(settings.useM3U8) ".m3u8" else ".m3u"
    withPrintWriter(new File(settings.outputDirectory, parseFileName(playlist) + extension), settings) { writer : PrintWriter =>
      writer.println("#EXTM3U")

      filterTracks(playlist.tracks, settings).foreach(track => {
        writer.println(format("#EXTINF:{0},{1} - {2}", track.timeInSeconds.toString, track.artist, track.name))
        writer.println(copyFiles(track, playlist))
      })
    }
  }
}