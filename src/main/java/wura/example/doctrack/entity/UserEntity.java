package wura.example.doctrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


//    @ManyToOne
//    private EmploymentEntity employmentStatus;

    @ManyToOne()
    @JoinColumn(name = "employment_status", referencedColumnName = "name")
    private EmploymentEntity employmentStatus;

}
