package com.mock.exchange.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.mock.exchange.entity.User;
// Provides JPA CRUD operations + custom query
public interface UserRepository extends JpaRepository<User, Long> { 
 Optional<User> findByUsername(String username);
}
