package pro.sky.telegrambot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private LocalDateTime date;

    public NotificationTask(Integer chatId,
                            String text,
                            LocalDateTime date) {
        this.chatId = chatId;
        this.text = text;
        this.date = date;
    }
}