package com.mindzone.util;

import com.mindzone.dto.MailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.mindzone.util.Constants.RABBIT_MAIL_ROUTING_KEY;
import static com.mindzone.util.Constants.RABBIT_MAIL_TOPIC_EXCHANGE;

@Service
public class MailProducer {

    public MailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private final RabbitTemplate rabbitTemplate;

    public void send(MailDTO dto) {
        rabbitTemplate.convertAndSend(RABBIT_MAIL_TOPIC_EXCHANGE, RABBIT_MAIL_ROUTING_KEY, dto);
    }
}
