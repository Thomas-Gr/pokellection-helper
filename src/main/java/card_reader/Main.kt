package card_reader

import com.google.common.base.Strings
import common.APP_DATA_SOURCE_PATH
import common.PATH_PREFIX
import ebay_scrapper.getSalesInformation
import exporter.getAllSeries
import types.Card
import java.io.File


data class CardData(var card: Card, var set: String, var key: String)


fun main() {
  performOcr()

  val dictionary = generateDictionary()

  val cards = listAllCards(dictionary)

  for (card in cards) {
    val result = getSalesInformation(card.value.key, true)
        ?: getSalesInformation(card.value.key, false)

    if (result != null) {
      println(card.value.key + ": " + result.stats + " " + result.ebaySalesUrl)
    } else {
      println("No price found for " + card.value.key)
    }
  }
}

private fun listAllCards(dictionary: Map<Pair<String, String>, MutableSet<CardData>>): Map<String, CardData> {
  return File("${PATH_PREFIX}pokellection-helper/src/main/resources/ocr/text/")
      .walk()
      .filter { !it.isDirectory && !it.name.contains("DS_Store") }
      .map { it.absoluteFile }
      .map {
        val ocr = it.readText(Charsets.UTF_8)

        it.name.replace(".ocr", "") to
            dictionary.filterKeys { key -> ocr.contains(key.first) && ocr.contains(key.second) }
                .map { entry -> entry.value }
                .first()
                .first()
      }
      .toMap()
}

private fun performOcr() {
  val texts = File("${PATH_PREFIX}pokellection-helper/src/main/resources/ocr/text/")
      .walk()
      .filter { !it.isDirectory && !it.name.contains("DS_Store") }
      .map { it.absoluteFile }
      .map { it.name.replace(".ocr", "") to it.readText(Charsets.UTF_8) }
      .toMap()

  val imagesToProcess = File("${PATH_PREFIX}pokellection-helper/src/main/resources/ocr/image/")
      .walk()
      .filter { !it.isDirectory && !it.name.contains("DS_Store") }
      .filter { !texts.containsKey(it.name) }
      .toList()

  if (imagesToProcess.isNotEmpty()) {
    val imageOcrConverter = ImageOcrConverter()
    val ocrResult = imageOcrConverter.read(imagesToProcess)

    for (i in ocrResult.indices) {
      File("${PATH_PREFIX}pokellection-helper/src/main/resources/ocr/text/" + imagesToProcess[i].name + ".ocr")
          .writeText(ocrResult[i])
    }
  }
}

private fun generateDictionary(): Map<Pair<String, String>, MutableSet<CardData>> {
  val allSeries = getAllSeries(File(APP_DATA_SOURCE_PATH))
      .filter { EX_ERA.contains(it.name) }

  val dictionary = mutableMapOf<Pair<String, String>, MutableSet<CardData>>()

  for (serie in allSeries) {
    val count = serie.cards.values.filter { it.number != -42 }.count()
    for (card in serie.cards.values) {
      if (card.number == -42 || card.japaneseName == null) {
        continue
      }
      val cardNumber = "%s/%s".format(
          Strings.padStart(card.number.toString(), 3, '0'),
          Strings.padStart(count.toString(), 3, '0'))

      val cardData = CardData(card, serie.name, "${card.name}+${cardNumber}")

      dictionary.compute(Pair(cardNumber, card.japaneseName)) { _, v ->
        if (v == null) mutableSetOf(cardData) else { v.add(cardData); v }
      }
    }
  }

  return dictionary.toMap()
}