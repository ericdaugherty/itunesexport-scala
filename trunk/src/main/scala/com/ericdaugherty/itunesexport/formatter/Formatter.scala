package com.ericdaugherty.itunesexport.formatter

import java.io.{PrintWriter, File}
import parser.{Track, Playlist}

/**
 * Trait containing common interface and methods for all Playlist Formatters
 *
 * @author Eric Daugherty
 */
trait Formatter {

  /** Converts the directory and playlist name into a valid file name */
  def parseFileName(playlist: Playlist ) = playlist.name.replaceAll("""[:<>|/\\\?\*\"]""", "_")

  /** Converts the default track location to a system specific and cleaned version */
  def parseLocation(track: Track) = {
    val location = track.location
    // First handle UNC paths, then normal paths, pass results to decodeUrl
    decodeUrl(
      (if(location(18) == ':') location.replaceAll("file://localhost/", "")
      else location.replaceAll("file://localhost", "")).replace('/', File.separatorChar)
    )
  }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern.  Defautls to UTF-8 encoding. */
  def withPrintWriter(file: File)(op: PrintWriter => Unit) { withPrintWriter(file, "UTF-8")(op) }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern */
  def withPrintWriter(file: File, encoding: String)(func: PrintWriter => Unit) {

    val writer = new PrintWriter(file, "UTF-8")
    try {
      func(writer)
    }
    finally {
      writer.close()
    }
  }

  def writePlaylist(directory: String, playlist:Playlist) : Unit

  /**
   * Performs a URL Decode to convert %xx into characters.  Many of these are illegal on many file systems
   * but they are all here for completeness and to handle any systems that do have them as legal characters
   */
  private def decodeUrl(url: String) = {
    url.replaceAll("%20", " ")
      .replaceAll("%3C", "<")
      .replaceAll("%3E", ">")
      .replaceAll("%23", "#")
      .replaceAll("%25", "%")
      .replaceAll("%7B", "{")
      .replaceAll("%7D", "}")
      .replaceAll("%7C", "")
      .replaceAll("%5C", "\\")
      .replaceAll("%5E", "^")
      .replaceAll("%7E", "~")
      .replaceAll("%5B", "[")
      .replaceAll("%5D", "]")
      .replaceAll("%60", "`")
      .replaceAll("%3B", ";")
      .replaceAll("%2F", "/")
      .replaceAll("%3F", "?")
      .replaceAll("%3A", ":")
      .replaceAll("%40", "@")
      .replaceAll("%3D", "=")
      .replaceAll("%26", "&")
      .replaceAll("%24", "$")
  }
}