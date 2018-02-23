package com.mak.batch.file.reader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author mayur.kalekar
 *
 */
@Deprecated
public interface ICreateCustomObject {

	/**
	 * @param list
	 * @param aClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@Deprecated
	public Object makeCustomObject(List<?> list, Class<?> aClass) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
