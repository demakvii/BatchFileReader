package com.mak.batch.file.reader.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.mak.batch.file.reader.CustomBusRoute;
import com.mak.batch.file.reader.JsonParser;

public class JsonApplicationStarter {

	public static void main(String[] args) {
		try {
			System.out.println(JsonParser.fromJson("jsonbatch.json", CustomBusRoute.class));
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
