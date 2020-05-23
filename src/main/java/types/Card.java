package types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Card {
    public String name;
    public Type type;
    public Rarity rarity;
    public int number;
    public String wikiLink;
    public String picture;
    public int id;
    public int pokemonNumber;
    public int count;
    public String explanation;
    public String frenchName;
    public String japaneseName;
    public String illustrator;
    public String bulbapediaId;

    @JsonCreator
    public Card(
            @JsonProperty("name") String name,
            @JsonProperty("type") Type type,
            @JsonProperty("rarity") Rarity rarity,
            @JsonProperty("number") int number,
            @JsonProperty("wikiLink") String wikiLink,
            @JsonProperty("picture") String picture,
            @JsonProperty("pokemonNumber") int pokemonNumber,
            @JsonProperty("count") int count,
            @JsonProperty("explanation") String explanation,
            @JsonProperty("frenchName") String frenchName,
            @JsonProperty("japaneseName") String japaneseName,
            @JsonProperty("illustrator") String illustrator,
            @JsonProperty("bulbapediaId") String bulbapediaId) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.number = number;
        this.wikiLink = wikiLink;
        this.picture = picture;
        this.pokemonNumber = pokemonNumber;
        this.id = generateId(name, wikiLink, number, explanation);
        this.count = count;
        this.explanation = explanation;
        this.frenchName = frenchName;
        this.japaneseName = japaneseName;
        this.illustrator = illustrator;
        this.bulbapediaId = bulbapediaId;
    }

    private int generateId(String name, String wikiLink, int number, String explanation) {
        if (explanation != null && explanation.trim().length() > 0) {
            return (name + wikiLink + number + explanation).hashCode();
        } else {
            return (name + wikiLink + number).hashCode();
        }
    }
}
