package com.pdf.parsing.queue;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class QueueConfig {

    Logger log = LoggerFactory.getLogger(QueueSender.class);

    private static final String LISTENER_METHOD = "processMessage";

    String rabbitUrl = "amqp://guest:guest@127.0.0.1/";

    @Bean
    Queue queue() {

    return new Queue("pdfQueue", true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("pdfExchange");
    }


        @Bean
    Channel binding(Queue queue, DirectExchange exchange) {

        CachingConnectionFactory factory = connectionFactory();
        Channel channel = null;

        factory.setUri(rabbitUrl);

        Connection conn = factory.createConnection();

        channel = conn.createChannel(true);

        try {

            channel.exchangeDeclare("pdfExchange","direct",true);
            channel.queueBind("pdfQueue","pdfExchange","visit_key");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //return BindingBuilder.bind(queue).to(exchange);
        return channel;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        URI rabbitMqUrl=null;

        try {
            rabbitMqUrl = new URI(rabbitUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //rabbitMqUrl = new URI("");

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
        factory.setHost(rabbitMqUrl.getHost());
        factory.setPort(rabbitMqUrl.getPort());
        factory.setVirtualHost(rabbitMqUrl.getPath().substring(1));
        return factory;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(QueueReceiver consumer) {
        return new MessageListenerAdapter(consumer, LISTENER_METHOD);
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer (CachingConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue().getName());
        container.setMessageListener(messageListenerAdapter);
        return container;

    }
}
