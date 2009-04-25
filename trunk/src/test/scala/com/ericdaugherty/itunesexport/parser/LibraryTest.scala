package com.ericdaugherty.itunesexport.parser

import scala.xml.XML.loadString

import org.scalatest.FunSuite

/**
 * Test class for Library
 *
 * @author Eric Daugherty
 */
class LibraryTest extends FunSuite {

  test("Library XML Parsing") {
    val library = new Library(libraryXml1)

    assert(library.majorVersion === 1)
    assert(library.minorVersion === 1)
    assert(library.applicationVersion === "8.1.1")
    assert(library.features === 5)
    assert(library.showContentRating === true)
    assert(library.musicFolder === "file://localhost/M:/Music/")
    assert(library.libraryPersistentId === "405C983B39AD456D")
    assert(library.tracks.length === 2)
    assert(library.tracks(0).trackId === 1589)
    assert(library.tracks(0).name === "Caught Up In You")
    assert(library.trackMap(1589).name === "Caught Up In You")
    assert(library.trackMap(1590).name === "Hey Ya")
  }

  //***************************************************************************
  // Test Constants
  //***************************************************************************

  val libraryXml1 = loadString("""<?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
    <plist version="1.0">
    <dict>
      <key>Major Version</key><integer>1</integer>
      <key>Minor Version</key><integer>1</integer>
      <key>Application Version</key><string>8.1.1</string>
      <key>Features</key><integer>5</integer>
      <key>Show Content Ratings</key><true/>
      <key>Music Folder</key><string>file://localhost/M:/Music/</string>
      <key>Library Persistent ID</key><string>405C983B39AD456D</string>
      <key>Tracks</key>
      <dict>
        <key>1589</key>
        <dict>
          <key>Track ID</key><integer>1589</integer>
          <key>Name</key><string>Caught Up In You</string>
          <key>Artist</key><string>38 Special</string>
          <key>Genre</key><string>80's</string>
          <key>Kind</key><string>MPEG audio file</string>
          <key>Size</key><integer>5521408</integer>
          <key>Total Time</key><integer>275983</integer>
          <key>Date Modified</key><date>2005-07-17T21:59:22Z</date>
          <key>Date Added</key><date>2005-07-17T13:56:06Z</date>
          <key>Bit Rate</key><integer>160</integer>
          <key>Sample Rate</key><integer>44100</integer>
          <key>Play Count</key><integer>2</integer>
          <key>Play Date</key><integer>3239788564</integer>
          <key>Play Date UTC</key><date>2006-08-30T18:16:04Z</date>
          <key>Rating</key><integer>60</integer>
          <key>Album Rating</key><integer>60</integer>
          <key>Album Rating Computed</key><true/>
          <key>Persistent ID</key><string>405C983B39AD457A</string>
          <key>Track Type</key><string>File</string>
          <key>Location</key><string>file://localhost/M:/Music/38%20Special/Unknown%20Album/Caught%20Up%20In%20You.mp3</string>
          <key>File Folder Count</key><integer>4</integer>
          <key>Library Folder Count</key><integer>1</integer>
        </dict>
        <dict>
          <key>Track ID</key><integer>1590</integer>
          <key>Name</key><string>Hey Ya</string>
          <key>Album Rating</key><integer>60</integer>
          <key>Album Rating Computed</key><true/>
          <key>Persistent ID</key><string>405C983B39AD457B</string>
          <key>Disabled</key><true/>
          <key>Track Type</key><string>File</string>
          <key>Location</key><string>file://localhost/M:/Music/HeyYa.mp3</string>
          <key>File Folder Count</key><integer>4</integer>
          <key>Library Folder Count</key><integer>1</integer>
        </dict>
      </dict>
      <key>Playlists</key>
      <array>
        <dict>
          <key>Name</key><string>Library</string>
          <key>Master</key><true/>
          <key>Playlist ID</key><integer>6995</integer>
          <key>Playlist Persistent ID</key><string>405C983B39AD456D</string>
          <key>Visible</key><false/>
          <key>All Items</key><true/>
          <key>Playlist Items</key>
          <array>
            <dict>
              <key>Track ID</key><integer>1589</integer>
            </dict>
            <dict>
              <key>Track ID</key><integer>1590</integer>
            </dict>
          </array>
        </dict>
        <dict>
          <key>Name</key><string>Favorites</string>
          <key>Playlist ID</key><integer>14825</integer>
          <key>Playlist Persistent ID</key><string>FC21CA4108DF0F03</string>
          <key>All Items</key><true/>
          <key>Playlist Items</key>
          <array>
            <dict>
              <key>Track ID</key><integer>1590</integer>
            </dict>
            <dict>
              <key>Track ID</key><integer>1589</integer>
            </dict>
          </array>
        </dict>
      </array>
    </dict>
    </plist>
  """)
}