package restapi.webapp.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import restapi.webapp.pojo.Wallet;

import java.util.List;
import java.util.Optional;

/**
 * repository for wallet entity.
 */
public interface WalletRepo extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByBalance(float balance);
    Optional<Wallet> findByHolders(int amount);
    Optional<Wallet> findByPercentageRate(float amount);

}
