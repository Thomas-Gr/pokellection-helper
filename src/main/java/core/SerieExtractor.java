package core;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.write;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static utils.RawTextExtractor.extractCards;
import static utils.RawTextExtractor.extractImage;
import static utils.RawTextExtractor.extractTitle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import types.Card;
import types.Pokedex;
import types.Serie;
import utils.PageReader;
import utils.RawTextExtractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

public class SerieExtractor {
    public static final String TYPE_OF_SET = "halfdecklist"; // Setlist // halfdecklist // Promo
    public static final boolean FAST = false;
    private static final boolean READS_FROM_BULBAPEDIA = false;

    private static final String SERIE = "Neo_Destiny_(TCG)";
    private static final String INITIAL_URL = format("https://bulbapedia.bulbagarden.net/w/index.php?title=%s&action=edit", SERIE);

    private static final String CONTENT = "{{halfdecklist/header|title=Totodile Half Deck|type=Water|symbol=yes|image=SetSymbolTotodileIntroPackNeo.png}}\n" +
            "{{halfdecklist/entry|1|{{TCG ID|Totodile Half Deck|Pikachu|1}}|Lightning||1}}\n" +
            "{{halfdecklist/entry|2|{{TCG ID|Totodile Half Deck|Marill|2}}|Water||1}}\n" +
            "{{halfdecklist/entry|3|{{TCG ID|Totodile Half Deck|Hoothoot|3}}|Colorless||1}}\n" +
            "{{halfdecklist/entry|4|{{TCG ID|Totodile Half Deck|Croconaw|4}}|Water||1}}\n" +
            "{{halfdecklist/footer}}";

    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        String page = READS_FROM_BULBAPEDIA
                ? PageReader.readPage(INITIAL_URL)
                : CONTENT;

        List<String> sets = RawTextExtractor.extractSets(page);

        System.setProperty("http.agent", "Chrome");

        Pokedex pokedex = new Pokedex();

        for (String set : sets) {
            Serie serie = new Serie(extractTitle(set), extractImage(set).orElse(""));

            int processThisSet = reader.nextInt();

            if (processThisSet == 0) {
                continue;
            }

            extractCards(set).forEach(serie::addCard);

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
        }
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
