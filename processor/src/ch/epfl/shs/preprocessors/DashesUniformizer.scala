package ch.epfl.shs.preprocessors

import ch.epfl.shs.PerprocessorStage

object DashesUniformizer extends PerprocessorStage {
  val dashesFrom: List[String] = List("-", "–", "—", "——")
  def process(x: String): String = dashesFrom.foldRight(x) {
    (dash, text) => {
      text.replaceAll(dash + "+", "-")
    }
  }
}