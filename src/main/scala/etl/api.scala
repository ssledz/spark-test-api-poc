package etl

import org.apache.spark.sql.{Encoder, Row}

object api {

  trait DataFrameApi[F[_]] {
    def select(frame: F[Row], col: String, cols: String*): F[Row]

    def join(left: F[Row], right: F[Row], usingCols: List[String]): F[Row]

    def readHive(table: String): F[Row]

    def creatDataFrame[B: Encoder](bs: List[B]): F[Row]
  }

  object DataFrameApi {
    def apply[A, F[_] : DataFrameApi]: DataFrameApi[F] = implicitly[DataFrameApi[F]]

    implicit class DataFrameApiOps[F[_]](val fa: F[Row]) extends AnyVal {
      def select(col: String, cols: String*)(implicit F: DataFrameApi[F]): F[Row] = F.select(fa, col, cols: _*)

      def join(right: F[Row], usingCols: List[String])(implicit F: DataFrameApi[F]): F[Row] = F.join(fa, right, usingCols)
    }

    implicit class ListOps[F[_], B: Encoder](val xs: List[B]) {
      def makeDF(implicit F: DataFrameApi[F]): F[Row] = F.creatDataFrame(xs)
    }

  }


}
