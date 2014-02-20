package dds

object DataReader {
  import org.omg.dds.topic.Topic
  import org.omg.dds.sub.{DataReader, DataReaderQos}

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T]) = {
    val dr = sub.createDataReader(t).asInstanceOf[DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T]())
    dr
  }

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T], qos: DataReaderQos) = {
    val dr = sub.createDataReader(t, qos).asInstanceOf[DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T]())
    dr
  }

  def apply[T](t: Topic[T])(implicit sub: org.omg.dds.sub.Subscriber) = {
    val dr = sub.createDataReader(t).asInstanceOf[DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T]())
    dr

  }

  def apply[T](t: Topic[T], qos: DataReaderQos)(implicit sub: org.omg.dds.sub.Subscriber) = {
    val dr = sub.createDataReader(t, qos).asInstanceOf[DataReader[T]]
    dr.setListener(new prelude.MCastReaderListener[T](), dds.core.Status.all()(sub.getEnvironment))
    dr
    
  }
}

