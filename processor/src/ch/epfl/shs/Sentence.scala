package ch.epfl.shs

import edu.stanford.nlp.trees.Tree
import sys.process._
import edu.stanford.nlp.parser.common.ParserGrammar


class Sentence(val content: String) {
  lazy val parsed: Tree = Sentence.parser.parse(content)
  val res: String = content
  
}
object Sentence {
  private val parser = ParserGrammar.loadModel("edu/stanford/nlp/models/lexparser/frenchFactored.ser.gz")
}