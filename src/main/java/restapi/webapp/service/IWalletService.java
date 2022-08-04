package restapi.webapp.service;

import org.springframework.scheduling.annotation.Async;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.exceptions.WalletNotFoundException;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;

import java.util.List;

public interface IWalletService {
    void addCoins(List<Coin> coin, Wallet wallet);
    void updateBalance(List<Wallet> walletList);
    @Async
    void enrolledUser(Wallet wallet, User user);
    void setBalanceAfterInsertingCoin(Wallet wallet,Coin coin);
    void getBalanceForWallet(Wallet wallet);
    Wallet addUserToWallet(Long user_id,Long wallet_id);
}
