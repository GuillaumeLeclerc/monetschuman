package ch.epfl.shs

import java.io.StringReader
import scala.collection.JavaConversions._
import edu.stanford.nlp.process.DocumentPreprocessor
import edu.stanford.nlp.ling.HasWord

case class Paragraph(val sentences: List[Sentence]) {
  val rawSentences: String = sentences.map(_.content)
    .foldLeft("")(_ + "\n" + _)
}

object Paragraph {
  def fromText(s: String): Paragraph = {
    //TODO find a way to force French
    val it = new DocumentPreprocessor(new StringReader(s)).iterator()
    val sentences: List[Sentence] = {
      var temp: List[Sentence] = Nil // CoreNlp is shit we have to do some non immutable stuff
      while (it.hasNext()) {
        temp = new Sentence(it.next().toList.mkString(" ")) :: temp
      }
      temp
    }
    new Paragraph(sentences)
  }
}