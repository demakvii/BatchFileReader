package com.mak.batch.file.reader;

/**
 * @author mayur.kalekar
 *
 */
@ObjectScanning
public class BusRoute {

	@Override
	public String toString() {
		return "BusRoute [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", startLocation="
				+ startLocation + ", endLocation=" + endLocation + "]";
	}

	private long id;

	private String startTime;

	private String endTime;

	private String startLocation;

	private String endLocation;

	public long getId() {
		return id;
	}

	@ObjectMapping(position = -1)
	public void setId(long id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	@ObjectMapping(position = 0)
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	@ObjectMapping(position = 2)
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartLocation() {
		return startLocation;
	}

	@ObjectMapping(position = 1)
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	@ObjectMapping(position = 3)
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
}
