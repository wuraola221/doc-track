package wura.example.doctrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wura.example.doctrack.dto.DocumentDto;
import wura.example.doctrack.entity.DocType;
import wura.example.doctrack.entity.DocumentEntity;
import wura.example.doctrack.entity.UserEntity;
import wura.example.doctrack.repository.DocumentRepository;
import wura.example.doctrack.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/documents/";



    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            log.error("Failed to read file bytes: {}", e.getMessage());
            throw new RuntimeException("Could not read file bytes");
        }
    }


    public ResponseEntity<DocumentDto> uploadDocument (
            DocType docType,
            String customDocumentType,
            MultipartFile frontFile,
            MultipartFile backFile,
            LocalDate expiryDate,
            LocalDate customReminder,
            Authentication auth

    ) {

        String creatorEmail = auth.getName();

        UserEntity user = userRepository.findByEmail(creatorEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + creatorEmail));

        if (docType == DocType.OTHERS && (customDocumentType == null || customDocumentType.isBlank())) {
            throw new RuntimeException("Please specify document type in the custom field");
        }

        // Validate front file - always required
        validateFile(frontFile, "front");

        // Validate back file only if provided
        if (backFile != null && !backFile.isEmpty()) {
            validateFile(backFile, "back");
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Expiry date cannot be in the past");
        }

        String frontFilePath = saveFileToDisk(frontFile, docType);
        byte[] frontBytes = readBytes(frontFile);

        String backFilePath = null;
        String backFileType = null;
        byte[] backBytes = null;
        if (backFile != null && !backFile.isEmpty()) {
            backFilePath = saveFileToDisk(backFile, docType);
            backFileType = backFile.getContentType();
            backBytes = readBytes(backFile);
        }

        LocalDate defaultReminder = expiryDate.minusWeeks(2);

        DocumentEntity document = DocumentEntity.builder()
                .docType(docType)
                .customDocumentType(
                        docType == DocType.OTHERS ? customDocumentType : null
                )
                .frontFilePath(frontFilePath)
                .frontFileType(frontFile.getContentType())
                .frontImageData(frontBytes)
                .backFilePath(backFilePath)
                .backFileType(backFileType)
                .backImageData(backBytes)
                .expiryDate(expiryDate)
                .defaultReminder(defaultReminder)
                .customReminder(customReminder)
                .user(user)
                .build();

        DocumentEntity saved = documentRepository.save(document);
        return ResponseEntity.ok(toDTO(saved));


    }


    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    private String saveFileToDisk(MultipartFile file, DocType docType) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Use documentName + UUID to keep it identifiable but unique
            String extension = getFileExtension(file.getOriginalFilename());
            String uniqueFileName = docType + "_" + UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage());
            throw new RuntimeException("Could not save file. Please try again.");
        }
    }

    private void validateFile(MultipartFile file, String label) {
        String fileType = file.getContentType();
        if (fileType == null ||
                (!fileType.equals("application/pdf") && !fileType.startsWith("image/"))) {
            throw new RuntimeException("The " + label + " file must be a PDF or image");
        }
    }

    public ResponseEntity<List<DocumentDto>> getUserDocuments(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        List<DocumentDto> documents = documentRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(documents);
    }

    public ResponseEntity<List<DocumentDto>> getExpiredDocuments(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        List<DocumentDto> expiredDocuments = documentRepository
                .findByUserIdAndExpiryDateBefore(user.getId(), LocalDate.now())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(expiredDocuments);
    }

    public ResponseEntity<List<DocumentDto>> getUpcomingReminders(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<DocumentDto> upcomingReminders = documentRepository
                .findUpcomingReminders(user.getId(), today, nextWeek)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(upcomingReminders);
    }

    public ResponseEntity<byte[]> getFrontImage(Long documentId) {
        DocumentEntity doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getFrontFileType()))
                .body(doc.getFrontImageData());
    }


    public ResponseEntity<DocumentDto> renewDocument(
            Long documentId,
            MultipartFile frontFile,
            MultipartFile backFile,
            LocalDate expiryDate,
            LocalDate customReminder,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        // Make sure the document belongs to the logged in user
        if (!document.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this document");
        }

        // Expiry date cannot be in the past
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Expiry date cannot be in the past");
        }

        // Update front file if provided
        if (frontFile != null && !frontFile.isEmpty()) {
            validateFile(frontFile, "front");
            document.setFrontFilePath(saveFileToDisk(frontFile, document.getDocType())); // pass DocType directly
            document.setFrontFileType(frontFile.getContentType());
            document.setFrontImageData(readBytes(frontFile));
        }

// Update back file if provided
        if (backFile != null && !backFile.isEmpty()) {
            validateFile(backFile, "back");
            document.setBackFilePath(saveFileToDisk(backFile, document.getDocType())); // pass DocType directly
            document.setBackFileType(backFile.getContentType());
            document.setBackImageData(readBytes(backFile));
        }

        // Update dates
        document.setExpiryDate(expiryDate);
        document.setDefaultReminder(expiryDate.minusWeeks(2)); // recalculate default reminder
        document.setCustomReminder(customReminder);            // null if not provided

        DocumentEntity updated = documentRepository.save(document);
        return ResponseEntity.ok(toDTO(updated));
    }



    public DocumentDto toDTO (DocumentEntity documentEntity) {
        return DocumentDto.builder()
                .id(documentEntity.getId())
                .docType(documentEntity.getDocType())
                .customDocumentType(documentEntity.getCustomDocumentType())
                .expiryDate(documentEntity.getExpiryDate())
                .frontFilePath(documentEntity.getFrontFilePath())
                .backFilePath(documentEntity.getBackFilePath())
                .frontFileType(documentEntity.getFrontFileType())
                .backFileType(documentEntity.getBackFileType())
                .frontImageBase64(
                        documentEntity.getFrontImageData() != null
                                ? Base64.getEncoder().encodeToString(documentEntity.getFrontImageData())
                                : null
                )
                .backImageBase64(
                        documentEntity.getBackImageData() != null
                                ? Base64.getEncoder().encodeToString(documentEntity.getBackImageData())
                                : null
                )
                .customReminder(documentEntity.getCustomReminder())
                .defaultReminder(documentEntity.getDefaultReminder())
//                .userId(documentEntity.getUser().getId())
                .uploadedAt(documentEntity.getUploadedAt())
                .build();
    }
}
