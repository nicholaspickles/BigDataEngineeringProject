import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper


class test extends AnyFlatSpec with Matchers {
  "Spark" should "test for coverage" in {
    udf_correct.readFile("passing_output/part-00000") should not be (null)
    udf_incorrect.readFile("failing_output/part-00000") should not be (null)
  }
}