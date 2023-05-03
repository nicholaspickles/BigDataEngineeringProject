object udf_incorrect {
  def readFile(filename: String): Map[String, Int] = {
    val counter = scala.io.Source.fromFile(filename)
      .getLines
      .flatMap(_.split("\\W+"))
      .foldLeft(Map.empty[String, Int]) {
        (count, word) =>
          count + (word -> {
            if (word.endsWith("ly")) {
              -100
            }
            else {
              count.getOrElse(word, 0) + 1
            }
          })
      }
    return counter
  }
}
