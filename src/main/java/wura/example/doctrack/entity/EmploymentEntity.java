package wura.example.doctrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employment_status")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmploymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true)
    private EmploymentStatus name;

    @OneToMany(mappedBy = "employmentStatus")
    private Set<UserEntity> profiles = new HashSet<>();

//    public EmploymentEntity(UserEntity profile) {
//    }
}
