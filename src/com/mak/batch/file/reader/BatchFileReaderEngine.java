package com.mak.batch.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mayur.kalekar
 * 
 */
public class BatchFileReaderEngine {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> build(String filePath, Class<?> classMapping, String startTag, String endTag, String separator)
			throws IOException {

		File file = new File(filePath);
		FileReader fileStream = new FileReader(file);
		List customObjList = new ArrayList();
		BufferedReader bufferedReader = new BufferedReader(fileStream);
		String lineRead;
		List<String> list = new ArrayList<String>();
		while ((lineRead = bufferedReader.readLine()) != null) {
			list.add(lineRead);
		}
		long startTime = System.currentTimeMillis();
		for (String fileInput : list) {
			try {
				customObjList
						.add(BatchFileReaderHelper.createObjectFromString(classMapping,
								BatchFileReaderHelper.readStringBetweenStartAndEndTags(fileInput, startTag, endTag),
								separator));
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		bufferedReader.close();
		fileStream.close();

		System.out.println("Batching finished in " + (endTime - startTime) + " msec");

		return customObjList;
	}

}
