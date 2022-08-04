package restapi.webapp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.assemblers.UserAssembler;
import restapi.webapp.assemblers.UserDTOAssembler;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.UserDTO;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.UserRepo;
import restapi.webapp.repo.WalletRepo;
import restapi.webapp.service.IUserService;
import restapi.webapp.service.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class UserController {
    @Autowired
    private UserAssembler userAssembler;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserDTOAssembler userDTOAssembler;
    @Autowired
    private RestClient restClient;
    @Autowired
    private IUserService userService;

    /**
     * the function finds the user according to the id
     * @param id
     * @return according to the optional if the value is not missing
     * it will return the user with response entity ok else it will return not found.
     */
    @GetMapping("/users/{id}")
    public Object getSingleProfile(@PathVariable Long id){
        return userRepo.findById(id) //
                .map(userAssembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    /**
     * the function takes all the profiles and show them with selfLinks and link to the collection
     * @return the collectionModel of the users
     */
    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllProfiles() {
        List<User> userList = userRepo.findAll();
        return userAssembler.toCollectionModel(userList);
    }

    /**
     * creating new user and setting the date time aswell
     * @param newUser json body response
     * @return saves the user into the repo
     */
    @PostMapping("/users")
    User createUser(@RequestBody User newUser) { // Relied on Jackson component for serialization
        newUser.setCreationTime(LocalDateTime.now());
        return userRepo.save(newUser); // Relied on Jackson component for deserialization
    }

    /**
     * the function takes a certain wallet and swipe it with other wallet else it makes a new wallet.
     * @param newUser
     * @return new wallet
     */
    @PutMapping("/users")
    User updateUser(@RequestBody User newUser) {
        return userRepo.findById(newUser.getId())
                // convert row in table to Java object
                .map(userToUpdate -> {
                    userToUpdate.setAge(newUser.getAge());
                    userToUpdate.setCountry(newUser.getCountry());
                    userToUpdate.setEmail(newUser.getEmail());
                    userToUpdate.setFullName(newUser.getFullName());
                    userToUpdate.setWalletList(newUser.getWalletList());
                    userToUpdate.setBestCoin(newUser.getBestCoin());
                    // save Java object to row in an SQL table
                    return userRepo.save(userToUpdate);
                    // if product does not exist either throw an exception or create a new one
                }).orElseGet(() -> {
                    return userRepo.save(newUser);
                });
    }

    /**
     *     * This method extracts id as path variable and replaces the old product if exists
     *      * @param id
     *      * @param newProduct
     *      * @return updated product or the newly created product
     *
     */
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userRepo.findById(id)
                // convert row in table to Java object
                .map(userToUpdate -> {
                    userToUpdate.setAge(newUser.getAge());
                    userToUpdate.setCountry(newUser.getCountry());
                    userToUpdate.setEmail(newUser.getEmail());
                    userToUpdate.setFullName(newUser.getFullName());
                    userToUpdate.setWalletList(newUser.getWalletList());
                    userToUpdate.setBestCoin(newUser.getBestCoin());
                    // save Java object to row in an SQL table
                    return userRepo.save(userToUpdate);
                    // if product does not exist either throw an exception or create a new one
                }).orElseGet(() -> {
                    return userRepo.save(newUser);
                });
    }

    /**
     * the function deletes the user with that certain ID
     * @param id
     */
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deletePersonFromWallet(id);
        userRepo.deleteById(id);
    }

    /**
     * the function finds the user according to fullName
     * @param name
     * @return response entity ok + userInfo or returns response not found
     */
    @GetMapping("/users/name/{name}")
    Object findByName(@PathVariable String name){
        return userRepo.findByFullName(name)
                .map(userAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new UserNotFoundException(name,"name: "));
    }
    /**
     * the function finds the user according to his Email
     * @param mail
     * @return response entity ok + userInfo or returns response not found
     */
    @GetMapping("/users/email/{mail}")
    Object findByMail(@PathVariable String mail){
        return userRepo.findByEmail(mail)
                .map(userAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new UserNotFoundException(mail,"email: "));
    }
    /**
     * the function finds the user according to his country.
     * @param country
     * @return response entity ok + userInfo or returns response not found
     */
    @GetMapping("/users/country/{country}")
    Object findByCountry(@PathVariable String country){
        return userRepo.findByCountry(country)
                .map(userAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new UserNotFoundException(country,"country: "));
    }
    /**
     * the function aims for certain user according to his ID and then shows the details according to the DTO
     * @param id
     * @return response entity ok + userDTOInfo or returns response not found
     */
    @GetMapping("/users/{id}/info")
    public Object userInfo(@PathVariable Long id){
        return userRepo.findById(id) //
                .map(UserDTO::new) //
                .map(userDTOAssembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElseThrow(()->new UserNotFoundException(id));
    }
    /**
     * the function shows all the usersDetails DTO style.
     * @return response entity ok + usersDTOInfo or returns response not found
     */
    @GetMapping("/users/info")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> allUsersInfo() {

        return ResponseEntity.ok( //
                userDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(userRepo.findAll().spliterator(), false) //
                                .map(UserDTO::new) //
                                .collect(Collectors.toList())));
    }

    /**
     * the function takes 2 request params , age and country of a user and finds all the users that in that range.
     * @param age
     * @param country
     * @return certain users that their country is similar to the param and age that need to be equal or above the param.
     */
    @GetMapping("/users/DTO/ageAndCountry/")
    ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findBetweenBalance(@RequestParam("age") float age, @RequestParam("country") String country){
        return ResponseEntity.ok( //
                userDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(userRepo.findAll().stream().filter(user -> user.getAge() >= age)
                                        .filter(user -> user.getCountry().equals(country)).spliterator(), false)
                                .map(UserDTO::new)///
                                .collect(Collectors.toList())));
    }
}