package com.ericdaugherty.itunesexport.parser

import java.io.File

import io.Source
import xml.{Node,XML}


/**
 * Helper Object to handle overloaded constructor logic.
 *
 * @author Eric Daugherty
 */
object Library {

  val defaultLocation = System.getProperty("user.home") + "/Music/iTunes/iTunes Music Library.xml"

  /**
   * Parser method to load the iTunes XML file into a Node.  This is neccessary
   * because we need to strip out the Schema to avoid the external lookup.  This is a HACK.
   * TODO: Disable validation on XML loadFile method
   */
  def parsePListXML(fileName: String) : Node = {
    val builder = new StringBuilder
    Source.fromFile(fileName).foreach(line => builder += line)
    XML.loadString(builder.toString.replace("""<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.example.com/DTDs/PropertyList-1.0.dtd">""", ""))
  }
}

/**
 * Parses an iTunes Music Library.xml file into an in-memory represntation of
 * the Playlists and individual tracks.
 *
 * @author Eric Daugherty
 */
class Library(libraryXml: Node) extends PListParser {

  /** Constructor that takes a file path */
  def this(libraryXmlFilePath: String) = this(Library.parsePListXML(libraryXmlFilePath))

  /** Constructor that takes a file */
  def this(libraryXmlFile: File) = this(libraryXmlFile.getPath)

  // Parse the XML into a pListMap
  private val pListMap : Map[String,Any] = parsePList((libraryXml \ "dict")(0))

  // Provide concrete impl for PListParser helper method
  def getPListValue(key: String, default: Any): Any = getPListValue(pListMap, key, default)

  val majorVersion: Double = getPListDouble("Major Version")
  val minorVersion: Double = getPListDouble("Minor Version")
  val applicationVersion: String = getPListString("Application Version")
  val features: Double = getPListDouble("Features")
  val showContentRating: Boolean = getPListBoolean("Show Content Ratings")
  val musicFolder: String = getPListString("Music Folder")
  val libraryPersistentId: String = getPListString("Library Persistent ID")

  //TODO: DO we need the tracks Array at all or should it just be a Map?  Parse it as a Map and then create a helper map.Values val or def
  /** The tracks contained in the library */
  val tracks: Seq[Track] = for(node <- libraryXml \ "dict" \ "dict" \ "dict") yield new Track(node)
  /** A Map of the Track IDs to the Track instance. */
  val trackMap: Map[Double,Track] = buildTrackMap

  /** The plyalists contained in the library */
  val playlists: Seq[Playlist] = for(node <- libraryXml \ "dict" \ "array" \ "dict") yield new Playlist(node, trackMap)

  /** Creates a Track Map from the Track Array for faster searching */
  private def buildTrackMap = {
    val tempTrackMap = new collection.mutable.HashMap[Double, Track]
    // Add each track to the HashMap
    tracks.foreach(track =>  tempTrackMap += (track.trackId -> track))

    // Convert to Immutable map and return
    Map() ++ tempTrackMap
  }

  override def toString = {
    "Major Version: " + majorVersion + "\n" +
            "Minor Version: " + minorVersion + "\n" +
            "Application Version: " + applicationVersion + "\n" +
            "Features: " + features + "\n" +
            "Show Content Rating: " + showContentRating + "\n" +
            "Music Folder: " + musicFolder + "\n" +
            "Library Persistent ID: " + libraryPersistentId + "\n" +
            "Track Count: " + tracks.length + "\n" +
            "Playlist Count: " + playlists.length + "\n" +        
            "Track #1: " + "\n" +
            tracks(0) + "\n" +
            "Playlist #1: " + "\n" +
            playlists(0)

  }

  /** Parse the Music Folder element out of the XML and remove the file://localhost/ prefix */
// Replaced by PListParser Trait.
//  private def parseMusicFolder = {
//    val musicFolderNode = (libraryXml \ "dict" \ "string").filter(node => node.text.startsWith("file:"))
//
//    musicFolderNode.text
//    // This should happen at consumption.  Will leave data read as pure as possible
////    val rawMusicFolder = musicFolderNode.text.replaceFirst("file://localhost/", "")
//
//    // TODO: Do we need to do a URLDecode?
////    if (rawMusicFolder.startsWith("/")) "/" + rawMusicFolder else rawMusicFolder
//  }

}