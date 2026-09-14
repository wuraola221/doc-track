package wura.example.doctrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wura.example.doctrack.entity.DocumentEntity;

import java.time.LocalDate;
import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {


    // Get all documents for a user
    List<DocumentEntity> findByUserId(Long userId);

    List<DocumentEntity> findByUserIdAndExpiryDateBefore(Long userId, LocalDate date);

//    List<DocumentEntity> findByUpcomingReminder(Long userId,  LocalDate date);


    @Query("SELECT d FROM DocumentEntity d WHERE d.user.id = :userId AND (d.defaultReminder BETWEEN :today AND :nextWeek OR d.customReminder BETWEEN :today AND :nextWeek)")
    List<DocumentEntity> findUpcomingReminders(
            @Param("userId") Long userId,
            @Param("today") LocalDate today,
            @Param("nextWeek") LocalDate nextWeek
    );

    @Query("""
    SELECT d
    FROM DocumentEntity d
    WHERE (d.defaultReminder = :today AND d.defaultReminderSent = false)
       OR (d.customReminder = :today AND d.customReminderSent = false)
""")
    List<DocumentEntity> findDocumentsDueToday(
            @Param("today") LocalDate today
    );
    // Get documents expiring before a certain date (useful for reminders later)
    List<DocumentEntity> findByExpiryDateBefore(LocalDate date);

    // Get documents where default reminder date is today
    List<DocumentEntity> findByDefaultReminder(LocalDate date);

    // Get documents where custom reminder date is today
    List<DocumentEntity> findByCustomReminder(LocalDate date);
}
