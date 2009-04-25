iTunesExport-Scala Console Application
http://www.ericdaugherty.com/dev/itunesexport/scala/

Usage

iTunes Export is a command line application that uses the Java Runtime Environment (JRE) 
to execute.  To use it, open a command window to the directory where iTunes Export is
installed and execute the following command:

java -jar itunesexport.jar

If iTunes Export is run without any parameters, it will attempt to locate
your iTunes Music Library and will write a .m3u playlist file for each
playlist defined in the iTunes Music Library.

The location of the iTunes Music Library can be overridden using a command
line parameter.  Example:

java -jar itunesexport.jar "C:\My Music\iTunes Music Library.xml"

iTunesExport Scala does not yet support many of the advanced features found
in the normal iTunesExport library.  However, unlike the normal version,
this version works on OS X.