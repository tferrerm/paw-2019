package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TemplateEngine htmlTemplateEngine;

	private static final String OFFER_ACCEPTED_TEMPLATE = "templates/offerAcceptedTemplate.html";

	@Override
	public void joinEventEmail(String email, String eventOwnerName, String eventName,
							   final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("email", email);

		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			message.setSubject(applicationContext.getMessage("offerAcceptedTitle", null, locale));
			message.setFrom("");
			message.setTo(email);

			final String htmlContent = this.htmlTemplateEngine.process(OFFER_ACCEPTED_TEMPLATE, ctx);
			message.setText(htmlContent, true );
		} catch (Exception e) {
			//LOGGER.error("Message sending exception", e);
		}


		this.mailSender.send(mimeMessage);
	}



}
