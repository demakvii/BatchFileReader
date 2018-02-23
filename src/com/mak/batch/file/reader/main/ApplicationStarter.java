package com.mak.batch.file.reader.main;

import java.io.IOException;
import java.util.List;

import com.mak.batch.file.reader.BatchFileReaderEngine;
import com.mak.batch.file.reader.BusRoute;
import com.mak.batch.file.reader.CustomBusRoute;

public class ApplicationStarter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		try {
			List<BusRoute> list = (List<BusRoute>) BatchFileReaderEngine.build("batch.txt", BusRoute.class, "||", "||",
					"|");
			/*
			 * for (BusRoute busRoute : list) { System.out.println(busRoute); }
			 */
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			List<CustomBusRoute> list = (List<CustomBusRoute>) BatchFileReaderEngine.build("custombatch.txt",
					CustomBusRoute.class, "||", "||", "|");

			/*
			 * for (CustomBusRoute busRoute : list) {
			 * System.out.println(busRoute); }
			 */

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
