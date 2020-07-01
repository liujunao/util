package DBUnitily;

import java.io.*;  
import java.sql.*;  
import java.util.*;

public class MySQLDBHelper {  

	private static DataSource dataSource = null;
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	//获取连接
   static{
		dataSource = new ComboPooledDataSource("helloc3p0");
	}
	
	public static Connection getConnection() throws Exception {
		//获取当前线程的事务连接
		Connection connection = threadLocal.get();
		if(connection != null){
			return connection;
		}
		return dataSource.getConnection();
	}
	
	//开始事务
	public static void beginTransation() throws SQLException{
		//获取当前线程的事务连接
		Connection connection = threadLocal.get();
		if(connection == null) throw new SQLException("已经开启了事务，不能重复开启");
		//给 connection 赋值，表示开启了事务
		connection = dataSource.getConnection();
		//设置为手动提交
		connection.setAutoCommit(false);
		threadLocal.set(connection);
	}
	
	//提交事务
	public static void commitTransaction() throws SQLException{
		//获取当前线程的事务连接
		Connection connection = threadLocal.get();
		if(connection == null) throw new SQLException("没有事务不能提交！");
		//提交事务
		connection.commit();
		//关闭连接
		connection.close();
		connection = null;//表示事务结束
		threadLocal.remove();
	}
	
	//回滚事务
	public static void rollback(Connection connection){
		//获取当前线程的事务连接
		Connection connection = threadLocal.get();
		if(connection == null) throw new SQLException("没有事务不能回滚！"); 
		connection.rollback();
		connection.close();
		connection = null;//表示事务结束
		threadLocal.remove();
	}
    
    //关闭连接
    private  void closeAll(Connection con,PreparedStatement pst,ResultSet rst){  
        if(rst!=null){  
            try {  
                rst.close();  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
          
        if(pst!=null){  
            try {  
                pst.close();  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
          
        if(con!=null){  
            try {  
                con.close();  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
          
          
    }  

	//查询数据
    public List<Map<String,Object>> query(String sql,Object ... args){
        ResultSet resultSet = null;
        List<Map<String,Object>> list = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (args != null && args.length > 0){
                for (int i = 0;i < args.length;i++){
                    preparedStatement.setObject(i + 1,args[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null){
                list = getResultMap(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(connection,preparedStatement,resultSet);
        }
        return list;
    }

    //查询结果集
    private List<Map<String,Object>> getResultMap(ResultSet resultSet) throws SQLException {

        Map<String,Object> map =null;
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int count = resultSetMetaData.getColumnCount();
        while (resultSet.next()){
            map = new HashMap<String,Object>();
            for (int i = 1;i <= count;i++){
                String key = resultSetMetaData.getColumnLabel(i);
                Object value = resultSet.getString(i);
                map.put(key,value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 查询结果返回 List<T>
     * @param clazz 泛型类
     * @param list 查询的 List<Map<String,Object>>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> List<T> queryList(Class<T> clazz,List<Map<String,Object>> list) throws IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<T>();
        T model = null;
        Map<String,Object> map = null;

        if (list != null && list.size() > 0){
            for (int i = 0;i < list.size();i++){
                model = clazz.newInstance();
                map = list.get(i);
                MapToModel(map,model);
                result.add(model);
            }
        }
        return result;
    }

    //将map通过反射转化为实体
    private Object MapToModel(Map<String, Object> map, Object model) throws IllegalAccessException {

        if (!map.isEmpty()){
            for (String key : map.keySet()){
                Object value = null;
                if (!key.isEmpty()){
                    value = map.get(key);
                }
                Field[] fields = null;
                fields = model.getClass().getDeclaredFields();
                for (Field field : fields){
                    if (field.getName().toUpperCase().equals(key.toUpperCase())){
                        //获取私有字段
						field.setAccessible(true);

                        //进行类型判断
                        String type = field.getType().toString();
                        if (type.endsWith("String")){
                            if (value != null){
                                value = value.toString();
                            }else {
                                value = "";
                            }
                        }
                        if (type.endsWith("Date")){
                            value = new Date(value.toString());
                        }
                        if (type.endsWith("Boolean")){
                            value = Boolean.getBoolean(value.toString());
                        }
                        if (type.endsWith("int")){
                            value = new Integer(value.toString());
                        }
                        if (type.endsWith("Long")){
                            value = new Long(value.toString());
                        }
                        field.set(model,value);
                    }
                }
            }
        }
        return model;
    }

    //增删改
    public int update(String sql,Object ... args){
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (args != null && args.length > 0){
                for (int i = 0;i < args.length;i++){
                    preparedStatement.setObject(i + 1,args[i]);;
                }
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(connection,preparedStatement,null);
        }
        return result;
    }
    //获取数据的总条数
    private int dataCount(String sql){
        sql = "SELECT COUNT(*) AS num FROM (" + sql + ") AS tcount";
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        connection =getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                count = Integer.parseInt(resultSet.getString("num"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(connection,preparedStatement,null);
        }
        return count;
    }

    //分页数据的 Object[] 数组第一个值是数据集，第二个值是数据总数
    public Object[] queryPage(String sql,String orderBy,int pageIndex,int pageSize){
        Object[] objects = new Object[2];
        String stringSql = sql + "ORDER BY " + orderBy + "LIMIT" + (pageIndex - 1) + "," + pageSize;
        List<Map<String,Object>>list = query(stringSql,null);
        int count = dataCount(sql);
        objects[0] = list;
        objects[1] = count;
        return objects;
    }
}

