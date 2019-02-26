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
    String name;
    String frenchName;
    String japaneseName;
    String language;
    String image;
    Map<Integer, Card> cards;
    boolean showNumbers;

    @JsonCreator
    public Serie(
            @JsonProperty("name") String name,
            @JsonProperty("frenchName") String frenchName,
            @JsonProperty("japaneseName") String japaneseName,
            @JsonProperty("cards") Map<Integer, Card> cards,
            @JsonProperty("language") String language,
            @JsonProperty("image") String image,
            @JsonProperty("showNumbers") boolean showNumbers) {
        this.frenchName = frenchName;
        this.name = name;
        this.japaneseName = japaneseName;
        this.cards = cards;
        this.image = image;
        this.language = language;
        this.showNumbers = showNumbers;
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
