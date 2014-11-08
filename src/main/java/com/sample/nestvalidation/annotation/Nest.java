package com.sample.nestvalidation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActionFormを入れ子にしてバリデーションする場合に
 * 子フィールドのプロパティにつけてください 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Nest {

}
