package ch.epfl.shs.preprocessors

import ch.epfl.shs.PerprocessorStage

object ApostropheUniformizer extends PerprocessorStage{
  def process(x: String): String = x.replaceAll("’", "'")
}