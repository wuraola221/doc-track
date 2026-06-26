package wura.example.doctrack.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.DocumentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "document")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DocumentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocType docType;

    @Column
    private String customDocumentType;


    // Front image
    @Column(nullable = false)
    private String frontFilePath;

    @Column(nullable = false)
    private String frontFileType;


    @Column(name = "front_image_data", columnDefinition = "bytea")
    private byte[] frontImageData;

    // Back image

    private String backFilePath;

    private String backFileType;


    @Column(name = "back_image_data", columnDefinition = "bytea")
    private byte[] backImageData;


    private LocalDate expiryDate;

    @Column(nullable = false)
    private LocalDate defaultReminder;

    private LocalDate customReminder;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;






}
