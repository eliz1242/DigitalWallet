package restapi.webapp.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.exceptions.WalletNotFoundException;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.UserRepo;
import restapi.webapp.repo.WalletRepo;

import java.util.Arrays;
import java.util.List;

@Service
@NoArgsConstructor
public class WalletService implements IWalletService{
    @Autowired
    private RestClient RestClient;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;

    /**
     * the function takes a certain wallet and inserting the list of coins given
     * @param coin
     * @param wallet
     */
    @Override
    public void addCoins(List<Coin> coin,Wallet wallet) {
        for(Coin c:coin){
            if(wallet.getCoinList().contains(c)){
                for(Coin b:wallet.getCoinList()){
                    if(b.getName().equals(c.getName())){
                        b.setAmount(b.getAmount()+c.getAmount());
                    }
                }
            }
            wallet.getCoinList().add(c);
        }
    }

    /**
     * the function updating the balance of every wallet from the list
     * @param walletList
     */
    @Override
    public void updateBalance(List<Wallet> walletList){
        float sum=0;
        for(Wallet wallet:walletList){
            for(Coin c:wallet.getCoinList()){
                float curr_price = RestClient.getCurrency(c.getName(),c.getCurrency()); //TODO: check this out
                sum+=c.getAmount()*curr_price;
            }
            wallet.setBalance(sum);
            walletRepo.save(wallet);
            sum=0;
        }
    }

    /**
     * the function inserting the user into the wallet userlist
     * @param wallet
     * @param user
     */
    @Async
    @Override
    public void enrolledUser(Wallet wallet,User user){
        wallet.getUserList().add(user);
    }

    /**
     * the function setting the balance of certain wallet after inserting certain coin
     * @param wallet
     * @param coin
     */
    @Override
    public void setBalanceAfterInsertingCoin(Wallet wallet,Coin coin){
        float curr_balance=wallet.getBalance();
        addCoins(Arrays.asList(coin),wallet);
        wallet.setBalance(wallet.getBalance()+coin.getAmount()* coin.getCurrentPrice());
        wallet.setPercentageRate(((wallet.getBalance()-curr_balance)/curr_balance)*100);
    }

    /**
     * the function setting the balance for the given wallet
     * @param wallet
     */
    @Override
    public void getBalanceForWallet(Wallet wallet){
        float sum=0;
        for(Coin c: wallet.getCoinList()){
            sum+=c.getAmount()*c.getCurrentPrice();
        }
        wallet.setBalance(sum);
    }

    /**
     * the function throws exception if the user or wallet not exist, otherwise it inserting the
     * user to the wallet userList and then updating the fields needed.
     * @param user_id
     * @param wallet_id
     * @return
     */
    @Override
    public Wallet addUserToWallet(Long user_id, Long wallet_id){
        User user = userRepo.findById(user_id).orElseThrow(()->new UserNotFoundException(user_id));
        Wallet wallet = walletRepo.findById(wallet_id).orElseThrow(()->new WalletNotFoundException(wallet_id));
        enrolledUser(wallet,user);
        wallet.setHolders(wallet.getHolders()+1);
        walletRepo.save(wallet);
        RestClient.checkingBestCoin(user.getFullName());
        return wallet;
    }

}
