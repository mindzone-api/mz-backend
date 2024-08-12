package com.mindzone.service.impl;

import com.mindzone.dto.MailDTO;
import com.mindzone.service.interfaces.MailService;
import com.mindzone.model.MailProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private MailProducer producer;

    public void sendMail(MailDTO mail) {
        producer.send(mail);
    }
}
