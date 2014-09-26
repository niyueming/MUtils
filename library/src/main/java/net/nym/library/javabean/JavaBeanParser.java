package net.nym.library.javabean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author nym
 * @date 2014/9/26 0026.
 */
public class JavaBeanParser {

    /**
     * 把JSONObject转化为JavaBean
     *
     * @param clazz      Class<JavaBean>
     * @param jsonObject
     * @return T JavaBean
     */
    public static <T> T parserJSONObject(Class<T> clazz, JSONObject jsonObject) {
        Class<T> superClazz = clazz;
        T instance = null;
        try {
            instance = superClazz.newInstance();
            Iterator<String> iterator = jsonObject.keys();
            String methodName = null;
            Method method = null;
            while (iterator.hasNext()) {
                String key = iterator.next();
                String firstLetter = key.substring(0, 1).toUpperCase();
                Field field = getDeclaredField(superClazz, key);
                if (key.startsWith("is")) {
                    methodName = String.format("set%s", key.substring(2));
                } else {
                    methodName = String.format("set%s%s", firstLetter, key.substring(1));
                }
                method = getDeclaredMethod(superClazz, methodName, field.getType());
                if (method != null) {
                    method.invoke(instance, jsonObject.opt(key));
                }

            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 把JSONArray转化为ArrayList<JavaBean>
     *
     * @param clazz Class<JavaBean>
     * @param array
     * @return ArrayList<JavaBean>
     */
    public static <T> ArrayList<T> parserJSONArray(Class<T> clazz, JSONArray array) {
        ArrayList<T> list = new ArrayList<T>();
        JSONObject jsonObject = null;
        for (int i = 0; i < array.length(); i++) {
            jsonObject = array.optJSONObject(i);
            list.add(parserJSONObject(clazz, jsonObject));
        }

        return list;
    }

    /**
     * @param object JavaBean
     * @return JSONObject
     */
    public static <T> JSONObject toJSONObject(T object) {
        if (object == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        copy(object.getClass(),jsonObject);

        return jsonObject;
    }

    /**
     * @param list List<JavaBean>
     * @return JSONArray
     */
    public static <T> JSONArray toJSONArray(List<T> list) {
        if (list == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (T object : list)
        {
            jsonObject = new JSONObject();
            copy(object.getClass(),jsonObject);
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    private static <T> void copy(Class<T> clazz,JSONObject jsonObject)
    {
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            copy(clazz.getSuperclass(),jsonObject);
        }
        copy0(clazz,jsonObject);
    }

    private static <T> void copy0(Class<T> clazz,JSONObject jsonObject)
    {
        String firstLetter;
        String methodName;
        Method method;
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().startsWith("is")) {
                    methodName = field.getName();
                } else {
                    firstLetter = field.getName().substring(0, 1).toUpperCase();
                    methodName = String.format("get%s%s", firstLetter, field.getName().substring(1));
                }
                method = getDeclaredMethod(clazz.getClass(),methodName,field.getType());
                if (method != null) {
                    jsonObject.putOpt(field.getName(),method.invoke(clazz));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Method method = getDeclaredMethod0(clazz,methodName,parameterTypes);
        return method;
    }

    private static Method getDeclaredMethod0(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            if (method == null) {
                if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                    method = getDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        Field field = getDeclaredField0(clazz, fieldName);
//        if (field == null) {
//            field = getDeclaredField0(clazz, "_" + fieldName);
//        }
//        if (field == null) {
//            field = getDeclaredField0(clazz, "m_" + fieldName);
//        }
        return field;

    }

    private static Field getDeclaredField0(Class<?> clazz, String fieldName)
    {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            if (field == null) {
                if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                    field = getDeclaredField(clazz.getSuperclass(),fieldName);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }


    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = getField0(clazz, fieldName);
        if (field == null) {
            field = getField0(clazz, "_" + fieldName);
        }
        if (field == null) {
            field = getField0(clazz, "m_" + fieldName);
        }
        return field;
    }

    private static Field getField0(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            return getField((Class<?>) clazz.getSuperclass(), (String) fieldName);
        }

        return null;
    }
}
