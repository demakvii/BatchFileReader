package com.mak.batch.file.reader;

import java.util.HashMap;

/*
 * {} - object/custom object(| separator internally)
 * <> - list
 * || - starter 
 * || - ender
 *  | - separator	
 */

public class AutoDetectionHelper implements IAutoDetectionConstant {

	private static HashMap<String, String> separatorMap;

	static {
		separatorMap = new HashMap<String, String>();
		separatorMap.put(openCurlyBracket, closeCurlyBracket);
		separatorMap.put(openAngleBracket, closeAngleBracket);
	}

	public static String someFuncToReturnStringBetweenBrackets(String objInfo, String openBracket) {
		int startLocationToSplit = 0;
		int endLocationToSplit = 0;
		int countOpenBracket = 0;
		int countClosedBracket = 0;
		String closedBracket = separatorMap.get(openBracket);
		boolean firstOpenBracketFound = false;
		for (int i = 0; i < objInfo.length(); i++) {
			String currChar = String.valueOf(objInfo.charAt(i));
			if (currChar.equals(openBracket)) {
				if (!firstOpenBracketFound) {
					startLocationToSplit = i;
					firstOpenBracketFound = true;
				}
				countOpenBracket++;
			}
			else if (currChar.equals(closedBracket)) {
				countClosedBracket++;
				if (countClosedBracket == countOpenBracket) {
					endLocationToSplit = i;
					break;
				}
			}
		}
		return objInfo.substring(startLocationToSplit + 1, endLocationToSplit);
	}

	public static void main(String args[]) {
		String xml = "||{lodhivali|1:23pm}|<{lodhivali|1:23pm}|<{lodhivali|1:23pm}|{lodhivali|1:23pm}>|{lodhivali|1:23pm}>{lodhivali|1:32}||";
		System.out.println(someFuncToReturnStringBetweenBrackets(xml, "<"));
	}
}
