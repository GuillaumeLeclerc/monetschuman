package ch.epfl.shs
import java.io.File
import ch.epfl.shs.preprocessors.DashesUniformizer
import ch.epfl.shs.preprocessors.ApostropheUniformizer
import ch.epfl.shs.preprocessors.LigatureRemover
import ch.epfl.shs.preprocessors.AnnotationRemover

object Text {
  private val pipeline = List(
      DashesUniformizer,
      ApostropheUniformizer,
      LigatureRemover,
      AnnotationRemover
  )
  private val files = new File("../txt/pages").listFiles().toList
  private val sortedFiles = files.sortBy { x => x.getName().split('.').apply(0).toInt }
  private val scalaFiles = sortedFiles.map { x => scala.io.Source.fromFile(x) }
  private val corpus = scalaFiles.foldLeft("") { (text, file) => text + "\r\n" + file.mkString }
  scalaFiles.foreach { x => x.close() }
  def getRaw(): String = corpus
  def getProcessed(): String = pipeline.foldLeft(getRaw()) { (x, y) => y.process(x) }
}