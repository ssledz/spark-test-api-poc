package etl

import etl.api.DataFrameApi
import org.apache.spark.sql.{Encoder, Row}

object Main extends App {

  sealed trait TestDataFrame[A]

  case class SelectingData(df: TestDataFrame[Row], cols: List[String]) extends TestDataFrame[Row]

  case class DataFrameFromCaseClass(xs: List[Any]) extends TestDataFrame[Row]

  case class JoinTwoDataFrames(l: TestDataFrame[Row], r: TestDataFrame[Row], cols: List[String]) extends TestDataFrame[Row]


  implicit val testDF: DataFrameApi[TestDataFrame] = new DataFrameApi[TestDataFrame] {

    def select(frame: TestDataFrame[Row], col: String, cols: String*): TestDataFrame[Row] =
      SelectingData(frame, col :: cols.toList)

    def join(left: TestDataFrame[Row], right: TestDataFrame[Row], usingCols: List[String]): TestDataFrame[Row] =
      JoinTwoDataFrames(left, right, usingCols)

    def readHive(table: String): TestDataFrame[Row] = ???

    def creatDataFrame[B: Encoder](bs: List[B]): TestDataFrame[Row] = DataFrameFromCaseClass(bs)
  }

  import domain.fake._

  val df = domain.businessLogic

  println(df)

}
