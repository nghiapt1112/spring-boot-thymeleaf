package com.lyna.commons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DataUtils {

    private static Map<Integer, String> mapStore = new HashMap<>();

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void putMapData(int type, String id) {
        mapStore.clear();
        mapStore.put(type, id);
    }

    public static void evicMapData() {
        mapStore.clear();
    }

    public static String getMapData() {
        AtomicReference<String> result = new AtomicReference<>("");
        mapStore.forEach((type, value) -> {
            if (type == 1) {
                result.set("成功に新規作成した");
            }
            if (type == 2) {
                result.set("成功に削除した ");
            }
            if (type == 3) {
                result.set("成功に更新した");
            }
            if (type == 4) {
                result.set(value);
            }
        });
        mapStore.clear();
        return result.get().isEmpty() ? null : result.get();
    }
}
