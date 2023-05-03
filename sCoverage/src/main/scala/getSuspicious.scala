import scala.xml.XML

object getSuspicious {
  def main(args: Array[String]) = {
    val xml = XML.loadFile("target/scala-2.12/scoverage-report/scoverage.xml")
    val statements = (xml \\ "statement").map { statement =>
      val class_ = (statement \ "@class").toString
      val method = (statement \ "@method").toString
      val line = (statement \ "@line").toString
      val invocationCount = (statement \ "@invocation-count").toString
      (class_, method, line, invocationCount)
    }
    val grouped = statements.groupBy { case (_, op, line, _) => (op, line) }
    val total = statements.groupBy(_._1).mapValues(_.map(_._4.toInt).sum)
    var suspiciousMap = Map[(String, String), Float]()
    grouped.foreach { case (key, value) =>
      val count = value.groupBy(_._1).mapValues(_.map(_._4.toInt).sum)
      // Change udf_incorrect and udf_correct to Correct and Incorrect class names
      suspiciousMap += (key -> (count("udf_incorrect").toFloat/total("udf_incorrect") / ( (count("udf_incorrect").toFloat/total("udf_incorrect")) + (count("udf_correct").toFloat/total("udf_correct")) ) ) )
    }
    suspiciousMap.toList.sortBy(-_._2).foreach { case (key, value) =>
      printf("File %s line %s has tarantula score of: %.2f \n", key._1, key._2, value)
    }
  }
}
