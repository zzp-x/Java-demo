package aspect;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Component //扫描到spring容器
//@Aspect	   //将该类作为切面组件
public class ExceptionBean {
	//e是目标组件抛出的异常对象
//	@AfterThrowing(throwing="e",pointcut="with(service..*)")
	public void execute(Exception e) {
		//将异常信息输入文件
		try {
			FileWriter fw = new FileWriter("D:\\note_err.log", true);
			PrintWriter pw = new PrintWriter(fw);
			Integer.parseInt("libai");
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = sdf.format(date);
			pw.println("*************************");
			pw.println("*异常类型：" + e);
			pw.println("*异常时间：" + dateStr);
			pw.println("******异常详细信息********");
			e.printStackTrace(pw);
			
			pw.close();
			fw.close();
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("记录异常失败!!!");
		}
	}
}
