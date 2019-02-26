package types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Card {
    String name;
    Type type;
    Rarity rarity;
    int number;
    String wikiLink;
    String picture;
    int id;
    int pokemonNumber;
    int count;
    String explanation;
    String frenchName;
    String japaneseName;

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
            @JsonProperty("japaneseName") String japaneseName) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.number = number;
        this.wikiLink = wikiLink;
        this.picture = picture;
        this.pokemonNumber = pokemonNumber;
        this.id = generateId(wikiLink, number, explanation);
        this.count = count;
        this.explanation = explanation;
        this.frenchName = frenchName;
        this.japaneseName = japaneseName;
    }

    private int generateId(String wikiLink, int number, String explanation) {
        if (explanation != null && explanation.trim().length() > 0) {
            return (wikiLink + number + explanation).hashCode();
        } else {
            return (wikiLink + number).hashCode();
        }
    }
}
