package exchange

import clients.Client
import orders.{Order, OrderBook}
import resourceManager.ResourceParseException
import stocks.Stock

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by kenneth on 09.03.18.
  */

abstract class ExchangeEngine {

  def clients: ArrayBuffer[Client]
  def start()

  var orderBooks = new mutable.HashMap[Stock.Value, OrderBook]


  def addClient(client: Client): Unit = {
    clients += client
  }

  def handleOrder(order: Order): Unit = {
    orderBooks.get(order.stock) match {
      case Some(orderBook) =>
        if (orderBook.addOrder(order))
          orderBook.matchOrders(order, 0)
      case None => throw ResourceParseException("No order book found for such stock " + order.stock.toString)
    }
  }
}
