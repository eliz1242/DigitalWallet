package restapi.webapp.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.UserRepo;
import restapi.webapp.repo.WalletRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Service
public class UserService implements IUserService{
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;

    /**
     * the function deletes the user from all the wallets userLists exists before deleting the person.
     * @param id
     */
    @Override
    public void deletePersonFromWallet(Long id) {
        List<Wallet> walletList = walletRepo.findAll();
        for(Wallet wallet:walletList){
            if(wallet.getUserList().contains(userRepo.findById(id).orElseThrow(()->new UserNotFoundException(id)))){
                Set<User> newUsers = new HashSet<>();
                for(User user:wallet.getUserList()){
                    if(!user.getFullName().equals(userRepo.findById(id).get().getFullName()))
                        newUsers.add(user);
                }
                wallet.setUserList(newUsers);
                walletRepo.save(wallet);
            }
        }
    }
}
