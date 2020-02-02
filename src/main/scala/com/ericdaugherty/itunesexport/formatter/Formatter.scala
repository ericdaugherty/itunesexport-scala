package com.ericdaugherty.itunesexport.formatter

import java.io.{File, FileInputStream, FileOutputStream, PrintWriter}
import java.nio.channels.FileChannel

import Formatter._
import java.net.URLDecoder

import com.ericdaugherty.itunesexport.parser.{Playlist, Track}

object Formatter {

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

  /**
   *  Performs a URL Decode to convert %xx into characters.  Many of these are illegal on many file systems
   * but they are all here for completeness and to handle any systems that do have them as legal characters
   */
  private def decodeUrl(url: String) = {
    // iTunes does not actually URL Encode the + char, so when URLDecoder decodes it,
    // it is replaced with a space.  Replace all + with encoded versions before decoding.
    URLDecoder.decode(url.replaceAll("\\+", "%2b"), "UTF-8")
  }
}

/**
 * Trait containing common interface and methods for all Playlist Formatters
 *
 * @author Eric Daugherty
 */
abstract class Formatter(settings: FormatterSettings) {

  /** Indicates whether the locationPrefix needs to be parsed */
  val replacePrefix  = !(parseLocation(settings.musicPath) == parseLocation(settings.musicPathOld))
  val parsedMusicPathOld = parseLocation(settings.musicPathOld)

  var fileIndex:Int = 0;
  
  /** Removes non-alphanumeric (A-Z 0-9) characters from playlist names and replaces with underscore (_) */
  def parseFileName(playlist: Playlist) = playlist.name.replaceAll("""[^\s\w\-\.\[\]=$&{}\xc0-\xff]""", "_")

  /** Converts the default track location to a system specific and cleaned version */
  def parseTrack(track: Track) : String = {
    val location = parseLocation(track.location)
    if(replacePrefix) location.replace(parsedMusicPathOld, settings.musicPath) else location
  }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern.  Defautls to UTF-8 encoding. */
  def withPrintWriter(file: File, settings:FormatterSettings)(func: PrintWriter => Unit) { withPrintWriter(file, settings, "UTF8")(func) }

  /** Helper method to enable save usage of PrintWiter.  Loan Pattern */
  def withPrintWriter(file: File, settings:FormatterSettings, encoding: String)(func: PrintWriter => Unit) {

    val writer = new PrintWriter(file, encoding)
    try {
      if(settings.includeUTFBOM) {
        writer.write(239)
        writer.write(187)
        writer.write(191)
      }
      func(writer)
    }
    finally {
      writer.close()
    }
  }

  def exportPlaylist(playlist:Playlist) : Unit = {

    // Reset the file index if we are doing playlist file copy.
    if(settings.copy.equals("PLAYLIST")) fileIndex = 0;

    writePlaylist(playlist)
  }

  def writePlaylist(playlist:Playlist) : Unit

  def filterTracks(tracks:Seq[Track], settings:FormatterSettings) : Seq[Track] = {
    tracks.filter(track => includeTrack(track, settings))
  }

  def includeTrack(track:Track, settings:FormatterSettings) : Boolean = {
    // Exclude songs that are disabled (unchecked) unless the includeUnchecked override is set.
    if(settings.includeDisabled || !track.disabled)
    {
      // Based on the file type determine if this song should be included.
      settings.fileType match {
//        case "MP3" => track.fileType == "MPEG audio file" || track.fileType == "MPEG-Audiodatei"
        case "MP3" => track.location.substring(track.location.length -4 ) == ".mp3"
        case "MP3M4A" => !track.protectedTrack
        case "ALL" => true
        case _ => true
      }
    }
    else false
  }

  def copyFiles(track:Track, playlist:Playlist) : String = {
    // Quick return if 'NONE'
    // TODO: Find faster solution for this.  Should be the primary use case.
    if(settings.copy == "NONE") return parseTrack(track).replace(File.separator, settings.separator);

    val sourceFile = new File(parseTrack(track))
    val filePrefix = if((settings.copy.equals("PLAYLIST") || settings.copy.equals("FLAT")) && settings.addIndex) {
      fileIndex = fileIndex + 1;
      String.format("%05d-", int2Integer(fileIndex))
    } else ""

    val targetFile : File = settings.copy match {
      case "PLAYLIST" => new File(new File(settings.outputDirectory, playlist.name), filePrefix + sourceFile.getName)
      case "ITUNES" => new File(settings.outputDirectory, parseTrack(track).replace(parseLocation(settings.musicPathOld), ""))
      case "FLAT" => new File(settings.outputDirectory, filePrefix + sourceFile.getName)
    }

    if(!targetFile.exists)
    {
      copy(sourceFile, targetFile)
    }

    targetFile.getPath.replace(settings.outputDirectory.getPath + File.separator, "").replace(File.separator, settings.separator);
  }

  /**
   *  Copy a file.
   */
  private def copy(source:File, destination:File) : Unit = {

    destination.getParentFile.mkdirs

    var in : FileChannel = null;
    var out : FileChannel = null;
    try
    {
      in = new FileInputStream(source).getChannel();
      out = new FileOutputStream(destination).getChannel();
      in.transferTo(0, in.size(), out);
    }
    finally {
      if (in != null)
        in.close();
      if (out != null)
        out.close();
    }
  }
}