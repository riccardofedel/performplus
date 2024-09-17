package com.finconsgroup.performplus.service.business.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class FVGEmailService {

	@Value("${spring.mail.host}")
	String host;

	@Value("${send.mail.enabled}")
	Boolean enabled;

	private static final Logger logger = LoggerFactory.getLogger(FVGEmailService.class);

	public void sendMail(String to, String from, String subject, String bodyMessage) throws MessagingException {

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		logger.info("Invio mail: host:" + host + ", to:" + to + ", from:" + from + ", subject:" + subject
				+ ", BodyMessage:" + bodyMessage);

		if (enabled) {
			Session session = Session.getDefaultInstance(properties);
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(bodyMessage);

			Transport.send(message);
			logger.info("Sent message successfully....");
		} else {
			logger.info("Sent message not enabled.");
		}

	}

}