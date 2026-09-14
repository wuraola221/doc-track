package wura.example.doctrack.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wura.example.doctrack.entity.DocumentEntity;
import wura.example.doctrack.repository.DocumentRepository;
import wura.example.doctrack.service.EmailService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final DocumentRepository documentRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?") // Every day at 8:00 AM
    public void sendReminderEmails() {

        LocalDate today = LocalDate.now();

        List<DocumentEntity> documents =
                documentRepository.findDocumentsDueToday(today);

        for (DocumentEntity document : documents) {

            try {
                emailService.sendReminderEmail(
                        document.getUser().getEmail(),
                        document.getUser().getFirstName(),
                        document
                );

                // Mark whichever reminder triggered this send, so it isn't resent
                boolean defaultDue = today.equals(document.getDefaultReminder());
                boolean customDue = today.equals(document.getCustomReminder());

                if (defaultDue) {
                    document.setDefaultReminderSent(true);
                }
                if (customDue) {
                    document.setCustomReminderSent(true);
                }

                documentRepository.save(document);

                log.info("Reminder sent for document {}", document.getDocType());

            } catch (RuntimeException e) {
                // Don't let one failed email (e.g. SMTP timeout) stop the rest of the batch,
                // and don't mark as sent if it actually failed
                log.error("Failed to send reminder for document {}", document.getId(), e);
            }
        }
    }
}