package it.unica.blockchain.externaldata.ICOAPIs.ethplorerAPIs

import java.net.SocketTimeoutException

import scalaj.http.Http
import it.unica.blockchain.externaldata.ICOAPIs.Utils

object EthplorerAPI {

  private def apiKey = "freekey"

  private def baseUrl = "https://api.ethplorer.io/"

  /**
    * @param tokenAddress
    * @return token Name
    */
  def getTokenNameByContractAddress(tokenAddress: String): String = {
    Utils.getMapper.readValue[EthplorerTokenInfo](
      send(
        String.join("", this.baseUrl, "getTokenInfo/", tokenAddress, "?apiKey=", apiKey)
      )
    ).name
  }

  /**
    * @param tokenAddress
    * @return token symbol
    */
  def getTokenSymbolByContractAddress(tokenAddress: String): String = {
    Utils.getMapper.readValue[EthplorerTokenInfo](
      send(
        String.join("", this.baseUrl, "getTokenInfo/", tokenAddress, "?apiKey=", apiKey)
      )
    ).symbol
  }

  /**
    * @param tokenAddress
    * @param currency
    * @return token unit price
    */
  def getTokenPriceByContractAddress(tokenAddress: String, currency: String): Double = {
    val price = Utils.getMapper.readValue[EthplorerTokenInfo](
      send(
        String.join("", this.baseUrl, "getTokenInfo/", tokenAddress, "?apiKey=", apiKey)
      )
    ).price
    try{
      if(price.asInstanceOf[Map[String, Any]]("currency").equals(currency)){
        price.asInstanceOf[Map[String, Any]]("rate").asInstanceOf[String].toDouble
      } else {
        0
      }
    } catch {
      case _: Exception => 0
    }
  }

  def checkIfTokenExists(tokenAddress: String): EthplorerTokenInfo = {
    Utils.getMapper.readValue[EthplorerTokenInfo](
      send(
        String.join("", this.baseUrl, "getTokenInfo/", tokenAddress, "?apiKey=", apiKey)
      )
    )
  }

  private def send(url: String): String = {
    try{
      Http(url).asString.body
    } catch {
      case _ : SocketTimeoutException => {
        send(url)
      }
    }
  }
}
