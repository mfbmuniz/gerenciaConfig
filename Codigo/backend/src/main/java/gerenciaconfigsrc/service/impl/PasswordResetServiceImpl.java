package gerenciaconfigsrc.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gerenciaconfigsrc.configs.jwt.JwtTokenUtil;
import gerenciaconfigsrc.models.PasswordReset;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.repository.PasswordResetServiceRepository;
import gerenciaconfigsrc.service.MailSenderService;
import gerenciaconfigsrc.service.PasswordResetService;
import gerenciaconfigsrc.service.UserService;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordResetServiceRepository passwordResetServiceRepository;

    @Autowired
    private MailSenderService mailSenderService;


    @Override
    public String saveToken(String userEmail) {
        Optional<User> userOptioinal = this.userService.findByEmail(userEmail);
        //Caso tente com um email que nao existe nao diga q nao existe
        if (!userOptioinal.isPresent()) {
            return "Verifique se recebeu";
        }

        User user = userOptioinal.get();

        String token = this.jwtTokenUtil.generateToken(user);

        Date expireDate = this.jwtTokenUtil.getExpirationDateFromToken(token);

        //token salvo
        PasswordReset passwordReset = this.passwordResetServiceRepository.save(new PasswordReset(user,
            token,
            expireDate));

        //envia email para o usuario
        this.mailSenderService.sendEmail(passwordReset);

        return "ok";

    }


}
