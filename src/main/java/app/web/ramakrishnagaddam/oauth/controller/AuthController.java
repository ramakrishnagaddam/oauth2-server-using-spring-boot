package app.web.ramakrishnagaddam.oauth.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.web.ramakrishnagaddam.oauth.model.Register;
import app.web.ramakrishnagaddam.oauth.model.Token;
import app.web.ramakrishnagaddam.oauth.model.User;
import app.web.ramakrishnagaddam.oauth.service.TokenGenerator;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

	private final UserDetailsManager userDetailsManager;
	private final TokenGenerator tokenGenerator;
	private final DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	@Qualifier("jwtRefreshTokenAuthProvider")
	private final JwtAuthenticationProvider refreshTokenAuthProvider;
	
	@PostMapping("/register")
	public ResponseEntity<Token> register(@RequestBody Register register) {
		User user = new User(register.getUsername(), register.getPassword());
		userDetailsManager.createUser(user);
		
		Authentication authentication = UsernamePasswordAuthenticationToken
						.authenticated(user, register.getPassword(), Collections.emptyList());
		
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	}
	
	@PostMapping("/login")
	public ResponseEntity<Token> login(@RequestBody Register register) {
		Authentication authentication = daoAuthenticationProvider
										.authenticate(
											UsernamePasswordAuthenticationToken
												.unauthenticated(register.getUsername(), register.getPassword())
										);
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	}
	
	@PostMapping("/token")
	public ResponseEntity<Token> token(@RequestBody Token token) {
		Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(token.getRefreshToken()));
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	}
	
}
