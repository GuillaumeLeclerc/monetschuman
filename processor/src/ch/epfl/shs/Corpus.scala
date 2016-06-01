package ch.epfl.shs
import java.io.File
import java.io.PrintWriter
import scala.util.matching.Regex

case class Corpus(val letters: List[Letter]) {
  lazy val rawLetters: List[String] = letters.map(_.rawLetter)

  def metadataToJSON(): String = "{" + letters.map { l =>
    (l.id, Map(
      "date" -> l.date.getOrElse(""),
      "type" -> l.tpe,
      "place" -> l.place.getOrElse(""),
      "from" -> l.from,
      "to" -> l.to))
  }.map(x => "\"" + x._1 + "\": {" + x._2.toList.map { x => "\"" + x._1 + "\" : \"" + x._2.toString() + "\"" }.mkString(",") + "}").mkString(",") + "}"

  def writeLetters() = {
    def writeLetterToFile(letterContent: String, index: Int) = {
      val writer = new PrintWriter(new File("../txt/letters/letter" + (index + 1) + ".txt"))
      writer.write(letterContent)
      writer.close()
    }
    rawLetters.zipWithIndex
      .map(x => writeLetterToFile(x._1, x._2))
  }

  def writeJSON() = {
    val writer = new PrintWriter(new File("../txt/letters/metadata.json"))
    writer.write(metadataToJSON());
    writer.close()
  }

  def writeIramuteq() = {
    val writer = new PrintWriter(new File("../txt/iramuteq/corpus.txt"))
    val iramuteqCorpus = letters.map(_.letterIramuteq)
      .foldLeft("")(_ + "\n" + _)
    writer.write(iramuteqCorpus)
    writer.close()
  }
}

object Corpus {
  val titleRegExtractor: Regex = new Regex("(\\d+)\\. (\\p{L}+(?: \\p{L}+)?) de (\\w+(?: \\w+)+) à (\\w+(?: \\w+))")

  //this is dirty as fuck but lookahead and lookbehind clauses expect finite length conditions
  val separatorLength = 10
  val letterSeparator = "æ" * separatorLength
  val attrSeparator = "đ" * separatorLength
  val nbAttr = 4
  val titleReplacement: String = letterSeparator + (1 to nbAttr).toList.map { x => "$" + x }.mkString(attrSeparator) + letterSeparator
  //end of this is dirty as fuck

  def generate(): Corpus = {
    val text = Text.getProcessed()
    val titlesParsed = titleRegExtractor.replaceAllIn(text, titleReplacement)
    val letters = titlesParsed.split(letterSeparator)
      .drop(1) // firstElement is bullshit
      .grouped(2) // we group titles and content
      .toList
      .map { x => x.toList }
      .map {
        case metadata :: content :: _ => {
          val attrs = metadata.split(attrSeparator).toList
          val id = attrs(0).toInt
          val tpe :: from :: to :: _ = attrs.drop(1) //Scala <3
          Letter.fromText(id, tpe, from, to, content)
        }
      }
    println(s"we got ${letters.size} letters")
    Corpus(letters)
  }

  def fromJsonFile(f: File): Corpus = ???
}