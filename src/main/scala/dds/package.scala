package dds

import org.omg.dds.core.policy.PolicyFactory

import scala.language.implicitConversions
import java.util.concurrent.atomic.AtomicReference

package object prelude {
  import org.omg.dds.core.event._


  def SoftStateReaderQos(history: Int = 1)(implicit pf: PolicyFactory, sub: org.omg.dds.sub.Subscriber) =
    DataReaderQos().withPolicies(
      Reliability.BestEffort,
      Durability.Volatile,
      History.KeepLast(history)
    )

  def SoftStateWriterQos(history: Int = 1)(implicit pf: PolicyFactory, pub: org.omg.dds.pub.Publisher) =
    DataWriterQos().withPolicies(
      Reliability.BestEffort,
      Durability.Volatile,
      History.KeepLast(history)
    )

  def SoftStateTopic(implicit dp: org.omg.dds.domain.DomainParticipant) = TopicQos()

  def HardStateTopic(durability: org.omg.dds.core.policy.Durability,
                     rlimits: org.omg.dds.core.policy.ResourceLimits)
                    (implicit pf: PolicyFactory, dp: org.omg.dds.domain.DomainParticipant) = {
    val ds = DurabilityService(History.KeepLast(1), rlimits)

    var durableTopicQos = TopicQos().withPolicies(History.KeepLast(1), ds, durability)
    var nonDurableTopicQos = TopicQos().withPolicies(History.KeepLast(1), durability)
    durability.getKind match {
      case org.omg.dds.core.policy.Durability.Kind.PERSISTENT => durableTopicQos
      case org.omg.dds.core.policy.Durability.Kind.TRANSIENT => durableTopicQos
      case _ => nonDurableTopicQos
    }
  }


  def HardStateReaderQos(durability: org.omg.dds.core.policy.Durability, history: Int = 1)
                        (implicit pf: PolicyFactory, sub: org.omg.dds.sub.Subscriber) =
    DataReaderQos().withPolicies(
      Reliability.BestEffort,
      durability,
      History.KeepLast(history)
    )

  def HardStateWriterQos(durability: org.omg.dds.core.policy.Durability, history: Int = 1)
                        (implicit pf: PolicyFactory, pub: org.omg.dds.pub.Publisher) =
    DataWriterQos().withPolicies(
      Reliability.BestEffort,
      durability,
      History.KeepLast(history))


  def EventReaderQos(durability: org.omg.dds.core.policy.Durability)
                    (implicit pf: PolicyFactory, sub: org.omg.dds.sub.Subscriber) =
    DataReaderQos().withPolicies(
      Reliability.Reliable,
      durability,
      History.KeepAll()
    )

  def EventWriterQos(durability: org.omg.dds.core.policy.Durability)
                    (implicit pf: PolicyFactory, pub: org.omg.dds.pub.Publisher) =
    DataWriterQos().withPolicies(
      Reliability.Reliable,
      durability,
      History.KeepAll()
    )

  case class Event[T: Manifest](name: String, durability: org.omg.dds.core.policy.Durability)
                          (implicit dp: org.omg.dds.domain.DomainParticipant,
                                    sub: org.omg.dds.sub.Subscriber,
                                    pub: org.omg.dds.pub.Publisher,
                                    pf: PolicyFactory
                          ) {

    lazy val topic = Topic[T](name, HardStateTopic(durability, ResourceLimits(1, 1024, 1024*1024)))
    lazy val writer = DataWriter[T](topic, EventWriterQos(durability))
    lazy val reader = DataReader[T](topic, EventReaderQos(durability))
  }

  case class SoftState[T: Manifest](name: String, history: Int = 1)
                          (implicit dp: org.omg.dds.domain.DomainParticipant,
                           sub: org.omg.dds.sub.Subscriber,
                           pub: org.omg.dds.pub.Publisher,
                           pf: PolicyFactory) {

    lazy val topic = Topic[T](name)
    lazy val writer = DataWriter[T](topic, SoftStateWriterQos(history))
    lazy val reader = DataReader[T](topic, SoftStateReaderQos(history))
  }

  case class HardState[T: Manifest](name: String, durability: org.omg.dds.core.policy.Durability, history: Int = 1)
                              (implicit dp: org.omg.dds.domain.DomainParticipant,
                               sub: org.omg.dds.sub.Subscriber,
                               pub: org.omg.dds.pub.Publisher,
                               pf: PolicyFactory) {

    lazy val topic = Topic[T](name, HardStateTopic(durability, ResourceLimits(1, 1024, 1024*1024)))
    lazy val writer = DataWriter[T](topic, HardStateWriterQos(durability, history))
    lazy val reader = DataReader[T](topic, HardStateReaderQos(durability, history))
  }

  case class Scope(partition: String)(implicit dp: org.omg.dds.domain.DomainParticipant, pf: PolicyFactory) {
    private val p = Partition(partition)
    lazy private val pqos = PublisherQos().withPolicy(p)
    lazy private val sqos = SubscriberQos().withPolicy(p)
    lazy val pub = Publisher(pqos)
    lazy val sub = Subscriber(sqos)
  }


  def history[T](dr: org.omg.dds.sub.DataReader[T]) = dr.select().dataState(DataState.allData).read()

  implicit class RichDataWriter[T](val dw: org.omg.dds.pub.DataWriter[T]) {
    def ! (x: T) = dw.write(x)

    def ! (xs: List[T]) = xs.foreach(dw.write(_))

    def write (xs: List[T]) = xs.foreach(dw.write(_))
  }

  class MCastReaderListener[T] extends org.omg.dds.sub.DataReaderListener[T] {
    val listeners = new AtomicReference[Map[Int, org.omg.dds.sub.DataReaderListener[T]]](Map())

    def addListener(l: org.omg.dds.sub.DataReaderListener[T]): Int = {
      var done = false
      do {
        val subs = listeners.get()
        val usubs = subs + (l.hashCode() -> l)
        done = listeners.compareAndSet(subs, usubs)
      }
      while(done == false)
      l.hashCode()
    }

    def removeListener(subid: Int) {
      var done = false
      do {
        val subs = listeners.get()
        val usubs = subs - subid
        done = listeners.compareAndSet(subs, usubs)
      }
      while(done == false)
    }

    def onRequestedDeadlineMissed(e: RequestedDeadlineMissedEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onRequestedDeadlineMissed(e))
    }

    def onRequestedIncompatibleQos(e: RequestedIncompatibleQosEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onRequestedIncompatibleQos(e))
    }

    def onSampleRejected(e: SampleRejectedEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onSampleRejected(e))
    }

    def onLivelinessChanged(e: LivelinessChangedEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onLivelinessChanged(e))
    }

    def onDataAvailable(e: DataAvailableEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onDataAvailable(e))

    }

    def onSubscriptionMatched(e: SubscriptionMatchedEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onSubscriptionMatched(e))
    }

    def onSampleLost(e: SampleLostEvent[T]) {
      val subs = listeners.get()
      subs.foreach(s => s._2.onSampleLost(e))
    }
  }

  implicit class ReaderListener[T, Q](val fun: PartialFunction[Any, Q])
    extends org.omg.dds.sub.DataReaderListener[T] {

    def onRequestedDeadlineMissed(e: RequestedDeadlineMissedEvent[T]) {
      val ce = RequestedDeadlineMissed(e)
      if (fun.isDefinedAt(ce)) fun(ce)
    }

    def onRequestedIncompatibleQos(e: RequestedIncompatibleQosEvent[T]) {
      val ce = RequestedIncompatibleQos(e)
      if (fun.isDefinedAt(ce)) fun(ce)
    }

    def onSampleRejected(e: SampleRejectedEvent[T]) {
      val ce = SampleRejected(e)
      if (fun.isDefinedAt(ce)) fun(ce)
    }

    def onLivelinessChanged(e: LivelinessChangedEvent[T]) {
      val ce = LivelinessChanged(e)
      if (fun.isDefinedAt(ce)) fun(ce)
    }

    def onDataAvailable(e: DataAvailableEvent[T]) {
      val dr = e.getSource.asInstanceOf[org.omg.dds.sub.DataReader[T]]
      val evt = DataAvailable(dr)
      if (fun.isDefinedAt(evt)) fun(evt)
    }

    def onSubscriptionMatched(e: SubscriptionMatchedEvent[T]) {
      val ce = SubscriptionMatched(e)
      if (fun.isDefinedAt(e)) fun(ce)
    }

    def onSampleLost(e: SampleLostEvent[T]) {
      val ce = SampleLost(e)
      if (fun.isDefinedAt(ce)) fun(ce)
    }
  }

  implicit class RichDataReader[T](dr: org.omg.dds.sub.DataReader[T]) {
    var subid = 0
    def listen[Q](fun: PartialFunction[Any, Q]): Int = {
      val mcl = dr.getListener().asInstanceOf[MCastReaderListener[T]]
      subid = mcl.addListener(fun)
      subid
    }
    def deaf() {
      val mcl = dr.getListener().asInstanceOf[MCastReaderListener[T]]
      mcl.removeListener(subid)

    }

    def deaf(lid: Int) {
      val mcl = dr.getListener().asInstanceOf[MCastReaderListener[T]]
      mcl.removeListener(lid)

    }
  }

  implicit class Command[T](fun:  () => T) extends Runnable {
    def run(): Unit = fun()
  }

  implicit class RichInstanceState(is: org.omg.dds.sub.InstanceState) {
    def isAlive = is.value == org.omg.dds.sub.InstanceState.ALIVE.value
    def notAlive = is.value == org.omg.dds.sub.InstanceState.NOT_ALIVE_NO_WRITERS.value
    def isDisposed = is.value == org.omg.dds.sub.InstanceState.NOT_ALIVE_DISPOSED.value
  }

}
