package etl

import etl.api.DataFrameApi
import etl.api.DataFrameApi._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Encoder, Row}

import scala.reflect.ClassTag

object domain {

  case class Person(id: Long, name: String)

  case class ContactData(id: Long, email: String)

  def businessLogic[F[_] : DataFrameApi](implicit pe: Encoder[Person], ce: Encoder[ContactData]): F[Row] = {
    val df1 = List(Person(1, "adam"), Person(2, "maniek")).makeDF
    val df2 = List(ContactData(1, "adam@wp.pl"), ContactData(2, "maniek@wp.pl")).makeDF
    val df3 = df1.join(df2, List("id")).select("id", "name", "email")
    df3
  }

  object fake {

    implicit def encoder[A]: Encoder[A] = new Encoder[A] {
      def schema: StructType = ???

      def clsTag: ClassTag[A] = ???
    }

  }

}
