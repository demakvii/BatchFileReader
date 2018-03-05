package com.mak.batch.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author mayur.kalekar
 * 
 */
public class BatchFileReaderEngine implements IAutoDetectionConstant {

	private static BatchFileReaderEngine engine;

	private boolean enableMultiCoreMode;

	public boolean isEnableMultiCoreMode() {
		return enableMultiCoreMode;
	}

	public boolean isEnableAutoDetectionMode() {
		return enableAutoDetectionMode;
	}

	private boolean enableAutoDetectionMode;

	private int processingUnit;

	public static BatchFileReaderEngine setupEngine(boolean enableMultiCoreMode, boolean enableAutoDetectionMode,
			int processingUnit) {
		engine = getInstance();
		engine.enableAutoDetectionMode = enableAutoDetectionMode;
		engine.enableMultiCoreMode = enableMultiCoreMode;
		engine.processingUnit = processingUnit;
		return engine;
	}

	public static BatchFileReaderEngine getInstance() {
		if (engine == null) {
			engine = new BatchFileReaderEngine();
			engine.enableAutoDetectionMode = false;
			engine.enableMultiCoreMode = false;
		}

		return engine;
	}

	public static List<?> build(String filePath, Class<?> classMapping) throws IOException, BatchFileReaderException {
		if (engine.enableAutoDetectionMode)
			return build(filePath, classMapping, delimiter, delimiter, separator);
		else
			throw new BatchFileReaderException("Wrong method in used!");
	}

	@SuppressWarnings({ "rawtypes" })
	public static List<?> build(String filePath, Class<?> classMapping, String startTag, String endTag, String separator)
			throws IOException, BatchFileReaderException {
		engine = getInstance();
		if (engine != null) {
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
			if (engine.enableMultiCoreMode) {
				MultiCoreProcessingHelper helper = new MultiCoreProcessingHelper(list, engine.processingUnit,
						classMapping, startTag, endTag, separator, engine.enableAutoDetectionMode);
				ForkJoinPool pool = ForkJoinPool.commonPool();
				customObjList = pool.invoke(helper);
			}
			else {
				customObjList = buildDelegator(list, classMapping, startTag, endTag, separator,
						engine.enableAutoDetectionMode);
			}
			long endTime = System.currentTimeMillis();
			bufferedReader.close();
			fileStream.close();

			System.out.println("Batching finished in " + (endTime - startTime) + " msec");

			return customObjList;
		}
		else {
			throw new BatchFileReaderException("Engine instance not initalized");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> buildDelegator(List<String> list, Class<?> classMapping, String startTag, String endTag,
			String separator, boolean autoModeEnabled) {
		List customObjList = new ArrayList();
		for (String fileInput : list) {
			try {
				customObjList.add(BatchFileReaderHelper.createObjectFromString(classMapping,
						BatchFileReaderHelper.readStringBetweenStartAndEndTags(fileInput, startTag, endTag), separator,
						autoModeEnabled));
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return customObjList;
	}
}
