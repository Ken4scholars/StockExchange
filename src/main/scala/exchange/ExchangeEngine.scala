package exchange

import clients.Client
import orders.{Order, OrderBook}
import stocks.Stock

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by kenneth on 09.03.18.
  */

trait ExchangeEngine {

  def clients: ArrayBuffer[Client]
  def orderBooks: mutable.HashMap[Stock.Value, OrderBook]

  def start()

  def addClient(client: Client)

  def handleOrder(order: Order)
}

case class SimpleExchangeEngine(clients: ArrayBuffer[Client]) extends ExchangeEngine{

  var orderBooks = new mutable.HashMap[Stock.Value, OrderBook]

  def start(): Unit ={
    Stock.values.foreach(s => orderBooks += s -> new OrderBook(s))
  }

  def addClient(client: Client): Unit = {
    clients += client
  }

  def handleOrder(order: Order): Unit = {
    orderBooks.get(order.stock) match {
      case Some(orderBook) =>
        orderBook.addOrder(order)
        orderBook.matchOrders(order)
      case None => println("No order book found for such stock")
    }
  }
}
