package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.jetbrains.annotations.NotNull;
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
import static common.ConstantsKt.APP_DATA_SOURCE_PATH;
import static common.ConstantsKt.APP_DATA_UPDATE_DESTINATION_PATH;
import static java.util.Objects.requireNonNull;

public class JapaneseNameExtractor {

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
      }
    }
  }

  private static final Pattern JNAME_PATTERN = Pattern.compile("\\|jname=(.*)");
  //private static final Pattern JNAME_PATTERN = Pattern.compile("jname=(.*) \\|");

  private static Serie process(Serie serie) {
    for (Card card : serie.getCards().values()) {
        getJapaneseName(card).ifPresent(card::setJapaneseName);
    }

    return serie;
  }

  private static Optional<String> getJapaneseName(Card card) {
    try {
      if (card.getWikiLink() != null && (card.getJapaneseName() == null || card.getJapaneseName().isEmpty())) {
        String wikiLink = String.format("https://bulbapedia.bulbagarden.net/w/index.php?title=%s&action=edit", card.wikiLink);
        System.out.println(wikiLink);
        return extractName(PageReader.readPage(wikiLink));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  @NotNull
  private static Optional<String> extractName(String page) {
    Matcher matcher = JNAME_PATTERN.matcher(page);
    if (matcher.find()) {
      return Optional.of(matcher.group(1));
    }

    return Optional.empty();
  }

}
