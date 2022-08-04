package restapi.webapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.repo.UserRepo;
import restapi.webapp.service.RestClient;
import restapi.webapp.service.WalletService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "wallet management REST API", version = "1.0"))
public class WebappApplication {
	private static RestClient restClient;
	private static UserRepo userRepo;
	private static JpaRepository<Wallet, Long> walletRepo;
	private static WalletService walletService;

	public WebappApplication(RestClient restClient, UserRepo userRepo, JpaRepository<Wallet, Long> walletRepo, WalletService walletService) {
		this.restClient = restClient;
		this.userRepo = userRepo;
		this.walletRepo = walletRepo;
		this.walletService = walletService;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				List<User> userList = userRepo.findAll();
				if(!userList.isEmpty()){
					for(User us : userList){
						restClient.checkingBestCoin(us.getFullName());
					}
				}
				List<Wallet> walletList = walletRepo.findAll();
				walletService.updateBalance(walletList);
				System.out.println("im done");
			}
		}, 0, 1000*60*60);//wait 0 ms before doing the action and do it evry 1000ms (1second)


	}

}
