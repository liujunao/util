package DBUnitily;

import java.io.*;  
import java.sql.*;  
import java.util.*;

public class MySQLDBHelper {  

	//提交事务
	public static void commit(Connection connection){
		if(connection != null){
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//回滚事务
	public static void rollback(Connection connection){
		if(connection != null){
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//开始事务
	public static void beginTx(Connection connection){
		if(connection != null){
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//获取连接
    private  Connection getConnection(){   
	    Connection con=null;
        String driver_class=null;  
        String driver_url=null;  
        String database_user=null;  
        String database_password=null;  
        try {  
            InputStream fis=getClass().getClassLoader().getResourceAsStream("/db.properties");  
            Properties p=new Properties();  
            p.load(fis);  
            driver_class=p.getProperty("driver").trim();
            driver_url=p.getProperty("url").trim();  
            database_user=p.getProperty("user").trim();  
            database_password=p.getProperty("password").trim();  
           Class.forName(driver_class);  
            con=DriverManager.getConnection(driver_url,database_user,database_password);  
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return con;  
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


    private  List<Map<String,Object>> getResultMap(ResultSet rs)  
            throws SQLException {  
        Map<String, Object> hm =null;
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        ResultSetMetaData rsmd = rs.getMetaData();  
        int count = rsmd.getColumnCount();
        while (rs.next()) {
        	hm= new HashMap<String, Object>();
	        for (int i = 1; i <= count; i++) { 
	            String key = rsmd.getColumnLabel(i);  
	            Object value = rs.getString(i);  
	            hm.put(key, value);  
	        }
	         list.add(hm);
        }
        return list;  
}  
    
    //获取数据的总条数
    private int dataCount(String sql)
    {
    	sql= "SELECT count(*) as num FROM (" + sql + ") tcount";
    	int count=0;
    	 Connection conn=null;  
         PreparedStatement pstmt=null;  
         ResultSet rs=null;
         conn=getConnection();//获取连接对象  
         try {
			pstmt=conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//预处理sql 
         try {
			rs=pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         try {
			while(rs.next()){
				 count=Integer.parseInt(rs.getString("num"));
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         closeAll(conn, pstmt, rs);//关闭连接 
    	return count;
    	
    }
    
    //查询数据
    public  List<Map<String,Object>> query(String sql,Object[] agrs)
    {
    	ResultSet rs=null;
    	 List<Map<String,Object>> mapList=null;
        try {  
            Connection conn=null;  
            PreparedStatement pstmt=null;  
            try {  
                conn=getConnection();//获取连接对象  
                pstmt=conn.prepareStatement(sql);//预处理sql  
                if(agrs!=null&&agrs.length>0){
                    
                    for (int i = 0; i < agrs.length; i++) {  
                    pstmt.setObject(i+1, agrs[i]);  
                    }  
                }
                rs=pstmt.executeQuery();//由于不需要返回结果集，所以直接使用executeUpdate  
                if(rs != null){
                	mapList=getResultMap(rs);
                	closeAll(conn, pstmt, null);//关闭连接  
               }
               
            } finally {  
            	closeAll(conn, pstmt, rs);//关闭连接 
                     }  
        }
        catch (Exception e) {  
        
        }
        return mapList;  	
    }
 
    //增删改
    public  int update(String sql, Object[] agrs){
    	int result=-1;  
        try {  
            Connection conn=null;  
            PreparedStatement pstmt=null;  
            try {  
                conn=getConnection();//获取连接对象  
                pstmt=conn.prepareStatement(sql);//预处理sql  
                if(agrs!=null&&agrs.length>0){
                    
                    for (int i = 0; i < agrs.length; i++) {  
                    pstmt.setObject(i+1, agrs[i]);  
                    }  
                }
                result=pstmt.executeUpdate();//由于不需要返回结果集，所以直接使用executeUpdate  
                closeAll(conn, pstmt, null);//关闭连接  
            } finally {  
            	closeAll(conn, pstmt, null);//关闭连接  
                     }  
        }
        catch (Exception e) {  
        
        }
        return result;  
    }  

    //分页数据的 Object[] 数组第一个值是 数据集  第二个值是 数据总数
    public  Object[] queryPage(String sql,String orderBy,int pageIndex,int pageSize){
    	Object[] objArry=new Object[2];
    	 String strSQL = "SELECT (@i:=@i+1) AS row_id,tab.* FROM (" + sql + ")  AS TAB,(SELECT @i:=0) AS it ORDER BY " + orderBy + " LIMIT " + (pageIndex - 1) + "," + pageSize;
    	List<Map<String,Object>> mapList=query(strSQL,null);
    	int count=dataCount(sql);
    	objArry[0]=mapList;
    	objArry[1]=count;
    	return objArry;
    }
}

