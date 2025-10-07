package dev.leialoha.configured.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.leialoha.configured.values.EmptyEnum;

@Repeatable(value = ConfigAnnotations.class)
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigAnnotation {

    String key() default "";
    String[] comments() default {};
    Class<? extends Enum<?>> enumType() default EmptyEnum.class;
    
    boolean executeLast() default false;
    boolean includePadding() default true;

}
