package resourceManager

import java.io.{File, PrintWriter}

import clients.Client
import orders.{BuyOrder, Order, SellOrder}
import stocks.Stock

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by kenneth on 08.03.18.
  */
object ResourceManager {

  def parseOrders(filepath: String, clients: ArrayBuffer[Client]): ArrayBuffer[Order] = {
    val orders = new ArrayBuffer[Order]
    parseFromFile(filepath).foreach(line => {
      val client = clients.find(_.name == line(0)) match {
        case Some(client: Client) => client
        case None => throw ResourceParseException("Client " + line(0) + " not found")
      }
      val orderClass = if (line(1).equals("b")) BuyOrder
      else if (!line(1).equals("s")) throw ResourceParseException("Operation " + line(1) + " is not supported")
      else SellOrder
      val stock = Stock.values.find(_.toString == line(2)) match {
        case Some(stock: Stock.Value) => stock
        case None => throw ResourceParseException("Stock " + line(2) + " not found")
      }
      orders.append(orderClass(client, stock, line(3).toLong, line(4).toLong))
    })
    orders

  }

  def parseClients(filepath: String): ArrayBuffer[Client] = {
    val clients = new ArrayBuffer[Client]
    parseFromFile(filepath).foreach(line => {
       clients.append(new Client(line(0), line(1).toLong,
         mutable.HashMap(
           Stock.A -> line(2).toLong,
           Stock.B -> line(3).toLong,
           Stock.C -> line(4).toLong,
           Stock.D -> line(5).toLong)
       ))
    })
    clients
  }

  def exportResult(filepath: String, clients: ArrayBuffer[Client]): Unit = {
    val pw = new PrintWriter(new File(filepath))

    clients.foreach(a => {
      pw.write(a.name + "\t" + a.usdBalance + "\t")
      a.stocks.toSeq.sortBy(_._1).foreach(s => pw.write(s._2 + "\t"))
      pw.write("\n")
    })

    pw.close()
  }

  private def parseFromFile(filepath: String): List[Array[String]] =
    Source.fromFile(filepath)
      .getLines
      .filter(!_.isEmpty)
      .toList
      .map(res => res
        .split("\\s+")
        .map(_.trim))

}
