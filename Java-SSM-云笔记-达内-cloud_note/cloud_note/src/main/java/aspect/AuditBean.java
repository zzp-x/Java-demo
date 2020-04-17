package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuditBean {
	@Around("within(service..*)")
	public Object audit(ProceedingJoinPoint point) throws Throwable {
		Object obj = null;
		try {
			long timeStart = System.currentTimeMillis();
			obj = point.proceed();
			long timeEnd = System.currentTimeMillis();
			String str = point.getSignature().toLongString();
			System.out.println("service×¢Èë£º" + str + "ºÄÊ±" + (timeEnd-timeStart));;
			
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("-------------------");
		return obj;
	}
	
}
