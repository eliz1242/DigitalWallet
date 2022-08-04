package restapi.webapp.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import restapi.webapp.pojo.User;

import java.util.List;
import java.util.Optional;
/**
 * repository for user entity.
 */
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<List<User>> findByFullName(String name);
    Optional<List<User>> findByEmail(String email);
    Optional<List<User>> findByCountry(String country);
}
