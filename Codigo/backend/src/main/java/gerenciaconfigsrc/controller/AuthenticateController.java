package gerenciaconfigsrc.controller;

import gerenciaconfigsrc.models.RequestEntity.CheckExistsRequest;
import gerenciaconfigsrc.models.ResponseEntity.CheckExistsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gerenciaconfigsrc.models.RequestEntity.LoginRequest;
import gerenciaconfigsrc.models.ResponseEntity.LoginResponse;
import gerenciaconfigsrc.service.AuthService;
import gerenciaconfigsrc.service.PasswordResetService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticateController {

	@Autowired
	private AuthService authService;
	@Autowired
	private PasswordResetService passwordResetService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> createAuthenticationToken(
			@RequestBody LoginRequest authenticationRequest) throws Exception {

		return ResponseEntity.ok(this.authService.login(authenticationRequest));
	}

	@RequestMapping(value = "/exists", method = RequestMethod.POST)
	public ResponseEntity<CheckExistsResponse> createAuthenticationToken(
			@RequestBody CheckExistsRequest checkExistsRequest) {

		return ResponseEntity.ok(this.authService.checkExists(checkExistsRequest));
	}

	@RequestMapping(value = "/reset-pwd/{email}", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(
			@PathVariable("email") String userEmail) {

		return ResponseEntity.ok(this.passwordResetService.saveToken(userEmail));
	}
}
