package com.mindzone.service;

import com.mindzone.dto.MailDTO;
import com.mindzone.service.impl.MailService;
import com.mindzone.util.MailProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private MailProducer producer;

    public String sendMail() {
        producer.send(new MailDTO(
                "sousa0240@gmail.com",
                "teste de email",
                "corpo do teste"
        ));
        return "deu certo";
    }
}
