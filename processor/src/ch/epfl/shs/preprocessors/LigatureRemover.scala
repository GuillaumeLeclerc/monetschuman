package ch.epfl.shs.preprocessors
import ch.epfl.shs.PerprocessorStage

object LigatureRemover extends PerprocessorStage{
  def process(x: String): String = x
    .replaceAll("ﬁ", "fi")
    .replaceAll("…", "...")
}