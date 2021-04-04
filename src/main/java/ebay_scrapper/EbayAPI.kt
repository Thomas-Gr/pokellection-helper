package ebay_scrapper

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import java.lang.IllegalArgumentException

const val usdToEurRate = 0.858009

@JsonIgnoreProperties(ignoreUnknown = true)
data class Price(
    var currencyId: String,
    var value: String
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SellingStatus(
    var currentPrice: List<Price>,
    var convertedCurrentPrice: List<Price>,
    var sellingState: List<String>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Item(
    var itemId: List<String>,
    var title: List<String>,
    var galleryURL: List<String>,
    var viewItemURL: List<String>,
    var sellingStatus: List<SellingStatus>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchResult(
    var item: List<Item>)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(
    var searchResult: List<SearchResult>)
@JsonIgnoreProperties(ignoreUnknown = true)
data class FindingServiceResponseCompleted(
    var findCompletedItemsResponse: List<Response>)
@JsonIgnoreProperties(ignoreUnknown = true)
data class FindingServiceResponseOnGoing(
    var findItemsByKeywordsResponse: List<Response>)

val mapper = jacksonObjectMapper()
val ebayClient = OkHttpClient()

data class SaleInformation(var stats: DescriptiveStatistics, var ebaySalesUrl: Set<String>, var soldOnly: Boolean)

fun main() {
  val averagePrice = getSalesInformation("Charizard 4/102", true)

  println(averagePrice)
}

fun getSalesInformation(request: String, soldOnly: Boolean): SaleInformation? {
  val sales = getSales(request, soldOnly)

  val prices = sales.map {
    val price = it.sellingStatus[0].convertedCurrentPrice[0]

    when (price.currencyId) {
      "USD" -> price.value.toDouble() * usdToEurRate
      "EUR" -> price.value.toDouble()
      else -> throw IllegalArgumentException(price.currencyId)
    }
  }

  if (prices.isEmpty()) {
    return null
  }

  val stats = DescriptiveStatistics(prices.toDoubleArray())

  return SaleInformation(stats, sales.flatMap { it.viewItemURL }.toSet(), soldOnly)
}

fun getSales(request: String, soldOnly: Boolean): List<Item> {
  val operation = if (soldOnly) "findCompletedItems" else "findItemsByKeywords"

  val ebayRequest = Request.Builder()
      .url("https://svcs.ebay.com/services/search/FindingService/v1?" +
          "OPERATION-NAME=${operation}&" +
          "SERVICE-VERSION=1.7.0&" +
          "SECURITY-APPNAME=nishantm-9735-41a4-b488-c3d2b6525d11&" +
          "RESPONSE-DATA-FORMAT=JSON&" +
          "REST-PAYLOAD&" +
          "keywords=${request}&" +
          "paginationInput.entriesPerPage=100")
      .build()

  return try {
    val body = (ebayClient.newCall(ebayRequest).execute().body?.string() ?: "")
        .replace("@currencyId", "currencyId")
        .replace("__value__", "value")

    val response =
        if (soldOnly) mapper.readValue<FindingServiceResponseCompleted>(body).findCompletedItemsResponse[0]
        else mapper.readValue<FindingServiceResponseOnGoing>(body).findItemsByKeywordsResponse[0]
    response
        .searchResult[0]
        .item
        .filter { if (soldOnly) it.sellingStatus[0].sellingState.contains("EndedWithSales") else true }
  } catch (e: Exception) {
    e.printStackTrace()
    listOf()
  }
}
