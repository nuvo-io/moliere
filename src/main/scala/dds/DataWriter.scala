package dds


object DataWriter {
  import org.omg.dds.topic.Topic
  import org.omg.dds.pub.DataWriterQos
  import scala.collection.JavaConversions._

  def create[T](pub: org.omg.dds.pub.Publisher, t: Topic[T]) = pub.createDataWriter[T](t)


  def apply[T](pub: org.omg.dds.pub.Publisher, t: Topic[T]) = pub.createDataWriter[T](t)



  def apply[T](pub: org.omg.dds.pub.Publisher, t: Topic[T], qos: DataWriterQos) =
    pub.createDataWriter[T](t, qos, null, List())

  /*
  def apply[T](t: Topic[T])(implicit pub: org.omg.dds.pub.Publisher, m: Manifest[T]) = {
    pub.createDataWriter[T](t).asInstanceOf[DataWriter[T]]

  }
  */
  def apply[T](t: Topic[T])(implicit pub: org.omg.dds.pub.Publisher) = pub.createDataWriter[T](t)

  def apply[T](t: Topic[T], qos: DataWriterQos)(implicit pub: org.omg.dds.pub.Publisher) =
    pub.createDataWriter[T](t, qos, null, List())
}
