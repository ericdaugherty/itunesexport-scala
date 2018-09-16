package com.ericdaugherty.itunesexport

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl

/**
 * Hack to prevent the external lookup of the DTD.
 */
class NonDTDSaxParserFactory extends SAXParserFactoryImpl {
  super.setNamespaceAware(false)
  super.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
  super.setFeature("http://xml.org/sax/features/validation", false)
}