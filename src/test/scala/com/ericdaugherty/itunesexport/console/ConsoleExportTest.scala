package com.ericdaugherty.itunesexport.console

import org.scalatest.{FunSuite,PrivateMethodTester}

/**
 * Test class for Export class
 */
class ConsoleExportTest extends FunSuite with PrivateMethodTester {

  test("getParameter") {
    val args = Array("badParam=bad", "-playlistType=wpl", "-playListType2=")

    // Setup function to call private method
    val getParameter = PrivateMethod[String]('getParameter)

    assert((ConsoleExport invokePrivate getParameter(args, "playlistType", "bad2")) === "wpl")
    assert((ConsoleExport invokePrivate getParameter(args, "playListType2", "bad2")) === "")
    assert((ConsoleExport invokePrivate getParameter(args, "badParam", "bad3")) === "bad3")
  }
}