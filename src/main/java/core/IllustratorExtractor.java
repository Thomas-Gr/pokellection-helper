package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import types.Card;
import types.Serie;
import utils.PageReader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Files.write;
import static common.ConstantsKt.APP_DATA_UPDATE_DESTINATION_PATH;
import static common.ConstantsKt.APP_DATA_SOURCE_PATH;
import static java.util.Objects.requireNonNull;

public class IllustratorExtractor {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {
    listFilesForFolder(new File(APP_DATA_SOURCE_PATH));
  }

  private static void listFilesForFolder(final File folder) throws IOException {
    for (final File fileEntry : requireNonNull(folder.listFiles())) {
      if (!fileEntry.isDirectory()) {
        Serie serie = process(objectMapper.readValue(fileEntry, Serie.class));

        String formattedSerie = new ObjectMapper().writeValueAsString(serie);

        File file = new File(String.format("%s/%s", APP_DATA_UPDATE_DESTINATION_PATH, fileEntry.getName()));
        write(formattedSerie, file, Charsets.UTF_8);
      }
    }
  }

  private static final Pattern ILLUSTRATOR_PATTERN = Pattern.compile("Illus. \\[\\[(.*)\\]\\]");
  private static final Pattern ADDITIONAL_ILLUSTRATOR_PATTERN = Pattern.compile("\\|illus([0-9]+)=(.*)");

  private static Serie process(Serie serie) {
    for (Card card : serie.getCards().values()) {
        getIllustrator(card).ifPresent(card::setIllustrator);
    }

    return serie;
  }

  private static Optional<String> getIllustrator(Card card) {
    try {
      if (card.getWikiLink() != null && (card.getIllustrator() == null || card.getIllustrator().isEmpty())) {
        String wikiLink = String.format("https://bulbapedia.bulbagarden.net/w/index.php?title=%s&action=edit", card.wikiLink);

        System.out.println(wikiLink);
        String page = PageReader.readPage(wikiLink);

        Set<String> illustrators = new HashSet<>();
        Matcher matcher = ILLUSTRATOR_PATTERN.matcher(page);
        while (matcher.find()) {
          illustrators.add(matcher.group(1));
        }

        Matcher matcher2 = ADDITIONAL_ILLUSTRATOR_PATTERN.matcher(page);
        while (matcher2.find()) {
          illustrators.add(matcher2.group(2));
        }

        if (!illustrators.isEmpty()) {
          return Optional.of(String.join(", ", illustrators));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

}
