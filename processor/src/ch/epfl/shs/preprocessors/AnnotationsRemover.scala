package ch.epfl.shs.preprocessors
import ch.epfl.shs.PerprocessorStage

object AnnotationRemover extends PerprocessorStage{
  def process(x: String): String = x.replaceAll("(\\s[a-zA-z]+)[0-9]+", "$1")
}