package dds

import org.omg.dds.core.policy.PolicyFactory

/**
  * Created by veda on 22/12/2016.
  */

case class Scope(partition: String)(implicit dp: org.omg.dds.domain.DomainParticipant, pf: PolicyFactory) {
  private val p = Partition(partition)
  lazy private val pqos = PublisherQos().withPolicy(p)
  lazy private val sqos = SubscriberQos().withPolicy(p)
  lazy val pub = Publisher(pqos)
  lazy val sub = Subscriber(sqos)
  def apply() = (pub, sub)
}