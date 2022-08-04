package restapi.webapp.service;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restapi.webapp.exceptions.CoinNotFoundException;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.pojo.Coin;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.UserRepo;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
public class RestClient {
    private String apiUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepo userRepo;

    public RestClient(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List getCoin(String cryptoType, String currency){
        setCoin(cryptoType,currency);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return restTemplate.exchange(this.apiUrl, HttpMethod.GET,null,List.class).getBody();
    }

    /**
     * the function setting the url for the api call
     * @param cryptoType
     * @param currency
     */
    public void setCoin(String cryptoType,String currency){
        String start = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=";
        String mid = "&ids=";
        String end = "&order=market_cap_desc&per_page=100&page=1&sparkline=false";
        this.apiUrl = start + currency + mid + cryptoType + end;
    }

    /**
     * the function get a certain name finds all the users that matches
     *  that name and change the coin values according to the api call
     *  and then sets the best coin in the user that owns that wallet according
     *  to the 24hr price of all the coins in his wallet
     * @param userName
     * @return
     */
    @Async
    public CompletableFuture<User> checkingBestCoin(String userName){
        List<User> user = userRepo.findByFullName(userName).orElseThrow(()->new UserNotFoundException(userName,"does not exist: "));
        for(User profile:user){
            if(!profile.getWalletList().isEmpty()){
                float sum = -50000;
                String name = "";
                for(Wallet w:profile.getWalletList()){
                    for(Coin c:w.getCoinList()){
                        c = parseCoin(c.getName(),c.getCurrency(),c.getAmount());  // update the coin
                        if(c.getPriceChanged24hr() > sum){
                            sum = c.getPriceChanged24hr();
                            name = c.getName();
                        }
                    }
                }
                profile.setBestCoin(new String[]{name,""+sum});
                userRepo.save(profile);
            }
        }
        return CompletableFuture.completedFuture(user.get(0));
    }

    /**
     * the function finds the current price of certain crypto
     * @param cryptoType
     * @param currency
     * @return the current price of the coin
     */
    public float getCurrency(String cryptoType, String currency){
        List<?> lst = getCoin(cryptoType,currency);
        JSONObject jsonObject = new JSONObject((Map<String, ?>) lst.get(0));
        float curr_price =  Float.parseFloat(jsonObject.get("current_price").toString());
        return curr_price;
    }

    /**
     * the function takes the response body from the api call and convert it into java object ( coin)
     * @param cryptoType
     * @param currency
     * @param amount
     * @return the coin from all the data from the api call
     * @throws CoinNotFoundException
     */
    public Coin parseCoin(String cryptoType, String currency, float amount) throws  CoinNotFoundException {
        List<?> lst = getCoin(cryptoType,currency);
        if(lst.size() == 0){
            throw new CoinNotFoundException(cryptoType," name: ");
        }
        JSONObject jsonObject = new JSONObject((Map<String, ?>) lst.get(0));
        float curr_price =  Float.parseFloat(jsonObject.get("current_price").toString());
        float high_24hr =  Float.parseFloat(jsonObject.get("high_24h").toString());
        float low_24hr =  Float.parseFloat(jsonObject.get("low_24h").toString());
        float price_change_24h =  Float.parseFloat(jsonObject.get("price_change_24h").toString());
        Coin coin = new Coin(cryptoType,curr_price,price_change_24h,high_24hr,low_24hr,currency,amount);
        return coin;
    }

}
