package ebay_scrapper

import com.fasterxml.jackson.annotation.JsonProperty

data class SellPrice(
    @JsonProperty("currency") val currency: String,
    @JsonProperty("amount") val amount: Double)

data class Metadata(
    @JsonProperty("key") val key: String,
    @JsonProperty("value") val value: String)

data class EbayData(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("condition") val condition: String,
    @JsonProperty("endedDate") val endedDate: String,
    @JsonProperty("price") var price: SellPrice,
    @JsonProperty("bids") val bids: Int,
    @JsonProperty("sellerReputation") val sellerReputation: String,
    @JsonProperty("meta") val meta: List<Metadata>)
