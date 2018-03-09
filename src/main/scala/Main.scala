import exchange.SimpleExchangeEngine
import resourceManager.ResourceManager

/**
  * Created by kenneth on 08.03.18.
  */
object Main {

  def main(args: Array[String]): Unit = {
    val baseDir = "src/main/resources/"
    val clients = ResourceManager.parseClients(baseDir + "clients.txt")
    val orders = ResourceManager.parseOrders(baseDir + "orders.txt", clients)
    val exchangeSystem = SimpleExchangeEngine(clients)
    exchangeSystem.start()
    orders.foreach(o => exchangeSystem.handleOrder(o))
    ResourceManager.exportResult(baseDir + "result.txt", clients)

  }

}
