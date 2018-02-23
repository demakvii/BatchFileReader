package com.mak.batch.file.reader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mayur.kalekar
 *
 */
public class CreateCustomeObjectHelper {

	/**
	 * @param list
	 * @param aClass
	 * @param startIndex
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T extends Object> Object makeCustomObject(List<?> list, Class<?> aClass, Integer startIndex)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (BatchFileReaderHelper.isAnnotationPresentOnClass(aClass, ObjectScanning.class)) {
			Object obj = aClass.newInstance();
			Method[] methods = aClass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(ObjectMapping.class)) {
					ObjectMapping annotation = method.getAnnotation(ObjectMapping.class);
					int position = annotation.position();
					if (position > -1) {
						if (startIndex != null)
							position += startIndex;
						boolean isCustomObject = annotation.isCustomObject();
						boolean isList = annotation.isList();
						method.setAccessible(true);
						if (isList) {
							int endLocation = annotation.endLocation();
							if (endLocation > -1) {
								List<T> listObj = new ArrayList<T>();
								for (int i = position; i <= endLocation;) {
									T innerListObj = null;
									Class<?> customClass = annotation.className();
									if (isCustomObject) {
										innerListObj = (T) makeCustomObject(list, customClass, i);
										i += internalObjectCountMappedWithLocationAnnotation(customClass);
									}
									else {
										innerListObj = (T) makeCustomObject(list, customClass, null);
										i++;
									}
									listObj.add(innerListObj);
								}
								method.invoke(obj, listObj);
							}
						}
						else if (isCustomObject) {
							Class<?> customClass = annotation.className();
							method.invoke(obj, makeCustomObject(list, customClass, position));
						}
						else {
							method.invoke(obj, list.get(position));
						}
					}
				}
			}
			return obj;
		}
		return null;
	}

	private static int internalObjectCountMappedWithLocationAnnotation(Class<?> aclass) {
		int count = 1;
		Method[] methods = aclass.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(ObjectMapping.class) && method.getAnnotation(ObjectMapping.class).position() > -1) {
				count++;
			}
		}
		return count;
	}
}
