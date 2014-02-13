package demo

object Main extends App {
  // Json4s exhibits the problem:
  {
    import org.json4s.JsonAST.JValue
    {
      import org.json4s.JsonDSL.{int2jvalue,double2jvalue}

      // INTELLIJ FALSE NEGATIVE!!!
      // double2jvalue and int2jvalue appear to collide, but the compiler doesn't mind
      val value: JValue = 10
      println("JInt = %s" format value)
    }

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

  // I can't reproduce it without the library
  {
    sealed trait XValue
    case class XInt(amount: Int) extends XValue {
      override def toString =
        "XInt(%s)" format amount
    }

    case class XDouble(amount: Double) extends XValue {
      override def toString =
        "XDouble(%s)" format amount
    }

    object Conversions {
      implicit def int2XValue(i: Int): XValue = XInt(i)
      implicit def double2XValue(d: Double): XValue = XDouble(d)
    }

    {
      import Conversions.{int2XValue,double2XValue}
      // THIS SHOULD BE A PROBLEM FOLLOWING ABOVE LOGIC, but isn't.
      val value: XValue = 5
      println("XInt = %s" format value)
    }
  }
}
