package ims.supporting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    private  Map<Object, Collection> cachedCollectionsByKey;
    private  Map<Object, Object> cachedObjectsByKey;

    public Cache() {
        cachedCollectionsByKey = new HashMap<>();
        cachedObjectsByKey = new HashMap<>();
    }

    public  void cacheCollection(Object key, Collection loadedFields) {
        cachedCollectionsByKey.put(key, loadedFields);
    }

    public  void cacheObject(Object key, Object loadedFields) {
        cachedObjectsByKey.put(key, loadedFields);
    }

    public  Collection getCachedCollection(Object key) {
        if (cachedCollectionsByKey.isEmpty())
            return null;
        return cachedCollectionsByKey.get(key);
    }

    public  Object getCachedObject(Object key) {
        if (cachedObjectsByKey.isEmpty())
            return null;
        return cachedObjectsByKey.get(key);
    }

    public  void clearCachedCollection(Object key){
        if (!cachedObjectsByKey.isEmpty())
            cachedCollectionsByKey.remove(key);
    }

}
