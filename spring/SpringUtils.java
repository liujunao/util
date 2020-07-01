import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("springUtils")
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取对象 这里重写了bean方法，起主要作用
	 *
	 * @return Object 一个以所给名字注册的bean的实例
	 */
	public static <T> T getBean(String name) throws BeansException {
		if (applicationContext == null) {
			return null;
		}
		return (T)applicationContext.getBean(name);
	}

	/**
	 * 获取对象 这里重写了bean方法，起主要作用
	 *
	 * @return Object 一个以所给名字注册的bean的实例
	 */
	public static Object getBean(Class classType) throws BeansException {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(classType);
	}


}
