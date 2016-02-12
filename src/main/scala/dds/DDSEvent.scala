package dds

import org.omg.dds.core.event._

abstract class DDSEvent

abstract class ReaderEvent[T] extends DDSEvent

case class DataAvailable[T](reader: org.omg.dds.sub.DataReader[T]) extends ReaderEvent[T]

case class RequestedDeadlineMissed[T](e: RequestedDeadlineMissedEvent[T]) extends ReaderEvent[T]

case class RequestedIncompatibleQos[T](e: RequestedIncompatibleQosEvent[T]) extends ReaderEvent[T]

case class SampleRejected[T](e: SampleRejectedEvent[T]) extends ReaderEvent[T]

case class LivelinessChanged[T](e: LivelinessChangedEvent[T]) extends ReaderEvent[T]

case class SubscriptionMatched[T](e: SubscriptionMatchedEvent[T]) extends ReaderEvent[T]

case class SampleLost[T](e: SampleLostEvent[T]) extends ReaderEvent[T]

