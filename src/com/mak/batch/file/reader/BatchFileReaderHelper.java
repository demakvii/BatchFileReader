package com.mak.batch.file.reader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author mayur.kalekar
 *
 */
public class BatchFileReaderHelper implements IAutoDetectionConstant {

	/**
	 * @param str
	 * @param startTag
	 * @param endTag
	 * @return
	 * @throws Exception
	 */
	public static String readStringBetweenStartAndEndTags(String str, String startTag, String endTag) throws Exception {
		if (str.length() == 0)
			return "";
		if (startTag.length() == 0)
			return "";
		if (endTag.length() == 0)
			return "";
		int i = str.indexOf(startTag);
		if (i < 0)
			throw new Exception("failed to find start tag: " + startTag + " in the message: " + str);
		str = str.substring(i + startTag.length(), str.length());
		i = str.indexOf(endTag);
		if (i < 0)
			throw new Exception("failed to find start tag: " + endTag + " in the message: " + str);
		str = str.substring(0, i);

		return str;
	}

	/**
	 * @param str
	 * @param separator
	 * @return
	 */
	private static List<String> splitString(String str, String separator) {
		List<String> str1 = new ArrayList<String>();
		for (String str2 : str.split(Pattern.quote(separator))) {
			str1.add(str2);
		}
		return str1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HashMap splitStringByStandardFormat(String str) {
		HashMap mapOfObject = new HashMap();
		int position = 0;
		for (int i = 0; i < str.length(); i++) {
			String currStr = String.valueOf(str.charAt(i));
			if (objectTypePresent(currStr) != -1) {
				String returnStr = AutoDetectionHelper.someFuncToReturnStringBetweenBrackets(str.substring(i), currStr);
				if (objectTypePresent(currStr) == normalObject) {
					mapOfObject.put(position, splitString(returnStr, separator));
				}
				else if (objectTypePresent(currStr) == listObject) {
					mapOfObject.put(position, splitStringByStandardFormat(returnStr));
				}
				position++;
				i = i + 1 + returnStr.length() + 1;
			}
		}

		return mapOfObject;
	}

	private static int objectTypePresent(String strType) {
		if (strType.equals(openAngleBracket))
			return listObject;
		if (strType.equals(openCurlyBracket))
			return normalObject;
		return -1;
	}

	/**
	 * @param classObj
	 * @param annotationType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAnnotationPresentOnClass(Class<?> classObj,
			@SuppressWarnings("rawtypes") Class annotationType) {
		if (classObj.getAnnotationsByType(annotationType) != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param classObj
	 * @param objSpec
	 * @param separator
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Object createObjectFromString(Class<T> classObj, String objSpec, String separator,
			boolean autoModeEnabled) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (isAnnotationPresentOnClass(classObj, ObjectScanning.class)) {

			if (autoModeEnabled) {
				return CreateCustomObjectHelper.makeCustomObject(splitStringByStandardFormat(objSpec), classObj, null);
			}
			else {
				return CreateCustomObjectHelper.makeCustomObject(splitString(objSpec, separator), classObj, null);
			}
		}
		return null;
	}
}
