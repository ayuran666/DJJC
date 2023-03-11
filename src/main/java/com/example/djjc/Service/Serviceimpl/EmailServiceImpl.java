package com.example.djjc.Service.Serviceimpl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.example.djjc.Dto.EmailDto;
import com.example.djjc.Service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.email}")
    private String email;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Override
    public void send(EmailDto emailDto) {

        if(email == null || host == null || port == null || username == null || password == null){
            throw new RuntimeException("邮件配置异常");
        }
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(host);
        mailAccount.setPort(Integer.parseInt(port));
        mailAccount.setFrom(username + "<" + email + ">");
        mailAccount.setUser(username);
        // 设置发送授权码
        mailAccount.setPass(password);
        mailAccount.setAuth(true);
        // ssl方式发送
        mailAccount.setSslEnable(false);
        // 使用安全连接
        mailAccount.setStarttlsEnable(false);

        try {
            int size = emailDto.getTos().size();
            Mail.create(mailAccount)
                    .setTos(emailDto.getTos().toArray(new String[size]))
                    .setContent(emailDto.getContent())
                    .setTitle(emailDto.getTheme())
                    .setHtml(true)
                    .setUseGlobalSession(false)
                    .send();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
