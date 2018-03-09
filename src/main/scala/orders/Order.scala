package orders

import stocks.Stock
import clients.Client

/**
  * Created by kenneth on 08.03.18.
  */

object Order {
  var counter = 0
  def increment = {
    counter += 1
    counter
  }
}
trait Order {
  def client: Client
  def stock: Stock.Value
  def price: Long
  def volume: Long

  def serialNumber: Long

  def split(newVolume: Long): Order
}

case class BuyOrder(
                     client: Client,
                     stock: Stock.Value,
                     price: Long,
                     volume: Long)
  extends Order {

  var serialNumber: Long = Order.increment

  override def split(newVolume: Long): BuyOrder = {
    BuyOrder(client, stock, price, newVolume)
  }
}

case class SellOrder(
                      client: Client,
                      stock: Stock.Value,
                      price: Long,
                      volume: Long)
  extends Order {

  var serialNumber: Long = Order.increment

  override def split(newVolume: Long): SellOrder = {
    SellOrder(client, stock, price, newVolume)
  }
}
