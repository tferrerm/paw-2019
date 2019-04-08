package ar.edu.itba.paw.service;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ar.edu.itba.paw.interfaces.EmailService;

@Async
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TemplateEngine htmlTemplateEngine;

	private static final String EVENT_JOINED_TEMPLATE = "templates/joinedEventMailTemplate.html";

	@Override
	public void joinEventEmail(String email, String eventOwnerName, String eventName,
							   final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("mailTitle", email);
		ctx.setVariable("mailBody", "Body");

		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			message.setSubject(applicationContext.getMessage("mailTitle", null, locale));
			message.setFrom("nosotros");
			message.setTo(email);

			final String htmlContent = this.htmlTemplateEngine.process(EVENT_JOINED_TEMPLATE, ctx);
			message.setText(htmlContent, true );
		} catch (Exception e) {
			//LOGGER.error("Message sending exception", e);
		}


		this.mailSender.send(mimeMessage);
	}



}
