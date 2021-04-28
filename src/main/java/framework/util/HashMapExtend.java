package framework.util;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;

/**
 * HashMap with extra functions
 *
 * @param <T>
 */
public class HashMapExtend<K, V> extends HashMap<K, V> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     */
    public HashMapExtend() {
        super();
    }

    /**
     * コンストラクタ
     *
     * @param map
     */
    public HashMapExtend(@NonNull Map<K, V> map) {
        super();
        this.putItems(map);
    }

    public HashMapExtend<K, V> add(K k, V v) {
        this.put(k, v);
        return this;
    }

    /**
     * Add item to map and return the map itself
     *
     * @param key
     * @param value
     * @return HashMapExtend<K, V>
     */
    @NonNull
    public HashMapExtend<K, V> putItem(@NonNull K key, @NonNull V value) {
        this.put(key, value);
        return this;
    }

    /**
     * Add item to list and return the list itself
     *
     * @param map
     * @return HashMapExtend<T>
     */
    @NonNull
    public HashMapExtend<K, V> putItems(@NonNull Map<K, V> map) {
        this.putAll(map);
        return this;
    }

    /**
     * remove item to map and return the map itself
     *
     * @param key
     * @return HashMapExtend<K, V>
     */
    @NonNull
    public HashMapExtend<K, V> removeItem(@NonNull K key) {
        this.remove(key);
        return this;
    }
}
