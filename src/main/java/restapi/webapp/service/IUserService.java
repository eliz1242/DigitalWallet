package restapi.webapp.service;

import restapi.webapp.exceptions.UserNotFoundException;

public interface IUserService {
    void deletePersonFromWallet(Long id);
}
