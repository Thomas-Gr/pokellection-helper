package types;

import java.util.HashSet;
import java.util.Set;

import lombok.Value;

@Value
public class Pokedex {
    Set<Serie> series = new HashSet<>();

    public Pokedex addSerie(Serie serie) {
        series.add(serie);
        return this;
    }
}
