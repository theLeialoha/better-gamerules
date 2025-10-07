package dev.leialoha.configured.exceptions;

import java.lang.annotation.Annotation;

public class MissingAnnotationException extends RuntimeException {

    public MissingAnnotationException(Class<?> clazz, Class<? extends Annotation> annotation) {
        super("Missing required annotation @" + annotation.getSimpleName() + " on class " + clazz.getName());
    }

}
