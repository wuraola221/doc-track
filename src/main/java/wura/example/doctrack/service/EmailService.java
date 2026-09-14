package wura.example.doctrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import wura.example.doctrack.entity.DocType;
import wura.example.doctrack.entity.DocumentEntity;


@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {


        private final JavaMailSenderImpl mailSender;
        private final TemplateEngine templateEngine;

        @Value("${app.mail.from}")
        private String fromEmail;

        @Value("${app.base-url}")
        private String baseUrl;

        public void sendReminderEmail(String to, String name, DocumentEntity document) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper =
                        new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(to);
                helper.setSubject("Document Renewal Reminder");

                Context context = new Context();

                String documentName;

                if (document.getDocType() == DocType.OTHERS) {
                    documentName = document.getCustomDocumentType();
                } else {
                    documentName = document.getDocType().name().replace("_", " ");
                }

                context.setVariable("name", name);
                context.setVariable("documentName", documentName);
                context.setVariable("expiryDate", document.getExpiryDate());

                String html = templateEngine.process("reminder-email", context);

                helper.setText(html, true);

                mailSender.send(message);

            } catch (MessagingException e) {
                log.error("Failed to send activation email to: {}", to, e);
                throw new RuntimeException("Failed to send activation email", e);
            }


        }




}
