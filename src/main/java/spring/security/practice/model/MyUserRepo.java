package spring.security.practice.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepo extends JpaRepository<MyUser, Long> {
    // Menyediakan metode untuk mencari user berdasarkan username
    Optional<MyUser> findByUsername(String username);
}
