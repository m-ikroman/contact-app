package project_skripsi.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_skripsi.rest.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findFirstByToken(String token);
}
