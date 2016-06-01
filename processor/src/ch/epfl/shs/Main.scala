package ch.epfl.shs

import java.io._
import edu.stanford.nlp.simple._
import edu.stanford.nlp.parser.common.ParserGrammar
import ch.epfl.shs.preprocessors.JoinWords

object Main {
  def main(args: Array[String]): Unit = {
    val corpus = Corpus.generate()
    //val parser = ParserGrammar.loadModel("edu/stanford/nlp/models/lexparser/frenchFactored.ser.gz")
    //val result = parser.parse("La SHS c'est trop bien")
    //println(result)
    if (args.length > 0) {
      args.apply(0) match {
        case "generate-json" => corpus.writeJSON()
        
        case "generate-letters" => corpus.writeLetters()
        
        case "generate-iramuteq" => corpus.writeIramuteq()
      }
    }
  }
}