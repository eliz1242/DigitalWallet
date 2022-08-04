package restapi.webapp.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    private String fullName;
    private String country;
    private String email;
    @Id
    @GeneratedValue
    private Long id;
    //private UUID myUUID;
    private LocalDateTime creationTime;
    private String[] bestCoin = new String[2];
    private float age;
    @JsonIgnore
    @ManyToMany(mappedBy = "userList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ElementCollection
    private Set<Wallet> walletList = new HashSet<>();

    public User(String name, String country, String email, int age) {
        this.fullName = name;
        this.country=country;
        this.age=age;
        this.email = email;
        this.creationTime = LocalDateTime.now();
    }
}