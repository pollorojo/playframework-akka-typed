package domain

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}

object NumeratorActor {
  sealed trait Command
  case class GetNextNumber(replyTo: ActorRef[String]) extends Command

  sealed trait Event
  case class OrderNumberGenerated(number: String) extends Event

  // State
  case class Store(currentNumber: String)

  def apply(): Behavior[Command] = {
    Behaviors.setup {
      _ => EventSourcedBehavior[Command, Event, Store](
        persistenceId = PersistenceId.ofUniqueId("MyStore"), // value used to identify in db
        emptyState = Store("100"), // Initial state
        commandHandler = commandHandler(), // <- invoking method
        eventHandler = eventHandler // <- passing constant
      )
    }
  }

  // Method
  def commandHandler(): (Store, Command) => Effect[Event, Store] = {
    (state: Store, command: Command) => command match {
      case GetNextNumber(replyTo) =>
        val newNumber = state.currentNumber + 1
        Effect
          .persist(OrderNumberGenerated(newNumber))
          .thenReply(replyTo)(_ => newNumber)
    }
  }

  // Constant
  val eventHandler: (Store, Event) => Store = {
    (state, event) => event match {
      case OrderNumberGenerated(number) => state.copy(currentNumber = number)
    }
  }
}
