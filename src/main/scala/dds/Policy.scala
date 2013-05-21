package dds

import org.omg.dds.core.policy.PolicyFactory
import scala.collection.JavaConversions._

object Partition {
  def apply(name: String)(implicit pf: PolicyFactory) = pf.Partition().withName(name)
  def apply(names: List[String])(implicit pf: PolicyFactory) = pf.Partition().withName(names)
}

object Reliability {
  def BestEffort(implicit pf: PolicyFactory) = pf.Reliability().withBestEffort()
  def Reliable(implicit pf: PolicyFactory) = pf.Reliability().withReliable()
}

object Durability {
  def Volatile(implicit pf: PolicyFactory) = pf.Durability().withVolatile()
  def TransientLocal(implicit pf: PolicyFactory) = pf.Durability().withTransientLocal()
  def Transient(implicit pf: PolicyFactory) = pf.Durability().withTransient()
  def Persistent(implicit pf: PolicyFactory) = pf.Durability().withPersitent()
}

object History {
  def KeepLast(depth: Int)(implicit pf: PolicyFactory) = pf.History().withKeepLast(depth)
  def KeepAll(depth: Int)(implicit pf: PolicyFactory) = pf.History().withKeepAll()
}