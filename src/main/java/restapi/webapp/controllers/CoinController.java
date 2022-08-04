package restapi.webapp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.assemblers.CoinAssembler;
import restapi.webapp.exceptions.CoinNotFoundException;
import restapi.webapp.pojo.User;
import restapi.webapp.service.RestClient;
import restapi.webapp.assemblers.CoinDTOAssembler;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.CoinDTO;
import restapi.webapp.repo.CoinRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CoinController {
    @Autowired
    private CoinRepo coinRepo;
    @Autowired
    private RestClient RestClient;
    @Autowired
    private CoinDTOAssembler coinDTOAssembler;
    @Autowired
    private CoinAssembler coinAssembler;
    /**
     * the function finds the coin according to the given id
     * @param id
     * @return according to the optional if the value is not missing
     * it will return the user with response entity ok else it will return not found.
     */
    @GetMapping("/coins/{id}")
    public Object getSingleCoin(@PathVariable Long id) {
        return coinRepo.findById(id) //
                .map(coinAssembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElseThrow(()->new CoinNotFoundException(id));
    }

    /**
     * the function takes all the coins and show them with selfLinks and link to the collection
     * @return the collectionModel of the users
     */
    @GetMapping("/coins")
    public CollectionModel<EntityModel<Coin>> getAllCoins() {
        List<Coin> coinList = coinRepo.findAll();
        return coinAssembler.toCollectionModel(coinList);
    }
    /**
     * params = coin passing 3 parameters for creating the coin (crypto name , currency ( usd/euro etc.) and amount.
     * the function is calling parseCoin in order to make the coin complete and save it to the repo after
     */
    @PostMapping("/coins/{amount}")
    Coin createCoin(@RequestBody Coin coin, @PathVariable float amount){ // Relied on Jackson component for serialization
        Coin realCoin = RestClient.parseCoin(coin.getName(),coin.getCurrency(),amount);
        return coinRepo.save(realCoin); // Relied on Jackson component for deserialization
    }


    // check the coin values using API

    /**
     *
     * @param crypto name of the coin
     * @param currency of the coin
     * @return response entity to the parsed info
     */
    @GetMapping("/coins/{crypto}/{currency}")
    Object getCoinInfo(@PathVariable String crypto, @PathVariable String currency) {
        return RestClient.parseCoin(crypto, currency, 1);
    }

    /**
     * the function ask the repo to find the coin according to the price
     * @param price amount
     * @return response entity of the product or not found entity(404)
     */
    @GetMapping("/coins/price/{price}")
    Object findByCurrentPrice(@PathVariable float price){
        return coinRepo.findByCurrentPrice(price)
                .map(coinAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new CoinNotFoundException(price," that price: "));
    }
    /**
     * the function ask the repo to find the coin according to the price24hr
     * @param price amount
     * @return response entity of the product or not found entity(404)
     */
    @GetMapping("/coins/price24hr/{price}")
    Object findByPriceChanged24hr(@PathVariable float price){
        return coinRepo.findByPriceChanged24hr(price)
                .map(coinAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new CoinNotFoundException(price," that price 24hr: "));
    }
    /**
     * the function ask the repo to find the coin according to his name
     * @param name
     * @return response entity of the product or not found entity(404)
     */
    @GetMapping("/coins/name/{name}")
    Object findByName(@PathVariable String name) {
        return coinRepo.findByName(name)
                .map(coinAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new CoinNotFoundException(name," that name: "));
    }

    /**
     * the function deletes the coin according to the given id
     * @param id
     */
    @DeleteMapping("/coins/{id}")
    void deleteCoin(@PathVariable Long id){
        coinRepo.deleteById(id);
    }

    /**
     * the function is taking the id , finds the coin with that id , make it into CoinDTO -> entityModel
     * @param id
     * @return response entity of the entity model or response not found
     */
    @GetMapping("/coins/{id}/info")
    public Object getCoinDTOInfo(@PathVariable Long id){
        return coinRepo.findById(id) //
                .map(CoinDTO::new) //
                .map(coinDTOAssembler::toModel) //
                .map(ResponseEntity::ok) //
                .orElseThrow(()->new CoinNotFoundException(id));
    }

    /**
     * the function shows you all the wallets exist DTO style
     * @return response entity with all the coins include links inside
     */
    @GetMapping("/coins/info")
    public ResponseEntity<CollectionModel<EntityModel<CoinDTO>>> getAllCoinsDTOInfo() {
        return ResponseEntity.ok( //
                coinDTOAssembler.toCollectionModel( //
                        StreamSupport.stream(coinRepo.findAll().spliterator(), false) //
                                .map(CoinDTO::new) //
                                .collect(Collectors.toList())));
    }
}

