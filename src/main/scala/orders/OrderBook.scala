package orders

import stocks.Stock

/**
  * Created by kenneth on 08.03.18.
  */
class OrderBook(val stock: Stock.Value) {
  var buyOrders: List[BuyOrder] = List[BuyOrder]()
  var sellOrders: List[SellOrder] = List[SellOrder]()

  def addOrder(order: Order) {
    assert(stock == order.stock)
    order match {
      case buy: BuyOrder ⇒

//        if (!checkBuyOrder(buy)){
//          println("The Order costs more than client's USD balance")
//        }
//        else
          buyOrders = (buy :: buyOrders).sortBy(o => (-o.price, o.serialNumber))
      case sell: SellOrder ⇒
//        if (checkSellOrder(sell)){
//          println("Order caThe client has less stock than he wants to sell")
//        }
//        else
          sellOrders = (sell :: sellOrders).sortBy(o => (o.price, o.serialNumber))
    }
  }


  def matchOrders(newOrder: Order) {
    if (buyOrders.nonEmpty && sellOrders.nonEmpty) {
      if (buyOrders.head.client.equals(sellOrders.head.client)){
        if (newOrder.isInstanceOf[BuyOrder]){
          sellOrders = sellOrders.tail
        }
      }
      val topOfBook = (buyOrders.head, sellOrders.head)
      topOfBook match {
        case (buyOrder, sellOrder) if buyOrder.price < sellOrder.price ||  buyOrder.client == sellOrder.client ⇒ // no match
        case (buyOrder, sellOrder) if buyOrder.price >= sellOrder.price  && buyOrder.volume == sellOrder.volume ⇒

          trade(buyOrder, sellOrder, if(buyOrder == newOrder) sellOrder.price else buyOrder.price)
          buyOrders = buyOrders.tail
          sellOrders = sellOrders.tail
//          matchOrders(newOrder)
        case (buyOrder, sellOrder) if buyOrder.price >= sellOrder.price  && buyOrder.volume < sellOrder.volume ⇒
          val matchingSells = sellOrder.split(buyOrder.volume)
          val remainingSells = sellOrder.split(sellOrder.volume - buyOrder.volume)
          trade(buyOrder, matchingSells, if(buyOrder == newOrder) sellOrder.price else buyOrder.price)
          buyOrders = buyOrders.tail
          sellOrders = remainingSells :: sellOrders.tail

          matchOrders(newOrder)
        case (buyOrder, sellOrder) if buyOrder.price >= sellOrder.price && buyOrder.volume > sellOrder.volume ⇒
          val matchingBuys = buyOrder.split(sellOrder.volume)
          val remainingBuys = buyOrder.split(buyOrder.volume - sellOrder.volume)
          trade(matchingBuys, sellOrder, if(buyOrder == newOrder) sellOrder.price else buyOrder.price)
          buyOrders = remainingBuys :: buyOrders.tail
          sellOrders = sellOrders.tail
          matchOrders(newOrder)
      }
    }
  }

  private def checkBuyOrder(buyOrder: BuyOrder): Boolean =
    buyOrder.client.usdBalance >= buyOrder.price * buyOrder.volume

  private def checkSellOrder(sellOrder: SellOrder): Boolean =
   sellOrder.client.stocks.getOrElse(sellOrder.stock, 0L) >= sellOrder.volume

  def trade(buyOrder: BuyOrder, sellOrder: SellOrder, price: Long): Unit = {
    buyOrder.client.buy(buyOrder, price)
    sellOrder.client.sell(sellOrder, price)
  }

}