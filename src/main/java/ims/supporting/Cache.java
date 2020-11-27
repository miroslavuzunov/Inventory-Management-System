package ims.supporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache{
    private static Map<String, List<CustomField>> cachedFieldsByName = new HashMap<>();

    public static void cacheList(String name, List<CustomField> loadedFields){
        cachedFieldsByName.put(name, loadedFields);
    }

    public static List<CustomField> getCachedFields(String name){
        if(cachedFieldsByName.isEmpty())
            return null;
        return cachedFieldsByName.get(name);
    }
}
