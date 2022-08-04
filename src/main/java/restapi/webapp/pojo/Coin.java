package restapi.webapp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.service.RestClient;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name = "coin")
public class Coin implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String currency;
    private float currentPrice;
    private float priceChanged24hr;
    private float high_24;
    private float low_24;
    private float amount;

    public Coin(String cryptoType, float curr_price, float price_change_24h, float high_24hr, float low_24hr, String currency, float amount) {
        this.name = cryptoType;
        this.currentPrice = curr_price;
        this.priceChanged24hr = price_change_24h;
        this.high_24 = high_24hr;
        this.low_24 = low_24hr;
        this.currency = currency;
        this.amount=amount;
    }

    public Coin(Coin coin){
        this.name = coin.getName();
        this.currentPrice = coin.getCurrentPrice();
        this.priceChanged24hr = coin.getPriceChanged24hr();
        this.high_24 = coin.high_24;
        this.low_24 = coin.low_24;
        this.currency = coin.getCurrency();
        this.amount=coin.getAmount();
    }

    public Coin(String name, String currency,float amount) {
        this.name = "bitcoin";
        this.currentPrice = 2555555;
        this.priceChanged24hr = 25;
        this.high_24 = 20;
        this.low_24 = 11;
        this.currency = "usd";
        this.amount=5;
    }
}
