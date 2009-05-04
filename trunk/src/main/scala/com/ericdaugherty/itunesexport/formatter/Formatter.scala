package com.ericdaugherty.itunesexport.formatter

import java.io.{PrintWriter, File}
import parser.{Track, Playlist}

/**
 * Trait containing common interface and methods for all Playlist Formatters
 *
 * @author Eric Daugherty
 */
abstract class Formatter(settings: FormatterSettings) {

  /** Indicates whether the locationPrefix needs to be parsed */
  val replacePrefix  = !(parseLocation(settings.musicPath) == parseLocation(settings.musicPathOld))
  val parsedMusicPathOld = parseLocation(settings.musicPathOld)

  /** Converts the directory and playlist name into a valid file name */
  def parseFileName(playlist: Playlist ) = playlist.name.replaceAll("""[:<>|/\\\?\*\"]""", "_")

  /** Converts the default track location to a system specific and cleaned version */
  def parseLocation(track: Track) : String = {
    val location = parseLocation(track.location)
    if(replacePrefix) location.replace(parsedMusicPathOld, settings.musicPath) else location
  }

  /** Overloaded parseLocation that handles all logic other than replacing the prefix */
  def parseLocation(location: String) : String = {
    // First handle UNC paths, then normal paths, pass results to decodeUrl
    decodeUrl(
      (if(location.contains("localhost")) {
        (if(location(18) == ':') location.replaceAll("file://localhost/", "")
        else location.replaceAll("file://localhost", ""))
      }
      else location).replace('/', File.separatorChar)
    )
  }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern.  Defautls to UTF-8 encoding. */
  def withPrintWriter(file: File)(func: PrintWriter => Unit) { withPrintWriter(file, "UTF8")(func) }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern */
  def withPrintWriter(file: File, encoding: String)(func: PrintWriter => Unit) {

    val writer = new PrintWriter(file, encoding)
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