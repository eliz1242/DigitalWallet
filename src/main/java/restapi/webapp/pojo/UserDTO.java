package restapi.webapp.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@JsonPropertyOrder({"id","fullName","walletList","greeting"})
public class UserDTO {
    @JsonIgnore
    public User user;

    public Long getId() {
        return user.getId();
    }
    public String getName(){
        return user.getFullName();
    }
    public Set<Wallet> getWallets(){
        return user.getWalletList();
    }
    public String getGreeting(){
        return "This is a DTO greeting to your wallet";
    }

}

