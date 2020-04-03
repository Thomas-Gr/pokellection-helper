package types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Serie {
    public String name;
    public String frenchName;
    public String japaneseName;
    public String language;
    public  String image;
    public  Map<Integer, Card> cards;
    public boolean showNumbers;
    public int numberOfZeros;
    public String numberSuffix;

    @JsonCreator
    public Serie(
            @JsonProperty("name") String name,
            @JsonProperty("frenchName") String frenchName,
            @JsonProperty("japaneseName") String japaneseName,
            @JsonProperty("cards") Map<Integer, Card> cards,
            @JsonProperty("language") String language,
            @JsonProperty("image") String image,
            @JsonProperty("showNumbers") boolean showNumbers,
            @JsonProperty("numberOfZeros") int numberOfZeros,
            @JsonProperty("numberSuffix") String numberSuffix) {
        this.frenchName = frenchName;
        this.name = name;
        this.japaneseName = japaneseName;
        this.cards = cards;
        this.image = image;
        this.language = language;
        this.showNumbers = showNumbers;
        this.numberOfZeros = numberOfZeros;
        this.numberSuffix = numberSuffix;
    }

    public Serie(String name, String image) {
        this.name = name;
        this.frenchName = name;
        this.japaneseName = name;
        this.cards = new HashMap<>();
        this.image = image;
        this.language = "JP";
        this.showNumbers = false;
    }

    public Serie addCard(Card card) {
        cards.put(card.getId(), card);
        return this;
    }
}
