package com.joao.cafe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("informatica@gralha-azul.ind.br");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list != null && list.size() > 0)
            message.setCc(getCcArray(list));
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for(int i = 0;i<ccList.size();i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("informatica@gralha-azul.ind.br");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Suas credenciais de login para o Sistema de gerenciamento do Cafe</b><br><b>Email: </b> " + to + " <br><b>Senha: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click aqui para logar</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);

    }

}
