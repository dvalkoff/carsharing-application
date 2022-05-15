package ru.valkov.carsharing.carsharingapplication.email;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@EnableAsync
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendEmail(String to, String email) throws IllegalStateException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setText(email, true);
            messageHelper.setTo(to);
            messageHelper.setSubject("Activate your account");
            messageHelper.setFrom(from);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
