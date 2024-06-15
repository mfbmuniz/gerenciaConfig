package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.PasswordReset;

public interface MailSenderService {

  String sendEmail(PasswordReset userResetToken);
}
