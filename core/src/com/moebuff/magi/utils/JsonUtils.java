package com.moebuff.magi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * 基于 google 的 json 工具类
 *
 * @author muto
 */
public class JsonUtils {

    private static final JsonUtils DEFAULT_UTILS = new JsonUtils(new Gson());

    public static void addProperty(JsonObject json, String name, Object value) {
        DEFAULT_UTILS.setElement(json.getAsJsonObject(), name, value);
    }

    @SuppressWarnings("unchecked")
    public static <T extends JsonElement> T getElement(JsonElement json, String name) {
        if (json == null) {
            return (T) JsonNull.INSTANCE;
        } else if (!json.isJsonObject() || name == null) {
            return (T) json;
        }

        JsonObject realJson = json.getAsJsonObject();
        if (name.contains(".")) {
            String[] members = name.split("\\.", 2);
            JsonObject object = realJson.getAsJsonObject(members[0]);
            return getElement(object, members[1]);
        }
        return getElement(realJson.get(name), null);
    }

    //---------------------------------------------------------------------------------------------

    private Gson gson;

    public JsonUtils(Gson gson) {
        this.gson = gson;
    }

    public void setElement(JsonObject json, String name, Object value) {
        if (name.contains(".")) {
            String[] members = name.split("\\.", 2);
            JsonObject object = json.getAsJsonObject(members[0]);
            if (object == null) {
                object = new JsonObject();
            }

            setElement(object, members[1], value);
            json.add(members[0], object);
            return;
        }

        if (value instanceof Number) {
            json.addProperty(name, (Number) value);
        } else if (value instanceof Boolean) {
            json.addProperty(name, (Boolean) value);
        } else if (value instanceof Character) {
            json.addProperty(name, (Character) value);
        } else if (value instanceof String) {
            json.addProperty(name, (String) value);
        } else if (value instanceof JsonElement) {
            json.add(name, (JsonElement) value);
        } else {
            setElement(json, name, gson.toJsonTree(value));
        }
    }

}
