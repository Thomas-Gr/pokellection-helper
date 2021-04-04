package mosaic_website

import types.Type

val EXPANSIONS =
    setOf("1996",
        "1997",
        "1998",
        "1999",
        "2000",
        "2001-2005",
        "ANA-JR",
        "Awakening Legends",
        "Bulbasaur Deck",
        "Challenge from the Darkness",
        "Championship",
        "Chikorita Half Deck",
        "CoroCoro Best Photo Contest",
        "Crossing the Ruins...",
        "Darkness, and to Light...",
        "Evolution Communication Campaign",
        "Expansion Pack",
        "Game Boy",
        "Gold, Silver, to a New World...",
        "Green Deck",
        "Guren Town Gym",
        "Gym",
        "Hanada City Gym",
        "How I Became a Pokémon Card",
        "Jumbo",
        "Kuchiba City Gym",
        "Leaders' Stadium",
        "Lucky Stadium",
        "Mystery of the Fossils",
        "Nivi City Gym",
        "Pikachu Illustrator",
        "Pokemon Fan Club",
        "Pokémon Jungle",
        "Pokémon Song Best Collection",
        "Premium File 1",
        "Premium File 2",
        "Premium File 3",
        "Rainbow Island",
        "Red Deck",
        "Rocket Gang",
        "Series 1 (Blue)",
        "Series 2 (Red)",
        "Series 3 (Green)",
        "Squirtle Deck",
        "Tamamushi City Gym",
        "Totodile Half Deck",
        "Trade Please",
        "Tropical Island",
        "Tropical Mega Battle Phone Cards",
        "Vending Machine",
        "Yamabuki City Gym")

val EXPANSIONS_GROUPS = mapOf(
    "1996" to "Promotional cards",
    "1997" to "Promotional cards",
    "1998" to "Promotional cards",
    "1999" to "Promotional cards",
    "Tropical Island" to "Southern Islands",
    "Rainbow Island" to "Southern Islands",
    "2000" to "Promotional cards",
    "2001-2005" to "Promotional cards",
    "ANA-JR" to "Promotional cards",
    "Championship" to "Promotional cards",
    "CoroCoro Best Photo Contest" to "Promotional cards",
    "Evolution Communication Campaign" to "Promotional cards",
    "Game Boy" to "Promotional cards",
    "Gym" to "Promotional cards",
    "How I Became a Pokémon Card" to "Promotional cards",
    "Jumbo" to "Promotional cards",
    "Lucky Stadium" to "Promotional cards",
    "Pikachu Illustrator" to "Promotional cards",
    "Pokemon Fan Club" to "Promotional cards",
    "Trade Please" to "Promotional cards",
    "Tropical Island" to "Promotional cards",
    "Tropical Mega Battle Phone Cards" to "Promotional cards",
    "Vending Machine" to "Promotional cards"
)

val SETS = listOf(
    Pair(
        "First generation",
        listOf("Expansion Pack",
            "Pokémon Jungle",
            "Mystery of the Fossils",
            "Rocket Gang",
            "Leaders' Stadium",
            "Challenge from the Darkness")),
    Pair(
        "Second generation",
        listOf("Gold, Silver, to a New World...",
            "Crossing the Ruins...",
            "Awakening Legends",
            "Darkness, and to Light...")),
    Pair(
        "Special sets first generation",
        listOf("Promotional cards",
            "Series 1 (Blue)",
            "Series 2 (Red)",
            "Series 3 (Green)",
            "Southern Islands")),
    Pair(
        "Decks 1st generation",
        listOf("Nivi City Gym",
            "Hanada City Gym",
            "Kuchiba City Gym",
            "Tamamushi City Gym",
            "Yamabuki City Gym",
            "Guren Town Gym",
            "Bulbasaur Deck",
            "Squirtle Deck")),
    Pair(
        "Special sets 2nd generation",
        listOf("Premium File 1",
            "Premium File 2",
            "Premium File 3",
            "Chikorita Half Deck",
            "Totodile Half Deck"))
)

val ENGLISH_TRANSLATIONS = mapOf(
    Pair("Expansion Pack", "Base set"),
    Pair("Pokémon Jungle", "Jungle"),
    Pair("Mystery of the Fossils", "Fossil"),
    Pair("Rocket Gang", "Team Rocket"),
    Pair("Leaders' Stadium", "Gym Heroes"),
    Pair("Challenge from the Darkness", "Gym Challenge"),
    Pair("Gold, Silver, to a New World...", "Neo Genesis"),
    Pair("Crossing the Ruins...", "Neo Discovery"),
    Pair("Awakening Legends", "Neo Revelation"),
    Pair("Darkness, and to Light...", "Neo Destiny"),
    Pair("Series 1 (Blue)", "Vending machine 1"),
    Pair("Series 2 (Red)", "Vending machine 2"),
    Pair("Series 3 (Green)", "Vending machine 3"),
    Pair("Nivi City Gym", "Brock"),
    Pair("Hanada City Gym", "Misty"),
    Pair("Kuchiba City Gym", "Lt. Surge"),
    Pair("Tamamushi City Gym", "Erika"),
    Pair("Yamabuki City Gym", "Sabrina"),
    Pair("Guren Town Gym", "Blaine")
)

val FRENCH_TRANSLATIONS = mapOf(
    Pair("First generation", "Première génération"),
    Pair("Expansion Pack", "Set de base"),
    Pair("Pokémon Jungle", "Jungle"),
    Pair("Mystery of the Fossils", "Fossile"),
    Pair("Rocket Gang", "Team Rocket"),
    Pair("Leaders' Stadium", "Gym Heroes"),
    Pair("Challenge from the Darkness", "Gym Challenge"),
    Pair("Second generation", "Deuxième génération"),
    Pair("Gold, Silver, to a New World...", "Neo Genesis"),
    Pair("Crossing the Ruins...", "Neo Discovery"),
    Pair("Awakening Legends", "Neo Revelation"),
    Pair("Darkness, and to Light...", "Neo Destiny"),
    Pair("Special sets first generation", "Sets spéciaux première génération"),
    Pair("Promotional cards", "Cartes promo"),
    Pair("Series 1 (Blue)", "Vending machine 1"),
    Pair("Series 2 (Red)", "Vending machine 2"),
    Pair("Series 3 (Green)", "Vending machine 3"),
    Pair("Decks 1st generation", "Decks première génération"),
    Pair("Nivi City Gym", "Pierre"),
    Pair("Hanada City Gym", "Ondine"),
    Pair("Kuchiba City Gym", "Major Bob"),
    Pair("Tamamushi City Gym", "Erika"),
    Pair("Yamabuki City Gym", "Sabrina"),
    Pair("Guren Town Gym", "Auguste"),
    Pair("Bulbasaur Deck", "Deck Bulbizarre"),
    Pair("Squirtle Deck", "Deck Carapuce"),
    Pair("Special sets 2nd generation", "Sets spéciaux deuxième génération"),
    Pair("Chikorita Half Deck", "Demi-deck Germignon"),
    Pair("Totodile Half Deck", "Demi-deck Kaiminus")
)

val TYPES =
    setOf(Type.FIRE,
        Type.WATER,
        Type.LIGHTNING,
        Type.PSYCHIC,
        Type.FIGHTING,
        Type.GRASS,
        Type.COLORLESS,
        Type.DARKNESS,
        Type.METAL)

val BANNED_CARDS = setOf(
    "PonytaExpedition126.jpg",
    "PryceLaprasVS41.jpg",
    "MeowthWizardsPromo10.jpg",
    "promo-3-006.jpg",
    "promo-3-045.jpg")

val GROUPED_CARDS = mapOf(
    Pair("promo-5-013.jpg", "promo-3-042.jpg"),
    Pair("promo-5-007.jpg", "promo-3-042.jpg"),
    Pair("promo-5-014.jpg", "promo-5-003.jpg"),
    Pair("base-set-003.jpg", "PikachuBaseSet58.png"),
    Pair("promo-5-025.jpg", "PikachuToyotaPromo.jpg"),
    Pair("promo-5-012.jpg", "promo-5-016.jpg"),
    Pair("PikachuWizardsPromo4.jpg", "promo-5-004.jpg"),
    Pair("neo-genesis-038.jpg", "ChikoritaNeoGenesis53.jpg"),
    Pair("neo-genesis-071.jpg", "CyndaquilNeoGenesis57.jpg"),
    Pair("neo-genesis-080.jpg", "TotodileNeoGenesis81.jpg")
)

val UPDATED_NAMES = mapOf(
    Pair("Pikachu [English]", "Pikachu"),
    Pair("Mewtwo [English]", "Mewtwo"),
    Pair("Pikachu [French]", "Pikachu"),
    Pair("Pikachu, Jigglypuff, and Clefairy [Jumbo]", "Pikachu, Jigglypuff, and Clefairy"),
    Pair("Pikachu & Pichu [Jumbo]", "Pikachu & Pichu"),
    Pair("Surfing Pikachu [Glossy]", "Surfing Pikachu"),
    Pair("Surfing Pikachu [Non-glossy]", "Surfing Pikachu"),
    Pair("Pikachu & Pichu [Jumbo]", "Pikachu & Pichu"),
    Pair("Pikachu [English w/Gray Star]", "Pikachu"),
    Pair("Pikachu [Glossy]", "Pikachu"),
    Pair("Pikachu [Non-glossy]", "Pikachu"),
    Pair("Jigglypuff [English w/Gray Star]", "Jigglypuff"),
    Pair("Jigglypuff [Glossy]", "Jigglypuff"),
    Pair("Exeggutor [Bilingual] [Non-glossy]", "Exeggutor"),
    Pair("Crystal Tower's Entei [Jumbo]", "Crystal Tower's Entei"),
    Pair("Ancient Mew v1", "Ancient Mew"),
    Pair("Ancient Mew v2", "Ancient Mew"),
    Pair("Ancient Mew v3", "Ancient Mew"),
    Pair("Mew [Glossy]", "Mew"),
    Pair("Mew [Non-glossy]", "Mew"),
    Pair("Nintendo World promo [Jumbo]", "Dark  Lugia"),
    Pair("Pikachu's Summer Vacation [Jumbo]", "Pikachu's Summer Vacation"),
    Pair("Articuno, Moltres, and Zapdos [Jumbo]", "Articuno, Moltres, and Zapdos"),
    Pair("Jigglypuff [Non-glossy]", "Jigglypuff")
)


val POKEMONS_TO_FRENCH = mapOf(
    Pair("Bulbasaur", "Bulbizarre"),
    Pair("Ivysaur", "Herbizarre"),
    Pair("Venusaur", "Florizarre"),
    Pair("Charmander", "Salamèche"),
    Pair("Charmeleon", "Reptincel"),
    Pair("Charizard", "Dracaufeu"),
    Pair("Squirtle", "Carapuce"),
    Pair("Wartortle", "Carabaffe"),
    Pair("Blastoise", "Tortank"),
    Pair("Caterpie", "Chenipan"),
    Pair("Metapod", "Chrysacier"),
    Pair("Butterfree", "Papilusion"),
    Pair("Weedle", "Aspicot"),
    Pair("Kakuna", "Coconfort"),
    Pair("Beedrill", "Dardargnan"),
    Pair("Pidgey", "Roucool"),
    Pair("Pidgeotto", "Roucoups"),
    Pair("Pidgeot", "Roucarnage"),
    Pair("Rattata", "Rattata"),
    Pair("Raticate", "Rattatac"),
    Pair("Spearow", "Piafabec"),
    Pair("Fearow", "Rapasdepic"),
    Pair("Ekans", "Abo"),
    Pair("Arbok", "Arbok"),
    Pair("Pikachu", "Pikachu"),
    Pair("Raichu", "Raichu"),
    Pair("Sandshrew", "Sabelette"),
    Pair("Sandslash", "Sablaireau"),
    Pair("Nidoran♀", "Nidoran♀"),
    Pair("Nidorina", "Nidorina"),
    Pair("Nidoqueen", "Nidoqueen"),
    Pair("Nidoran♂", "Nidoran♂"),
    Pair("Nidorino", "Nidorino"),
    Pair("Nidoking", "Nidoking"),
    Pair("Clefairy", "Mélofée"),
    Pair("Clefable", "Mélodelfe"),
    Pair("Vulpix", "Goupix"),
    Pair("Ninetales", "Feunard"),
    Pair("Jigglypuff", "Rondoudou"),
    Pair("Wigglytuff", "Grodoudou"),
    Pair("Zubat", "Nosferapti"),
    Pair("Golbat", "Nosferalto"),
    Pair("Oddish", "Mystherbe"),
    Pair("Gloom", "Ortide"),
    Pair("Vileplume", "Rafflésia"),
    Pair("Paras", "Paras"),
    Pair("Parasect", "Parasect"),
    Pair("Venonat", "Mimitoss"),
    Pair("Venomoth", "Aéromite"),
    Pair("Diglett", "Taupiqueur"),
    Pair("Dugtrio", "Triopikeur"),
    Pair("Meowth", "Miaouss"),
    Pair("Persian", "Persian"),
    Pair("Psyduck", "Psykokwak"),
    Pair("Golduck", "Akwakwak"),
    Pair("Mankey", "Férosinge"),
    Pair("Primeape", "Colossinge"),
    Pair("Growlithe", "Caninos"),
    Pair("Arcanine", "Arcanin"),
    Pair("Poliwag", "Ptitard"),
    Pair("Poliwhirl", "Têtarte"),
    Pair("Poliwrath", "Tartard"),
    Pair("Abra", "Abra"),
    Pair("Kadabra", "Kadabra"),
    Pair("Alakazam", "Alakazam"),
    Pair("Machop", "Machoc"),
    Pair("Machoke", "Machopeur"),
    Pair("Machamp", "Mackogneur"),
    Pair("Bellsprout", "Chétiflor"),
    Pair("Weepinbell", "Boustiflor"),
    Pair("Victreebel", "Empiflor"),
    Pair("Tentacool", "Tentacool"),
    Pair("Tentacruel", "Tentacruel"),
    Pair("Geodude", "Racaillou"),
    Pair("Graveler", "Gravalanch"),
    Pair("Golem", "Grolem"),
    Pair("Ponyta", "Ponyta"),
    Pair("Rapidash", "Galopa"),
    Pair("Slowpoke", "Ramoloss"),
    Pair("Slowbro", "Flagadoss"),
    Pair("Magnemite", "Magnéti"),
    Pair("Magneton", "Magnéton"),
    Pair("Farfetch'd", "Canarticho"),
    Pair("Doduo", "Doduo"),
    Pair("Dodrio", "Dodrio"),
    Pair("Seel", "Otaria"),
    Pair("Dewgong", "Lamantine"),
    Pair("Grimer", "Tadmorv"),
    Pair("Muk", "Grotadmorv"),
    Pair("Shellder", "Kokiyas"),
    Pair("Cloyster", "Crustabri"),
    Pair("Gastly", "Fantominus"),
    Pair("Haunter", "Spectrum"),
    Pair("Gengar", "Ectoplasma"),
    Pair("Onix", "Onix"),
    Pair("Drowzee", "Soporifik"),
    Pair("Hypno", "Hypnomade"),
    Pair("Krabby", "Krabby"),
    Pair("Kingler", "Krabboss"),
    Pair("Voltorb", "Voltorbe"),
    Pair("Electrode", "Électrode"),
    Pair("Exeggcute", "Nœunœuf"),
    Pair("Exeggutor", "Noadkoko"),
    Pair("Cubone", "Osselait"),
    Pair("Marowak", "Ossatueur"),
    Pair("Hitmonlee", "Kicklee"),
    Pair("Hitmonchan", "Tygnon"),
    Pair("Lickitung", "Excelangue"),
    Pair("Koffing", "Smogo"),
    Pair("Weezing", "Smogogo"),
    Pair("Rhyhorn", "Rhinocorne"),
    Pair("Rhydon", "Rhinoféros"),
    Pair("Chansey", "Leveinard"),
    Pair("Tangela", "Saquedeneu"),
    Pair("Kangaskhan", "Kangourex"),
    Pair("Horsea", "Hypotrempe"),
    Pair("Seadra", "Hypocéan"),
    Pair("Goldeen", "Poissirène"),
    Pair("Seaking", "Poissoroy"),
    Pair("Staryu", "Stari"),
    Pair("Starmie", "Staross"),
    Pair("Mr. Mime", "M. Mime"),
    Pair("Scyther", "Insécateur"),
    Pair("Jynx", "Lippoutou"),
    Pair("Electabuzz", "Élektek"),
    Pair("Magmar", "Magmar"),
    Pair("Pinsir", "Scarabrute"),
    Pair("Tauros", "Tauros"),
    Pair("Magikarp", "Magicarpe"),
    Pair("Gyarados", "Léviator"),
    Pair("Lapras", "Lokhlass"),
    Pair("Ditto", "Métamorph"),
    Pair("Eevee", "Évoli"),
    Pair("Vaporeon", "Aquali"),
    Pair("Jolteon", "Voltali"),
    Pair("Flareon", "Pyroli"),
    Pair("Porygon", "Porygon"),
    Pair("Omanyte", "Amonita"),
    Pair("Omastar", "Amonistar"),
    Pair("Kabuto", "Kabuto"),
    Pair("Kabutops", "Kabutops"),
    Pair("Aerodactyl", "Ptéra"),
    Pair("Snorlax", "Ronflex"),
    Pair("Articuno", "Artikodin"),
    Pair("Zapdos", "Électhor"),
    Pair("Moltres", "Sulfura"),
    Pair("Dratini", "Minidraco"),
    Pair("Dragonair", "Draco"),
    Pair("Dragonite", "Dracolosse"),
    Pair("Mewtwo", "Mewtwo"),
    Pair("Mew", "Mew"),
    Pair("Chikorita", "Germignon"),
    Pair("Bayleef", "Macronium"),
    Pair("Meganium", "Méganium"),
    Pair("Cyndaquil", "Héricendre"),
    Pair("Quilava", "Feurisson"),
    Pair("Typhlosion", "Typhlosion"),
    Pair("Totodile", "Kaiminus"),
    Pair("Croconaw", "Crocrodil"),
    Pair("Feraligatr", "Aligatueur"),
    Pair("Sentret", "Fouinette"),
    Pair("Furret", "Fouinar"),
    Pair("Hoothoot", "Hoothoot"),
    Pair("Noctowl", "Noarfang"),
    Pair("Ledyba", "Coxy"),
    Pair("Ledian", "Coxyclaque"),
    Pair("Spinarak", "Mimigal"),
    Pair("Ariados", "Migalos"),
    Pair("Crobat", "Nostenfer"),
    Pair("Chinchou", "Loupio"),
    Pair("Lanturn", "Lanturn"),
    Pair("Pichu", "Pichu"),
    Pair("Cleffa", "Mélo"),
    Pair("Igglybuff", "Toudoudou"),
    Pair("Togepi", "Togepi"),
    Pair("Togetic", "Togetic"),
    Pair("Natu", "Natu"),
    Pair("Xatu", "Xatu"),
    Pair("Mareep", "Wattouat"),
    Pair("Flaaffy", "Lainergie"),
    Pair("Ampharos", "Pharamp"),
    Pair("Bellossom", "Joliflor"),
    Pair("Marill", "Marill"),
    Pair("Azumarill", "Azumarill"),
    Pair("Sudowoodo", "Simularbre"),
    Pair("Politoed", "Tarpaud"),
    Pair("Hoppip", "Granivol"),
    Pair("Skiploom", "Floravol"),
    Pair("Jumpluff", "Cotovol"),
    Pair("Aipom", "Capumain"),
    Pair("Sunkern", "Tournegrin"),
    Pair("Sunflora", "Héliatronc"),
    Pair("Yanma", "Yanma"),
    Pair("Wooper", "Axoloto"),
    Pair("Quagsire", "Maraiste"),
    Pair("Espeon", "Mentali"),
    Pair("Umbreon", "Noctali"),
    Pair("Murkrow", "Cornèbre"),
    Pair("Slowking", "Roigada"),
    Pair("Misdreavus", "Feuforêve"),
    Pair("Unown", "Zarbi"),
    Pair("Wobbuffet", "Qulbutoké"),
    Pair("Girafarig", "Girafarig"),
    Pair("Pineco", "Pomdepik"),
    Pair("Forretress", "Foretress"),
    Pair("Dunsparce", "Insolourdo"),
    Pair("Gligar", "Scorplane"),
    Pair("Steelix", "Steelix"),
    Pair("Snubbull", "Snubbull"),
    Pair("Granbull", "Granbull"),
    Pair("Qwilfish", "Qwilfish"),
    Pair("Scizor", "Cizayox"),
    Pair("Shuckle", "Caratroc"),
    Pair("Heracross", "Scarhino"),
    Pair("Sneasel", "Farfuret"),
    Pair("Teddiursa", "Teddiursa"),
    Pair("Ursaring", "Ursaring"),
    Pair("Slugma", "Limagma"),
    Pair("Magcargo", "Volcaropod"),
    Pair("Swinub", "Marcacrin"),
    Pair("Piloswine", "Cochignon"),
    Pair("Corsola", "Corayon"),
    Pair("Remoraid", "Rémoraid"),
    Pair("Octillery", "Octillery"),
    Pair("Delibird", "Cadoizo"),
    Pair("Mantine", "Démanta"),
    Pair("Skarmory", "Airmure"),
    Pair("Houndour", "Malosse"),
    Pair("Houndoom", "Démolosse"),
    Pair("Kingdra", "Hyporoi"),
    Pair("Phanpy", "Phanpy"),
    Pair("Donphan", "Donphan"),
    Pair("Porygon2", "Porygon2"),
    Pair("Stantler", "Cerfrousse"),
    Pair("Smeargle", "Queulorior"),
    Pair("Tyrogue", "Debugant"),
    Pair("Hitmontop", "Kapoera"),
    Pair("Smoochum", "Lippouti"),
    Pair("Elekid", "Élekid"),
    Pair("Magby", "Magby"),
    Pair("Miltank", "Écrémeuh"),
    Pair("Blissey", "Leuphorie"),
    Pair("Raikou", "Raikou"),
    Pair("Entei", "Entei"),
    Pair("Suicune", "Suicune"),
    Pair("Larvitar", "Embrylex"),
    Pair("Pupitar", "Ymphect"),
    Pair("Tyranitar", "Tyranocif"),
    Pair("Lugia", "Lugia"),
    Pair("Ho-Oh", "Ho-Oh"),
    Pair("Celebi", "Celebi"),
    Pair("Treecko", "Arcko"),
    Pair("Grovyle", "Massko"),
    Pair("Sceptile", "Jungko"),
    Pair("Torchic", "Poussifeu"),
    Pair("Combusken", "Galifeu"),
    Pair("Blaziken", "Braségali"),
    Pair("Mudkip", "Gobou"),
    Pair("Marshtomp", "Flobio"),
    Pair("Swampert", "Laggron"),
    Pair("Poochyena", "Medhyèna"),
    Pair("Mightyena", "Grahyèna"),
    Pair("Zigzagoon", "Zigzaton"),
    Pair("Linoone", "Linéon"),
    Pair("Wurmple", "Chenipotte"),
    Pair("Silcoon", "Armulys"),
    Pair("Beautifly", "Charmillon"),
    Pair("Cascoon", "Blindalys"),
    Pair("Dustox", "Papinox"),
    Pair("Lotad", "Nénupiot"),
    Pair("Lombre", "Lombre"),
    Pair("Ludicolo", "Ludicolo"),
    Pair("Seedot", "Grainipiot"),
    Pair("Nuzleaf", "Pifeuil"),
    Pair("Shiftry", "Tengalice"),
    Pair("Taillow", "Nirondelle"),
    Pair("Swellow", "Hélédelle"),
    Pair("Wingull", "Goélise"),
    Pair("Pelipper", "Bekipan"),
    Pair("Ralts", "Tarsal"),
    Pair("Kirlia", "Kirlia"),
    Pair("Gardevoir", "Gardevoir"),
    Pair("Surskit", "Arakdo"),
    Pair("Masquerain", "Maskadra"),
    Pair("Shroomish", "Balignon"),
    Pair("Breloom", "Chapignon"),
    Pair("Slakoth", "Parécool"),
    Pair("Vigoroth", "Vigoroth"),
    Pair("Slaking", "Monaflèmit"),
    Pair("Nincada", "Ningale"),
    Pair("Ninjask", "Ninjask"),
    Pair("Shedinja", "Munja"),
    Pair("Whismur", "Chuchmur"),
    Pair("Loudred", "Ramboum"),
    Pair("Exploud", "Brouhabam"),
    Pair("Makuhita", "Makuhita"),
    Pair("Hariyama", "Hariyama"),
    Pair("Azurill", "Azurill"),
    Pair("Nosepass", "Tarinor"),
    Pair("Skitty", "Skitty"),
    Pair("Delcatty", "Delcatty"),
    Pair("Sableye", "Ténéfix"),
    Pair("Mawile", "Mysdibule"),
    Pair("Aron", "Galekid"),
    Pair("Lairon", "Galegon"),
    Pair("Aggron", "Galeking"),
    Pair("Meditite", "Méditikka"),
    Pair("Medicham", "Charmina"),
    Pair("Electrike", "Dynavolt"),
    Pair("Manectric", "Élecsprint"),
    Pair("Plusle", "Posipi"),
    Pair("Minun", "Négapi"),
    Pair("Volbeat", "Muciole"),
    Pair("Illumise", "Lumivole"),
    Pair("Roselia", "Rosélia"),
    Pair("Gulpin", "Gloupti"),
    Pair("Swalot", "Avaltout"),
    Pair("Carvanha", "Carvanha"),
    Pair("Sharpedo", "Sharpedo"),
    Pair("Wailmer", "Wailmer"),
    Pair("Wailord", "Wailord"),
    Pair("Numel", "Chamallot"),
    Pair("Camerupt", "Camérupt"),
    Pair("Torkoal", "Chartor"),
    Pair("Spoink", "Spoink"),
    Pair("Grumpig", "Groret"),
    Pair("Spinda", "Spinda"),
    Pair("Trapinch", "Kraknoix"),
    Pair("Vibrava", "Vibraninf"),
    Pair("Flygon", "Libégon"),
    Pair("Cacnea", "Cacnea"),
    Pair("Cacturne", "Cacturne"),
    Pair("Swablu", "Tylton"),
    Pair("Altaria", "Altaria"),
    Pair("Zangoose", "Mangriff"),
    Pair("Seviper", "Séviper"),
    Pair("Lunatone", "Séléroc"),
    Pair("Solrock", "Solaroc"),
    Pair("Barboach", "Barloche"),
    Pair("Whiscash", "Barbicha"),
    Pair("Corphish", "Écrapince"),
    Pair("Crawdaunt", "Colhomard"),
    Pair("Baltoy", "Balbuto"),
    Pair("Claydol", "Kaorine"),
    Pair("Lileep", "Lilia"),
    Pair("Cradily", "Vacilys"),
    Pair("Anorith", "Anorith"),
    Pair("Armaldo", "Armaldo"),
    Pair("Feebas", "Barpau"),
    Pair("Milotic", "Milobellus"),
    Pair("Castform", "Morphéo"),
    Pair("Kecleon", "Kecleon"),
    Pair("Shuppet", "Polichombr"),
    Pair("Banette", "Branette"),
    Pair("Duskull", "Skelénox"),
    Pair("Dusclops", "Téraclope"),
    Pair("Tropius", "Tropius"),
    Pair("Chimecho", "Éoko"),
    Pair("Absol", "Absol"),
    Pair("Wynaut", "Okéoké"),
    Pair("Snorunt", "Stalgamin"),
    Pair("Glalie", "Oniglali"),
    Pair("Spheal", "Obalie"),
    Pair("Sealeo", "Phogleur"),
    Pair("Walrein", "Kaimorse"),
    Pair("Clamperl", "Coquiperl"),
    Pair("Huntail", "Serpang"),
    Pair("Gorebyss", "Rosabyss"),
    Pair("Relicanth", "Relicanth"),
    Pair("Luvdisc", "Lovdisc"),
    Pair("Bagon", "Draby"),
    Pair("Shelgon", "Drackhaus"),
    Pair("Salamence", "Drattak"),
    Pair("Beldum", "Terhal"),
    Pair("Metang", "Métang"),
    Pair("Metagross", "Métalosse"),
    Pair("Regirock", "Regirock"),
    Pair("Regice", "Regice"),
    Pair("Registeel", "Registeel"),
    Pair("Latias", "Latias"),
    Pair("Latios", "Latios"),
    Pair("Kyogre", "Kyogre"),
    Pair("Groudon", "Groudon"),
    Pair("Rayquaza", "Rayquaza"),
    Pair("Jirachi", "Jirachi"),
    Pair("Deoxys", "Deoxys")
)

val INDEX_CARDS = setOf(1429677911, 2105031630, 1370572567, 1952485113, 1213946982, 229721942)