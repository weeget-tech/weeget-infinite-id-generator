package cn.weeget.service.id;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GlobalUtils implements ApplicationContextAware {

	/**
	 * 项目名称
	 */
	public static String PROJECT_NAME;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		PROJECT_NAME = applicationContext.getEnvironment().getProperty("spring.application.name");
	}
}