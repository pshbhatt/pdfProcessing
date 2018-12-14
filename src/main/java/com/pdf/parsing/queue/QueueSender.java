package com.pdf.parsing.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {

    Logger log = LoggerFactory.getLogger(QueueSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    public QueueSender(AmqpTemplate amqpTemplate) {
        super();
        this.amqpTemplate = amqpTemplate;
    }

    public QueueSender() {
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        for(int i=0;i<11;i++) {
            String message = "\n" +
                    "{\n" +
                    "    blueprintId: '5d5f044f-ea3b-4533-9f55-2e1a45b02aab',\n" +
                    "    currentProcessingPhase: 'pdf_to_image_"+i+"',\n" +
                    "    fileLocation: 'https://s3.us-east-2.amazonaws.com/someco.com/uploads/pdfs/74efe087-7949-46db-8a8d-ee06776eb2b0.pdf',\n" +
                    "    createTime: '1544404634',\n" +
                    "}\n";
            log.debug(message);
            amqpTemplate.convertAndSend(queue.getName(), message);
            log.debug(" [x] Sent '" + message + "'");
        }
    }
}
