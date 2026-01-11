package com.puneetxp.lib;

import java.util.HashMap;
import java.util.Map;

public class Response {

    public static Map<String, Object> json(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        return map;
    }

    public static Map<String, Object> notFound(Object details) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Not Found");
        map.put("details", details);
        return map;
    }

    public static Map<String, Object> unprocessable(Object details) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Unprocessable Entity");
        map.put("details", details);
        return map;
    }

    public static Map<String, Object> unauthorized(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", "Unauthorized");
        map.put("message", message);
        return map;
    }
}
