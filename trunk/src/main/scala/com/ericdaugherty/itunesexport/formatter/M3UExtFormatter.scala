package com.ericdaugherty.itunesexport.formatter

import java.io.File
import java.text.MessageFormat.format

import parser.Playlist

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
class M3UExtFormatter extends Formatter {

  def writePlaylist(directory: String, playlist: Playlist) {
    // Write out each track using a PrintWriter
    withPrintWriter(new File(directory, parseFileName(playlist) + ".m3u")) { writer =>
      writer.println("#EXTM3U")

      playlist.tracks.foreach(track => {
        writer.println(format("#EXTINF:{0},{1} - {2}", track.timeInSeconds.toString, track.artist, track.name))
        writer.println(parseLocation(track))
      })
    }
  }
}