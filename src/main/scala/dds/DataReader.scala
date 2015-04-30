package dds

object DataReader {
  import org.omg.dds.topic.Topic
  import org.omg.dds.sub.DataReaderQos
  import scala.collection.JavaConversions._

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T]): org.omg.dds.sub.DataReader[T] = {
    val dr = sub.createDataReader(t).asInstanceOf[org.omg.dds.sub.DataReader[T]]

    dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all)
    dr
  }

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T], qos: DataReaderQos): org.omg.dds.sub.DataReader[T] = {
    val dr = sub.createDataReader(t, qos).asInstanceOf[org.omg.dds.sub.DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all)
    dr
  }

  def apply[T](t: Topic[T])(implicit sub: org.omg.dds.sub.Subscriber): org.omg.dds.sub.DataReader[T] = {
    val dr = sub.createDataReader(t).asInstanceOf[org.omg.dds.sub.DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all)
    dr
  }

  def apply[T](t: Topic[T], qos: DataReaderQos)(implicit sub: org.omg.dds.sub.Subscriber): org.omg.dds.sub.DataReader[T] = {
    val dr = sub.createDataReader(t, qos).asInstanceOf[org.omg.dds.sub.DataReader[T]]
    // dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all()(sub.getEnvironment))
    dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all)
    dr
  }
}

