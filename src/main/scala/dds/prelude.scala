package dds

import scala.language.implicitConversions

package object prelude {
  import org.omg.dds.core.event._
  import org.omg.dds.sub.DataReader

  implicit class ReaderListener[T, Q](val fun: PartialFunction[Any, Q])
    extends org.omg.dds.sub.DataReaderListener[T] {

    def onRequestedDeadlineMissed(e: RequestedDeadlineMissedEvent[T]) {}

    def onRequestedIncompatibleQos(e: RequestedIncompatibleQosEvent[T]) {}

    def onSampleRejected(e: SampleRejectedEvent[T]) {}

    def onLivelinessChanged(e: LivelinessChangedEvent[T]) {}

    def onDataAvailable(e: DataAvailableEvent[T]) {
      val evt = DataAvailable(e.getSource.asInstanceOf[DataReader[T]])
      if (fun.isDefinedAt(evt)) fun(evt)
    }

    def onSubscriptionMatched(e: SubscriptionMatchedEvent[T]) {}

    def onSampleLost(e: SampleLostEvent[T]) {}
  }

  implicit class RichDataReader[T](dr: org.omg.dds.sub.DataReader[T]) {
    class ReaderIteratee[T](val dr: org.omg.dds.sub.DataReader[T]) extends Iteratee[T] {
      def doM(a: T => Unit): Unit = ???

      def filter(p: (T) => Boolean): Iteratee[T] = ???

      def map[Q](f: (T) => Q): Iteratee[Q] = ???

      def dropWhile(p: (T) => Boolean): Iteratee[T] = ???

      def takeWhile(p: (T) => Boolean): Iteratee[T] = ???

      def push(d: T) {}
    }
    def listen[Q](fun: PartialFunction[Any, Q]): Unit = {
      dr.setListener(fun)
    }
    def deaf() {
      dr.setListener(null)
    }
    def iteratee = new ReaderIteratee[T](dr)
  }

  implicit class Command[T](fun:  () => T) extends Runnable {
    def run(): Unit = fun()
  }
}
