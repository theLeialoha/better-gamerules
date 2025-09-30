package dev.leialoha.bettergamerules.configs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
