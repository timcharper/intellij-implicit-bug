package demo

import org.json4s.JsonAST.{JValue,JInt,JDouble}

object DirectlyCopied {
  trait DoubleMode { self: Implicits ⇒
    implicit def double2jvalue(x: Double): JValue = JDouble(x)

  }
  object DoubleMode extends Implicits with DoubleMode
  trait Implicits {
    implicit def int2jvalue(x: Int): JValue = JInt(x)
    // ...
  }
}

object Reimplemented {
  object DoubleMode {
    implicit def int2jvalue(x: Int): JValue = JInt(x)
    implicit def double2jvalue(x: Double): JValue = JDouble(x)
  }
}

object Main extends App {
  // Json4s exhibits the problem:
  {
    {
      import org.json4s.DoubleMode.{int2jvalue,double2jvalue}

      // INTELLIJ FALSE NEGATIVE!!!
      // double2jvalue and int2jvalue appear to collide, but the compiler doesn't mind
      val value: JValue = 5
      println("JInt = %s" format value)
    }

    // For some reason, if I re-implement them (with identical implementations), it works.
    {
      import DirectlyCopied.DoubleMode.{int2jvalue,double2jvalue}
      val value: JValue = 5
      println("XInt = %s" format value)
    }

    // Works
    {
      import org.json4s.JsonDSL.int2jvalue
      val value: JValue = 10
      println("JInt = %s" format value)
    }

    {
      import org.json4s.JsonDSL.double2jvalue
      val value: JValue = 10
      println("JDouble = %s" format value)
    }
  }


}
