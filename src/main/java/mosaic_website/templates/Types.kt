package mosaic_website.templates

data class CardPage(var pageTitle: String, var id: String, var path: String)
data class CardBlock(var name: String, var id: String, var artist: String, var id_artist: String, var expansion: String, var ad: Boolean)
data class Option(var value: String, var text: String, var selected: String = "")
data class OptionGroup(var title: String, var options: List<Option>)
data class Page(
    var pageTitle: String,
    var cards: List<CardBlock>,
    var pokemon_options: List<Option>,
    var artist_options: List<Option>,
    var expansion_options: List<OptionGroup>)
data class CardData(
    val picture: String,
    val artist: String?,
    val name: String,
    val number: String)
