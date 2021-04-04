package ebay_scrapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import common.DRIVER
import common.PATH_PREFIX
import java.io.File

val cards = listOf(
    "Clefable 1/64",
    "Electrode 2/64",
    "Flareon 3/64",
    "Jolteon 4/64",
    "Kangaskhan 5/64",
    "Mr. Mime 6/64",
    "Nidoqueen 7/64",
    "Pidgeot 8/64",
    "Pinsir 9/64",
    "Scyther 10/64",
    "Snorlax 11/64",
    "Vaporeon 12/64",
    "Venomoth 13/64",
    "Victreebel 14/64",
    "Vileplume 15/64",
    "Wigglytuff 16/64",
    "Aerodactyl 1/62",
    "Articuno 2/62",
    "Ditto 3/62",
    "Dragonite 4/62",
    "Gengar 5/62",
    "Haunter 6/62",
    "Hitmonlee 7/62",
    "Hypno 8/62",
    "Kabutops 9/62",
    "Lapras 10/62",
    "Magneton 11/62",
    "Moltres 12/62",
    "Muk 13/62",
    "Raichu 14/62",
    "Zapdos 15/62",
    "Dark Alakazam 1/82",
    "Dark Arbok 2/82",
    "Dark Blastoise 3/82",
    "Dark Charizard 4/82",
    "Dark Dragonite 5/82",
    "Dark Dugtrio 6/82",
    "Dark Golbat 7/82",
    "Dark Gyarados 8/82",
    "Dark Hypno 9/82",
    "Dark Machamp 10/82",
    "Dark Magneton 11/82",
    "Dark Slowbro 12/82",
    "Dark Vileplume 13/82",
    "Dark Weezing 14/82",
    "Here Comes Team Rocket! 15/82",
    "Rocket's Sneak Attack 16/82",
    "Rainbow Energy 17/82",
    "Alakazam 1/102",
    "Blastoise 2/102",
    "Chansey 3/102",
    "Charizard 4/102",
    "Clefairy 5/102",
    "Gyarados 6/102",
    "Hitmonchan 7/102",
    "Machamp 8/102",
    "Magneton 9/102",
    "Mewtwo 10/102",
    "Nidoking 11/102",
    "Ninetales 12/102",
    "Poliwrath 13/102",
    "Raichu 14/102",
    "Venusaur 15/102",
    "Zapdos 16/102",
    "Alakazam 1/130",
    "Blastoise 2/130",
    "Chansey 3/130",
    "Charizard 4/130",
    "Clefable 5/130",
    "Clefairy 6/130",
    "Gyarados 7/130",
    "Hitmonchan 8/130",
    "Magneton 9/130",
    "Mewtwo 10/130",
    "Nidoking 11/130",
    "Nidoqueen 12/130",
    "Ninetales 13/130",
    "Pidgeot 14/130",
    "Poliwrath 15/130",
    "Raichu 16/130",
    "Scyther 17/130",
    "Venusaur 18/130",
    "Wigglytuff 19/130",
    "Zapdos 20/130",
    "Blaine's Arcanine 1/132",
    "Blaine's Charizard 2/132",
    "Brock's Ninetales 3/132",
    "Erika's Venusaur 4/132",
    "Giovanni's Gyarados 5/132",
    "Giovanni's Machamp 6/132",
    "Giovanni's Nidoking 7/132",
    "Giovanni's Persian 8/132",
    "Koga's Beedrill 9/132",
    "Koga's Ditto 10/132",
    "Lt. Surge's Raichu 11/132",
    "Misty's Golduck 12/132",
    "Misty's Gyarados 13/132",
    "Rocket's Mewtwo 14/132",
    "Rocket's Zapdos 15/132",
    "Sabrina's Alakazam 16/132",
    "Blaine 17/132",
    "Giovanni 18/132",
    "Koga 19/132",
    "Sabrina 20/132",
    "Blaine's Moltres 1/132",
    "Brock's Rhydon 2/132",
    "Erika's Clefable 3/132",
    "Erika's Dragonair 4/132",
    "Erika's Vileplume 5/132",
    "Lt. Surge's Electabuzz 6/132",
    "Lt. Surge's Fearow 7/132",
    "Lt. Surge's Magneton 8/132",
    "Misty's Seadra 9/132",
    "Misty's Tentacruel 10/132",
    "Rocket's Hitmonchan 11/132",
    "Rocket's Moltres 12/132",
    "Rocket's Scyther 13/132",
    "Sabrina's Gengar 14/132",
    "Brock 15/132",
    "Erika 16/132",
    "Lt. Surge 17/132",
    "Misty 18/132",
    "The Rocket's Trap 19/132",
    "Ampharos 1/111",
    "Azumarill 2/111",
    "Bellossom 3/111",
    "Feraligatr 4/111",
    "Feraligatr 5/111",
    "Heracross 6/111",
    "Jumpluff 7/111",
    "Kingdra 8/111",
    "Lugia 9/111",
    "Meganium 10/111",
    "Meganium 11/111",
    "Pichu 12/111",
    "Skarmory 13/111",
    "Slowking 14/111",
    "Steelix 15/111",
    "Togetic 16/111",
    "Typhlosion 17/111",
    "Typhlosion 18/111",
    "Metal Energy 19/111",
    "Espeon 1/75",
    "Forretress 2/75",
    "Hitmontop 3/75",
    "Houndoom 4/75",
    "Houndour 5/75",
    "Kabutops 6/75",
    "Magnemite 7/75",
    "Politoed 8/75",
    "Poliwrath 9/75",
    "Scizor 10/75",
    "Smeargle 11/75",
    "Tyranitar 12/75",
    "Umbreon 13/75",
    "Unown A 14/75",
    "Ursaring 15/75",
    "Wobbuffet 16/75",
    "Yanma 17/75",
    "Ampharos 1/64",
    "Blissey 2/64",
    "Celebi 3/64",
    "Crobat 4/64",
    "Delibird 5/64",
    "Entei 6/64",
    "Ho-Oh 7/64",
    "Houndoom 8/64",
    "Jumpluff 9/64",
    "Magneton 10/64",
    "Misdreavus 11/64",
    "Porygon2 12/64",
    "Raikou 13/64",
    "Suicune 14/64",
    "Dark Ampharos 1/105",
    "Dark Crobat 2/105",
    "Dark Donphan 3/105",
    "Dark Espeon 4/105",
    "Dark Feraligatr 5/105",
    "Dark Gengar 6/105",
    "Dark Houndoom 7/105",
    "Dark Porygon2 8/105",
    "Dark Scizor 9/105",
    "Dark Typhlosion 10/105",
    "Dark Tyranitar 11/105",
    "Light Arcanine 12/105",
    "Light Azumarill 13/105",
    "Light Dragonite 14/105",
    "Light Togetic 15/105",
    "Miracle Energy 16/105",
    "Alakazam 1/110",
    "Articuno 2/110",
    "Charizard 3/110",
    "Dark Blastoise 4/110",
    "Dark Dragonite 5/110",
    "Dark Persian 6/110",
    "Dark Raichu 7/110",
    "Dark Slowbro 8/110",
    "Dark Vaporeon 9/110",
    "Flareon 10/110",
    "Gengar 11/110",
    "Gyarados 12/110",
    "Hitmonlee 13/110",
    "Jolteon 14/110",
    "Machamp 15/110",
    "Muk 16/110",
    "Ninetales 17/110",
    "Venusaur 18/110",
    "Zapdos 19/110",
    "Shining Celebi 106/105",
    "Shining Charizard 107/105",
    "Shining Kabutops 108/105",
    "Shining Mewtwo 109/105",
    "Shining Noctowl 110/105",
    "Shining Raichu 111/105",
    "Shining Steelix 112/105",
    "Shining Tyranitar 113/105",
    "Shining Gyarados 65/64",
    "Shining Magikarp 66/64",
    "Kingdra 148/147",
    "Lugia 149/147",
    "Nidoking 150/147",
    "Celebi 145/144",
    "Charizard 146/144",
    "Crobat 147/144",
    "Golem 148/144",
    "Ho-Oh 149/144",
    "Kabutops 150/144",
    "Charizard δ 100/101",
    "Mew 101/101",
    "Alakazam 99/100",
    "Celebi 100/100",
    "Gyarados δ 102/110",
    "Mewtwo 103/110",
    "Pikachu 104/110",
    "Regice 90/92",
    "Regirock 91/92",
    "Registeel 92/92",
    "Pikachu δ 93/92",
    "Groudon 111/113",
    "Kyogre 112/113",
    "Metagross 113/113",
    "Entei 113/115",
    "Raikou 114/115",
    "Suicune 115/115",
    "Latias 105/107",
    "Latios 106/107",
    "Rayquaza 107/107",
    "Ancient Mew",
    "Shining Mew"
)

fun main() {
  cards.shuffled().parallelStream().forEach { process(it) }

/*  ForkJoinPool(4).submit(Callable {
    cards.shuffled().parallelStream().forEach { process(it) }
  }).get()*/
}

var total = 0

fun process(name: String) {
  println("Starting $name")
  val fileName = name
      .toLowerCase()
      .replace(Regex("[ /]"), "_")
  val requestName = name
      .toLowerCase()
      .replace(" δ ", " ")
      .replace("δ", "")
      .replace("/", "%2F")
      .replace(Regex(".*'s "), "")
      .replace(" ", "+")

  val objectMapper = ObjectMapper()
  val memoryFile = File("${PATH_PREFIX}/pokellection-helper/out/ebay/$fileName.json")

  val oldData =
      if (!memoryFile.exists()) {
        mapOf()
      } else {
        objectMapper
            .readValue(memoryFile.readText(), object : TypeReference<List<EbayData>>() {})
            .map { it.id to it }
            .toMap()
      }

  val ebayScrapper = EbayScrapper(DRIVER, false)
  val newData =
      ebayScrapper.scrap(requestName, oldData.keys)
          .map { it.id to it }
          .toMap()
  ebayScrapper.close()

  val refreshedData = (newData.keys + oldData.keys)
      .associateWith { newData[it] ?: (oldData[it] ?: error("")) }

  memoryFile.writeText(objectMapper.writeValueAsString(refreshedData.values))

  total++
  println("Finished $name ($total/${cards.size})")
}
