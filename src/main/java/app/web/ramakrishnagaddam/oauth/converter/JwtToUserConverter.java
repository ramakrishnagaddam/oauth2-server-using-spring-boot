package app.web.ramakrishnagaddam.oauth.converter;

import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import app.web.ramakrishnagaddam.oauth.model.User;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken>{@Override
	
	public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
		User user = new User();
		user.setId(jwt.getSubject());
		return new UsernamePasswordAuthenticationToken(user, jwt, Collections.emptyList());
	}
	
}
