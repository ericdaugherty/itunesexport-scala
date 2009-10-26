import sbt._

class ProjectSettings(info: ProjectInfo) extends DefaultProject(info)
{
  override def mainClass = Some("com.ericdaugherty.itunesexport.console.ConsoleExport")
  override def manifestClassPath = Some("scala-library.jar")
  
  override def testOptions = ExcludeTests("com.ericdaugherty.itunesexport.TestSuite" :: Nil) :: super.testOptions.toList

//  val scalaSwing = "org.scala-lang" % "scala-swing" % "2.7.4"

  /* Setup Bindary distribution */
  def srcDistName = "iTunesExportScala-src-" + version + ".zip"
  def srcDistPaths = descendents("src" +++ "docs" +++ "project" / "build" / "src", "*" ) +++ ("project") ** ("*.properties")
  lazy val distSrc = zipTask(srcDistPaths, outputPath, srcDistName) dependsOn(`package`)

  def distName = "iTunesExportScala-" + version + ".zip"
  // Does not work yet but patch coming in next version?
//  def distPaths = ((outputPath ##) / defaultJarName) +++ descendents("docs" ##, "*") +++ Path.lazyPathFinder( scalaJars.map(Path.fromFile) )
  def distPaths = ((outputPath ##) / defaultJarName) +++ descendents("docs" ##, "*") +++ ("project" / "boot" / "scala-2.7.4" / "lib" ##) ** "scala-library.jar"
  lazy val dist = zipTask(distPaths, outputPath, distName) dependsOn(distSrc)


}