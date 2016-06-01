package ch.epfl.shs.preprocessors
import ch.epfl.shs.PerprocessorStage

object JoinWords extends PerprocessorStage{
  def process(x: String): String = x.replaceAll("([a-zA-z\\-\\']+)\\-+\n([a-zA-z\\-\']+)", "$1$2\n")
}