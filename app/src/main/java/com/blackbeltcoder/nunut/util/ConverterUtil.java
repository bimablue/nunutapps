package com.blackbeltcoder.nunut.util;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by ainozenbook on 9/20/2016.
 */
public class ConverterUtil {

    @SuppressWarnings("rawtypes")
    public static ContentValues ObjecttoContentValue(Object obj) {
        ContentValues cv = new ContentValues();
        Class objectClass = obj.getClass();
        Field[] listField = objectClass.getFields();
        for (int x = 0; x < listField.length; x++) {
            Field fld = listField[x];
            String fldName = fld.getName();

            if(!fldName.equals("_id")) {
                try {
                    /*if (fld.getType() == String[].class) {
                        String[] cbVal = String[].class.cast(fld.get(obj));
                        if (cbVal != null) {
                            JSONArray jsonArr = new JSONArray();
                            for (int loop = 0; loop < cbVal.length; loop++)
                                jsonArr.put(cbVal[loop]);
                            cv.put(fldName, jsonArr.toString());
                        }
                    }
                    if (fld.getType() == List.class) {
                        List list = (List) fld.get(obj);
                        if (list != null) {
                            JSONArray jsonArr = new JSONArray();
                            for (Object temp : list) {
                                jsonArr.put(ObjecttoJSON(temp));
                            }
                            cv.put(fldName, jsonArr.toString());
                        }
                    } else*/
                    if (fld.getType() == String.class)
                        cv.put(fldName, (String) fld.get(obj));
                    else if (fld.getType() == Long.class)
                        cv.put(fldName, (Long) fld.get(obj));
                    else if (fld.getType() == Boolean.class)
                        cv.put(fldName, (Boolean) fld.get(obj));
                    else if (fld.getType() == Byte.class)
                        cv.put(fldName, (Byte) fld.get(obj));
                    else if (fld.getType() == Double.class)
                        cv.put(fldName, (Double) fld.get(obj));
                    else if (fld.getType() == Float.class)
                        cv.put(fldName, (Float) fld.get(obj));
                    else if (fld.getType() == Integer.class)
                        cv.put(fldName, (Integer) fld.get(obj));
                    else if (fld.getType() == Short.class)
                        cv.put(fldName, (Short) fld.get(obj));
                    else if (fld.getType() == Date.class) {
                        Date d = (Date) fld.get(obj);
                        cv.put(fldName, d.getTime());
                    } else if (fld.getType() == long.class)
                        cv.put(fldName, fld.getLong(obj));
                    else if (fld.getType() == boolean.class)
                        cv.put(fldName, fld.getBoolean(obj));
                    else if (fld.getType() == byte.class)
                        cv.put(fldName, fld.getByte(obj));
                    else if (fld.getType() == double.class)
                        cv.put(fldName, fld.getDouble(obj));
                    else if (fld.getType() == float.class)
                        cv.put(fldName, fld.getFloat(obj));
                    else if (fld.getType() == int.class)
                        cv.put(fldName, fld.getInt(obj));
                    else if (fld.getType() == short.class)
                        cv.put(fldName, fld.getShort(obj));
                } catch (Exception e) {
                }
            }
        }

        return cv;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cursorToObject(Class<T> objectClass, Cursor cursor) {
        try {
            Object obj = objectClass.newInstance();
            Field[] listField = objectClass.getFields();

            for (int x = 0; x < listField.length; x++) {
                Field fld = listField[x];
                String fldName = fld.getName();
                try {
                    int colIndex = cursor.getColumnIndex(fldName);
                    if (colIndex >= 0 && !cursor.isNull(colIndex)) {
                        if (fld.getType() == String.class) {
                            fld.set(obj, cursor.getString(colIndex));
                        /*} else if (fld.getType() == String[].class) {
                            if (cursor.getString(colIndex) != null) {
                                JSONArray jarr = new JSONArray(cursor.getString(colIndex));
                                if (jarr != null) {
                                    String[] cbVal = new String[jarr.length()];
                                    for (int loop = 0; loop < cbVal.length; loop++)
                                        cbVal[loop] = jarr.getString(loop);
                                    fld.set(obj, cbVal);
                                }
                            }
                        } else if (fld.getType() == List.class) {
                            if (cursor.getString(colIndex) != null) {
                                JSONArray jarr = new JSONArray(cursor.getString(colIndex));
                                if (jarr != null) {
                                    ParameterizedType stringListType = (ParameterizedType) fld.getGenericType();
                                    fld.set(obj, JSONToList((Class<?>) stringListType.getActualTypeArguments()[0], jarr));
                                }
                            }
                        }*/
                        } else if (fld.getType() == Long.class || fld.getType() == long.class) {
                            fld.set(obj, cursor.getLong(colIndex));
                        } else if (fld.getType() == Double.class || fld.getType() == double.class) {
                            fld.set(obj, cursor.getDouble(colIndex));
                        } else if (fld.getType() == Float.class || fld.getType() == float.class) {
                            fld.set(obj, cursor.getFloat(colIndex));
                        } else if (fld.getType() == Integer.class || fld.getType() == int.class) {
                            fld.set(obj, cursor.getInt(colIndex));
                        } else if (fld.getType() == Byte.class || fld.getType() == byte.class) {
                            fld.set(obj, (byte) cursor.getInt(colIndex));
                        } else if (fld.getType() == Short.class || fld.getType() == short.class) {
                            fld.set(obj, (short) cursor.getInt(colIndex));
                        } else if (fld.getType() == Boolean.class || fld.getType() == boolean.class) {
                            if (cursor.getInt(colIndex) == 0)
                                fld.set(obj, false);
                            else
                                fld.set(obj, true);
                        } else if (fld.getType() == Date.class) {
                            long t = cursor.getLong(colIndex);
                            if (t == 0)
                                fld.set(obj, null);
                            else
                                fld.set(obj, new Date(t));
                        }
                    }
                } catch (Exception e) {
                }
            }
            return (T) obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static JSONObject ObjecttoJSON(Object obj) {
        JSONObject json = new JSONObject();
        Class objectClass = obj.getClass();
        Field[] listField = objectClass.getDeclaredFields();
        for (int x = 0; x < listField.length; x++) {
            Field fld = listField[x];
            fld.setAccessible(true);
            String fldtypeName = fld.getType().getName();
            String fldName = fld.getName();
            try {
                if (fldtypeName.compareTo("java.util.Date") == 0) {
                    Date d = (Date) fld.get(obj);
                    if (d != null)
                        json.put(fldName, d.getTime());
                } /*else if (fldtypeName.compareTo("java.util.List") == 0) {
                    json.put(fldName, listObjectClass2JSONArray((List) fld.get(obj)));
                }*/ else if (fldtypeName.contains("java.lang")) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("long") == 0) {
                    json.put(fldName, fld.getLong(obj));
                } else if (fldtypeName.compareTo("boolean") == 0) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("byte") == 0) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("double") == 0) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("float") == 0) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("int") == 0) {
                    json.put(fldName, fld.get(obj));
                } else if (fldtypeName.compareTo("short") == 0) {
                    json.put(fldName, fld.get(obj));
                }
            } catch (Exception e) {

            }
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    public static <T> T JSONtoObject(Class<T> objectClass, JSONObject jsondata) {
        try {
            Object obj = objectClass.newInstance();
            Field[] listField = objectClass.getFields();
            for (int x = 0; x < listField.length; x++) {
                Field fld = listField[x];
                String fldtypeName = fld.getType().getName();
                String fldName = fld.getName();
                try {
                    /*if (fldtypeName.compareTo("java.util.List") == 0) {
                        ParameterizedType stringListType = (ParameterizedType) fld.getGenericType();
                        fld.set(obj, JSONToList((Class<?>) stringListType.getActualTypeArguments()[0], jsondata.getJSONArray(fldName)));
                    } else if (fld.getType() == String[].class) {
                        if (jsondata.has(fldName)) {
                            JSONArray jarr = jsondata.getJSONArray(fldName);
                            if (jarr != null) {
                                String[] cbVal = new String[jarr.length()];
                                for (int loop = 0; loop < cbVal.length; loop++)
                                    cbVal[loop] = jarr.getString(loop);
                                fld.set(obj, cbVal);
                            }
                        }
                    } else*/ if (fld.getType() == String.class) {
                        fld.set(obj, jsondata.getString(fldName));
                    } else if (fld.getType() == Long.class || fld.getType() == long.class) {
                        fld.set(obj, jsondata.getLong(fldName));
                    } else if (fld.getType() == Boolean.class || fld.getType() == boolean.class) {
                        fld.set(obj, jsondata.getBoolean(fldName));
                    } else if (fld.getType() == Byte.class || fld.getType() == byte.class) {
                        fld.set(obj, Integer.valueOf(jsondata.getInt(fldName)).byteValue());
                    } else if (fld.getType() == Double.class || fld.getType() == double.class) {
                        fld.set(obj, jsondata.getDouble(fldName));
                    } else if (fld.getType() == Float.class || fld.getType() == float.class) {
                        fld.set(obj, Double.valueOf(jsondata.getDouble(fldName)).floatValue());
                    } else if (fld.getType() == Integer.class || fld.getType() == int.class) {
                        fld.set(obj, jsondata.getInt(fldName));
                    } else if (fld.getType() == Short.class || fld.getType() == short.class) {
                        fld.set(obj, Double.valueOf(jsondata.getInt(fldName)).shortValue());
                    } else if (fldtypeName.compareTo("java.util.Date") == 0) {
                        long t = jsondata.getLong(fldName);
                        fld.set(obj, new Date(t));
                    }
                } catch (Exception e) {
                    // Log.e("CONVERT JSON", fldName + " cannot convert");
                }
            }
            return (T) obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot Instantiation class " + objectClass.getSimpleName());
        }
        return null;
    }
}
