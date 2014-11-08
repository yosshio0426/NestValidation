package com.sample.nestvalidation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActionFormを入れ子にしてバリデーションする場合に
 * 子フィールドが配列またはListの時、プロパティにつけてください<br/>
 *  valueに指定したクラスと実クラスの型が違う場合の動作は検証していません。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NestArray {
	
	/** 入れ子にしたフォームのクラス */
	Class<?> value();

}
