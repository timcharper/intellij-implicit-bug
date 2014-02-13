package demo

sealed trait JValue
case class JInt(amount: BigInt) extends JValue {
  override def toString = "JInt(%s)" format amount
}

case class JDouble(amount: Double) extends JValue {
  override def toString = "JDouble(%s)" format amount
}

trait Implicits {
  implicit def int2jvalue(x: Int): JValue = JInt(x)
  implicit def double2jvalue(x: Double): JValue
}

object Problematic {
  trait DoubleConversion { self: Implicits =>
    implicit def double2jvalue(x: Double): JValue = JDouble(x)
  }

  object Conversions extends Implicits with DoubleConversion
  object DoubleConversion extends Implicits with DoubleConversion // Scala plugin 0.30.380 does not like this line, even though it's ultimately unused.
}

object NotProblematic {
  trait DoubleConversion { self: Implicits =>
    implicit def double2jvalue(x: Double): JValue = JDouble(x)
  }

  object Conversions extends Implicits with DoubleConversion
}

object Main extends App {
  {
    {
      import Problematic.Conversions.{int2jvalue,double2jvalue}

      // INTELLIJ FALSE NEGATIVE!!!
      val value: JValue = 5
      println("JInt = %s" format value)
    }

    {
      import NotProblematic.Conversions.{int2jvalue,double2jvalue}
      val value: JValue = 5
      println("JInt = %s" format value)
    }

    {
      import Problematic.Conversions.int2jvalue
      val value: JValue = 10
      println("JInt = %s" format value)
    }

    {
      import Problematic.Conversions.double2jvalue
      val value: JValue = 10
      println("JDouble = %s" format value)
    }
  }
}
