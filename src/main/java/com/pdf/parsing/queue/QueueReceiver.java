package com.pdf.parsing.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

@RabbitListener(queues = "pdfQueue")
public class QueueReceiver {
    Logger log = LoggerFactory.getLogger(QueueSender.class);


    private void processMessage(String message) {

        log.debug("Message Received::" + message);

    }


}

