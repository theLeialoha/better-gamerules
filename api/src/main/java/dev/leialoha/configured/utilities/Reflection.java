package dev.leialoha.configured.utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflection {

    public static boolean isType(Object object, Class<?> type) {
        if (object == null || type == null) return false;

        if (object instanceof Class<?> clazz)
            return type.isAssignableFrom(clazz);
        if (object instanceof Field field)
            return type.isAssignableFrom(field.getType());
        
        return type.isAssignableFrom(object.getClass());
    }

    public static <T> T getType(Object object, Class<T> parent) {
        if (!isType(object, parent)) return null;

        if (object instanceof Field field)
            return getField(field, null, parent);

        return (T) object;
    }

    public static <T> T getField(Field field, Object parent) {
        return (T) getField(field, parent, field.getType());
    }

    public static <T> T getField(Field field, Object parent, Class<T> type) {
        if (field == null) return null;

        boolean isStatic = Modifier.isStatic(field.getModifiers());
        boolean hasParent = parent != null;

        if (!isType(field, type)) return null;
        if (isStatic && hasParent) return null;
        else if (!isStatic && !hasParent) return null;

        try {
            makeAccessible(field);
            return (T) field.get(parent);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static <T extends Annotation> T getAnnotation(Object object, Class<T> clazz) {
        if (!hasAnnotation(object, clazz))
            return null;

        if (object instanceof AnnotatedElement)
            return ((AnnotatedElement) object).getAnnotation(clazz);
        return object.getClass().getAnnotation(clazz);
    }

    public static <T extends Annotation> boolean hasAnnotation(Object object, Class<T> clazz) {
        AnnotatedElement element = object.getClass();

        if (object instanceof AnnotatedElement annotatedElement)
            element = annotatedElement;

        return element.isAnnotationPresent(clazz);
    }

    public static Field getFieldByName(Object object, String name) {
        if (object == null) return null;
        if (name == null || name.isEmpty() || name.isBlank()) return null;

        if (object instanceof Field field)
            if (field.getName().equals(name)) return field;
            else object = field.getDeclaringClass();

        if (!(object instanceof Class<?>))
            object = object.getClass();

        Class<?> clazz = (Class<?>) object;
        
        try {
            Field field = clazz.getField(name);
            // Let's make sure we can access it
            makeAccessible(field);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static void makeAccessible(Field field) {
        if (!field.isAccessible()) field.setAccessible(true);
    }

    public static void throwException(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    public static boolean notNull(Object obj) {
        return obj != null;
    }

}
