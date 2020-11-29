package ims.supporting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    private static Map<Object, Collection> cachedCollectionsByKey = new HashMap<>();
    private static Map<Object, Object> cachedObjectsByKey = new HashMap<>();

    public static void cacheCollection(Object key, Collection loadedFields) {
        cachedCollectionsByKey.put(key, loadedFields);
    }

    public static void cacheObject(Object key, Object loadedFields) {
        cachedObjectsByKey.put(key, loadedFields);
    }

    public static Collection getCachedCollection(Object key) {
        if (cachedCollectionsByKey.isEmpty())
            return null;
        return cachedCollectionsByKey.get(key);
    }

    public static Object getCachedObject(Object key) {
        if (cachedObjectsByKey.isEmpty())
            return null;
        return cachedObjectsByKey.get(key);
    }

}
