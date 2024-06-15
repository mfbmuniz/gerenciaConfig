package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.RequestEntity.CheckExistsRequest;
import gerenciaconfigsrc.models.ResponseEntity.CheckExistsResponse;
import org.springframework.security.core.Authentication;

import gerenciaconfigsrc.models.RequestEntity.LoginRequest;
import gerenciaconfigsrc.models.ResponseEntity.LoginResponse;

public interface AuthService {
    CheckExistsResponse checkExists(CheckExistsRequest checkExistsRequest);

    LoginResponse login(LoginRequest authenticationRequest) throws Exception;

    Authentication getAuthenticatedUser();
}
