package gerenciaconfigsrc.service.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import gerenciaconfigsrc.models.PasswordReset;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.service.MailSenderService;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    @Value("${app_mail_password}")
    private String APP_MAIL_PASSWORD;
    @Value("${app_mail}")
    private String APP_MAIL;

    @Value("${app_url}")
    private String APP_URL;

    @Override
    public String sendEmail(PasswordReset userResetToken) {

        JavaMailSenderImpl mailSender = this.getMailSender();

        mailSender.send(this.constructResetTokenEmail(APP_URL, userResetToken.getToken(), userResetToken.getUser()));

        return "ok";
    }

    private JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("noreply.dcm.sup");//username do email
        mailSender.setPassword(APP_MAIL_PASSWORD);//senha aplicativo

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }


    private SimpleMailMessage constructResetTokenEmail(
        String contextPath, String token, User user) {
        //@TODO: url aqui deve ser a que leva para a pagina do front para que digite a senha nova (senha e confirmação de senha)
        String url = contextPath + "/resetPassword?token=" + token;
        String message = "Link para troca de senha";
        return constructEmail("Reset Senha", message + " \r\n" + url, user);
    }
    private SimpleMailMessage constructEmail(String subject, String body,
        User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(APP_MAIL);
        return email;
    }
}
