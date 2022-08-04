package restapi.webapp.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    private float balance;
    private float percentageRate;
    private int holders;
    @ManyToMany
    @JoinTable(
            name = "wallet_users",
            joinColumns = {@JoinColumn(name = "wallet_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @ElementCollection
    private Set<User> userList = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Coin> coinList = new HashSet<>();


    public Wallet(Set<Coin> coinList) {
        addCoinsForConstructor(coinList);
        this.setHolders(0);
        this.setPercentageRate(0);
        this.getBalanceForWalletConstructor();
    }
    public void addCoinsForConstructor(Set<Coin> coin) {
        for(Coin c:coin){
            if(this.getCoinList().contains(c)){
                for(Coin b:this.getCoinList()){
                    if(b.getName().equals(c.getName())){
                        b.setAmount(b.getAmount()+c.getAmount());
                    }
                }
            }
            this.getCoinList().add(c);
        }
    }
    public void getBalanceForWalletConstructor(){
        float sum=0;
        for(Coin c: this.getCoinList()){
            sum+=c.getAmount()*c.getCurrentPrice();
        }
        this.setBalance(sum);
    }
}
