package com.mak.batch.file.reader;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MultiCoreProcessingHelper extends RecursiveTask<List<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3003764312946687197L;

	private int SPLIT_THRESHOLD = 100;

	private List<String> list;

	private Class<?> classMapping;

	private String startTag;

	private String endTag;

	private String separator;

	private boolean autoModeEnabled;

	public MultiCoreProcessingHelper(List<String> list, int threshold, Class<?> classMapping, String startTag,
			String endTag, String separator, boolean autoModeEnabled) {
		this.list = list;
		this.SPLIT_THRESHOLD = threshold;
		this.classMapping = classMapping;
		this.startTag = startTag;
		this.endTag = endTag;
		this.separator = separator;
		this.autoModeEnabled = autoModeEnabled;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List compute() {
		if (this.list.size() < this.SPLIT_THRESHOLD) {
			return computeListDirectly(this.classMapping, this.startTag, this.endTag, this.separator);
		}
		else {
			int half = this.list.size() / this.SPLIT_THRESHOLD;
			MultiCoreProcessingHelper firstSubTask = new MultiCoreProcessingHelper(this.list.subList(0, half),
					this.SPLIT_THRESHOLD, this.classMapping, this.startTag, this.endTag, this.separator,
					this.autoModeEnabled);
			MultiCoreProcessingHelper secondSubTask = new MultiCoreProcessingHelper(this.list.subList(half,
					this.list.size()), this.SPLIT_THRESHOLD, this.classMapping, this.startTag, this.endTag,
					this.separator, this.autoModeEnabled);
			firstSubTask.fork();

			List subList2 = secondSubTask.compute();
			List subList1 = firstSubTask.join();
			subList2.addAll(subList1);

			return subList2;
		}
	}

	private List<?> computeListDirectly(Class<?> classMapping, String startTag, String endTag, String separator) {

		return BatchFileReaderEngine.buildDelegator(this.list, classMapping, startTag, endTag, separator,
				this.autoModeEnabled);

	}

}
