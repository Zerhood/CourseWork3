package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageSevice {

    private final NotificationTaskRepository repository;

    @Autowired
    private TelegramBot telegramBot;

    public MessageSevice(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    public void saveMessage(Update update) {
        int chatId = update.message().chat().id().intValue();
        String text = update.message().text();
        String dateParse = "(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)";
        Matcher matcher = Pattern.compile(dateParse).matcher(text);
        if (matcher.find()) {
            if (matcher.group(1) != null && matcher.group(2) != null) {
                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                String textMessages = matcher.group(3);
                repository.saveAndFlush(new NotificationTask(chatId, textMessages, dateTime));
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sheduledMessage() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> allMessage = repository.findAllByDate(localDateTime);
        allMessage.forEach(notificationTask -> {
            if (!allMessage.isEmpty()) {
                allMessage.forEach(s ->
                        telegramBot.execute(new SendMessage(s.getChatId(), s.getText())));
            }
        });
    }
}