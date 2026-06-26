package wura.example.doctrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wura.example.doctrack.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);


}
