package com.pdf.parsing;

import com.pdf.parsing.queue.QueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParsingApplication {

	public static void main(String[] args) {

		SpringApplication.run(ParsingApplication.class, args);
		ParsingApplication app = new ParsingApplication();
		app.sendMessage();


	}

	public void sendMessage(){
		QueueSender sender = new QueueSender();
		sender.send();
	}


}

