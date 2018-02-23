package com.mak.batch.file.reader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author mayur.kalekar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectMapping {
	/**
	 * @return
	 */
	public int position() default -1;

	public boolean isCustomObject() default false;

	public Class<?> className() default Object.class;

	public int endLocation() default -1;

	public boolean isList() default false;
}
