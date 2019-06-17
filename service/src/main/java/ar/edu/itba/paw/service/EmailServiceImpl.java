package ar.edu.itba.paw.service;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

@Async
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine htmlTemplateEngine;
	
	@Qualifier("emailMessageSource")
	@Autowired
	private MessageSource ems;

	private static final String USER_REGISTERED_TEMPLATE = "userRegistered";
	private static final String SOMEONE_JOINED_YOUR_EVENT_TEMPLATE = "someoneJoinedYourEvent";
	private static final String YOU_WERE_KICKED_TEMPLATE = "youWereKicked";
	private static final String TOURNAMENT_STARTED_TEMPLATE = "tournamentStarted";

	void sendMail(final User user, final Locale locale, String titleMessage, Context ctx, String  template, Object[] titleParams) {

		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			message.setSubject(ems.getMessage(titleMessage, titleParams, locale));
			message.setFrom("SportMatcher");
			message.setTo(user.getUsername());

			final String htmlContent = this.htmlTemplateEngine.process(template, ctx);
			message.setText(htmlContent, true);
		} catch (Exception e) {
			//LOGGER.error("Message sending exception", e);
		}


		this.mailSender.send(mimeMessage);
	}

	@Override
	public void userRegistered(final User user, final Locale locale) {

		final Context ctx = new Context(locale);
		String username = user.getFirstname() + " " + user.getLastname();
		ctx.setVariable("email_body", ems.getMessage("user_registered_body", new Object[]{username}, locale));

		sendMail(user,locale,"user_registered_title",ctx,USER_REGISTERED_TEMPLATE,null);
	}
	
	@Override
	public void someoneJoinedYourEvent(final User joinedUser, final Event event, final Locale locale) {
		final Context ctx = new Context(locale);
		String ownerName = event.getOwner().getFirstname() + " " + event.getOwner().getLastname();
		String eventName = event.getName();
		String joinedUserName = joinedUser.getFirstname() + " " + joinedUser.getLastname();
		ctx.setVariable("event_name", eventName);
		ctx.setVariable("email_body", ems.getMessage("someone_joined_body", new Object[] {ownerName, eventName, joinedUserName}, locale));

		sendMail(joinedUser,locale,"someone_joined_title",ctx,SOMEONE_JOINED_YOUR_EVENT_TEMPLATE, new Object[]{eventName});
	}

	@Override
	public void youWereKicked(User kickedUser, Event event, final Locale locale) {
		final Context ctx = new Context(locale);
		String ownerName = event.getOwner().getFirstname() + " " + event.getOwner().getLastname();
		String eventName = event.getName();
		String kickedUserName = kickedUser.getFirstname() + " " + kickedUser.getLastname();
		ctx.setVariable("event_name", eventName);
		ctx.setVariable("email_body", ems.getMessage("user_kicked_body", new Object[]{ownerName, eventName, kickedUserName}, locale));

		sendMail(kickedUser, locale, "user_kicked_title",ctx, YOU_WERE_KICKED_TEMPLATE, new Object[]{eventName});
	}

	@Override
	public void youWereKicked(User kickedUser, Tournament tournament, Locale locale) {
		// HACER!
	}

	public void tournamentStarted(final User user, final Tournament tournament, final Locale locale) {
		final Context ctx = new Context(locale);
		String tournamentName = tournament.getName();
		String userName = user.getFirstname() + " " + user.getLastname();
		ctx.setVariable("tournament_name", tournamentName);
		ctx.setVariable("email_body", ems.getMessage("tournament_started_body", new Object[]{userName, tournamentName}, locale));

		sendMail(user, locale, "tournament_started_title", ctx, TOURNAMENT_STARTED_TEMPLATE, new Object[]{tournamentName});
	}


}
