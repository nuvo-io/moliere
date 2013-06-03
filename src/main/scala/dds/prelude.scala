package dds

import scala.language.implicitConversions

package object prelude {
  import org.omg.dds.core.event._
  import org.omg.dds.sub.DataReader

  implicit class ReaderListener[T](val fun: PartialFunction[Any, Unit])
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
    def listen(fun: PartialFunction[Any, Unit]): Unit = {
      dr.setListener(fun)
    }
    def deaf() {
      dr.setListener(null)
    }
  }

  implicit class Command(fun:  () => Unit) extends Runnable {
    def run() = fun()
  }
}
