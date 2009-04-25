package com.ericdaugherty.itunesexport.parser

import scala.xml.XML.loadString

import org.scalatest.FunSuite

/**
 * Test class for Playlist
 *
 * @author Eric Daugherty
 */
class PlaylistTest extends FunSuite {

  test("Playlist XML Parsing") {
    val trackMap = Map(track1.trackId -> track1, track2.trackId -> track2, track3.trackId -> track3)
    val playlist = new Playlist(playlist1Xml, trackMap)

    assert(playlist.name === "Library")
    assert(playlist.playlistId === 6995)
    assert(playlist.playlistPersistentId === "405C983B39AD456D")
    assert(playlist.music === false)
    assert(playlist.tvShows === false)
    assert(playlist.podcasts === false)
    assert(playlist.audiobooks === false)
    assert(playlist.purchasedMusic === false)
    assert(playlist.partyShuffle === false)    
    assert(playlist.visible === false)
    assert(playlist.allItems === true)
    assert(playlist.tracks.length === 3)
    assert(playlist.tracks(1).name === "Track Num 5001")
  }

  test("Playlist XML Parsing 2") {
    val trackMap = Map(track1.trackId -> track1, track2.trackId -> track2, track3.trackId -> track3)
    val playlist = new Playlist(playlist2Xml, trackMap)

    assert(playlist.name === "Library")
    assert(playlist.playlistId === 6995)
    assert(playlist.playlistPersistentId === "405C983B39AD456D")
    assert(playlist.distinguishedKind === 22)
    assert(playlist.allItems === true)
    assert(playlist.music === true)
    assert(playlist.tvShows === true)
    assert(playlist.podcasts === true)
    assert(playlist.audiobooks === true)
    assert(playlist.purchasedMusic === true)
    assert(playlist.partyShuffle === true)
    assert(playlist.visible === false)
    assert(playlist.tracks.length === 3)
    assert(playlist.tracks(1).name === "Track Num 5001")
  }

  //***************************************************************************
  // Test Constants
  //***************************************************************************

  val track1 = new Track(loadString("""
    <dict>
			<key>Track ID</key><integer>5000</integer>
			<key>Name</key><string>Track Num 5000</string>
	  </dict>
  """))

  val track2 = new Track(loadString("""
    <dict>
			<key>Track ID</key><integer>5001</integer>
			<key>Name</key><string>Track Num 5001</string>
	  </dict>
  """))

  val track3 = new Track(loadString("""
    <dict>
			<key>Track ID</key><integer>5002</integer>
			<key>Name</key><string>Track Num 5002</string>
	  </dict>
  """))

  val playlist1Xml = loadString("""
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
					<key>Track ID</key><integer>5000</integer>
				</dict>
				<dict>
					<key>Track ID</key><integer>5001</integer>
				</dict>
				<dict>
					<key>Track ID</key><integer>5002</integer>
				</dict>
		  </array>
		</dict>
  """)

    val playlist2Xml = loadString("""
		<dict>
			<key>Name</key><string>Library</string>
			<key>Master</key><true/>
			<key>Playlist ID</key><integer>6995</integer>
			<key>Playlist Persistent ID</key><string>405C983B39AD456D</string>
			<key>Distinguished Kind</key><integer>22</integer>
			<key>Music</key><true/>
		  <key>Movies</key><true/>
		  <key>TV Shows</key><true/>
		  <key>Podcasts</key><true/>
		  <key>Audiobooks</key><true/>
		  <key>Purchased Music</key><true/>
		  <key>Party Shuffle</key><true/>
			<key>Visible</key><false/>
			<key>All Items</key><true/>
			<key>Playlist Items</key>
			<array>
				<dict>
					<key>Track ID</key><integer>5000</integer>
				</dict>
				<dict>
					<key>Track ID</key><integer>5001</integer>
				</dict>
				<dict>
					<key>Track ID</key><integer>5002</integer>
				</dict>
		  </array>
		</dict>
  """)
}