package core;

import static com.google.common.io.Files.write;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static utils.PokemonsData.DARK;
import static utils.PokemonsData.FRENCH_NAMES;
import static utils.PokemonsData.FRENCH_POKEMON_NAMES;
import static utils.PokemonsData.LIGHT;
import static utils.PokemonsData.POKEMONS_TO_FRENCH;
import static utils.PokemonsData.POKEMONS_TO_JAPANESE;
import static utils.PokemonsData.SHINING;
import static utils.PokemonsData.TRAINERS;
import static utils.PokemonsData.TRAINERS_MAPPING;
import static utils.PokemonsData.TRAINERS_MAPPING_PRONOUN;
import static utils.PokemonsData.ZARBI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import types.Card;
import types.Serie;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;

public class JsonReader {

    private static final String SOURCE_PATH = "/Users/grillett/dev/Pokellection/resources/series";
    private static final String DESTINATION = "/Users/grillett/dev/Pokellection/resources/series";

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

    private static Serie process(Serie serie) {

        if (FRENCH_NAMES.containsKey(serie.getName())) {
            serie.setFrenchName(FRENCH_NAMES.get(serie.getName()));
        }

        if (serie.getJapaneseName() == null) {
            // TODO
        }

        for (Card card : serie.getCards().values()) {
            getFrenchName(card).ifPresent(card::setFrenchName);

            if (card.getFrenchName() == null) {
                System.out.println(card.getName());
            }

            if (card.getJapaneseName() == null || card.getJapaneseName().isEmpty()) {
                if (POKEMONS_TO_JAPANESE.containsKey(card.getName())) {
                    card.setJapaneseName(POKEMONS_TO_JAPANESE.get(card.getName()));
                }
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
        }

        return empty();
    }
}
