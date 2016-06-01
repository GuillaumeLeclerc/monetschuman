package ch.epfl.shs

trait PerprocessorStage {
  def process(x : String) : String
  def apply(x: String) = process(x)
}