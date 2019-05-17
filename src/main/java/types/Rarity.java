package types;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Stream;

public enum Rarity {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    RARE_HOLO("Rare Holo"),
    SUPER_RARE("SuperRare"),
    SUPER_RARE_HOLO("SuperRare Holo"),
    ULTRA_RARE_UNCOMMON("Ultra-Rare Uncommon"),
    EX("Rare Holo ex"),
    SHINING_HOLO("Shining Holo"),
    NONE("None");

    private static Map<String, Rarity> bindings = Stream.of(Rarity.values()).collect(toMap(a -> a.readableName, identity()));

    String readableName;

    Rarity(String readableName) {
        this.readableName = readableName;
    }

    public static Rarity fromName(String name) {
        return bindings.getOrDefault(name, NONE);
    }

}
