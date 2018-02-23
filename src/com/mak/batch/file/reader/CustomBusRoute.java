package com.mak.batch.file.reader;

import java.util.List;

@ObjectScanning
public class CustomBusRoute {

	private Route startRoute;

	private Route endRoute;

	private List<Route> internalRoute;

	public Route getStartRoute() {
		return startRoute;
	}

	@ObjectMapping(position = 0, isCustomObject = true, className = Route.class)
	public void setStartRoute(Route startRoute) {
		this.startRoute = startRoute;
	}

	public Route getEndRoute() {
		return endRoute;
	}

	@ObjectMapping(position = 6, isCustomObject = true, className = Route.class)
	public void setEndRoute(Route endRoute) {
		this.endRoute = endRoute;
	}

	@Override
	public String toString() {
		return "CustomBusRoute [startRoute=" + startRoute + ", endRoute=" + endRoute + ", internalRoute="
				+ internalRoute + "]";
	}

	public List<Route> getInternalRoute() {
		return internalRoute;
	}

	@ObjectMapping(position = 2, isCustomObject = true, isList = true, className = Route.class, endLocation = 5)
	public void setInternalRoute(List<Route> internalRoute) {
		this.internalRoute = internalRoute;
	}

}
