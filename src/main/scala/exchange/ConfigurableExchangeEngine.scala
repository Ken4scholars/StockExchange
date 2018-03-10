package exchange

import clients.Client
import orders.OrderBook
import stocks.Stock

import scala.collection.mutable.ArrayBuffer

/**
  * Created by kenneth on 09.03.18.
  */
case class ConfigurableExchangeEngine(clients: ArrayBuffer[Client],
                                      exactMatch: Boolean,
                                      checkBalance: Boolean) extends ExchangeEngine {
  def start(): Unit ={
    Stock.values.foreach(s => orderBooks += s -> new OrderBook(s, exactMatch = exactMatch, checkBalance = checkBalance))
  }
}
