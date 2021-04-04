package ebay_scrapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.pow
import kotlin.streams.asSequence

private val OBJECT_MAPPER = ObjectMapper()

private val CURRENCIES = mapOf(
    Pair("US", "USD"),
    Pair("JPY", "JPY"),
    Pair("C", "CAD"),
    Pair("AU", "AUD")
)

fun main() {
  /*
  val ebayData = readData()

  printDistribution(ebayData)
  printDataPerAge(ebayData)
  */

  val groupAllDataPerFile = groupAllDataPerFile()

  groupAllDataPerFile.filter { it.key == "charizard_4_102.json" }.entries.forEach {
    printDistribution(it.value)
    printDataPerAge(it.value)
  }

}

private fun printDataPerAge(ebayData: List<EbayData>) {
  val dataPerAge = ebayData
      .groupBy { ChronoUnit.WEEKS.between(LocalDateTime.parse(it.endedDate), LocalDateTime.now()) }
      .toSortedMap()

  val ageToStats = dataPerAge.mapValues { getStats(it.value) }

  ageToStats.forEach {
    println(
        "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s".format(
            it.key,
            it.value.n,
            it.value.min,
            it.value.max,
            it.value.mean,
            it.value.standardDeviation,
            it.value.getPercentile(20.0),
            it.value.getPercentile(50.0),
            it.value.getPercentile(80.0),
            it.value.sum))
  }
}

private fun printDistribution(data: List<EbayData>) {
  val intervalLimits = (-2..5).flatMap { listOf(1, 2, 5).map { num -> num * 10.0.pow(it) } }

  val distribution = data
      .map { it.price.amount }
      .groupBy { price -> intervalLimits.find { limit -> price <= limit } }
      .mapValues { it.value.count() }

  distribution.entries.forEach { println("%s\t%s".format(it.key, it.value)) }
}

private fun getStats(value: List<EbayData>): DescriptiveStatistics {
  val descriptiveStatistics = DescriptiveStatistics()
  value
      .map { it.price.amount }
      .forEach { descriptiveStatistics.addValue(it) }
  return descriptiveStatistics
}

private fun groupAllDataPerFile(): Map<String, List<EbayData>> {
  val ebayData = readFiles()
  val conversionRates =
      getAllConvertionRates(ebayData.values.asSequence().flatten())

  return ebayData.mapValues { postProcess(it.value, conversionRates) }
}

private fun readFiles(): Map<String, List<EbayData>> {
  val objectMapper = ObjectMapper()
  val memoryFolder = File("/Users/grillett/dev/pokellection-helper/out/ebay")

  return memoryFolder.walk()
      .filter { !it.isDirectory }
      .filter { !it.name.contains("DS_Store") }
      .map {
        it.name to objectMapper
            .readValue(it.readText(), object : TypeReference<List<EbayData>>() {}).orEmpty()
      }
      .toMap()
}

private fun readData(): List<EbayData> {
  val ebayData = readFiles()
      .values
      .flatten()
      .toList()

  val conversionRates = ebayData.map { it.price.currency }.distinct()
      .filter { it != "US" }
      .map { it to getConversionRates(symbol(it), "USD", "2020-08-01", LocalDate.now().toString()) }
      .toMap()

  postProcess(ebayData, conversionRates)
  return ebayData
}

private fun symbol(it: String) = if (CURRENCIES.containsKey(it)) CURRENCIES[it] ?: error("") else it

fun postProcess(data: List<EbayData>, conversionRates: Map<String, Map<String, Double>>): List<EbayData> {
  return data
      .filter { !it.title.contains("Proxy") }
      .filter { !it.title.contains("Fake") }
      .map {
        if (it.price.currency != "US") {
          val ratio = getRate(
              LocalDateTime.parse(it.endedDate).toLocalDate(),
              it.price.currency,
              conversionRates)
          it.price = SellPrice("US", it.price.amount / ratio)
        }

        it
      }
}

private fun getAllConvertionRates(data: Sequence<EbayData>): Map<String, Map<String, Double>> {
  return data
      .map { it.price.currency }
      .distinct()
      .filter { it != "US" }
      .map { it to getConversionRates(symbol(it), "USD", "2020-08-01", LocalDate.now().toString()) }.toList()
      .toMap()
}


fun getConversionRates(source: String, target: String, startDate: String, endDate: String): Map<String, Double> {
  val url = URL("https://api.exchangeratesapi.io/history?start_at=$startDate&end_at=$endDate&base=$target&symbols=$source")

  with(url.openConnection() as HttpURLConnection) {
    requestMethod = "GET"
    inputStream.bufferedReader().use {
      return it.lines().asSequence().flatMap { line ->
        OBJECT_MAPPER.readTree(line)
            .path("rates")
            .fields()
            .asSequence()
            .map { a -> a.key!! to a.value.path(source).asDouble()!! }
      }.toMap()
    }
  }
}

private fun getRate(date: LocalDate, currency: String, conversionRates: Map<String, Map<String, Double>>): Double {
  val rates = conversionRates[currency].orEmpty()
  var newDate = date
  var i = 0
  while (!rates.containsKey(newDate.toString()) && i++ < 7) {
    newDate = newDate.minusDays(1)
  }
  return rates[newDate.toString()] ?: error("")
}