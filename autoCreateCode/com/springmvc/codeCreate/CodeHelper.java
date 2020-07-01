package com.springmvc.codeCreate;

import com.springmvc.db.JdbcUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/28.
 */
public class CodeHelper {

    private JdbcUtils jdbcUtils = new JdbcUtils("db");

    //获取主键值
    public String getKeyName(String tableName){
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DISTINCT COLUMN_NAME ");
        stringBuilder.append("FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name='");
        stringBuilder.append(tableName);
        stringBuilder.append("' AND COLUMN_KEY='PRI'");
        String string = stringBuilder.toString();
        List<Map<String,Object>>list = jdbcUtils.query(string,null);
        result = list.get(0).get("COLUMN_NAME").toString();
        return result;
    }

    //将数据库字段类型转换为 java 类型
    public String setType(String string){
        String result = string;
        switch (string.toLowerCase()){
            case "nvarchar": result = "String";break;
            case "varchar": result = "String";break;
            case "char": result = "String";break;
            case "text": result = "String";break;
            case "int": result = "int";break;
            case "tinyint": result = "int";break;
            case "smallint": result = "int";break;
            case "bigint": result = "int";break;
            case "datetime": result = "Date";break;
            case "blob":result = "String";break;
            default:result = "String";
        }
        return result;
    }

    //根据数据类型设置默认值
    public String setDefaultValue(String string){
        String result = string;
        switch (string.toLowerCase()){
            case "string": result = "\"\"";break;
            case "int": result = "0";break;
            case "date": result = "new Date()";break;
        }
        return result;
    }

    //获取数据表字段名和字段类型
    public Map<String,String> getFields(String tableName){
        Map<String,String> map = new HashMap<String,String>();

        String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name='" + tableName + "'";
        List<Map<String,Object>>list = jdbcUtils.query(sql,null);
        String key = "";
        String value = "";
        Map<String,Object> map1 = null;
        for (int i = 0;i < list.size();i++){
            map1 = list.get(i);
            value = map1.get("DATA_TYPE").toString();
            value = setType(value);
            key = map1.get("COLUMN_NAME").toString();
            map.put(key,value);
        }
        return map;
    }

    //获取所有表名
    public List<String> getAllTableName(){
        List<String>list = new ArrayList<>();
        String sql = "show tables";
        List<Map<String,Object>>result = jdbcUtils.query(sql,null);
        Map<String,Object> map = null;
        for (int i = 0;i < result.size();i++){
            map = result.get(i);
            for (Object object : map.values()){
                list.add(object.toString());
            }
        }
        return list;
    }

    /**
     * 获取保存路径
     * @param path 路径
     * @param tableName 数据表名
     * @param isAuto 是否生成父类
     * @return 返回结果的路径字符串
     */
    public String getSavePath(String path,String tableName,boolean isAuto){
        String savePath = "";
        if (isAuto){
            savePath += path + "\\Auto\\";
            savePath += tableName + "Auto.java";
        }else {
            savePath += path + "\\";
            savePath += tableName + ".java";
        }
        return savePath;
    }

    /**
     * 保存文件
     * @param content 文件内容
     * @param path 路径
     * @param tableName 数据表名
     * @param isAuto 是否生成父类
     * @throws IOException
     */
    public void saveFile(String content,String path,String tableName,boolean isAuto) throws IOException {
        String fileName = getSavePath(path,tableName,isAuto);
        File file = new File(fileName);
        path = file.getParent();
        if (isAuto){
            FileCommon.createDirectory(path);
            FileCommon.createFile(fileName);
            FileCommon.writeText(fileName,content);
        }else {
            if (!FileCommon.isExitsFile(fileName)){
                FileCommon.createDirectory(path);
                FileCommon.createFile(fileName);
                FileCommon.writeText(fileName,content);
            }
        }
    }


    //生成 bean
    public void createBean(String tableName,String path) throws IOException {
        String keyNames = getKeyName(tableName);

        //生成父类
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("package beans.Auto;\n\n ");
        //stringBuilder.append("import java.util.Date;\n");
        stringBuilder.append("public class " + tableName + "Auto{\n");

        Map<String,String> map = getFields(tableName);
        for (Map.Entry<String,String> entry : map.entrySet()){
            stringBuilder.append("  private " + entry.getValue() + " " + StringCommon.initialsLowerCase(entry.getKey())
            + " = " + setDefaultValue(entry.getValue()) + ";\n");
        }
        for (Map.Entry<String,String> entry : map.entrySet()){
            stringBuilder.append("  public " + entry.getValue() + " get" + StringCommon.initialsUpperCase(entry.getKey()) + "() {\n");
            stringBuilder.append("      return " + StringCommon.initialsLowerCase(entry.getKey() + ";\n}\n"));

            stringBuilder.append("  public void set" + StringCommon.initialsUpperCase(entry.getKey()) + "(" +
                    entry.getValue() + " " + StringCommon.initialsLowerCase(entry.getKey()) + "){\n");
            stringBuilder.append("      this." + StringCommon.initialsLowerCase(entry.getKey() + " = " + StringCommon.initialsLowerCase(entry.getKey())
            + ";\n}\n"));
        }
        stringBuilder.append("}\n");
        String string = stringBuilder.toString();
        saveFile(string,path + "\\beans",tableName,true);

        //生成子类
        createChild(tableName,path,"","beans");
    }

    //生成 DAO
    public void createDao(String tableName,String path) throws IOException {
        String keyNames = getKeyName(tableName);
        Map<String,String> map = getFields(tableName);
        //获取最后一个字段的值
        String lastKey = map.keySet().toArray()[map.size() - 1].toString();

        int fieldCount = map.size() - 1;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("public class " + tableName + "DAOAuto {\n");
        stringBuffer.append("   JdbcUtils jdbcUtils = new JdbcUtils();\n");

        //单例模式
        stringBuffer.append("   //单利模式\n");
        stringBuffer.append("   private static class " + tableName + "DAOAutoSingleton{\n");
        stringBuffer.append("   private static final " + tableName + "DAOAuto INSTANCE = ");
        stringBuffer.append("new " + tableName + "DAOAuto();\n}\n");
        stringBuffer.append("   public " + tableName + "DAOAuto(){}\n");
        stringBuffer.append("   public static final " + tableName + "DAOAuto" + " getInstance(){\n");
        stringBuffer.append("       return " + tableName + "DAOAutoSingleton.INSTANCE;\n}\n");

        //分页
        stringBuffer.append("   //分页\n");
        stringBuffer.append("   public Object[] getPage(int pageIndex,int pageSize,String orderBy,String otherWhere)throws Exception{\n");
        stringBuffer.append("       Object[] objectArray = new Object[2];\n");
        stringBuffer.append("       List<" + tableName + ">list = null;\n");
        stringBuffer.append("       String sql = \"SELECT * FROM " + tableName + "where 1=1 \" + otherWhere;\n");
        stringBuffer.append("       Object[] result = jdbcUtils.queryPage(sql,orderBy,pageIndex,pageSize);\n");
        stringBuffer.append("       List<Map<String,Object>> mapList = (List<Map<String,Object>>)result[0];\n");
        stringBuffer.append("       int count = Integer.parseInt(result[1].toString());\n");
        stringBuffer.append("       list = jdbcUtils.queryList(" + tableName + ".class,mapList);\n");
        stringBuffer.append("       objectArray[0] = list;\n");
        stringBuffer.append("       objectArray[1] = count;\n");
        stringBuffer.append("       return objectArray;\n}\n");

        //查询
        stringBuffer.append("   //查询\n");
        stringBuffer.append("   public List<" + tableName + ">queryList(String otherWhere,Object ... args) throws Exception{\n");
        stringBuffer.append("       List<" + tableName + "> list = null;\n");
        stringBuffer.append("       String sql = \"SELECT * FROM " + tableName + " WHERE 1 = 1\" + otherWhere;\n");
        stringBuffer.append("       List<Map<String,Object>> result = jdbcUtils.query(sql,args);\n");
        stringBuffer.append("       list = jdbcUtils.queryList(" + tableName + ".class,result);\n");
        stringBuffer.append("       return list;\n}\n");

        //单个查询
        stringBuffer.append("   //单个查询\n");
        stringBuffer.append("   public " + tableName + " query(String id)throws Exception{\n");
        stringBuffer.append("       " + tableName + " model = null;\n");
        stringBuffer.append("       String sql = \"SELECT * FROM " + tableName + " WHERE " + keyNames + " = \" + id;\n");
        stringBuffer.append("       List<Map<String,Object>> result = jdbcUtils.query(sql,null);\n");
        stringBuffer.append("       List<" + tableName + ">list = jdbcUtils.queryList(" + tableName + ".class,result);\n");
        stringBuffer.append("       if(list != null && list.size() > 0){\n");
        stringBuffer.append("           model = list.get(0);\n}\n");
        stringBuffer.append("       return model;\n}\n");

        //添加
        stringBuffer.append("   //添加\n");
        stringBuffer.append("   public int add(" + tableName + " model){\n");
        stringBuffer.append("       String sql = \"\";\n");
        stringBuffer.append("       StringBuffer stringBuffer = new StringBuffer();\n");
        stringBuffer.append("       stringBuffer.append(\"INSERT INTO " + tableName + "(\");\n");
        //循环生成添加 sql 语句
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (entry.getKey() != keyNames){
                if (entry.getKey() != lastKey){
                    stringBuffer.append("       stringBuffer.append(\"" + entry.getKey() + ",\");\n");
                }else {
                    stringBuffer.append("       stringBuffer.append(\"" + entry.getKey() + "\");\n");
                }
            }
        }
        stringBuffer.append("       stringBuffer.append(\") VALUES (\");\n");
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (entry.getKey() != lastKey){
                stringBuffer.append("       stringBuffer.append(\"?,\");\n");
            }else {
                stringBuffer.append("       stringBuffer.append(\"?\");\n");
            }
        }
        stringBuffer.append("       sql = stringBuffer.toString();\n");

        //填充参数值
        stringBuffer.append("       Object[] args = new Object[]{\n");
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (entry.getKey() != keyNames){
                if (entry.getKey() != lastKey){
                    stringBuffer.append("       model.get" + StringCommon.initialsUpperCase(entry.getKey()) + "(),\n");
                }else {
                    stringBuffer.append("       model.get" + StringCommon.initialsUpperCase(entry.getKey()) + "()\n");
                }
            }
        }
        stringBuffer.append("       };\n");

        stringBuffer.append("       int result = jdbcUtils.update(sql,args);\n");
        stringBuffer.append("       return result;\n}\n");

        //删除
        stringBuffer.append("   //删除\n");
        stringBuffer.append("   public int deleteById(String id){\n");
        stringBuffer.append("       String sql = \"DELETE FROM " + tableName + " WHERE " + keyNames + "=\" + id;\n");
        stringBuffer.append("       int result = jdbcUtils.update(sql,null);\n");
        stringBuffer.append("       return result;\n}\n");

        //修改
        stringBuffer.append("   //修改\n");
        stringBuffer.append("   public int update(" + tableName + " model){\n");
        stringBuffer.append("       String sql = \"\";\n");
        stringBuffer.append("       StringBuffer stringBuffer = new StringBuffer();\n");
        stringBuffer.append("       stringBuffer.append(\"update " + tableName + " SET \");\n");
        //循环生成 sql 语句
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (entry.getKey() != keyNames){
                if (entry.getKey() != lastKey){
                    stringBuffer.append("       stringBuffer.append(\"" + entry.getKey() + " = ?,\");\n");
                }else {
                    stringBuffer.append("       stringBuffer.append(\"" + entry.getKey() + " = ?\");\n");
                }
            }
        }
        stringBuffer.append("       stringBuffer.append(\"WHERE " + keyNames + " = model.get" + StringCommon.initialsUpperCase(keyNames) + "()\");\n");
        stringBuffer.append("       sql = stringBuffer.toString();\n");
        //填充参数值
        stringBuffer.append("       Object[] args = new Object[]{\n");
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (entry.getKey() != keyNames){
                if (entry.getKey() != lastKey){
                    stringBuffer.append("       model.get" + entry.getKey() + "(),\n");
                }else {
                    stringBuffer.append("       model.get" + entry.getKey() + "()\n");
                }
            }
        }
        stringBuffer.append("       };\n");
        stringBuffer.append("       int result = jdbcUtils.update(sql,args);\n");
        stringBuffer.append("       return result;\n}\n}");

        String string = stringBuffer.toString();
        saveFile(string,path + "\\dao",tableName + "DAO",true);

        //生成子类
        createChild(tableName,path,"DAO","dao");
    }

    //生成 Service
    public void createService(String tableName,String path) throws IOException {
        //获取主键值
        String keyNames = getKeyName(tableName);
        //获取字段名和字段类型
        Map<String,String> map = getFields(tableName);
        //获取最后一个字段的值
        String lastKey = map.keySet().toArray()[map.size() - 1].toString();

        //生成父类
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("public class " + tableName + "ServiceAuto{\n");

        //单例模式
        stringBuffer.append("   //单利模式\n");
        stringBuffer.append("   private static class " + tableName + "ServiceAutoSingleton{\n");
        stringBuffer.append("   private static final " + tableName + "ServiceAuto INSTANCE = ");
        stringBuffer.append("new " + tableName + "ServiceAuto();\n}\n");
        stringBuffer.append("   public " + tableName + "ServiceAuto(){}\n");
        stringBuffer.append("   public static final " + tableName + "ServiceAuto" + " getInstance(){\n");
        stringBuffer.append("       return " + tableName + "ServiceAutoSingleton.INSTANCE;\n}\n");

        //分页
        stringBuffer.append("   //分页\n");
        stringBuffer.append("   public Object[] getPage(int pageIndex,int pageSize,String orderBy,String otherWhere)throws Exception{\n");
        stringBuffer.append("       Object[] objectArray = new Object[2];\n");
        stringBuffer.append("       objectArray = " + tableName + "DAO.getInstance().getPage(pageIndex,pageSize,orderBy,otherWhere);\n");
        stringBuffer.append("       return objectArray;\n}\n");

        //查询
        stringBuffer.append("   //查询\n");
        stringBuffer.append("   public List<" + tableName + ">queryList(String otherWhere,Object[] args)throws Exception{\n");
        stringBuffer.append("       List<" + tableName + ">list = null;\n");
        stringBuffer.append("       list = " + tableName + "DAO.getInstance().queryList(otherWhere,args);\n");
        stringBuffer.append("       return list;\n}\n");

        //单个查询
        stringBuffer.append("   //单个查询\n");
        stringBuffer.append("   public " + tableName + " query(String id)throws Exception{\n");
        stringBuffer.append("       " + tableName + " model = null;\n");
        stringBuffer.append("       model = " + tableName + "DAO.getInstance().query(id);\n");
        stringBuffer.append("       return model;\n}\n");

        //添加
        stringBuffer.append("   //添加\n");
        stringBuffer.append("   public int add(" + tableName + " model){\n");
        stringBuffer.append("       int result = 0;\n");
        stringBuffer.append("       result = " + tableName + "DAO.getInstance().add(model);\n");
        stringBuffer.append("       return result;\n}\n");

        //删除
        stringBuffer.append("   //删除\n");
        stringBuffer.append("   public int deleteById(String id){\n");
        stringBuffer.append("       int result = 0;\n");
        stringBuffer.append("       result = " + tableName + "DAO.getInstance().deleteById(id);\n");
        stringBuffer.append("       return result;\n}\n");

        //修改
        stringBuffer.append("   //修改\n");
        stringBuffer.append("   public int update(" + tableName + " model){\n");
        stringBuffer.append("       int result = 0;\n");
        stringBuffer.append("       result = " + tableName + "DAO.getInstance().update(model);\n");
        stringBuffer.append("       return result;\n}\n}");

        String string = stringBuffer.toString();
        saveFile(string,path + "\\service",tableName + "Service",true);

        //生成子类

        createChild(tableName,path,"Service","service");
    }

    //生成子类
    public void createChild(String tableName,String path,String type,String pack) throws IOException {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("public class " + tableName + type +" extends " + tableName + type +"Auto{\n}\n");
            String string1 = new String();
            string1 = stringBuffer.toString();
            saveFile(string1,path + "\\"+pack,tableName + type,false);
    }
}
