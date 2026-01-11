package com.puneetxp.lib;

import java.util.HashMap;
import java.util.Map;

public class Session {
    // Mock Session
    // In Spring, inject HttpSession
    public static Map<String, Object> current = new HashMap<>();

    public static void set(String key, Object value) {
        current.put(key, value);
    }

    public static Object get(String key) {
        return current.get(key);
    }

    public static void destroy() {
        current.clear();
    }

    public static Object create(Map<String, Object> auth) {
        set("user_id", auth.get("id"));
        return Response.json(auth);
    }
}
