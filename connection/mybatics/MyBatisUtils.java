package db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author yinren
 * @date 2018/6/27
 */
public class MyBatisUtils {

    public static SqlSessionFactory getFactory() {
        String resource = "config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            System.out.println("config.xml 读取出错");
            e.printStackTrace();
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        return factory;
    }
}
