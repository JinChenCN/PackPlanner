package Controller;


import java.io.File;

import Service.PackService;

public class PackController {
	public static void main(String[] args) {
		String testFilePath = System.getProperty("user.dir") + "/tests";
		File file = new File(testFilePath);
		if (!file.exists()) {
			System.out.print("Can't find: " + testFilePath);
			return;
		}
		if (!file.isDirectory()) {
			System.out.print(testFilePath + " isn't directory");
			return;
		}
		File[] files = file.listFiles();
		for (File subFile : files) {
			if (subFile.isFile()) {
				System.out.println("Input: " + subFile.getAbsolutePath());
				PackService service = new PackService();
				service.process(subFile);				
			}
		}
	}
}
