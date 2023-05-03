import org.apache.spark.lineage.LineageContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.lineage.LineageContext._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object WordCount {

  def test(output: (String, Int)): Boolean = {
    output._2 > 0
  }

  def selectRecordSubset[T](list: Array[(T, Long)],
                                    test: T => Boolean): (ListBuffer[Long],ListBuffer[Long], ListBuffer[Long]) = {
    var correct = new ArrayBuffer[Long]()
    var failure = new ArrayBuffer[Long]()

    val correctFactor = 2
    val failureFactor = 2

    list.foreach(s => if (test(s._1)) correct += s._2 else failure += s._2)

    var retList = new ListBuffer[Long]()
    var correctList = new ListBuffer[Long]()
    var failureList = new ListBuffer[Long]()

    if (failure.length < failureFactor) {
      failure.foreach(retList += _)
      failure.foreach(failureList += _)
    } else {
      val items = scala.util.Random.shuffle(failure).take(failureFactor)
      retList = retList ++ items
      failureList = failureList ++ items
    }

    if (correct.length < correctFactor) {
      correct.foreach(retList += _)
      correct.foreach(correctList += _)
    } else {
      val items = scala.util.Random.shuffle(correct).take(correctFactor)
      retList = retList ++ items
      correctList = correctList ++ items
    }

    (retList, correctList, failureList)
  }


  def main(array: Array[String]): Unit = {

    val log = "words.txt"
    val conf = new SparkConf().setMaster("local").setAppName("My app")

    val context = new SparkContext(conf)
    val lc = new LineageContext(context)
    lc.setCaptureLineage(true)

    val lines = lc.textFile(log)

    val pairs = lines.flatMap(line => line.trim()
      .replaceAll("""\p{Punct}&&[^.]""", "")
      .split(" ")).map(word => if (word.endsWith("ly")) (word.trim(), -100) else (word.trim(), 1))

    val output = pairs.reduceByKey(_ + _)

    val out = output.collectWithId()

    val (list, correct, failure) = selectRecordSubset(out, test)

    lc.setCaptureLineage(false)

    var linRDD = output.getLineage()
    linRDD.collect

    linRDD = linRDD.filter(l => list.contains(l))
    var posRDD = linRDD.filter(l => correct.contains(l))
    var failRDD = linRDD.filter(l => failure.contains(l))
    linRDD.collect()

    posRDD = posRDD.goBackAll()
    posRDD.show().saveAsTextFile("passing_output")

    linRDD = linRDD.goNextAll()

    failRDD = failRDD.goBackAll()
    failRDD.show().saveAsTextFile("failing_output")

  }

}
