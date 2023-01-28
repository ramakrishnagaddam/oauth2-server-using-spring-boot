package app.web.ramakrishnagaddam.oauth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import app.web.ramakrishnagaddam.oauth.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
