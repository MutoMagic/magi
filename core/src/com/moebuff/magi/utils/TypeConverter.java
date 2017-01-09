package com.moebuff.magi.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换辅助工具类
 *
 * @author muto
 */
public class TypeConverter {
    private static final Map<Class, Object> primitiveDefaults;

    static {
        Map<Class, Object> map = new HashMap<>();
        map.put(Boolean.TYPE, Boolean.FALSE);
        map.put(Byte.TYPE, Byte.valueOf((byte) 0));
        map.put(Short.TYPE, Short.valueOf((short) 0));
        map.put(Character.TYPE, new Character((char) 0));
        map.put(Integer.TYPE, Integer.valueOf(0));
        map.put(Long.TYPE, Long.valueOf(0L));
        map.put(Float.TYPE, new Float(0.0f));
        map.put(Double.TYPE, new Double(0.0));
        map.put(BigInteger.class, new BigInteger("0"));
        map.put(BigDecimal.class, new BigDecimal(0.0));
        primitiveDefaults = Collections.unmodifiableMap(map);
    }

    /**
     * Returns the value converted numerically to the given class type
     * <p>
     * This method also detects when arrays are being converted and converts the
     * components of one array to the type of the other.
     *
     * @param value  an object to be converted to the given type
     * @param toType class type to be converted to
     * @return converted value of the type given, or value if the value cannot
     * be converted to the given type.
     */
    public static <T> T parse(Object value, Class<T> toType) {
        Object result = null;
        if (value != null) {
            // If array -> array then convert components of array individually
            if (value.getClass().isArray() && toType.isArray()) {
                Class type = toType.getComponentType();

                int length = Array.getLength(value);
                result = Array.newInstance(type, length);
                for (int i = 0; i < length; i++) {
                    Array.set(result, i, parse(Array.get(value, i), type));
                }
            } else {
                if (toType == Integer.class || toType == Integer.TYPE)
                    result = (int) longValue(value);
                if (toType == Double.class || toType == Double.TYPE)
                    result = doubleValue(value);
                if (toType == Boolean.class || toType == Boolean.TYPE)
                    result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
                if (toType == Byte.class || toType == Byte.TYPE)
                    result = (byte) longValue(value);
                if (toType == Character.class || toType == Character.TYPE)
                    result = (char) longValue(value);
                if (toType == Short.class || toType == Short.TYPE)
                    result = (short) longValue(value);
                if (toType == Long.class || toType == Long.TYPE)
                    result = longValue(value);
                if (toType == Float.class || toType == Float.TYPE)
                    result = new Float(doubleValue(value));
                if (toType == BigInteger.class)
                    result = bigIntValue(value);
                if (toType == BigDecimal.class)
                    result = bigDecValue(value);
                if (toType == String.class)
                    result = stringValue(value);
                if (Enum.class.isAssignableFrom(toType))
                    result = enumValue(toType, value);
            }
        } else {
            if (toType.isPrimitive()) {
                result = primitiveDefaults.get(toType);
            }
        }
        return (T) result;
    }

    /**
     * Evaluates the given object as a boolean: if it is a Boolean object, it's
     * easy; if it's a Number or a Character, returns true for non-zero objects;
     * and otherwise returns true for non-null objects.
     *
     * @param value an object to interpret as a boolean
     * @return the boolean value implied by the given object
     */
    public static boolean booleanValue(Object value) {
        if (value == null)
            return false;
        Class c = value.getClass();
        if (c == Boolean.class)
            return (Boolean) value;
        // if ( c == String.class )
        // return ((String)value).length() > 0;
        if (c == Character.class)
            return (Character) value != 0;
        if (value instanceof Number)
            return ((Number) value).doubleValue() != 0;
        return true; // non-null
    }

    public static Enum<?> enumValue(Class toClass, Object o) {
        Enum<?> result = null;
        if (o == null) {
            result = null;
        } else if (o instanceof String[]) {
            String[] args = (String[]) o;
            result = Enum.valueOf(toClass, args[0]);
        } else if (o instanceof String) {
            result = Enum.valueOf(toClass, (String) o);
        }
        return result;
    }

    /**
     * Evaluates the given object as a long integer.
     *
     * @param value an object to interpret as a long integer
     * @return the long integer value implied by the given object
     * @throws NumberFormatException if the given object can't be understood as a long integer
     */
    public static long longValue(Object value) throws NumberFormatException {
        if (value == null)
            return 0L;
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).longValue();
        if (c == Boolean.class)
            return (Boolean) value ? 1 : 0;
        if (c == Character.class)
            return (Character) value;
        return Long.parseLong(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a double-precision floating-point number.
     *
     * @param value an object to interpret as a double
     * @return the double value implied by the given object
     * @throws NumberFormatException if the given object can't be understood as a double
     */
    public static double doubleValue(Object value) throws NumberFormatException {
        if (value == null)
            return 0.0;
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).doubleValue();
        if (c == Boolean.class)
            return (Boolean) value ? 1 : 0;
        if (c == Character.class)
            return (Character) value;
        String s = stringValue(value, true);

        return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
        /*
         * For 1.1 parseDouble() is not available
         */
        // return Double.valueOf( value.toString() ).doubleValue();
    }

    /**
     * Evaluates the given object as a BigInteger.
     *
     * @param value an object to interpret as a BigInteger
     * @return the BigInteger value implied by the given object
     * @throws NumberFormatException if the given object can't be understood as a BigInteger
     */
    public static BigInteger bigIntValue(Object value)
            throws NumberFormatException {
        if (value == null)
            return BigInteger.valueOf(0L);
        Class c = value.getClass();
        if (c == BigInteger.class)
            return (BigInteger) value;
        if (c == BigDecimal.class)
            return ((BigDecimal) value).toBigInteger();
        if (c.getSuperclass() == Number.class) {
            Number num = (Number) value;
            return BigInteger.valueOf(num.longValue());
        }
        if (c == Boolean.class)
            return BigInteger.valueOf((Boolean) value ? 1 : 0);
        if (c == Character.class) {
            Character chr = (Character) value;
            return BigInteger.valueOf(chr.charValue());
        }
        return new BigInteger(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a BigDecimal.
     *
     * @param value an object to interpret as a BigDecimal
     * @return the BigDecimal value implied by the given object
     * @throws NumberFormatException if the given object can't be understood as a BigDecimal
     */
    public static BigDecimal bigDecValue(Object value)
            throws NumberFormatException {
        if (value == null)
            return BigDecimal.valueOf(0L);
        Class c = value.getClass();
        if (c == BigDecimal.class)
            return (BigDecimal) value;
        if (c == BigInteger.class)
            return new BigDecimal((BigInteger) value);
        if (c.getSuperclass() == Number.class) {
            Number num = (Number) value;
            return new BigDecimal(num.doubleValue());
        }
        if (c == Boolean.class)
            return BigDecimal.valueOf((Boolean) value ? 1 : 0);
        if (c == Character.class) {
            Character chr = (Character) value;
            return BigDecimal.valueOf(chr.charValue());
        }
        return new BigDecimal(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a String and trims it if the trim flag is
     * true.
     *
     * @param value an object to interpret as a String
     * @param trim  trims the result if true
     * @return the String value implied by the given object as returned by the
     * toString() method, or "null" if the object is null.
     */
    public static String stringValue(Object value, boolean trim) {
        String result;

        if (value == null) {
            result = StringKit.NULL_STRING;
        } else {
            result = value.toString();
            if (trim) {
                result = result.trim();
            }
        }
        return result;
    }

    /**
     * Evaluates the given object as a String.
     *
     * @param value an object to interpret as a String
     * @return the String value implied by the given object as returned by the
     * toString() method, or "null" if the object is null.
     */
    public static String stringValue(Object value) {
        return stringValue(value, false);
    }
}
