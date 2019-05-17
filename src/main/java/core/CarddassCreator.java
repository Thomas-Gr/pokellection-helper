package core;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.write;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import types.Card;
import types.Pokedex;
import types.Rarity;
import types.Serie;
import types.Type;
import utils.PageReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class CarddassCreator {
    private static final String CONTENT = "001\tBulbasaur\timage_Bulbasaur-Green\timage_Bulbasaur-Red\n"+
            "002\tIvysaur\timage_Ivysaur-Green\timage_Ivysaur-Red\n"+
            "003\tVenusaur\timage_Venusaur-Green\timage_Venusaur-Red\n"+
            "004\tCharmander\timage_Charmander-Green\timage_Charmander-Red\n"+
            "005\tCharmeleon\timage_Charmeleon-Green\timage_Charmeleon-Red\n"+
            "006\tCharizard\timage_Charizard-Green\timage_Charizard-Red\n"+
            "007\tSquirtle\timage_Squirtle-Green\timage_Squirtle-Red\n"+
            "008\tWartortle\timage_Wartortle-Green\timage_Wartortle-Red\n"+
            "009\tBlastoise\timage_Blastoise-Green\timage_Blastoise-Red\n"+
            "010\tCaterpie\timage_Caterpie-Green\timage_Caterpie-Red\n"+
            "011\tMetapod\timage_Metapod-Green\timage_Metapod-Red\n"+
            "012\tButterfree\timage_Butterfree-Green\timage_Butterfree-Red\n"+
            "013\tWeedle\timage_Weedle-Green\timage_Weedle-Red\n"+
            "014\tKakuna\timage_Kakuna-Green\timage_Kakuna-Red\n"+
            "015\tBeedrill\timage_Beedrill-Green\timage_Beedrill-Red\n"+
            "016\tPidgey\timage_Pidgey-Green\timage_Pidgey-Red\n"+
            "017\tPidgeotto\timage_Pidgeotto-Green\timage_Pidgeotto-Red\n"+
            "018\tPidgeot\timage_Pidgeot-Green\timage_Pidgeot-Red\n"+
            "019\tRattata\timage_Rattata-Green\timage_Rattata-Red\n"+
            "020\tRaticate\timage_Raticate-Green\timage_Raticate-Red\n"+
            "021\tSpearow\timage_Spearow-Green\timage_Spearow-Red\n"+
            "022\tFearow\timage_Fearow-Green\timage_Fearow-Red\n"+
            "023\tEkans\timage_Ekans-Green\timage_Ekans-Red\n"+
            "024\tArbok\timage_Arbok-Green\timage_Arbok-Red\n"+
            "025\tPikachu\timage_Pikachu-Green\timage_Pikachu-Red\n"+
            "026\tRaichu\timage_Raichu-Green\timage_Raichu-Red\n"+
            "027\tSandshrew\timage_Sandshrew-Green\timage_Sandshrew-Red\n"+
            "028\tSandslash\timage_Sandslash-Green\timage_Sandslash-Red\n"+
            "029\tNidoran♀\timage_Nidoran♀-Green\timage_Nidoran♀-Red\n"+
            "030\tNidorina\timage_Nidorina-Green\timage_Nidorina-Red\n"+
            "031\tNidoqueen\timage_Nidoqueen-Green\timage_Nidoqueen-Red\n"+
            "032\tNidoran♂\timage_Nidoran♂-Green\timage_Nidoran♂-Red\n"+
            "033\tNidorino\timage_Nidorino-Green\timage_Nidorino-Red\n"+
            "034\tNidoking\timage_Nidoking-Green\timage_Nidoking-Red\n"+
            "035\tClefairy\timage_Clefairy-Green\timage_Clefairy-Red\n"+
            "036\tClefable\timage_Clefable-Green\timage_Clefable-Red\n"+
            "037\tVulpix\timage_Vulpix-Green\timage_Vulpix-Red\n"+
            "038\tNinetales\timage_Ninetales-Green\timage_Ninetales-Red\n"+
            "039\tJigglypuff\timage_Jigglypuff-Green\timage_Jigglypuff-Red\n"+
            "040\tWigglytuff\timage_Wigglytuff-Green\timage_Wigglytuff-Red\n"+
            "041\tZubat\timage_Zubat-Green\timage_Zubat-Red\n"+
            "042\tGolbat\timage_Golbat-Green\timage_Golbat-Red\n"+
            "043\tOddish\timage_Oddish-Green\timage_Oddish-Red\n"+
            "044\tGloom\timage_Gloom-Green\timage_Gloom-Red\n"+
            "045\tVileplume\timage_Vileplume-Green\timage_Vileplume-Red\n"+
            "046\tParas\timage_Paras-Green\timage_Paras-Red\n"+
            "047\tParasect\timage_Parasect-Green\timage_Parasect-Red\n"+
            "048\tVenonat\timage_Venonat-Green\timage_Venonat-Red\n"+
            "049\tVenomoth\timage_Venomoth-Green\timage_Venomoth-Red\n"+
            "050\tDiglett\timage_Diglett-Green\timage_Diglett-Red\n"+
            "051\tDugtrio\timage_Dugtrio-Green\timage_Dugtrio-Red\n"+
            "052\tMeowth\timage_Meowth-Green\timage_Meowth-Red\n"+
            "053\tPersian\timage_Persian-Green\timage_Persian-Red\n"+
            "054\tPsyduck\timage_Psyduck-Green\timage_Psyduck-Red\n"+
            "055\tGolduck\timage_Golduck-Green\timage_Golduck-Red\n"+
            "056\tMankey\timage_Mankey-Green\timage_Mankey-Red\n"+
            "057\tPrimeape\timage_Primeape-Green\timage_Primeape-Red\n"+
            "058\tGrowlithe\timage_Growlithe-Green\timage_Growlithe-Red\n"+
            "059\tArcanine\timage_Arcanine-Green\timage_Arcanine-Red\n"+
            "060\tPoliwag\timage_Poliwag-Green\timage_Poliwag-Red\n"+
            "061\tPoliwhirl\timage_Poliwhirl-Green\timage_Poliwhirl-Red\n"+
            "062\tPoliwrath\timage_Poliwrath-Green\timage_Poliwrath-Red\n"+
            "063\tAbra\timage_Abra-Green\timage_Abra-Red\n"+
            "064\tKadabra\timage_Kadabra-Green\timage_Kadabra-Red\n"+
            "065\tAlakazam\timage_Alakazam-Green\timage_Alakazam-Red\n"+
            "066\tMachop\timage_Machop-Green\timage_Machop-Red\n"+
            "067\tMachoke\timage_Machoke-Green\timage_Machoke-Red\n"+
            "068\tMachamp\timage_Machamp-Green\timage_Machamp-Red\n"+
            "069\tBellsprout\timage_Bellsprout-Green\timage_Bellsprout-Red\n"+
            "070\tWeepinbell\timage_Weepinbell-Green\timage_Weepinbell-Red\n"+
            "071\tVictreebel\timage_Victreebel-Green\timage_Victreebel-Red\n"+
            "072\tTentacool\timage_Tentacool-Green\timage_Tentacool-Red\n"+
            "073\tTentacruel\timage_Tentacruel-Green\timage_Tentacruel-Red\n"+
            "074\tGeodude\timage_Geodude-Green\timage_Geodude-Red\n"+
            "075\tGraveler\timage_Graveler-Green\timage_Graveler-Red\n"+
            "076\tGolem\timage_Golem-Green\timage_Golem-Red\n"+
            "077\tPonyta\timage_Ponyta-Green\timage_Ponyta-Red\n"+
            "078\tRapidash\timage_Rapidash-Green\timage_Rapidash-Red\n"+
            "079\tSlowpoke\timage_Slowpoke-Green\timage_Slowpoke-Red\n"+
            "080\tSlowbro\timage_Slowbro-Green\timage_Slowbro-Red\n"+
            "081\tMagnemite\timage_Magnemite-Green\timage_Magnemite-Red\n"+
            "082\tMagneton\timage_Magneton-Green\timage_Magneton-Red\n"+
            "083\tFarfetch'd\timage_Farfetch'd-Green\timage_Farfetch'd-Red\n"+
            "084\tDoduo\timage_Doduo-Green\timage_Doduo-Red\n"+
            "085\tDodrio\timage_Dodrio-Green\timage_Dodrio-Red\n"+
            "086\tSeel\timage_Seel-Green\timage_Seel-Red\n"+
            "087\tDewgong\timage_Dewgong-Green\timage_Dewgong-Red\n"+
            "088\tGrimer\timage_Grimer-Green\timage_Grimer-Red\n"+
            "089\tMuk\timage_Muk-Green\timage_Muk-Red\n"+
            "090\tShellder\timage_Shellder-Green\timage_Shellder-Red\n"+
            "091\tCloyster\timage_Cloyster-Green\timage_Cloyster-Red\n"+
            "092\tGastly\timage_Gastly-Green\timage_Gastly-Red\n"+
            "093\tHaunter\timage_Haunter-Green\timage_Haunter-Red\n"+
            "094\tGengar\timage_Gengar-Green\timage_Gengar-Red\n"+
            "095\tOnix\timage_Onix-Green\timage_Onix-Red\n"+
            "096\tDrowzee\timage_Drowzee-Green\timage_Drowzee-Red\n"+
            "097\tHypno\timage_Hypno-Green\timage_Hypno-Red\n"+
            "098\tKrabby\timage_Krabby-Green\timage_Krabby-Red\n"+
            "099\tKingler\timage_Kingler-Green\timage_Kingler-Red\n"+
            "100\tVoltorb\timage_Voltorb-Green\timage_Voltorb-Red\n"+
            "101\tElectrode\timage_Electrode-Green\timage_Electrode-Red\n"+
            "102\tExeggcute\timage_Exeggcute-Green\timage_Exeggcute-Red\n"+
            "103\tExeggutor\timage_Exeggutor-Green\timage_Exeggutor-Red\n"+
            "104\tCubone\timage_Cubone-Green\timage_Cubone-Red\n"+
            "105\tMarowak\timage_Marowak-Green\timage_Marowak-Red\n"+
            "106\tHitmonlee\timage_Hitmonlee-Green\timage_Hitmonlee-Red\n"+
            "107\tHitmonchan\timage_Hitmonchan-Green\timage_Hitmonchan-Red\n"+
            "108\tLickitung\timage_Lickitung-Green\timage_Lickitung-Red\n"+
            "109\tKoffing\timage_Koffing-Green\timage_Koffing-Red\n"+
            "110\tWeezing\timage_Weezing-Green\timage_Weezing-Red\n"+
            "111\tRhyhorn\timage_Rhyhorn-Green\timage_Rhyhorn-Red\n"+
            "112\tRhydon\timage_Rhydon-Green\timage_Rhydon-Red\n"+
            "113\tChansey\timage_Chansey-Green\timage_Chansey-Red\n"+
            "114\tTangela\timage_Tangela-Green\timage_Tangela-Red\n"+
            "115\tKangaskhan\timage_Kangaskhan-Green\timage_Kangaskhan-Red\n"+
            "116\tHorsea\timage_Horsea-Green\timage_Horsea-Red\n"+
            "117\tSeadra\timage_Seadra-Green\timage_Seadra-Red\n"+
            "118\tGoldeen\timage_Goldeen-Green\timage_Goldeen-Red\n"+
            "119\tSeaking\timage_Seaking-Green\timage_Seaking-Red\n"+
            "120\tStaryu\timage_Staryu-Green\timage_Staryu-Red\n"+
            "121\tStarmie\timage_Starmie-Green\timage_Starmie-Red\n"+
            "122\tMr. Mime\timage_Mr. Mime-Green\timage_Mr. Mime-Red\n"+
            "123\tScyther\timage_Scyther-Green\timage_Scyther-Red\n"+
            "124\tJynx\timage_Jynx-Green\timage_Jynx-Red\n"+
            "125\tElectabuzz\timage_Electabuzz-Green\timage_Electabuzz-Red\n"+
            "126\tMagmar\timage_Magmar-Green\timage_Magmar-Red\n"+
            "127\tPinsir\timage_Pinsir-Green\timage_Pinsir-Red\n"+
            "128\tTauros\timage_Tauros-Green\timage_Tauros-Red\n"+
            "129\tMagikarp\timage_Magikarp-Green\timage_Magikarp-Red\n"+
            "130\tGyarados\timage_Gyarados-Green\timage_Gyarados-Red\n"+
            "131\tLapras\timage_Lapras-Green\timage_Lapras-Red\n"+
            "132\tDitto\timage_Ditto-Green\timage_Ditto-Red\n"+
            "133\tEevee\timage_Eevee-Green\timage_Eevee-Red\n"+
            "134\tVaporeon\timage_Vaporeon-Green\timage_Vaporeon-Red\n"+
            "135\tJolteon\timage_Jolteon-Green\timage_Jolteon-Red\n"+
            "136\tFlareon\timage_Flareon-Green\timage_Flareon-Red\n"+
            "137\tPorygon\timage_Porygon-Green\timage_Porygon-Red\n"+
            "138\tOmanyte\timage_Omanyte-Green\timage_Omanyte-Red\n"+
            "139\tOmastar\timage_Omastar-Green\timage_Omastar-Red\n"+
            "140\tKabuto\timage_Kabuto-Green\timage_Kabuto-Red\n"+
            "141\tKabutops\timage_Kabutops-Green\timage_Kabutops-Red\n"+
            "142\tAerodactyl\timage_Aerodactyl-Green\timage_Aerodactyl-Red\n"+
            "143\tSnorlax\timage_Snorlax-Green\timage_Snorlax-Red\n"+
            "144\tArticuno\timage_Articuno-Green\timage_Articuno-Red\n"+
            "145\tZapdos\timage_Zapdos-Green\timage_Zapdos-Red\n"+
            "146\tMoltres\timage_Moltres-Green\timage_Moltres-Red\n"+
            "147\tDratini\timage_Dratini-Green\timage_Dratini-Red\n"+
            "148\tDragonair\timage_Dragonair-Green\timage_Dragonair-Red\n"+
            "149\tDragonite\timage_Dragonite-Green\timage_Dragonite-Red\n"+
            "150\tMewtwo\timage_Mewtwo-Green\timage_Mewtwo-Red\n"+
            "151\tMew\timage_Mew-Green\timage_Mew-Red\n"+
            "000\tList card 3\timage_List\tcard 3-Green\tcard 3-Green\n" +
            "000\tList card 4\timage_List\tcard 4-Green\tcard 4-Green\n" +
            "000\tMap card 3\timage_Map\tcard 3-Green\tcard 3-Green";

    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Pokedex pokedex = new Pokedex();

        Serie serie = new Serie("Carddass Pocket Monster Red", "");

        for (String line : CONTENT.split("\n")) {
            String[] data = line.split("\t");

            System.out.println(data[1]);
            Card card = new Card(
                    data[1],
                    Type.NON_TCG,
                    Rarity.NONE,
                    data[0].equals("000") ? -42 : Integer.parseInt(data[0]),
                    "",
                    data[3] + ".jpg",
                    data[0].equals("000") ? -42 : Integer.parseInt(data[0]),
                    1,
                    "",
                    "",
                    "");

           serie.addCard(card);
        }

        pokedex.addSerie(serie);

        String formattedSerie = new ObjectMapper().writeValueAsString(serie);

        File file = new File(format("out/series/%s.json", serie.getName()));
        write(formattedSerie, file, UTF_8);

        String content = serie.getCards().values()
                .stream()
                .map(Card::getPicture)
                .distinct()
                .map(a -> format("'%s' : require('../../../resources/images/cards/%s')", a, a))
                .collect(joining(",\n"));

        File file2 = new File(format("out/jsFiles/%s.js", serie.getName()));
        write(format("export default ChangeName =\n  {%s}", content), file2, UTF_8);

        /*
        for (Card card : serie.getCards().values()) {
            String cardName = card.getPicture();
            File f = new File("out/images/cards/" + cardName.replace('é', 'e'));
            if (!f.exists()) {
                try {
                    downloadImage(cardName);
                } catch (Exception e) {
                    System.out.println(cardName + " doesn't exist");
                }
            }
        }

        if (!serie.getImage().equals("")) {
            String imageName = serie.getImage();
            File f = new File("out/images/series/" + imageName);
            if (!f.exists()) {
                try {
                    downloadImage(imageName);
                } catch (Exception e) {
                    System.out.println(imageName + " doesn't exist");
                }
            }
        }
        */
    }

    private static void downloadImage(String imageName) throws IOException {
        String file = PageReader.readImagePathFromFilePage("https://bulbapedia.bulbagarden.net/wiki/File:" + imageName);

        URLConnection urlConn = new URL(file).openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

        try (InputStream is = urlConn.getInputStream()) {
            FileUtils.copyInputStreamToFile(is, new File("out/images/cards/" + imageName.replace('é', 'e')));
        }
    }
}
