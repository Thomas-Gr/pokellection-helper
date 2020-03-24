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
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static utils.PokemonsData.*;

public class JsonReader {

    private static final String SOURCE_PATH = "/Users/grillett/dev/Pokellection/resources/series";
    private static final String DESTINATION = "/Users/grillett/dev/Pokellection/resources/series";
    private static final Map<Integer, Integer> CARD_TO_IMAGE = ImmutableMap.<Integer, Integer>builder()
            .put(-1592629316, 32)
            .build();

    private static final String SERIE_NAME = "Pokémon Web";
    private static final String SERIE_PICTURES_PREFIX = "web-";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        listFilesForFolder(new File(SOURCE_PATH));
    }

    private static void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                Serie serie = process(objectMapper.readValue(fileEntry, Serie.class));

                String formattedSerie = new ObjectMapper().writeValueAsString(serie);

                File file = new File(String.format("%s/%s", DESTINATION, fileEntry.getName()));
                write(formattedSerie, file, Charsets.UTF_8);
            }
        }
    }

    private static ImmutableSet<String> RARITIES = ImmutableSet.of("NONE",
            "COMMON",
            "UNCOMMON",
            "RARE", "RARE_HOLO");

    private static Serie process(Serie serie) {


        if (FRENCH_NAMES.containsKey(serie.getName())) {
            serie.setFrenchName(FRENCH_NAMES.get(serie.getName()));
        }

        if (serie.getJapaneseName() == null) {
            // TODO
        }

        for (Card card : serie.getCards().values()) {
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

            /*
            if (card.getFrenchName() == null) {
                System.out.println(String.format(
                        "%s\t%s\t%s\t%s\t%s\thttps://bulbapedia.bulbagarden.net/wiki/%s",
                        serie.getName(),
                        card.getId(),
                        card.getPokemonNumber(),
                        card.getName(),
                        card.getRarity(),
                        card.getWikiLink()));
            }
            */

            if (card.getJapaneseName() == null) {
                //System.out.println(card.getName());
            }

        }

        return serie;
    }

    private static void updatePictureName(Serie serie, Card card) {
        if (serie.getName().equals(SERIE_NAME) && CARD_TO_IMAGE.containsKey(card.getId())) {
            String image = String.format(SERIE_PICTURES_PREFIX + "%03d.jpg", CARD_TO_IMAGE.get(card.getId()));
            //String image = String.format("%s.jpg", CARD_TO_IMAGE.get(card.getId()));
            //System.out.println(String.format("'%s' : require('../../../resources/images/cards/%s'),", image, image));
            card.setPicture(image);
        }

        if (serie.getName().equals(SERIE_NAME)) {
            System.out.println(String.format("'%s' : require('../../../resources/images/cards/%s'),", card.getPicture(), card.getPicture()));
        }
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
            }
            /*
            else if (FRENCH_POKEMON_NAMES.containsKey(card.getName())) {
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

                matcher = DARK.matcher(card.getName());

                if (matcher.find() && POKEMONS_TO_FRENCH.get(matcher.group(1)) != null) {
                    return Optional.of(POKEMONS_TO_FRENCH.get(matcher.group(1)) + " obscur");
                }

                matcher = LIGHT.matcher(card.getName());

                if (matcher.find() && POKEMONS_TO_FRENCH.get(matcher.group(1)) != null) {
                    return Optional.of(POKEMONS_TO_FRENCH.get(matcher.group(1)) + " lumineux");
                }

                matcher = SHINING.matcher(card.getName());

                if (matcher.find() && POKEMONS_TO_FRENCH.get(matcher.group(1)) != null) {
                    return Optional.of(POKEMONS_TO_FRENCH.get(matcher.group(1)) + " brillant");
                }
            }
            //*/
        }

        return empty();
    }
}
