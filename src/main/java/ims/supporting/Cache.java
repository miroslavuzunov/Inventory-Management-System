package ims.supporting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cache{
    private static Map<String, Collection> cachedFieldsByKey = new HashMap<>();

    public static String cacheCollection(Collection loadedFields){
        String key = String.valueOf(System.nanoTime());
        cachedFieldsByKey.put(key, loadedFields);

        return key;
    }

    public static void cacheCollectionByCustomKey(String key, Collection loadedFields) {
        cachedFieldsByKey.put(key, loadedFields);
    }

    public static Collection getCachedFields(String key){
        if(cachedFieldsByKey.isEmpty())
            return null;
        return cachedFieldsByKey.get(key);
    }

}
