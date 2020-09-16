package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import types.Card;
import types.Serie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Files.write;
import static common.ConstantsKt.APP_DATA_SOURCE_PATH;
import static common.ConstantsKt.APP_DATA_UPDATE_DESTINATION_PATH;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;
import static utils.PokemonsData.*;

public class ImageChooser {

    private static final Map<String, String> CACHE = new HashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        listFilesForFolder(new File(APP_DATA_SOURCE_PATH));
    }

    private static void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory() && !fileEntry.getName().contains("DS_Store")) {
                Serie serie = process(objectMapper.readValue(fileEntry, Serie.class));

                String formattedSerie = new ObjectMapper().writeValueAsString(serie);

                File file = new File(String.format("%s/%s", APP_DATA_UPDATE_DESTINATION_PATH, fileEntry.getName()));
                write(formattedSerie, file, Charsets.UTF_8);

                String content = serie.getCards().values()
                    .stream()
                    .map(Card::getPicture)
                    .distinct()
                    .map(a -> format("'%s' : require('../../../resources/images/cards/%s')", a, a))
                    .collect(joining(",\n"));

                String expansionName = serie.getName()
                    .replaceAll("['(), !\\[\\]:\\-]", "")
                    .replaceAll("é", "e");

                File file2 = new File(format("out/jsFiles/%s.js", expansionName));
                write(format("const %s =\n {%s};\n\nexport default %s", expansionName, content, expansionName), file2, UTF_8);
            }
        }
    }

    private static Scanner reader = new Scanner(System.in);

    private static Serie process(Serie serie) {
        for (Card card : serie.getCards().values()) {
            if (card.getPicture().contains(", ")) {
                System.out.println();
                System.out.println(String.format("%s - %s", serie.getName(), card.getName()));
                System.out.println(String.format("https://bulbapedia.bulbagarden.net/w/index.php?title=%s", card.getWikiLink()));
                System.out.println();
                String[] pictures = card.getPicture().split(", ");

                if (CACHE.containsKey(card.getWikiLink())) {
                    card.setPicture(CACHE.get(card.getWikiLink()));
                } else {
                    int i = 0;
                    for (String picture : pictures) {
                        System.out.println(String.format("[%s]  https://bulbapedia.bulbagarden.net/wiki/File:%s", i++, picture));
                    }

                    int cardIndex = reader.nextInt();

                    if (cardIndex >= 0 && cardIndex < pictures.length) {
                        card.setPicture(pictures[cardIndex]);
                        CACHE.put(card.getWikiLink(), pictures[cardIndex]);
                    }
                }
            }

        }

        return serie;
    }
}
