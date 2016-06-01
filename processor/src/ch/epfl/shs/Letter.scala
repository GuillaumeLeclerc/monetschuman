package ch.epfl.shs
import java.time.Instant
import scala.util.matching.Regex

case class Letter(
    val id: Int,
    // We might want to parse this date depending on the visualization
    val date: Option[String],
    val from: String,
    val to: String,
    val place: Option[String],
    val content: List[Paragraph],
    val tpe: String) {
  val rawLetter: String = content.map(_.rawSentences)
    .foldLeft("")(_ + _)

  val letterIramuteq: String = {
    def filterWhiteSpace(c: Char): Char =
      if (c.isWhitespace) '-'
      else c

    Iramuteq.textSeparator + Iramuteq.idPrefix + id + " " +
      Iramuteq.fromPrefix + from.map(filterWhiteSpace) + " " +
      Iramuteq.toPrefix + to.map(filterWhiteSpace) + " " +
      Iramuteq.typePrefix + tpe + " " +
      Iramuteq.datePrefix + date.getOrElse("unknown").map(filterWhiteSpace) + " " +
      Iramuteq.placePrefix + place.getOrElse("unknown").map(filterWhiteSpace) + " " +
      rawLetter
  }

  def toJson(): String = ???
}

object Letter {
  val metadataParsers = List(
    new Regex("\\[(.+)\\] (\\d+ \\p{L}+ \\d+)"),
    new Regex("(.+), le (\\d+ \\p{L}+ \\d+)"))

  // We might want to ad a placeOnlyParser
  val dateOnlyParser = new Regex("(\\d+ \\p{L}+ \\d+)")

  private var lastLetterId = 0
  def fromText(id: Int, tpe: String, from: String, to: String, content: String): Letter = {
    /**
     * Check for missing letters
     * Warning, missing letters at the end of the corpus will not be detected
     */
    for (i <- (lastLetterId + 1 to id - 1)) {
      println(s"WARNING: Letter $i is missing! Please refine the letter separation")
    }
    lastLetterId = id

    /**
     * Parsing date and place of the letter
     */
    val results = metadataParsers.flatMap { _.findFirstMatchIn(content) }
    val place :: date :: _ = results match {
      case x :: _ => List(1, 2).map { v => Some(x.group(v)) }
      case Nil    => List(None, dateOnlyParser.findFirstMatchIn(content).map { _.group(1) })
    }

    if (place == None) {
      println(s"WARNING: No place for Letter $id")
    }
    if (date == None) {
      println(s"WARNING: No date place for Letter $id")
    }

    /**
     * Split into paragraphs
     */
    val paragraphs = content.split("\n{2,}").map { p => Paragraph.fromText(p) }.toList

    Letter(id, date, from, to, place, paragraphs, tpe)
  }
  def fromJson(json: String): Letter = ???
}