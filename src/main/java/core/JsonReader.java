package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import types.Card;
import types.Serie;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Files.write;
import static common.ConstantsKt.APP_DATA_UPDATE_DESTINATION_PATH;
import static common.ConstantsKt.APP_DATA_SOURCE_PATH;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static utils.PokemonsData.*;

public class JsonReader {

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

    private static Serie process(Serie serie) {
        if (FRENCH_NAMES.containsKey(serie.getName())) {
            serie.setFrenchName(FRENCH_NAMES.get(serie.getName()));
        }

        for (Card card : serie.getCards().values()) {
            if (card.explanation != null && (card.explanation.isEmpty() || card.explanation.length() == 1)) {
                card.explanation = null;
            }
            card.setPicture(card.getPicture().replaceAll("é", "e"));

            getFrenchName(card).ifPresent(card::setFrenchName);
            getJapaneseName(card).ifPresent(card::setJapaneseName);

            if (card.getFrenchName() != null && card.getFrenchName().equals("")) {
                card.setFrenchName(null);
            }

            if (card.getJapaneseName() != null && card.getJapaneseName().equals("")) {
                card.setJapaneseName(null);
            }

            if (card.getWikiLink() != null && card.getWikiLink().equals("")) {
                card.setWikiLink(null);
            }

        }

        return serie;
    }

    private static Optional<String> getFrenchName(Card card) {
        if (card.getFrenchName() == null || card.getFrenchName().isEmpty()) {
            if (POKEMONS_TO_FRENCH.containsKey(card.getName())) {
                return Optional.of(POKEMONS_TO_FRENCH.get(card.getName()));
            } else if (FRENCH_POKEMON_NAMES.containsKey(card.getName())) {
                return Optional.of(FRENCH_POKEMON_NAMES.get(card.getName()));
            } else {
                Matcher matcher = TRAINERS.matcher(card.getName());
                if (matcher.find()) {
                    if (POKEMONS_TO_FRENCH.get(matcher.group(2)) != null) {
                        String name = String.format(
                                "%s %s%s",
                                POKEMONS_TO_FRENCH.get(matcher.group(2)),
                                TRAINERS_MAPPING_PRONOUN.get(matcher.group(1)),
                                TRAINERS_MAPPING.get(matcher.group(1)));

                        return Optional.of(name);
                    }
                }

                matcher = ZARBI.matcher(card.getName());

                if (matcher.find()) {
                    return Optional.of("Zarbi " + matcher.group(1));
                }

                for (Map.Entry<Pattern, String> pattern : FRENCH_PATTERNS.entrySet()) {
                    matcher = pattern.getKey().matcher(card.getName());

                    if (matcher.find() && POKEMONS_TO_FRENCH.get(matcher.group(1)) != null) {
                        return Optional.of(String.format(pattern.getValue(), POKEMONS_TO_FRENCH.get(matcher.group(1))));
                    }
                }
            }
        }

        return empty();
    }

    private static Optional<String> getJapaneseName(Card card) {
        if (card.getJapaneseName() == null || card.getJapaneseName().isEmpty()) {
            if (POKEMONS_TO_JAPANESE.containsKey(card.getName())) {
                return Optional.of(POKEMONS_TO_JAPANESE.get(card.getName()));
            } else if (JAPANESE_POKEMON_NAMES.containsKey(card.getName())) {
                return Optional.of(JAPANESE_POKEMON_NAMES.get(card.getName()));
            } else {
                Matcher matcher = TRAINERS.matcher(card.getName());
                if (matcher.find()) {
                    if (POKEMONS_TO_JAPANESE.get(matcher.group(2)) != null) {
                        String name = String.format(
                            "%sの%s",
                            JAPANESE_TRAINERS_MAPPING.get(matcher.group(1)),
                            POKEMONS_TO_JAPANESE.get(matcher.group(2)));

                        return Optional.of(name);
                    }
                }

                matcher = ZARBI.matcher(card.getName());

                if (matcher.find()) {
                    return Optional.of("アンノーン " + matcher.group(1));
                }

                for (Map.Entry<Pattern, String> pattern : JAPANESE_PATTERNS.entrySet()) {
                    matcher = pattern.getKey().matcher(card.getName());

                    if (matcher.find() && POKEMONS_TO_JAPANESE.get(matcher.group(1)) != null) {
                        return Optional.of(String.format(pattern.getValue(), POKEMONS_TO_JAPANESE.get(matcher.group(1))));
                    }
                }
            }
        }

        return empty();
    }
}
