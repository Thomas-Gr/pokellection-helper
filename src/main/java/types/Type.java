package types;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Stream;

public enum Type {
    FIRE("Fire"),
    WATER("Water"),
    LIGHTNING("Lightning"),
    PSYCHIC("Psychic"),
    FIGHTING("Fighting"),
    GRASS("Grass"),
    COLORLESS("Colorless"),
    DARKNESS("Darkness"),
    METAL("Metal"),
    TRAINER("Trainer"),
    ENERGY("Energy"),
    DECK_LIST("DeckList"),
    ARTWORK("Artwork"),
    EXTRA_RULE("ExtraRule"),
    PASS_CARD("PassCard"),
    ;

    private static Map<String, Type> bindings = Stream.of(Type.values()).collect(toMap(a -> a.readableName, identity()));

    String readableName;

    Type(String readableName) {
        this.readableName = readableName;
    }

    public static Type fromName(String name) {
        return bindings.getOrDefault(name, ENERGY);
    }

}
