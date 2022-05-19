//package kr.sofaware.slas.mail;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import javax.mail.MessagingException;
//import java.io.IOException;
//
//@SpringBootTest
//public class MailSendTest {
//
//    @Autowired
//    JavaMailSender mailSender;
//
//    @Test
//    public void send() throws MessagingException, IOException {
//        MailHandler mailHandler = new MailHandler(mailSender);
//
//        mailHandler.setTo("chchch1213@naver.com");
//        mailHandler.setSubject("스프링의 제목제목제목제목제목제목제목제목");
//
//        String htmlContent = "<h1>쉬운길을 택했다</h1> <img src='cid:slas-logo'>";
//        mailHandler.setText(htmlContent, true);
//        mailHandler.setAttach("en.json", "static/assets/lang/en.json");
//        mailHandler.setInline("slas-logo", "static/assets/images/favicon.png");
//        mailHandler.send();
//    }
//}
