package kr.sofaware.slas.function.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
public class MyMailSender {

    private final JavaMailSender mailSender;

    public void send(String receiverAddress, String title, String href, String content) {
        MailHandler mailHandler = null;
        try {
            mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(receiverAddress);
            mailHandler.setSubject(title);

            String htmlContent = "<a style=\"font-size: 32px\" href=\"" + href + "\">" + href + "</a><br><br>"
                    + content;
            mailHandler.setText(htmlContent.replaceAll("\n", "<br>"), true);
            mailHandler.send();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}