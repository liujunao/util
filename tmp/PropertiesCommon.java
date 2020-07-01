package Common;

import java.io.IOException;
import java.util.Properties;

public class PropertiesCommon {
	static Properties prop = new Properties();

	/**
	 * *
	 * 
	 * @param fileName
	 *            需要加载的properties文件，文件需要放在src根目录下
	 * @return 是否加载成功
	 */
	public static boolean loadFile(String fileName) {
		try {
			prop.load(PropertiesCommon.class.getClassLoader()
					.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据KEY取回相应的value
	 * 
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(String key) {
		return prop.getProperty(key);
	}

}
