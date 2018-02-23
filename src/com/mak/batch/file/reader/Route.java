package com.mak.batch.file.reader;

@ObjectScanning
public class Route {

	@Override
	public String toString() {
		return "Route [id=" + id + ", startTime=" + startTime + ", startLocation=" + startLocation + "]";
	}

	private long id;

	private String startTime;

	private String startLocation;

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

	public String getStartLocation() {
		return startLocation;
	}

	@ObjectMapping(position = 1)
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}
}
