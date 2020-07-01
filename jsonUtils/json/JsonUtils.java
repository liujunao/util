import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by lenovo on 2017/8/1.
 */
public class JsonUtils {

    //将 List 对象序列化为 JSON 文本
    public <T> String toJsonString(List<T> list) {
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray.toString();
    }

    //将对象序列化为 JSON 文本
    public String toJsonString(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        return jsonArray.toString();
    }

    //将 JSON 对象数组序列化为 JSON 文本
    public String toJsonString(JSONArray jsonArray) {
        return jsonArray.toString();
    }

    //将对象转换成 List 对象
    public List toArrayList(Object object) {
        List list = new ArrayList();
        JSONArray jsonArray = JSONArray.fromObject(object);
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                Object value = jsonObject.get(key);
                list.add(value);
            }
        }
        return list;
    }

    //将对象转换为 Collection 对象
    public Collection toCollection(Object object) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        return JSONArray.toCollection(jsonArray);
    }

    //将对象转换为 JSON 对象数组
    public JSONArray toJsonArray(Object object) {
        return JSONArray.fromObject(object);
    }

    //将对象转换为 JSON 对象
    public JSONObject toJsonObject(Object object) {
        return JSONObject.fromObject(object);
    }

    //将对象转换为 HashMap
    public HashMap toHashMap(Object object) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        JSONObject jsonObject = toJsonObject(object);
        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = String.valueOf(iterator.next());
            Object value = jsonObject.get(key);
            data.put(key, value);
        }
        return data;
    }

    //将对象转换为 List<Map<String,Object>>
    public List<Map<String, Object>> toList(Object object) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = JSONArray.fromObject(object);
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            Map<String, Object> map = new HashMap<String, Object>();
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = jsonObject.get(key);
                map.put(key, value);
            }
            list.add(map);
        }


        return list;
    }

    //将 JSON 对象数组转换为传入类型的 List
    public <T> List<T> toList(JSONArray jsonArray, Class<T> clazz) {
        return JSONArray.toList(jsonArray, clazz);
    }

    //将对象转换为传入类型的 List
    public <T> List<T> toList(Object object, Class<T> clazz) {
        JSONArray jsonArray = JSONArray.fromObject(object);
        return JSONArray.toList(jsonArray, clazz);
    }
    //将 JSON 对象转换为传入类型的对象
    public <T> T toBean(Object object,Class<T> clazz){
        JSONObject jsonObject = JSONObject.fromObject(object);
        return (T) JSONObject.toBean(jsonObject,clazz);
    }

    /**
     * 将 JSON 文本反序列化为主从关系的实体
     * @param jsonString JSON 文本
     * @param mainClass 主实体类型
     * @param detailName 从实体类在主实体类中的属性名称
     * @param detailClass 从实体类型
     * @param <T> 泛型 T 代表主实体类型
     * @param <D> 泛型 D 代表从实体类型
     * @return
     */
    public <T,D> T toBean(String jsonString,Class<T> mainClass,String detailName,Class<D> detailClass){
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = (JSONArray) jsonObject.get(detailName);
        T mainEntity = toBean(jsonObject,mainClass);
        List<D> detailList = toList(jsonArray,detailClass);
        try {
            BeanUtils.setProperty(mainEntity,detailName,detailList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return mainEntity;
    }

    /**
     * 将 JSON 文本反序列化为主从关系的实体
     * @param jsonString JSON 文本
     * @param mainClass 主实体类型
     * @param detailName1 从实体类型在主实体类中的属性
     * @param detailClass1 从实体类型
     * @param detailName2 从实体类型在主实体类中的属性
     * @param detailClass2 从实体类型
     * @param <T> 泛型 T 代表主实体类型
     * @param <D1> 泛型 D1 代表从实体类型
     * @param <D2> 泛型 D2 代表从实体类型
     * @return
     */
    public <T,D1,D2> T toBean(String jsonString,Class<T> mainClass,String detailName1,Class<D1> detailClass1,
                              String detailName2,Class<D2> detailClass2){
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = (JSONArray) jsonObject.get(detailName1);
        JSONArray jsonArray2 = (JSONArray) jsonObject.get(detailName2);

        T mainEntity = toBean(jsonObject,mainClass);
        List<D1> detailList1 = toList(jsonArray,detailClass1);
        List<D2> detailList2 = toList(jsonArray2,detailClass2);

        try {
            BeanUtils.setProperty(mainEntity,detailName1,detailClass1);
            BeanUtils.setProperty(mainEntity,detailName2,detailClass2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return mainEntity;
    }

    public <T> T toBean(String jsonString,Class<T> mainClass,HashMap<String,Class> detailClass){
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        T mainEntity = toBean(jsonObject,mainClass);
        for (Object key : detailClass.keySet()){
            Class value = detailClass.get(key);
            try {
                BeanUtils.setProperty(mainEntity,key.toString(),value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return mainEntity;
    }
}
