package gr.codelearn.spring.cloud.showcase.app.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public abstract class BaseComponent {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	private void init() {
		logger.trace("Loaded {}.", getClass().getName());
	}

	@PreDestroy
	private void destroy() {
		logger.trace("About to unload {} class.", getClass().getName());
	}
}
