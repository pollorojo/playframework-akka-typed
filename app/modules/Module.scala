package modules

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    super.configure()

    val system = ActorSystem(Behaviors.empty, "Dojo")

    bind[ActorSystem[_]].toInstance(system)
  }
}
