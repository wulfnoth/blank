package org.wulfnoth.blank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WBlankFactory {

	private WBlankFactory() {}

	private static BufferedWriter initWriter() {
		try {
			return new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("/home/cloud/WBlank/log.txt", true)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final static BufferedWriter writer = initWriter();

	private final static SimpleDateFormat dateFormat =
			new SimpleDateFormat("HH:mm:ss");
//			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static class DefaultWBlank extends WBlank {

		private String prefix;
		DefaultWBlank(Class clazz) {
			prefix = clazz.getSimpleName();
		}

		@Override
		public void info(String msg) {
			synchronized (writer) {
				StringBuilder sb = new StringBuilder();
				sb.append(dateFormat.format(System.currentTimeMillis()))
						.append(" [").append(Thread.currentThread().getName())
						.append("] ")
						.append(prefix)
						.append(": ")
						.append(msg)
						.append("\n");
				try {
					writer.write(sb.toString());
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static Map<Class, WBlank> cache = new ConcurrentHashMap<>();

	private static boolean initialled = false;

	public static WBlank getWBlank(Class clazz) {
		if (!initialled) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					assert writer != null;
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
			initialled = true;
		}
		return cache.computeIfAbsent(clazz, o -> new DefaultWBlank(clazz));
	}

}
