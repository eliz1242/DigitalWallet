package restapi.webapp.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"id","holders","balance","greeting"})
public class WalletDTO {
    @JsonIgnore
    public Wallet wallet;

    public Long getId() {
        return wallet.getId();
    }
    public int getHolders(){
        return wallet.getHolders();
    }
    public float getBalance(){
        return wallet.getBalance();
    }
    public String getGreeting(){
        return "This is a DTO greeting to your wallet";
    }

}

