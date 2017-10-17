package com.yxkj.controller.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * gson parse data
 *
 * @author huyong
 */
// TODO : GsonUtil current use just mock logic;
public class GsonUtil {

    private static final GsonUtil util = new GsonUtil();

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "CanBeFinal"})
    private GsonBuilder builder;

    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private Gson gson;

    private GsonUtil() {
        builder = new GsonBuilder();
        gson = builder.create();
    }

    /**
     * @return GsonUtil
     */
    public static GsonUtil getInstance() {
        return util;
    }

    /**
     * convert Json string to Obejct
     *
     * @param source json source
     * @param cls    tag
     * @return Object Object
     */
    public <T> T convertJsonStringToObject(String source, Class<T> cls) {
        T o = null;
        if (source != null) {
            try {
                o = gson.fromJson(source, cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    /**
     * convert string to list
     *
     * @param source json source
     * @param type   List type
     * @return List
     * <p>
     * <p>
     * simple： Type type = new TypeToken<List<AppInfo>>(){}.getType()
     * </p>
     */
    @SuppressWarnings({"TypeParameterExplicitlyExtendsObject", "unused"})
    public List<? extends Object> convertJsonStringToList(String source, Type type) {
        @SuppressWarnings("TypeParameterExplicitlyExtendsObject") List<? extends Object> l = null;
        if (source != null) {
            try {
                l = gson.fromJson(source, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return l;
    }

    /**
     * convert object to json string
     *
     * @param o source object
     * @return Json Json string
     */
    @SuppressWarnings("unused")
    public String convertObjectToJsonString(Object o) {
        String s = null;
        if (o != null) {
            try {
                s = gson.toJson(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    /**
     * 将Map转化为Json
     *
     * @param list
     * @return String
     */
    public static <T> String listToJson(List<T> list) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(list);
        return jsonStr;
    }
}
