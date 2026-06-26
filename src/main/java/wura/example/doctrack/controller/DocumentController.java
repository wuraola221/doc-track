package wura.example.doctrack.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wura.example.doctrack.dto.DocumentDto;
import wura.example.doctrack.entity.DocType;
import wura.example.doctrack.service.DocumentService;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> uploadDocument(
            @RequestParam("docType") DocType docType,
            @RequestParam(value = "customDocumentType", required = false) String customDocumentType,
            @RequestPart("frontFile") MultipartFile frontFile,
            @RequestPart(value = "backFile", required = false) MultipartFile backFile,
            @RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
            @RequestParam(value = "customReminder", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate customReminder,
            Authentication authentication

    ) {
        return documentService.uploadDocument( docType, customDocumentType, frontFile, backFile, expiryDate, customReminder, authentication);
    }


    @GetMapping("/{id}/front-image")
    public ResponseEntity<byte[]> getFrontImage(@PathVariable Long id) {
        return documentService.getFrontImage(id);
    }

    @GetMapping("/my-documents")
    public ResponseEntity<List<DocumentDto>> getMyDocuments(Authentication authentication) {
        return documentService.getUserDocuments(authentication);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<DocumentDto>> getExpiredDocuments(Authentication authentication) {
        return documentService.getExpiredDocuments(authentication);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<DocumentDto>> getUpcomingReminders(Authentication authentication) {
        return documentService.getUpcomingReminders(authentication);
    }

    @PutMapping(value = "/{id}/renew", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> renewDocument(
            @PathVariable Long id,
            @RequestPart(value = "frontFile", required = false) MultipartFile frontFile,
            @RequestPart(value = "backFile", required = false) MultipartFile backFile,
            @RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
            @RequestParam(value = "customReminder", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate customReminder,
            Authentication authentication
    ) {
        return documentService.renewDocument(
                id, frontFile, backFile, expiryDate, customReminder, authentication
        );
    }


}
