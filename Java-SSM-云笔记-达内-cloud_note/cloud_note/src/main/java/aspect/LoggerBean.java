package aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerBean {
	@Before("within(controller..*)")
	public void logController() {
		System.out.println("AOP×¢ÈëController");
	}
	
	@Before("within(service..*)")
	public void logService() {
		System.out.println("AOP×¢ÈëService");
	}
}
