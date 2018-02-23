package com.mak.batch.file.reader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author mayur.kalekar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectScanning {
	/**
	 * @return
	 */
	public boolean isScanning() default true;
}
