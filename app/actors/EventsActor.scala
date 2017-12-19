package actors

import java.io.File
import java.util.stream.Collectors

import actors.commands.events._
import akka.actor._
import com.netflix.hollow.api.consumer.HollowConsumer
import com.netflix.hollow.api.consumer.fs.{HollowFilesystemAnnouncementWatcher, HollowFilesystemBlobRetriever}
import com.netflix.hollow.explorer.ui.jetty.HollowExplorerUIServer
import com.netflix.hollow.history.ui.jetty.HollowHistoryUIServer
import commons.{CancellableFuture, LookupBusImpl, MsgEnvelope}
import configs.Config
import services.generated.AlreadyAppliedAPI
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import akka.actor.{Actor, ActorLogging}
import akka.actor._
import commons.{CancellableFuture, LookupBusImpl, MsgEnvelope}
import configs.Config
import models.{AlreadyApplied, AlreadyAppliedContainer}
import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.{WS, WSResponse}
import play.api.libs.concurrent.Akka.system
import play.api.Play.current
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._
import play.api.libs.json._

import collection.JavaConverters._
import scala.concurrent.duration._

/**
  * Created by mac on 24.02.17.
  */
class EventsActor(eventBus: LookupBusImpl, actorSystem: ActorSystem) extends Actor with ActorLogging {

  val config = Config()

  val publishDir = new File(config.scratchDir)
  System.out.println("I AM THE CONSUMER.  I WILL READ FROM " + publishDir.getAbsolutePath)
  val blobRetriever = new HollowFilesystemBlobRetriever(publishDir)
  val announcementWatcher = new HollowFilesystemAnnouncementWatcher(publishDir)
  val consumer: HollowConsumer = HollowConsumer.withBlobRetriever(blobRetriever).withAnnouncementWatcher(announcementWatcher).withGeneratedAPIClass(classOf[AlreadyAppliedAPI]).build
  consumer.triggerRefresh()
  val historyServer = new HollowHistoryUIServer(consumer, 7777)
  historyServer.start()
  val explorerServer = new HollowExplorerUIServer(consumer, 7778)
  explorerServer.start()
  //historyServer.join()
  val alreadyAppliedAPI = consumer.getAPI.asInstanceOf[AlreadyAppliedAPI]

  var schedule: Cancellable  =  _



  override def receive:  Receive = {
    case _: RunEvents => {
      sender() ! new AttachEvents
      self ! new RefreshEvents
    }
    case _: KillEvents => {
      schedule.cancel()
    }
    case _: RefreshEvents => {
      implicit val format = Json.format[AlreadyAppliedContainer]
      schedule = actorSystem.scheduler.schedule(1 second, 2 seconds) {
        consumer.triggerRefresh()
        val jsonTry = Try {
          val res = Json.stringify(Json.toJson(alreadyAppliedAPI.getAllAlreadyApplied.stream().collect(Collectors.toList()).asScala.map(x => AlreadyAppliedContainer(x.getOfferId))))
          res
        }
        val json = jsonTry match {
          case Success(js) => eventBus.publish(MsgEnvelope("events",js))
          case Failure(err) => {
            log.error("could not parse pojo")
            throw new RuntimeException("Pojo json is wrong")
          }
        }

        printHeapStats()
        printGCStats()
        System.out.println("      ")
      }
    }
    case _ => {
      sender() ! OutputMessage("Events: Unknown command or parameters list")
      log.error("Unknown command type")
    }
  }

  def formatSize(v: Long): String = {
    if (v < 1024) return v + " B"
    val z = (63 - java.lang.Long.numberOfLeadingZeros(v)) / 10
    s"${v.toDouble / (1L << (z * 10))}  ${" KMGTPE".charAt(z)}B"
  }

  private def printHeapStats(): Unit = {
    val heapSize = Runtime.getRuntime.totalMemory

    // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
    val heapMaxSize = Runtime.getRuntime.maxMemory

    // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
    val heapFreeSize = Runtime.getRuntime.freeMemory

    val nativeMemory = sun.misc.SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed();

    System.out.println("Heap Size " + formatSize(heapSize))
    System.out.println("Heap Max Size " + formatSize(heapMaxSize))
    System.out.println("Heap Free Size " + formatSize(heapFreeSize))
    System.out.println("Native Memory " + formatSize(nativeMemory))
  }

  import java.lang.management.ManagementFactory

  def printGCStats(): Unit = {
    var totalGarbageCollections: Long = 0
    var garbageCollectionTime: Long = 0
    import scala.collection.JavaConversions._
    for (gc <- ManagementFactory.getGarbageCollectorMXBeans) {
      val count = gc.getCollectionCount
      if (count >= 0) totalGarbageCollections += count
      val time = gc.getCollectionTime
      if (time >= 0) garbageCollectionTime += time
    }
    System.out.println("Total Garbage Collections: " + totalGarbageCollections)
    System.out.println("Total Garbage Collection Time (ms): " + garbageCollectionTime)
  }

}
