package etl

import etl.api.DataFrameApi
import org.apache.spark.sql.{Dataset, Encoder, Row, SparkSession}

object SparkMain extends App {

  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  implicit val sparkDF: DataFrameApi[Dataset] = new DataFrameApi[Dataset] {

    def select(frame: Dataset[Row], col: String, cols: String*): Dataset[Row] = frame.select(col, cols: _*)

    def join(left: Dataset[Row], right: Dataset[Row], usingCols: List[String]): Dataset[Row] = left.join(right, usingCols)

    def readHive(table: String): Dataset[Row] = ???

    def creatDataFrame[B: Encoder](bs: List[B]): Dataset[Row] = bs.toDF
  }

  val df: Dataset[Row] = domain.businessLogic

  df.show()


}
