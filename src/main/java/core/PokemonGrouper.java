package core;

import static com.google.common.io.Files.write;
import static common.ConstantsKt.APP_DATA_UPDATE_DESTINATION_FOLDER_PATH;
import static java.util.Objects.requireNonNull;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import types.Card;
import types.Serie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PokemonGrouper {

    private static final String FILE_NAME = "appearances.json";

    private static final HashMap<Integer, Map<String, Set<Integer>>> POKEMON_TO_CARDS = new HashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        readAppearances(new File(APP_DATA_UPDATE_DESTINATION_FOLDER_PATH));
        //extractAppearancesFromSourceFiles(new File(SOURCE_PATH));
        //computeMostCommonPokemon(POKEMON_TO_CARDS);
    }

    private static void readAppearances(final File folder) throws IOException {
        final TypeReference<HashMap<Integer, Map<String, Set<Integer>>>> typeRef =
                new TypeReference<HashMap<Integer, Map<String, Set<Integer>>>>() {};
        HashMap<Integer, Map<String, Set<Integer>>> result =
                objectMapper.readValue(new File(folder + "/" + FILE_NAME), typeRef);

        System.out.println(result);
    }

    private static void extractAppearancesFromSourceFiles(final File folder) throws IOException {
        for (final File fileEntry : requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                process(objectMapper.readValue(fileEntry, Serie.class));
            }
        }

        String formattedResult = new ObjectMapper().writeValueAsString(POKEMON_TO_CARDS);

        File file = new File(String.format("%s/%s", APP_DATA_UPDATE_DESTINATION_FOLDER_PATH, "appearances.json"));
        write(formattedResult, file, Charsets.UTF_8);
    }

    private static void computeMostCommonPokemon(Map<Integer, Map<String, Set<Integer>>> input) {
        for (Map.Entry<Integer, Map<String, Set<Integer>>> integerMapEntry : input.entrySet()) {
            int sum = integerMapEntry.getValue()
                    .values()
                    .stream()
                    .map(Set::size)
                    .mapToInt(a -> a)
                    .sum();

            System.out.println(sum + " " + integerMapEntry.getKey());
        }
    }

    private static void process(Serie serie) {
        for (Card card : serie.getCards().values()) {
            int pokemonNumber = card.getPokemonNumber();

            if (pokemonNumber >= 1) {
                if (!POKEMON_TO_CARDS.containsKey(pokemonNumber)) {
                    POKEMON_TO_CARDS.put(pokemonNumber, new HashMap<>());
                }

                Map<String, Set<Integer>> serieBinding = POKEMON_TO_CARDS.get(pokemonNumber);

                if (!serieBinding.containsKey(serie.getName())) {
                    serieBinding.put(serie.getName(), new HashSet<>());
                }

                serieBinding.get(serie.getName()).add(card.getId());
            }
        }
    }

}
