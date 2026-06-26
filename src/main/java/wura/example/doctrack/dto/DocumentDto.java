package wura.example.doctrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wura.example.doctrack.entity.DocType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {

    private Long id;
    private DocType docType;
    private String customDocumentType;
    private String frontFilePath;
    private String frontFileType;
    private String frontImageBase64;

    private String backFilePath;
    private String backFileType;
    private String backImageBase64;

    private LocalDate expiryDate;
    private LocalDate defaultReminder;
    private LocalDate customReminder;
    private LocalDateTime uploadedAt;
    private Long userId;


}
