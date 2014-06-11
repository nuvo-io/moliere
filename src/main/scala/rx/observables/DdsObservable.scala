package rx.observables


import org.omg.dds.sub.{SampleState, InstanceState, DataReader, Sample}
import dds._
import dds.prelude._
import rx.lang.scala.{Subscription, Observer, Observable}

/**
 * This class defines the DDS Overvable types
 *
 */
object DdsObservable {
  import scala.collection.JavaConversions._

  def fromDataReaderData[T](dr: DataReader[T]): Observable[T] = Observable.create((o: Observer[T]) => {
    val lid = dr.listen {
      case DataAvailable(_) => {
        val state = dr.getParent.createDataState()
          .`with`(InstanceState.ALIVE)
          .`with`(SampleState.NOT_READ)
          .`with`(SampleState.READ)
          .withAnyViewState()
        dr.select().dataState(state).read().foreach(s => {
          println(this.hashCode() + ", ")
          o.onNext(s.getData)
        })
      }
    }
    Subscription(dr.deaf(lid))
  })

  def fromDataReaderEvents[T](dr: DataReader[T]): Observable[ReaderEvent[T]] =
    Observable.create((o: Observer[ReaderEvent[T]]) => {
    val lid = dr.listen {
      case e @ DataAvailable(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case e @ RequestedDeadlineMissed(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case  e @ RequestedIncompatibleQos(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case e @ SampleRejected(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case e @ LivelinessChanged(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case e @ SubscriptionMatched(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
      case e @ SampleLost(_) => o.onNext(e.asInstanceOf[ReaderEvent[T]])
    }

    Subscription(dr.deaf(lid))

  })

}
