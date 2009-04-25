package com.ericdaugherty.itunesexport.formatter


import java.io.{File, PrintWriter}
import parser.Playlist

/**
 * Formats a given playlist as an m3u playlist file
 *
 * @author Eric Daugherty
 */
class M3UFormatter extends Formatter {

  def writePlaylist(directory: String, playlist: Playlist) {

    // Write out each track using a PrintWriter
    withPrintWriter(new File(directory, parseFileName(playlist) + ".m3u")) { writer =>
      playlist.tracks.foreach(track => writer.println(parseLocation(track)))      
    }
  }
}