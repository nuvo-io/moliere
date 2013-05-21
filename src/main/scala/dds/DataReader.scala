package dds

object DataReader {
  import org.omg.dds.topic.Topic
  import org.omg.dds.sub.{DataReader, DataReaderQos}

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T]) = sub.createDataReader(t).asInstanceOf[DataReader[T]]

  def apply[T](sub: org.omg.dds.sub.Subscriber, t: Topic[T], qos: DataReaderQos) =
    sub.createDataReader(t, qos).asInstanceOf[DataReader[T]]

  def apply[T](t: Topic[T])(implicit sub: org.omg.dds.sub.Subscriber) = sub.createDataReader(t).asInstanceOf[DataReader[T]]

  def apply[T](t: Topic[T], qos: DataReaderQos)(implicit sub: org.omg.dds.sub.Subscriber) =
    sub.createDataReader(t, qos).asInstanceOf[DataReader[T]]
}

