package restapi.webapp.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.exceptions.WalletNotFoundException;
import restapi.webapp.service.IWalletService;
import restapi.webapp.service.RestClient;
import restapi.webapp.assemblers.WalletAssembler;
import restapi.webapp.assemblers.WalletDTOAssembler;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.pojo.WalletDTO;
import restapi.webapp.repo.UserRepo;
import restapi.webapp.repo.WalletRepo;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
//@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private WalletAssembler walletAssembler;
    @Autowired
    private RestClient RestClient;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletDTOAssembler walletDTOAssembler;
    @Autowired
    private IWalletService walletService;

    /**
     * the function finds the wallet according to the given id
     *
     * @param id
     * @return according to the optional if the value is not missing
     * it will return the wallet with response entity ok else it will return not found.
     */
    @GetMapping("/wallets/{id}")
    public Object getSingleWallet(@PathVariable Long id) {
        return walletRepo.findById(id).
                map(walletAssembler::toModel).
                map(ResponseEntity::ok).
                orElseThrow(()->new WalletNotFoundException(id));
    }

    /**
     * the function adds user into wallet
     *
     * @param user_id
     * @param wallet_id
     * @return the saved data to the repo + the new wallet at the response.
     */
    @PutMapping("/wallets/{user_id}/{wallet_id}")
    Wallet updateWallet(@PathVariable Long user_id, @PathVariable Long wallet_id) { // Relied on Jackson component for serialization
        Wallet wallet =  walletService.addUserToWallet(user_id, wallet_id);
        return walletRepo.save(wallet);
    }

    /**
     * the function takes all the wallets update their currencies from the api and show them with selfLinks and link to the collection
     *
     * @return the collectionModel of the wallets
     */
    @GetMapping("/wallets")
    public CollectionModel<EntityModel<Wallet>> getAllWallets() {
        List<Wallet> walletList = walletRepo.findAll();
        walletService.updateBalance(walletList);
        return walletAssembler.toCollectionModel(walletList);
    }

    /**
     * the function creates a wallet according to the given list of coins
     *
     * @param coin
     * @return the new wallet into the DB
     */
    @PostMapping("/wallets")
    Wallet createWallet(@RequestBody List<Coin> coin) {
        Wallet newWallet = new Wallet();
        walletService.addCoins(coin, newWallet);
        newWallet.setHolders(0);
        newWallet.setPercentageRate(0);
        walletService.getBalanceForWallet(newWallet);
        return walletRepo.save(newWallet);
    }

    /**
     * the function takes a specific wallet according to his id and inserts the given coin to the wallet.
     *
     * @param coin
     * @param id
     * @return saving the updated wallet and return the new wallet in the response.
     */
    @PutMapping("/wallets/addCoin/{id}")
    // add coin to existing wallet
    Wallet addCoinToWallet(@RequestBody Coin coin, @PathVariable Long id) {
        Wallet wallet = walletRepo.findById(id).orElseThrow(()-> new WalletNotFoundException(id));
        walletService.setBalanceAfterInsertingCoin(wallet, coin);
        walletRepo.save(wallet);
        return wallet;
    }

    /**
     * * This method extracts id as path variable and replaces the old wallet if exists
     * * @param id
     * * @param newProduct
     * * @return updated wallet or the newly created product
     */
    @PutMapping("/wallets")
    Wallet updateWallet(@RequestBody Wallet newWallet) {
        return walletRepo.findById(newWallet.getId())
                // convert row in table to Java object
                .map(walletToUpdate -> {
                    walletToUpdate.setBalance(newWallet.getBalance());
                    walletToUpdate.setUserList(newWallet.getUserList());
                    walletToUpdate.setCoinList(newWallet.getCoinList());
                    walletToUpdate.setHolders(newWallet.getHolders());
                    walletToUpdate.setPercentageRate(newWallet.getPercentageRate());
                    // save Java object to row in an SQL table
                    return walletRepo.save(walletToUpdate);
                    // if product does not exist either throw an exception or create a new one
                }).orElseGet(() -> walletRepo.save(newWallet));
    }

    /**
     * * This method extracts id as path variable and replaces the old wallet if exists
     * * @param id
     * * @param newProduct
     * * @return updated wallet or the newly created product
     */
    @PutMapping("/wallets/{id}")
    Wallet updateWallet(@PathVariable Long id, @RequestBody Wallet newWallet) {
        return walletRepo.findById(id)
                // convert row in table to Java object
                .map(walletToUpdate -> {
                    walletToUpdate.setBalance(newWallet.getBalance());
                    walletToUpdate.setUserList(newWallet.getUserList());
                    walletToUpdate.setCoinList(newWallet.getCoinList());
                    walletToUpdate.setHolders(newWallet.getHolders());
                    walletToUpdate.setPercentageRate(newWallet.getPercentageRate());
                    // save Java object to row in an SQL table
                    return walletRepo.save(walletToUpdate);
                    // if product does not exist either throw an exception or create a new one
                }).orElseGet(() -> walletRepo.save(newWallet));
    }

    /**
     * the function delete user according to the given id(userID).
     *
     * @param id
     */
    @DeleteMapping("/wallets/{id}")
    void deleteUser(@PathVariable Long id) {
        walletRepo.deleteById(id);
    }

    /**
     * the function find the wallet to update with the given id and converts it
     * into responseEntity of entity model of WALLET DTO
     *
     * @param id
     * @return ResponseEntity ok + the certain wallet in the response DTO style or ResponseEntity not found.
     */
    @GetMapping("/wallets/{id}/info")
    public Object walletInfo(@PathVariable Long id) {
        return walletRepo.findById(id) //
                .map(WalletDTO::new) //
                .map(walletDTOAssembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElseThrow(()->new WalletNotFoundException(id));
    }

    /**
     * the function returns all the existing wallets DTO style.
     *
     * @return
     */
    @GetMapping("/wallets/info")
    public ResponseEntity<CollectionModel<EntityModel<WalletDTO>>> allWalletsInfo() {

        return ResponseEntity.ok( //
                walletDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(walletRepo.findAll().spliterator(), false) //
                                .map(WalletDTO::new) //
                                .collect(Collectors.toList())));
    }


    /**
     * the function aims for certain wallet according to his balance and then shows the details according to the DTO
     *
     * @param price
     * @return response entity ok + walletDTOInfo or returns response not found
     */
    @GetMapping("/wallets/balance/{price}")
    Object findByBalance(@PathVariable float price) {
        return walletRepo.findByBalance(price)
                .map(walletAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new WalletNotFoundException(price,"that balance: "));
    }

    /**
     * the function aims for certain wallet according to amount of holders and then shows the details according to the DTO
     *
     * @param amount
     * @return response entity ok + walletDTOInfo or returns response not found
     */
    @GetMapping("/wallets/holders/{amount}")
    Object findByHolders(@PathVariable int amount) {
        return walletRepo.findByHolders(amount)
                .map(walletAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new WalletNotFoundException(amount,"that holders amount : "));
    }

    /**
     * the function aims for certain wallet according to his percentage in his account and then shows the details according to the DTO
     *
     * @param amount
     * @return response entity ok + walletDTOInfo or returns response not found
     */
    @GetMapping("/wallets/percentage/{amount}")
    Object findByPercentage(@PathVariable float amount) {
        return walletRepo.findByPercentageRate(amount)
                .map(walletAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new WalletNotFoundException(amount,"that percentage amount: "));
    }

    /**
     * the function finds all the wallets that are in the balance range
     * @param start -> balance minimum
     * @param end -> balance maximum
     * @return all the wallets as DTO style that in that range.
     */
    @GetMapping("/wallets/DTO/balanceBetween/")
    ResponseEntity<CollectionModel<EntityModel<WalletDTO>>> findBetweenBalance(@RequestParam("start") float start,@RequestParam("end") float end){
        return ResponseEntity.ok( //
                walletDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(walletRepo.findAll().stream().filter(wallet -> wallet.getBalance() >= start)
                                        .filter(wallet -> wallet.getBalance()<= end).spliterator(), false)
                                .map(WalletDTO::new)///
                                .collect(Collectors.toList())));
    }

    /**
     * the function fetch from the db all the wallets exists and then make manipulating in
     * order to find all the wallets with balance > 500 and holders > 1
     * @return all the wallets DTO style that their balance >500 and holders >1
     */
    @GetMapping("/wallets/DTO/holdersAndBalance") // holders >1 && balance > 500
    ResponseEntity<CollectionModel<EntityModel<WalletDTO>>> findByHoldersAndBalance(){
        return ResponseEntity.ok(
                walletDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(walletRepo.findAll().stream().filter(wallet -> wallet.getHolders()>1)
                                        .filter(wallet -> wallet.getBalance() > 500).spliterator(), false)
                                .map(WalletDTO::new)///
                                .collect(Collectors.toList())));
    }
}
