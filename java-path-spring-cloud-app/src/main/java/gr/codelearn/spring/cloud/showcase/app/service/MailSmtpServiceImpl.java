package gr.codelearn.spring.cloud.showcase.app.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import gr.codelearn.spring.cloud.showcase.app.base.BaseComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Profile("mail-smtp")
@Service
@RequiredArgsConstructor
public class MailSmtpServiceImpl extends BaseComponent implements MailService {
	private final Environment env;
	private final Configuration freemarkerConfiguration;
	private final JavaMailSender mailSender;

	public void sendEmail(String toSender, String subject, String emailBody) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
															 MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
															 StandardCharsets.UTF_8.name());
			helper.setSubject(subject);
			helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")),
						   Objects.requireNonNull(env.getProperty("spring.mail.properties.sender.name")));
			helper.setTo(toSender);
			helper.setText(getEmailContent(), true);
			mailSender.send(mimeMessage);
			logger.debug("Email sent to '{}' successfully.", toSender);
		} catch (AuthenticationFailedException afex) {
			logger.error("Authentication failed, credentials were rejected by '{}'.",
						 env.getProperty("spring.mail.host"), afex);
		} catch (MessagingException | TemplateException | IOException ex) {
			logger.error("Unable to send mail to '{}'.", toSender, ex);
		}
	}

	private String getEmailContent() throws IOException, TemplateException {
		StringWriter stringWriter = new StringWriter();
		Map<String, Object> model = new HashMap<>();
		model.put("name", env.getProperty("spring.mail.properties.sender.name"));
		model.put("username", "costas.giannacoulis");
		freemarkerConfiguration.getTemplate("template.ftl").process(model, stringWriter);
		return stringWriter.getBuffer().toString();
	}
}
