package com.ericdaugherty.itunesexport.console

import org.scalatest.{FunSuite,PrivateMethodTester}

/**
 * Test class for Export class
 */
class ExportTest extends FunSuite with PrivateMethodTester {

  test("getParameter") {
    val args = Array("badParam=bad", "-playlistType=wpl", "-playListType2=")

    // Setup function to call private method
    val getParameter = PrivateMethod[String]('getParameter)

    assert((Export invokePrivate getParameter(args, "playlistType", "bad2")) === "wpl")
    assert((Export invokePrivate getParameter(args, "playListType2", "bad2")) === "")
    assert((Export invokePrivate getParameter(args, "badParam", "bad3")) === "bad3")
  }
}