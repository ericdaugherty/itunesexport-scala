package com.ericdaugherty.itunesexport.parser

import java.io.File

import xml.{Node,XML}

import com.ericdaugherty.itunesexport.formatter.Formatter._


/**
 * Helper Object to handle overloaded constructor logic.
 *
 * @author Eric Daugherty
 */
object Library {

  // Hack to prevent DTD lookups.
  System.setProperty("javax.xml.parsers.SAXParserFactory", "com.ericdaugherty.itunesexport.NonDTDSaxParserFactory")
  
  val defaultLocation = System.getProperty("user.home") + "/Music/iTunes/iTunes Music Library.xml"
}

/**
 * Parses an iTunes Music Library.xml file into an in-memory represntation of
 * the Playlists and individual tracks.
 *
 * @author Eric Daugherty
 */
class Library(libraryXml: Node) extends PListParser {

  /** Constructor that takes a file path */
  def this(libraryXmlFilePath: String) = this(XML.loadFile(libraryXmlFilePath))

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
  val musicFolder: String = parseMusicFolder(parseLocation(getPListString("Music Folder")))
  val libraryPersistentId: String = getPListString("Library Persistent ID")

  //TODO: DO we need the tracks Array at all or should it just be a Map?  Parse it as a Map and then create a helper map.Values val or def
  /** The tracks contained in the library */
  val tracks: Seq[Track] = for(node <- libraryXml \ "dict" \ "dict" \ "dict") yield new Track(node)
  /** A Map of the Track IDs to the Track instance. */
  val trackMap: Map[Double,Track] = buildTrackMap

  /** The plyalists contained in the library */
  val playlists: Seq[Playlist] = for(node <- libraryXml \ "dict" \ "array" \ "dict") yield new Playlist(node, trackMap)
  
  /** Determines whether to use the iTunes 9+ or older musicFolder */
  private def parseMusicFolder(musicFolder:String) : String = {
    val newPath = new File(musicFolder, "Music");
    if(newPath.exists) newPath.getAbsolutePath + File.separator
    else musicFolder
  }
  
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
}