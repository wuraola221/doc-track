package wura.example.doctrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import wura.example.doctrack.entity.EmploymentEntity;
import wura.example.doctrack.entity.EmploymentStatus;
import wura.example.doctrack.repository.EmploymentRepository;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EmploymentRepository employmentRepository;

    @Override
    public void run(String... args) {
        for (EmploymentStatus status : EmploymentStatus.values()) {
            if (employmentRepository.findByName(status).isEmpty()) {
                EmploymentEntity entity = new EmploymentEntity();
                entity.setName(status);
                employmentRepository.save(entity);
            }
        }
        System.out.println("Employment statuses seeded.");
    }

}
