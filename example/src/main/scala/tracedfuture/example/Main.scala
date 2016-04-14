package trackedfuture.example

import scala.concurrent._
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object Main
{

  def main(args: Array[String]):Unit =
  {
    val f = f0("222")
    try {
       val r = Await.result(f,10 seconds)
    } catch {
       // will print with f0 when agent is enabled
       case ex: Throwable => ex.printStackTrace
    }
    try {
       val r = Await.result(f3("AAA"),10 seconds)
    } catch {
       case ex: Throwable => ex.printStackTrace
    }
  }

  def f0(x:String): Future[Unit] =
  {
    System.err.print("f0:");
    f1(x)
  }

  def f1(x: String): Future[Unit] =
   Future{
     throw new RuntimeException("AAA");
   }

  def f3(x: String):Future[Unit] = f4(x)

  def f4(x: String):Future[Unit] =
  {
    Future{ "aaaa " } map { _ =>  throw new Exception("bbb-q1") }
  }

  def fFlatMap0():Future[Unit] = fFlatMap1()

  def fFlatMap1():Future[Unit] = {
    Future{ Future{ "aaaa " } } flatMap { _ =>  throw new Exception("bbb-q1") }
  }

  def fFilter0():Future[Unit] = fFilter1()

  def fFilter1():Future[Unit] = {
    Future{ () } filter { _ => false }
  }

  def withFilter0():Future[Unit] = withFilter1()

  def withFilter1():Future[Unit] = {
    Future{ () } withFilter { _ => throw new Exception("AAA") }
  }


}
