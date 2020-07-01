package common;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/9/24.
 */
public class JsonCommon {

    /**
     * 将传入的 json 字符串按类模板解析成对象
     *
     * @param json  需要解析的 json 字符串
     * @param clazz 类模板
     * @param <T>   泛型
     * @return 解析好的对象
     */
    public <T> T getObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        T bean = gson.fromJson(json, clazz);
        return bean;
    }

    /**
     * 将传入的 json 字符串转换成 json 对象
     *
     * @param json
     * @return
     */
    public JsonObject toJsonObject(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        return jsonObject;
    }

    /**
     * 将 jsonArray 转换为 List<T> 类型
     *
     * @param jsonArray
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> jsonArrayToObject(JsonArray jsonArray, Class<T> clazz) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            T element = gson.fromJson(jsonElement, clazz);
            list.add(element);
        }

        return list;
    }

    //将传入的对象解析成 json 字符串
    public <T> String getJsonString(T bean) {
        Gson gson = new Gson();
        String json = gson.toJson(bean, bean.getClass());
        return json;
    }

    //将获取到的 json 字符串转换为对象集合进行返回
    public <T> List<T> getObjectList(String jsonData, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
            //截取字符串，去除中括号
            jsonData = jsonData.substring(1, jsonData.length() - 1);
            //将字符串以 "}," 分解成数组//////
            String[] strings = jsonData.split("},");
            //分解后的字符串数组长度
            int stringsLength = strings.length;
            //遍历数组，进行解析，字符串解析成对象
            for (int i = 0; i < stringsLength; i++) {
                String newJsonString = null;
                if (i == stringsLength - 1) {
                    newJsonString = strings[i];
                } else {
                    newJsonString = strings[i] + "}";
                }
                T bean = getObject(newJsonString, clazz);
                list.add(bean);
            }
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
