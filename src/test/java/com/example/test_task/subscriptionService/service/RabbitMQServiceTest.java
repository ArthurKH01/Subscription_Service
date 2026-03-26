package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RabbitMQServiceTest {
    private RabbitTemplate rabbitTemplate;
    private RabbitMQService rabbitMQService;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        rabbitMQService = new RabbitMQService(rabbitTemplate);
        rabbitMQService.queueName = "test-queue";
    }

    @Test
    void sendInvoice_success_callsConvertAndSend() {
        InvoiceMessageDto dto = new InvoiceMessageDto();
        rabbitMQService.sendInvoice(dto);
        verify(rabbitTemplate).convertAndSend("test-queue", dto);
    }

    @Test
    void sendInvoice_amqpException_throwsRuntimeException() {
        InvoiceMessageDto dto = new InvoiceMessageDto();
        doThrow(new AmqpException("fail"))
                .when(rabbitTemplate)
                .convertAndSend(anyString(), any(InvoiceMessageDto.class));

        assertThrows(RuntimeException.class, () -> rabbitMQService.sendInvoice(dto));
    }
}
