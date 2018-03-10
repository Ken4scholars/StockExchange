package exchange

import clients.Client
import orders.OrderBook
import stocks.Stock

import scala.collection.mutable.ArrayBuffer

/**
  * Created by kenneth on 09.03.18.
  */
case class ComplexExchangeEngine(clients: ArrayBuffer[Client]) extends ExchangeEngine{
  def start(): Unit ={
    Stock.values.foreach(s => orderBooks += s -> new OrderBook(s, exactMatch = false, checkBalance = true))
  }
}
