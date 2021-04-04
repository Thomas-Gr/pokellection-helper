package ebay_scrapper

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val LINK_PATTERN = Regex(".*/([0-9]+\\?hash=.*)")
private val DATE_PATTERN = DateTimeFormatter.ofPattern("MMM d, u , h:ma")
private val PRICE_PATTERN = Regex("([A-Z]+) \\$?([0-9.,]+)")
private val REPUTATION_PATTERN = Regex("\\(([-0-9]+) ?\\)")

class EbayScrapper(driverPath: String, private val reprocessAll: Boolean) {
  private val driver: WebDriver

  init {
    System.setProperty("webdriver.chrome.driver", driverPath)
    driver = ChromeDriver(ChromeOptions().addArguments("headless"))
  }

  fun scrap(page: String, alreadyScrappedKeys: Set<String>): List<EbayData> {
    val data = mutableListOf<EbayData>()
    var alreadyProcessedData = 0
    val limit = if (reprocessAll) 1000 else 7
    for (i in 1..50) {
      val readListingPage = readListingPage(i, page)

      if (alreadyProcessedData > limit || readListingPage == null) {
        break
      } else {
        readListingPage
            .filter {
              val pageId = LINK_PATTERN.find(it)?.destructured?.component1().orEmpty()

              if (alreadyScrappedKeys.contains(pageId)) {
                alreadyProcessedData++
              }
              !alreadyScrappedKeys.contains(pageId)
            }
            .mapNotNull { readPage(it) }
            .forEach { data.add(it) }
      }
    }

    return data
  }

  private fun readListingPage(i: Int, page: String): List<String>? {
    println("Page $i - $page")
    driver.get("https://www.ebay.com/sch/i.html?_from=R40&_nkw=$page&_sacat=0&rt=nc&LH_Sold=1&LH_Complete=1&_ipg=200&_pgn=$i")

    if (i > 1 && !driver.findElements(By.className("pagination__item"))
            .map { it.text }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .contains(i)) {
      return null
    }

    return driver.findElements(By.className("s-item"))
        .map {
          val link = it.findElements(By.className("s-item__link"))
          val striked = it.findElements(By.className("STRIKETHROUGH")).isNotEmpty()
          if (!striked && link.isNotEmpty()) {
            link[0].getAttribute("href").orEmpty()
          } else {
            ""
          }
        }
        .filter { it.isNotEmpty() }
  }

  private fun readPage(page: String): EbayData? {
    for (i in 1..5) {
      try {
        redirectPageIfNeeded(driver.get(page))

        val itemStillSellable = driver.findElements(By.id("binBtn_btn"))
        if (itemStillSellable.isNotEmpty()) {
          return null
        }

        if (driver.title.contains("Error Page")) {
          return null
        }

        return extractData(page)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      println("Retry on page $page")
      Thread.sleep(i * 500L)
    }

    throw IllegalStateException("Couldn't parse $page")
  }

  private fun extractData(page: String): EbayData? {
    val id = LINK_PATTERN.find(page)?.destructured?.component1().orEmpty()
    val title = parsedText(driver.findElement(By.id("itemTitle")))
    val condition = parsedText(driver.findElement(By.id("vi-itm-cond")))
    val endDateCells = driver.findElements(By.id("bb_tlft"))

    if (endDateCells.isEmpty()) {
      return null
    }

    val endDate = LocalDateTime.parse(parsedText(endDateCells[0]), DATE_PATTERN).toString()
    val priceAndBids = getPriceAndNumberOfBids()
    val (currency, amount) = PRICE_PATTERN.find(priceAndBids.first)?.destructured!!
    val priceAmount = amount.replace(",", "").toDouble()

    val sellerReputation = REPUTATION_PATTERN.find(parsedText(
        driver.findElement(By.className("mbg-l"))))?.destructured?.component1()!!

    val metadata = driver.findElements(By.cssSelector("div.section tr"))
        .flatMap {extractMetadata(it) }

    return EbayData(
        id,
        title,
        condition,
        endDate,
        SellPrice(currency, priceAmount),
        priceAndBids.second,
        sellerReputation,
        metadata)
  }

  private fun extractMetadata(it: WebElement): List<Metadata> {
    val cells = it.findElements(By.cssSelector("td"))

    return if (cells.size == 4) {
      listOf(
          Metadata(parsedText(cells[0]).substringBeforeLast(":"), parsedText(cells[1])),
          Metadata(parsedText(cells[2]).substringBeforeLast(":"), parsedText(cells[3])))
    } else if (cells.size == 2) {
      listOf(
          Metadata(parsedText(cells[0]).substringBeforeLast(":"), parsedText(cells[1])))
    } else {
      listOf()
    }
  }

  private fun redirectPageIfNeeded(pageToRead: Unit) {
    val originalItem = driver.findElements(By.className("fake-btn")).filter { it.text == "View original item" }
    if (originalItem.isNotEmpty()) {
      return driver.get(originalItem[0].getAttribute("href"))
    }

    val originalPage = driver.findElements(By.className("nodestar-item-card-details__view-link"))
    if (originalPage.isNotEmpty()) {
      return driver.get(originalPage[0].getAttribute("href"))
    }
    return pageToRead
  }

  private fun parsedText(cell: WebElement) = cell.text.orEmpty().trim()

  private fun getPriceAndNumberOfBids(): Pair<String, Int> {
    val prices = driver.findElements(By.className("vi-price"))
    if (prices.isNotEmpty()) {
      return Pair(parsedText(prices[0]), 0)
    }

    val bidPrice = driver.findElements(By.className("prcIsum_bidPrice"))

    val price =
        if (bidPrice.isNotEmpty()) {
          parsedText(prices[0])
        } else {
          parsedText(driver.findElement(By.className("vi-VR-cvipPrice")))
        }

    val bid = parsedText(driver.findElement(By.id("vi-VR-bid-lnk")))

    return Pair(price, bid.substringBefore(" ").toInt())
  }

  fun close() {
    driver.close()
  }
}
