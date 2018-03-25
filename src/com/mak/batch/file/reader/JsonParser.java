package com.mak.batch.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonParser implements IAutoDetectionConstant {

	public static List fromJson(String path, Class<?> obj) throws IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		File file = new File(path);
		FileReader fileStream = new FileReader(file);
		String lineRead;
		String line = "";
		List mainList = new ArrayList();
		BufferedReader bufferedReader = new BufferedReader(fileStream);
		while ((lineRead = bufferedReader.readLine()) != null) {
			line = line + lineRead;
		}
		String lr = AutoDetectionHelper.someFuncToReturnStringBetweenBrackets(line, openSquareBracket).trim();
		List<String> objList = buildObjectList(lr);
		for (int i = 0; i < objList.size(); i++) {
			mainList.add(buildObjectFromMap(
					(HashMap) buildActualObject(objList.get(i), buildMapAsObject(obj), buildBlueprintOfObject(obj)),
					obj));
		}
		bufferedReader.close();

		return mainList;
	}

	private static <T extends Object> Object buildObjectFromMap(HashMap map, Class aClass)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		T obj = (T) aClass.newInstance();
		Method[] methods = aClass.getDeclaredMethods();
		HashMap bluePrint = buildBlueprintOfObject(aClass);
		for (Method method : methods) {
			String setterName = method.getName().replaceAll("set", "").toLowerCase();
			if (map.containsKey(setterName) && !map.get(setterName).equals("")) {
				Boolean isList = false;
				if (map.get(setterName) instanceof List) {
					isList = true;
				}
				if (bluePrint.containsKey(setterName) && isList) {
					method.setAccessible(true);
					List<HashMap> list = (List) map.get(setterName);
					List result = new ArrayList();
					for (HashMap mapInsideList : list) {
						result.add(buildObjectFromMap(mapInsideList,
								returnClassTypeOfList((ParameterizedType) bluePrint.get(setterName))));
					}
					method.setAccessible(true);
					method.invoke(obj, result);
				}

				else if (bluePrint.containsKey(setterName)
						&& isRecognizableTypeClass((Class) bluePrint.get(setterName))) {
					method.setAccessible(true);
					if (((String) map.get(setterName)).length() > 0) {
						method.invoke(
								obj,
								buildObjectFromClassAndValue((Class) bluePrint.get(setterName),
										(String) map.get(setterName)));
					}

				}
				else {
					// not Basic Data object if not in recognizable list.
					method.setAccessible(true);
					method.invoke(obj,
							buildObjectFromMap((HashMap) map.get(setterName), (Class) bluePrint.get(setterName)));
				}
			}
		}
		return obj;
	}

	private static List<String> buildObjectList(String str) {
		List<String> objList = new ArrayList<String>();
		str = str.trim();
		while (str.length() > 0) {
			String substr = AutoDetectionHelper.someFuncToReturnStringBetweenBrackets(str, openCurlyBracket);
			if (substr != null && substr.length() > 0) {
				str = str.substring(1 + substr.length() + 1 + 1, str.length());
				objList.add(substr.trim());
			}
			else
				break;

		}
		return objList;
	}

	private static Object buildObjectFromClassAndValue(Class aClass, String value) {
		Object obj = null;

		if (aClass.equals(double.class)) {
			obj = new Double(value).doubleValue();
		}
		else if (aClass.equals(Double.class)) {
			obj = new Double(value);
		}
		else if (aClass.equals(Long.class)) {
			obj = new Long(value);
		}
		else if (aClass.equals(long.class)) {
			obj = new Long(value).longValue();
		}
		else if (aClass.equals(String.class)) {
			obj = new String(value);
		}
		else if (aClass.equals(Boolean.class)) {
			obj = new Boolean(value);
		}
		else if (aClass.equals(boolean.class)) {
			obj = new Boolean(value).booleanValue();
		}
		else if (aClass.equals(float.class)) {
			obj = new Float(value).floatValue();
		}
		else if (aClass.equals(Float.class)) {
			obj = new Float(value);
		}

		return obj;
	}

	private static List<Class> recognizableClassList;

	static {
		recognizableClassList = Arrays.asList(double.class, Double.class, Long.class, long.class, String.class,
				Boolean.class, boolean.class, float.class, Float.class);
	}

	private static boolean isRecognizableTypeClass(Class aClass) {
		if (recognizableClassList.contains(aClass))
			return true;
		return false;
	}

	private static HashMap buildBlueprintOfObject(Class aClass) {
		HashMap bluePrintMap = new HashMap();
		for (Field field : aClass.getDeclaredFields()) {
			bluePrintMap.put(field.getName().toLowerCase(), field.getGenericType());

		}
		return bluePrintMap;
	}

	private static HashMap buildMapAsObject(Class aClass) {
		HashMap internalMap = new HashMap();
		for (Field field : aClass.getDeclaredFields()) {
			internalMap.put(field.getName().toLowerCase(), "");

		}
		return internalMap;
	}

	private static Class returnClassTypeOfList(ParameterizedType type) {
		return (Class) type.getActualTypeArguments()[0];
	}

	private static HashMap buildActualObject(String objDetails, HashMap map, HashMap bluePrintMap)
			throws InstantiationException, IllegalAccessException {
		String[] brackets = new String[] { "\"", "{", "[" };
		while (objDetails.length() > 0) {
			String internalProp = AutoDetectionHelper.someFuncToReturnStringBetweenBrackets(objDetails, doubleQuote);
			if (internalProp != null & internalProp.length() > 0) {
				objDetails = objDetails.substring(1 + internalProp.length() + 1 + 1, objDetails.length());
				if (objDetails != null & objDetails.length() > 0) {
					if (map.get(internalProp.toLowerCase()) != null) {
						String bracketToScanFirst = AutoDetectionHelper.detectFirstCharInString(objDetails, brackets);

						String internalObjDetails = AutoDetectionHelper.someFuncToReturnStringBetweenBrackets(
								objDetails, bracketToScanFirst);
						if (bracketToScanFirst.equals(doubleQuote)) {
							map.put(internalProp.toLowerCase(), internalObjDetails);
						}
						else if (bracketToScanFirst.equals(openSquareBracket)) {
							List list = new ArrayList();
							ParameterizedType listType = (ParameterizedType) (bluePrintMap.get(internalProp
									.toLowerCase()));
							List<String> internalInnerObjList = buildObjectList(internalObjDetails);
							for (String internalInnerObjDetails : internalInnerObjList) {
								HashMap internalObjMap = buildMapAsObject(returnClassTypeOfList(listType));
								HashMap internalObjBluePrint = buildBlueprintOfObject(returnClassTypeOfList(listType));
								list.add(buildActualObject(internalInnerObjDetails, internalObjMap,
										internalObjBluePrint));
							}
							map.put(internalProp.toLowerCase(), list);
						}
						else if (bracketToScanFirst.equals(openCurlyBracket)) {
							HashMap internalObjMap = buildMapAsObject((Class) bluePrintMap.get(internalProp
									.toLowerCase()));
							map.put(internalProp.toLowerCase(),
									buildActualObject(internalObjDetails, internalObjMap, bluePrintMap));
						}
						if ((1 + internalObjDetails.length() + 1 + 1) > objDetails.length())
							objDetails = objDetails.substring(1 + internalObjDetails.length() + 1, objDetails.length());
						else
							objDetails = objDetails.substring(1 + internalObjDetails.length() + 1 + 1,
									objDetails.length());

					}
				}
			}
		}
		return map;
	}
}
