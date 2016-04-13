package trackedfuture.example

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

import org.scalatest._
import org.scalatest.concurrent._



class MainCallSpec extends FlatSpec with AsyncAssertions
{

  "MainCall" should "show origin method between future " in {
    callAndCheckMethod( Main.f0("AAA"), "f0")
  }

  "MainCall" should "show origin method with map " in {
    callAndCheckMethod( Main.f3("AAA"), "f3")
  }

  "MainCall" should "show origin method with flatMap " in {
    callAndCheckMethod( Main.fFlatMap0(), "fFlatMap0")
  }

  "MainCall" should "show origin method with filter " in {
    callAndCheckMethod( Main.fFilter0(), "fFilter0")
  }


  private def callAndCheckMethod(body: =>Future[_],method:String): Unit = {
    val f = body
    val w = new Waiter
    f onComplete {
       case Failure(ex) => 
                           val checked = checkMethod(method,ex)
                           w{ assert(checked) }
                           w.dismiss()
       case _ => w{ assert(false) }
                 w.dismiss()
    }
    w.await{timeout(10 seconds)}
  }

  private def checkMethod(method:String, ex: Throwable): Boolean = {
    ex.printStackTrace()
    ex.getStackTrace.toSeq.find( _.getMethodName == method ).isDefined
  }


}
