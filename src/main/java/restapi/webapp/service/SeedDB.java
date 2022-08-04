package restapi.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.CoinRepo;
import restapi.webapp.repo.UserRepo;

import java.util.HashSet;
import java.util.Set;

@Configuration
// class declares one or one @Bean method
// Spring container to generates bean definitions and handles requests beans (at runtime)
public class SeedDB {
    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);
    @Bean
        // CommandLineRunner beans once the application context is loaded.
    CommandLineRunner initDatabase(JpaRepository<Wallet, Long> walletRepo, UserRepo userRepo, CoinRepo coinRepo) {

        // runner gets a copy of the new DB and creates the following
        // products and saves them

        return args -> {
            User user = userRepo.save(new User("Eliko","Israel","Eli1242@gmail.com",27));
            logger.info("new user" + user);
            Coin coin = coinRepo.save(new Coin("bitcoin","usd",5));
            logger.info("new coin " + coin);
            Coin coin2 = coinRepo.save(new Coin("ethereum","usd",5));
            logger.info("new coin " + coin2);
            Set<Coin> coinSet = Set.of(coin,coin2);
            Wallet wallet = walletRepo.save(new Wallet(coinSet));
            logger.info("new wallet " + wallet);
            Set<User> userList = Set.of(user);
            wallet.setUserList(userList);
            wallet.setHolders(wallet.getUserList().size());
            logger.info("updated wallet" +walletRepo.save(wallet));
        };
    }
}