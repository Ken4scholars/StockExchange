package clients

import orders.{BuyOrder, SellOrder}
import stocks.Stock

import scala.collection.mutable

/**
  * Created by kenneth on 08.03.18.
  */
class Client(val name: String, var usdBalance: Long, var stocks: mutable.HashMap[Stock.Value, Long]) {

  require(stocks.size == 4 && Stock.values.forall(s => stocks.contains(s)), "There must be exactly 4 stocks A, B, C and D")

  def buy(buyOrder: BuyOrder, price: Long): Unit ={
    assert(buyOrder.client == this)
    usdBalance -= price * buyOrder.volume
    stocks.update(buyOrder.stock, stocks(buyOrder.stock) + buyOrder.volume)

  }

  def sell(sellOrder: SellOrder, price: Long): Unit ={
    assert(sellOrder.client == this)
    usdBalance += price * sellOrder.volume
    stocks.update(sellOrder.stock, stocks(sellOrder.stock) - sellOrder.volume)
  }



}
