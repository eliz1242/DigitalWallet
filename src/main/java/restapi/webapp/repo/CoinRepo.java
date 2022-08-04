package restapi.webapp.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.User;

import java.util.List;
import java.util.Optional;

/**
 * repository for coin entity.
 */
public interface CoinRepo extends JpaRepository<Coin,Long> {
    Optional<List<Coin>> findByCurrentPrice(float price);
    Optional<Coin> findByName(String name);
    Optional<List<Coin>> findByPriceChanged24hr(float priceChanged24hr);
}
