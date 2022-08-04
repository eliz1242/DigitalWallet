package restapi.webapp.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"id","name","currency","currentPrice","greeting"})
public class CoinDTO {
    @JsonIgnore
    public Coin coin;

    public Long getId(){
        return coin.getId();
    }
    public String getName(){
        return coin.getName();
    }
    public String getCurrency(){
        return coin.getCurrency();
    }
    public float getCurrentPrice(){
        return coin.getCurrentPrice();
    }
    public String getGreeting(){
        return "you are the best";
    }


}

