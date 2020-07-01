package common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lenovo
 * @date 2018/7/3
 */
public class CacheUtils<T> {

    private Map<String, T> cache = new ConcurrentHashMap<>();

    /**
     * 获取缓存值
     *
     * @param key
     * @return
     */
    public T getValue(Object key) {
        return cache.get(key);
    }

    /**
     * 添加或更新缓存
     *
     * @param key
     * @param value
     */
    public void addOrUpdate(String key, T value) {
        cache.put(key, value);
    }

    /**
     * 删除指定缓存
     *
     * @param key
     */
    public void delete(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }

    /**
     * 清空所有缓存
     */
    public void deleteAll() {
        cache.clear();
    }
}
