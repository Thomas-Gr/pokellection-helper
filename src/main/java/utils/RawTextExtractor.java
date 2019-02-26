package utils;

import static core.SerieExtractor.TYPE_OF_SET;
import static java.util.regex.Pattern.DOTALL;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import core.SerieExtractor;
import types.Card;
import types.Rarity;
import types.Type;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawTextExtractor {
    private static Scanner reader = new Scanner(System.in);

    private static final Pattern SET_PATTERN = Pattern.compile(String.format("(\\{\\{%s/header.*?|\\{\\{%s/header.*?)\\{\\{(%s|%s)/footer", TYPE_OF_SET, TYPE_OF_SET.toLowerCase(), TYPE_OF_SET, TYPE_OF_SET.toLowerCase()), DOTALL);
    private static final Pattern ENTRY_PATTERN = Pattern.compile(String.format("\\{\\{(%s|%s)/entry\\|(.*)\\|\\{\\{(.*)}}(.*)\\|(.*)\\|(.*)\\|(.*)\\|(.*)}}", TYPE_OF_SET, TYPE_OF_SET.toLowerCase()));
    private static final Pattern ENTRY_PATTERN_2 = Pattern.compile(String.format("\\{\\{(%s|%s)/entry\\|(.*)\\|\\{\\{(.*)}}(.*)\\|(.*)\\|(.*)\\|(.*)}}", TYPE_OF_SET, TYPE_OF_SET.toLowerCase()));
    private static final String EMPTY_LINE_PATTERN = "(?m)^[ \t]*\r?\n";
    private static final String NEW_LINE = "\\r?\\n";
    private static final String ENTRY_START = String.format("{{%s/entry", TYPE_OF_SET.toLowerCase());
    private static final String EDIT_PAGE_LINK = "https://bulbapedia.bulbagarden.net/w/index.php?title=%s_(%s_%s)&action=edit";
    private static final String EDIT_PAGE_LINK_LENGTH_2 = "https://bulbapedia.bulbagarden.net/w/index.php?title=%s_(%s)&action=edit";
    private static final String EDIT_PAGE_LINK_2 = "https://bulbapedia.bulbagarden.net/w/index.php?title=%s&action=edit";
    private static final Pattern TITLE_EXTRACTOR = Pattern.compile("\\{\\{.*/header.*\\|title=(.*?)[|}]");
    private static final Pattern IMAGE_EXTRACTOR = Pattern.compile("\\{\\{.*/header.*\\|image=(.*?)[|}]");
    private static final Pattern REDIRECT = Pattern.compile("(?m) *#(REDIRECT|redirect|Redirect:)[ \n]?\\[\\[(.*)]] *");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("image[0-9]*=(.*?\\.(jpg|png))");

    public static List<String> extractHalfDeck(String text) {
        Matcher regexMatcher = SET_PATTERN.matcher(text);

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        while (regexMatcher.find()) {
            builder.add(regexMatcher.group(1).replaceAll(EMPTY_LINE_PATTERN, "").trim());
        }

        return builder.build();
    }

    public static List<String> extractSets(String text) {
        Matcher regexMatcher = SET_PATTERN.matcher(text);

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        while (regexMatcher.find()) {
            builder.add(regexMatcher.group(1).replaceAll(EMPTY_LINE_PATTERN, "").trim());
        }

        return builder.build();
    }

    public static List<Card> extractCards(String text) {
        String[] lines = text.split(NEW_LINE);
        List<Card> result = new ArrayList<>();
        for (String line : lines) {
            String s = line.toLowerCase();

            if (s.startsWith(ENTRY_START)) {
                Matcher regexMatcher = ENTRY_PATTERN.matcher(line);
                if (!regexMatcher.find()) {
                    regexMatcher = ENTRY_PATTERN_2.matcher(line);

                    if (!regexMatcher.find()) {
                        continue;
                    }
                }

                String[] split = regexMatcher.group(3).split("\\|");

                String wikiLink = stringToLink(extractLink(regexMatcher.group(3).replaceAll("&", "%26")));
                String wikiContent = PageReader.readPage(wikiLink);

                if (wikiContent.toUpperCase().trim().startsWith("#REDIRECT")) {
                    Matcher matcher = REDIRECT.matcher(wikiContent);
                    if (matcher.find()) {
                        wikiLink = String.format(EDIT_PAGE_LINK_2, stringToLink(matcher.group(2)));
                        wikiContent = PageReader.readPage(wikiLink);
                    }
                }

                String actualWikiLink = wikiLink
                        .replace("https://bulbapedia.bulbagarden.net/w/index.php?title=", "")
                        .replace("&action=edit", "");

                String cardName = split.length == 2 ? split[1] : split[2];

                String additionalName = regexMatcher.group(4).trim();

                if (additionalName.length() > 0) {
                    cardName += " " + additionalName;
                }

                String description = regexMatcher.groupCount() >= 8 ? regexMatcher.group(8) : "";

                String picture = getPictureName(wikiContent, cardName, actualWikiLink, description);

                String[] words = cardName.split("[ ,']");

                ImmutableBiMap<String, Integer> pokemonList = PokemonsData.POKEMONS.inverse();

                int number = -42;
                for (String word : words) {
                    if (pokemonList.containsKey(word)) {
                        number = pokemonList.get(word);

                        break;
                    }
                }

                if (TYPE_OF_SET.equals("halfdecklist")) {
                    result.add(new Card(
                            cardName,
                            Type.fromName(regexMatcher.group(5)),
                            Rarity.NONE,
                            extractNumber(regexMatcher.group(2)),
                            actualWikiLink,
                            picture,
                            number,
                            extractNumber(regexMatcher.group(7)),
                            regexMatcher.group(7),
                            "", ""));
                } else {
                    result.add(new Card(
                            cardName,
                            Type.fromName(regexMatcher.group(5)),
                            Rarity.fromName(regexMatcher.group(7)),
                            extractNumber(regexMatcher.group(2)),
                            actualWikiLink,
                            picture,
                            number,
                            1,
                            description,
                            "", ""));
                }
            }
        }

        return result;
    }

    private static String getPictureName(String wikiContent, String cardName, String wikiLink, String description) {
        wikiContent = wikiContent.replaceAll("(?m)^<!--.*", "");
        Matcher matcher = IMAGE_PATTERN.matcher(wikiContent);

        List<String> pictures = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);

            if (group != null) {
                pictures.add(stringToLink(group.trim()));
            }
        }

        if (pictures.isEmpty()) {
            System.out.println("WARNING: No picture for: " + wikiContent);
            return "";
        } else if (pictures.size() == 1 || SerieExtractor.FAST) {
            return pictures.get(0);
        } else {
            System.out.println("Multiple options for: " + cardName + " " + description);

            int cardIndex = -1;

            while (cardIndex < 0 || cardIndex >= pictures.size()) {
                for (int i = 0; i < pictures.size(); i++) {
                    System.out.println(String.format("\t%s - %s", i, pictures.get(i)));

                    if (cardIndex > 0) openInBrowser("https://bulbapedia.bulbagarden.net/wiki/File:" + pictures.get(i));
                }

                if (cardIndex > 0) openInBrowser("https://bulbapedia.bulbagarden.net/wiki/" + wikiLink);

                cardIndex = reader.nextInt();
            }

            return pictures.get(cardIndex);
        }
    }

    private static void openInBrowser(String link) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI oURL = new URI(link);
            desktop.browse(oURL);
        } catch (Exception e) {
        }
    }

    private static int extractNumber(String numberString) {
        if (numberString.contains("/")) {
            return Integer.valueOf(numberString.substring(0, numberString.indexOf("/")));
        } else if (numberString.equals("None") || numberString.contains("LV")) {
            return -42;
        } else {
            try {
                return Integer.valueOf(numberString);
            } catch (NumberFormatException e) {
                return -42;
            }
        }
    }

    private static String extractLink(String group) {
        String[] split = group.split("\\|");

        if (split.length == 2) {
            return String.format(EDIT_PAGE_LINK_LENGTH_2, split[1], split[0]);
        } else if (split.length == 3) {
            return String.format(EDIT_PAGE_LINK_LENGTH_2, split[1], split[2]);
        } else {
            return String.format(EDIT_PAGE_LINK, split[2], split[1], split[3]);
        }
    }

    private static String stringToLink(String s) {
        return s.replaceAll(" ", "_");
    }

    public static String extractTitle(String text) {
        Matcher regexMatcher = TITLE_EXTRACTOR.matcher(text);

        if (regexMatcher.find()) {
            return regexMatcher.group(1);
        }

        throw new IllegalStateException("No title found in " + text);
    }

    public static Optional<String> extractImage(String text) {
        Matcher regexMatcher = IMAGE_EXTRACTOR.matcher(text);

        if (regexMatcher.find()) {
            return Optional.of(stringToLink(regexMatcher.group(1)));
        }

        return Optional.empty();
    }

}
