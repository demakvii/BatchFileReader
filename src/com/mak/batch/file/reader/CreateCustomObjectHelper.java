package com.mak.batch.file.reader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author mayur.kalekar
 *
 */
public class CreateCustomObjectHelper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Object> Object makeCustomObject(T unknownObj, Class<?> aClass, Integer startIndex)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean isMapTypeObj = false;
		HashMap mapOfObj = null;
		if (unknownObj instanceof HashMap) {
			isMapTypeObj = true;
			mapOfObj = new HashMap((HashMap) unknownObj);

		}

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
							if (endLocation > -1 || (endLocation == -1 && isMapTypeObj)) {
								List<T> listObj = new ArrayList<T>();
								int mockPosition = 0;
								if (isMapTypeObj) {
									endLocation = ((HashMap) mapOfObj.get(position)).size() - 1;
									mockPosition = position;
									position = 0;
								}
								for (int i = position; i <= endLocation;) {
									T innerListObj = null;
									Class<?> customClass = annotation.className();
									if (isMapTypeObj) {
										listObj.add((T) makeCustomObject(
												(T) ((List<?>) ((HashMap) mapOfObj.get(mockPosition)).get(i)),
												customClass, null));
										i++;
									}
									else {
										if (isCustomObject) {
											innerListObj = (T) makeCustomObject(unknownObj, customClass, i);
											i += internalObjectCountMappedWithLocationAnnotation(customClass);
										}
										else {
											innerListObj = (T) makeCustomObject(unknownObj, customClass, null);
											i++;
										}
										listObj.add(innerListObj);
									}

								}
								method.invoke(obj, listObj);
							}
						}
						else if (isCustomObject) {
							Class<?> customClass = annotation.className();
							if (isMapTypeObj)
								method.invoke(obj,
										makeCustomObject((T) ((List<?>) mapOfObj.get(position)), customClass, null));
							else
								method.invoke(obj, makeCustomObject(unknownObj, customClass, position));
						}
						else {
							if (isMapTypeObj)
								method.invoke(obj, mapOfObj.get(position));
							else {
								List<?> list = (List<?>) unknownObj;
								method.invoke(obj, list.get(position));
							}
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
			if (method.isAnnotationPresent(ObjectMapping.class)
					&& method.getAnnotation(ObjectMapping.class).position() > -1) {
				count++;
			}
		}
		return count;
	}
}
