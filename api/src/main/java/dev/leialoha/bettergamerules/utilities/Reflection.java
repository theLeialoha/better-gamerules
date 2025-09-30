package dev.leialoha.bettergamerules.utilities;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflection {

    public static void validateType(Object object, Class<?> parent) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        if (parent == null) throw new IllegalArgumentException("Parent class is null");

        if (object instanceof Class<?> clazz)
            if (!parent.isAssignableFrom(clazz))
                throw new IllegalArgumentException(clazz.getName() + " is not of type " + parent.getName());
        else if (object instanceof Field field) 
            if (!parent.isAssignableFrom(field.getType()))
                throw new IllegalArgumentException(field.getType().getName() + " is not of type " + parent.getName());
        else
            if (!parent.isAssignableFrom(object.getClass()))
                throw new IllegalArgumentException(object.getClass().getName() + " is not of type " + parent.getName());
    }

    public static <T> T getType(Object object, Class<T> parent) throws IllegalArgumentException {
        if (object == null) throw new IllegalArgumentException("Object is null");

        if (object instanceof Field field) {
            return getField(field, null, parent);
        } else {
            validateType(object.getClass(), parent);
            return (T) object;
        }
    }

    public static <T> T getField(Field field, Object parentObj) throws IllegalArgumentException {
        return (T) getField(field, parentObj, field.getType());
    }

    public static <T> T getField(Field field, Object parentObj, Class<T> type) throws IllegalArgumentException {
        if (field == null) throw new IllegalArgumentException("Field is null");
        if (Modifier.isStatic(field.getModifiers()) && parentObj != null)
            throw new IllegalArgumentException("Field " + field.getName() + " in " + field.getDeclaringClass().getSimpleName() + " is static, parent object must be null");
        if (!Modifier.isStatic(field.getModifiers()) && parentObj == null)
            throw new IllegalArgumentException("Field " + field.getName() + " in " + field.getDeclaringClass().getSimpleName() + " is not static, parent object must not be null");

        validateType(field.getType(), type);

        try {
            return (T) field.get(parentObj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalArgumentException("Couldn't get value of field " + field.getName() + " in " + field.getDeclaringClass().getSimpleName(), e);
        }
    }

    public static <T extends Annotation> T getAnnotation(Object object, Class<T> clazz) throws IllegalClassFormatException {
        validateAnnotation(object, clazz);

        if (object instanceof AnnotatedElement)
            return ((AnnotatedElement) object).getAnnotation(clazz);
        return object.getClass().getAnnotation(clazz);
    }

    public static <T extends Annotation> void validateAnnotation(Object object, Class<T> clazz) throws IllegalClassFormatException {
        AnnotatedElement element = object.getClass();
        String name = object.getClass().getName();

        if (object instanceof AnnotatedElement annotatedElement) {
            element = annotatedElement;

            try {
                Class<?> elementClass = element.getClass();
                Field field = getFieldByName(elementClass, "name");
                name = (String) getField(field, element);
            } catch (IllegalArgumentException e) {
                // Ignore, just use the class name
            }
        }

        if (!element.isAnnotationPresent(clazz))
            throw new IllegalClassFormatException(name + " is missing " + clazz + " @interface annotation");
    }

    public static Field getFieldByName(Object object, String fieldName) throws IllegalArgumentException {
        if (object == null) throw new IllegalArgumentException("Object is null");
        if (fieldName == null || fieldName.isBlank()) throw new IllegalArgumentException("Field name is null or blank");
        if (object instanceof Field f && f.getName().equals(fieldName)) return f;
        if (!(object instanceof Class<?>)) return getFieldByName(object.getClass(), fieldName);

        Field field;
        Class<?> clazz = (Class<?>) object;
        
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Couldn't find field " + fieldName + " in " + clazz.getSimpleName(), e);
        }

        // Let's make sure we can access it
        makeAccessible(field);

        return field;
    }

    @SuppressWarnings("deprecation")
    public static void makeAccessible(Field field) {
        if (!field.isAccessible()) field.setAccessible(true);
    }

    public static void throwException(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

}
