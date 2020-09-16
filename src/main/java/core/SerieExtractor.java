package core;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.write;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static utils.RawTextExtractor.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
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
    public static final String TYPE_OF_SET = "Setlist"; // Setlist // halfdecklist // Promo
    public static final boolean FAST = true;
    public static final boolean JOIN_ALL_POSSIBLE_IMAGES = true;
    private static final boolean READS_FROM_BULBAPEDIA = true;

    private static final List<String> SERIES = ImmutableList.of(
        "PokéPark_Premium_Files_(TCG)"
        );

    private static final String INITIAL_URL = "https://bulbapedia.bulbagarden.net/w/index.php?title=%s&action=edit";

    private static final String CONTENT = "";

    private static final boolean EXPORT_IMAGE_JS_FILE = true;

    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.setProperty("http.agent", "Chrome");

        if (READS_FROM_BULBAPEDIA) {
            for (String serie : SERIES) {
                String page = PageReader.readPage(format(INITIAL_URL, serie));

                extractPage(page);
            }
        } else {
            extractPage(CONTENT);
        }
    }

    private static void extractPage(String page) throws IOException {
        List<String> sets = RawTextExtractor.extractSets(page);

        Pokedex pokedex = new Pokedex();

        int numberOfSets = 0;
        for (String set : sets) {
            Serie serie = new Serie(extractTitle(set), extractImage(set).orElse(""));

            System.out.println(serie.getName());
            if (!FAST) {
                int processThisSet = reader.nextInt();

                if (processThisSet == 0) {
                    continue;
                }
            }

            if (numberOfSets++ < 2) {
                //continue;
            }
            //*/

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

            if (EXPORT_IMAGE_JS_FILE) {
                File file2 = new File(format("out/jsFiles/%s.js", serie.getName()));
                write(format("export default ChangeName =\n  {%s}", content), file2, UTF_8);
            }

            for (Card card : serie.getCards().values()) {
                String[] images = card.getPicture().split(IMAGE_DELIMITER);
                System.out.println(format("(exporting %d images)", images.length));
                for (String cardName : images) {
                    File f = new File("out/images/cards/" + cardName.replace('é', 'e'));
                    if (!f.exists()) {
                        try {
                            downloadImage(cardName);
                        } catch (Exception e) {
                            System.out.println(cardName + " doesn't exist");
                        }
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
