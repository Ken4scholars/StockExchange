import exchange.{ComplexExchangeEngine, ConfigurableExchangeEngine, ExchangeEngine, SimpleExchangeEngine}
import resourceManager.ResourceManager
import org.rogach.scallop._

/**
  * Created by kenneth on 08.03.18.
  */
object Main {

  def main(args: Array[String]): Unit = {
    val baseDir = "src/main/resources/"
    val clients = ResourceManager.parseClients(baseDir + "clients.txt")
    val orders = ResourceManager.parseOrders(baseDir + "orders.txt", clients)
    if(clients.isEmpty || orders.isEmpty)return
    var exchangeEngine: ExchangeEngine = null
    println(args)
    val conf = new Conf(args)
    (conf.exactMatch.apply(), conf.checkBalance.apply()) match {
      case (true, false) => exchangeEngine = SimpleExchangeEngine(clients)
      case (false, true) => exchangeEngine = ComplexExchangeEngine(clients)
      case (bool1, bool2) => exchangeEngine = ConfigurableExchangeEngine(clients, bool1, bool2)
    }
    exchangeEngine.start()
    orders.foreach(o => exchangeEngine.handleOrder(o))
    ResourceManager.exportResult(baseDir + "result.txt", clients)

  }

}


class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  version("StockExchangeEngine 1.0.0 (c) 2018 Kenneth Nwafor")
  banner("""Usage: sbt run [OPTIONS]
           |E.g: sbt run --exact-match --checkBalance
           |StockExchangeEngine is a simple matching engine for orders in a stock market.
           |Options:
           |""".stripMargin)
  footer("\nIf you have any questions, please consult Kenneth")
  val exactMatch = opt[Boolean](argName = "exact-match", required = false, default = Option(false),
    descr = "Match only when both stock price and volume match.")
  val checkBalance = opt[Boolean](argName = "check-balance", required = false, default = Option(false),
    descr = "Check price and volume balances for validity before processing orders.")
  verify()
}