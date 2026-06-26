package wura.example.doctrack.repository;

import org.springframework.data.repository.CrudRepository;
import wura.example.doctrack.entity.EmploymentEntity;
import wura.example.doctrack.entity.EmploymentStatus;

import java.util.Optional;

public interface EmploymentRepository extends CrudRepository<EmploymentEntity, Long> {
    Optional<EmploymentEntity> findByName(EmploymentStatus name);
}
