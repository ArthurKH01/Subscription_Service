package com.example.test_task.subscriptionservice.service;

import com.example.test_task.subscriptionservice.exception.InvoiceSendingException;
import com.example.test_task.subscriptionservice.model.dto.InvoiceMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue}")
    String queueName;

    public void sendInvoice(InvoiceMessageDto invoice) {
        try {
            rabbitTemplate.convertAndSend(queueName, invoice);
        } catch (AmqpException e) {
            throw new InvoiceSendingException("Ошибка при отправки счёта в RabbitMQ: ", e);
        }
    }
}
