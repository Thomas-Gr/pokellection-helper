package mosaic_website

import com.github.mustachejava.DefaultMustacheFactory
import com.google.common.base.Strings.padStart
import common.APP_DATA_SOURCE_PATH
import common.WEBSITE_PATH
import exporter.getAllSeries
import types.Card
import types.Serie
import utils.PokemonsData.POKEMONS
import mosaic_website.templates.*
import java.io.File
import java.util.*

fun main() {
  Exporter(true).run(File(APP_DATA_SOURCE_PATH))
  Exporter(false).run(File(APP_DATA_SOURCE_PATH))
}

class Exporter(private val inFrench: Boolean) {
  private val keysToTitle = mutableMapOf<String, String>()

  fun run(folder: File) {
    val allCards = groupSimilarCardsTogether(getAllSeries(folder).filter { EXPANSIONS.contains(it.name) })

    val indexCards = allCards
        .flatMap { it.cards.values }
        .filter { INDEX_CARDS.contains(nameHashCode(it.picture)) }
        .map { toCardData(it) }
        .map {
          CardBlock(
              it.name,
              nameHashCode(it.picture).toString(),
              (it.artist ?: ""),
              hash(it.artist),
              "",
              false)
        }
        .distinct()

    val cardsPerArtist = groupCardsPerArtist(allCards)
    val createListingPokemons = createListingPokemons()
    val createListingArtists = createListingArtists(cardsPerArtist)
    val createListingExpansions = createListingExpansions()

    val pokemons = createFilesForListing(true, groupCardsPerPokemon(allCards))
    val artists = createFilesForListing(true, cardsPerArtist)
    val expansions = createFilesForListing(false, groupCardsPerExpansions(allCards))

    writeFiles(
        pokemons,
        createListingArtists,
        createListingExpansions,
        artists,
        createListingPokemons,
        cardsPerArtist,
        expansions,
        allCards,
        indexCards)
  }

  private fun writeFiles(
      pokemons: Map<String, List<CardBlock>>,
      createListingArtists: List<Option>,
      createListingExpansions: List<OptionGroup>,
      artists: Map<String, List<CardBlock>>,
      createListingPokemons: List<Option>,
      cardsPerArtist: Map<String, SortedMap<CardData, MutableSet<String>>>,
      expansions: Map<String, List<CardBlock>>, allCards: List<Serie>,
      indexCards: List<CardBlock>) {
    val mf = DefaultMustacheFactory()
    val mustache =
        if (inFrench) mf.compile("src/main/java/website/templates/pageFr.mustache")
        else mf.compile("src/main/java/website/templates/page.mustache")

    pokemons.forEach {
      mustache.execute(
          File(WEBSITE_PATH + "%s%s.html".format(separator(), it.key)).writer(),
          Page(
              keysToTitle[it.key] ?: "",
              it.value,
              createListingPokemons(it.key),
              createListingArtists,
              createListingExpansions))
          .flush()
    }

    artists.forEach {
      mustache.execute(
          File(WEBSITE_PATH + "%s%s.html".format(separator(), it.key)).writer(),
          Page(
              keysToTitle[it.key] ?: "",
              it.value,
              createListingPokemons,
              createListingArtists(cardsPerArtist, it.key),
              createListingExpansions))
          .flush()
    }

    expansions.forEach {
      mustache.execute(
          File(WEBSITE_PATH + "%s%s.html".format(separator(), it.key)).writer(),
          Page(
              keysToTitle[it.key] ?: "",
              it.value,
              createListingPokemons,
              createListingArtists,
              createListingExpansions(it.key)))
          .flush()
    }

    val mustacheCards = mf.compile(
        if (inFrench) "src/main/java/website/templates/cardPageFr.mustache"
        else "src/main/java/website/templates/cardPage.mustache")

    cardsPages(allCards).forEach {
      mustacheCards.execute(
          File(WEBSITE_PATH + "%scard%s.html".format(separator(), it.id)).writer(),
          it)
          .flush()
    }

    val mustacheIndex =
        if (inFrench) mf.compile("src/main/java/website/templates/indexFr.mustache")
        else mf.compile("src/main/java/website/templates/index.mustache")

    mustacheIndex.execute(
        File(WEBSITE_PATH + "%sindex.html".format(separator())).writer(),
        Page(
            "",
            indexCards,
            createListingPokemons,
            createListingArtists,
            createListingExpansions))
        .flush()
  }

  private fun groupSimilarCardsTogether(input: List<Serie>): List<Serie> {
    val output = mutableMapOf<String, Serie>()
    val pictures = mutableSetOf<Pair<String, String>>()
    for (serie in input) {
      val name = localizedSetName(EXPANSIONS_GROUPS[serie.name] ?: serie.name ?: "")
      val expansion = output.getOrPut(name) { Serie(name, "") }

      serie.cards.values
          .filter { !BANNED_CARDS.contains(it.picture) }
          .forEach {
            if (!pictures.contains(Pair(name, it.picture))) {
              expansion.addCard(it)

              pictures.add(Pair(name, it.picture))
            }
          }
    }

    return output.values.toList()
  }

  private fun createListingExpansions(selectedKey: String = ""): List<OptionGroup> {
    return SETS.map { set ->
      val values = set.second
          .map { localizedSetName(EXPANSIONS_GROUPS[it] ?: it) }
          .distinct()
          .map { Option(hash(it), it, if (selectedKey == hash(it)) "selected" else "") }

      OptionGroup(localizedSetName(set.first), values)
    }
  }

  private fun createListingArtists(cardsPerArtist: Map<String, Map<CardData, MutableSet<String>>>,
                                   selectedKey: String = ""): List<Option> {
    return cardsPerArtist
        .filter { it.value.isNotEmpty() }
        .map { Pair(it.value.iterator().next().key.artist ?: "", it.value.size) }
        .filter { it.first.isNotEmpty() }
        .sortedWith(compareBy<Pair<String, Int>> { -it.second }.thenBy { it.first })
        .map {
          Option(
              hash(it.first),
              "(%s) %s".format(it.second, it.first),
              if (selectedKey == hash(it.first)) "selected" else "")
        }
  }

  private fun cardsPages(allCards: List<Serie>): List<CardPage> {
    return allCards
        .map { getValidCards(it) }
        .flatten()
        .map { toCardData(it) }
        .map {
          val key = nameHashCode(it.picture).toString()

          CardPage("%s (%s)".format(it.name, it.artist), key, "%s.jpg/%s".format(key, key))
        }
  }

  private fun createListingPokemons(selectedKey: String = ""): List<Option> {
    return POKEMONS
        .filterKeys { it <= 251 }
        .map {
          Option(
              "pokemon-" + it.key.toString(),
              "%s - %s".format(padStart(it.key.toString(), 3, '0'), localizedPokemonName(it.value)),
              if (selectedKey == "pokemon-" + it.key.toString()) "selected" else "")
        }
  }

  private fun groupCardsPerPokemon(allCards: List<Serie>): MutableMap<String, MutableMap<CardData, MutableSet<String>>> {
    val cardsPerPokemon = mutableMapOf<String, MutableMap<CardData, MutableSet<String>>>()

    allCards
        .map { Pair(it.name, getValidCards(it)) }
        .forEach { expansion ->
          expansion.second.map {
            val key = "pokemon-" + it.pokemonNumber.toString()

            if (inFrench) {
              keysToTitle.computeIfAbsent(key) { _ -> it.frenchName }
            } else {
              keysToTitle.computeIfAbsent(key) { _ -> it.name }
            }

            cardsPerPokemon
                .getOrPut(key) { mutableMapOf() }
                .getOrPut(toCardData(it)) { mutableSetOf() }
                .add(expansion.first)
          }
        }

    return cardsPerPokemon
  }

  private fun groupCardsPerArtist(allCards: List<Serie>): Map<String, SortedMap<CardData, MutableSet<String>>> {
    val cardsPerArtist = mutableMapOf<String, MutableMap<CardData, MutableSet<String>>>()

    allCards
        .map { Pair(it.name, getValidCards(it)) }
        .forEach { expansion ->
          expansion.second.map {
            val key = hash(it.illustrator)

            keysToTitle.computeIfAbsent(key) { _ -> it.illustrator }

            cardsPerArtist
                .getOrPut(key) { mutableMapOf() }
                .getOrPut(toCardData(it)) { mutableSetOf() }
                .add(expansion.first)
          }
        }

    return cardsPerArtist
        .map { map -> map.key to map.value.toSortedMap(compareBy<CardData> { it.number }.thenBy { it.name }) }
        .toMap()
  }

  private fun groupCardsPerExpansions(allCards: List<Serie>): Map<String, SortedMap<CardData, MutableSet<String>>> {
    val cardsPerExpansion = mutableMapOf<String, MutableMap<CardData, MutableSet<String>>>()

    allCards
        .map { Pair(it.name, getValidCards(it)) }
        .forEach { expansion ->
          val key = hash(expansion.first)

          if (inFrench) {
            keysToTitle.computeIfAbsent(key) { ENGLISH_TRANSLATIONS[expansion.first] ?: expansion.first }
          } else {
            keysToTitle.computeIfAbsent(key) { expansion.first }
          }

          expansion.second.map {
            cardsPerExpansion
                .getOrPut(key) { mutableMapOf() }
                .getOrPut(toCardData(it)) { mutableSetOf() }
                .add(expansion.first)
          }
        }

    return cardsPerExpansion
        .map { map -> map.key to map.value.toSortedMap(compareBy<CardData> { it.number }.thenBy { it.name }) }
        .toMap()
  }

  private fun createFilesForListing(
      showExpansion: Boolean,
      groupedCards: Map<String, Map<CardData, MutableSet<String>>>): Map<String, List<CardBlock>> {
    var i = 0;
    return groupedCards
        .map { pokemon ->
          val cards = pokemon.value

          val list = cards
              .map {
                CardBlock(
                    it.key.name,
                    nameHashCode(it.key.picture).toString(),
                    (it.key.artist ?: ""),
                    hash(it.key.artist),
                    if (showExpansion) it.value.joinToString(", ") else "",
                    i++ % 25 == 0)
              }

          pokemon.key to list
        }
        .toMap()
  }

  private fun getValidCards(serie: Serie) = serie.cards.values
      .filter { TYPES.contains(it.type) }
      .filter { nameHashCode(it.picture) != 1882713931 }
      .filter { nameHashCode(it.picture) != -1907326878 }

  private fun hash(name: String?): String =
      name?.replace("""[$,. '"@]""".toRegex(), "_") ?: "unknown"
  private fun nameHashCode(name: String?): Int = name?.hashCode() ?: 0

  private fun toCardData(card: Card) =
      CardData(GROUPED_CARDS[card.picture] ?: card.picture,
          //card.picture,
          card.illustrator,
          UPDATED_NAMES[localizedPokemonName(card)] ?: localizedPokemonName(card),
          padStart(card.pokemonNumber.toString(), 3, '0'))

  // i18n

  private fun separator(): String {
    return if (inFrench) {
      "/fr/"
    } else {
      ""
    }
  }

  private fun localizedPokemonName(card: Card): String {
    return if (inFrench) {
      card.frenchName
    } else {
      card.name
    }
  }

  private fun localizedPokemonName(name: String): String {
    return if (inFrench) {
      POKEMONS_TO_FRENCH[name] ?: name
    } else {
      name
    }
  }

  private fun localizedSetName(name: String): String {
    return if (inFrench) {
      FRENCH_TRANSLATIONS[name] ?: name
    } else {
      ENGLISH_TRANSLATIONS[name] ?: name
    }
  }

}