package com.ericdaugherty.itunesexport.formatter

import java.io.File
import java.text.MessageFormat.format
import parser.Playlist

/**
 * Generates MPL playlist files.
 *
 * @author Eric Daugherty
 */
class MPLFormatter(settings: FormatterSettings) extends Formatter(settings) {

  def writePlaylist(playlist: Playlist) {
    // Write out each track using a PrintWriter
    withPrintWriter(new File(settings.outputDirectory, parseFileName(playlist) + ".mpl"), settings) {
      writer => filterTracks(playlist.tracks,settings).foreach(track => writer.println(format("{0} - {1}|{2}", track.artist, track.name, copyFiles(track, playlist))));
    }
  }
}