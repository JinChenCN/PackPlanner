package Controller;


import java.io.File;

import Service.PackService;

public class PackController {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.print("Please specific input file path!");
			return;
		}
		String filePath = args[0];
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.print("Can't find input file: " + filePath);
			return;
		}
		PackService service = new PackService();
		service.process(file);
		System.out.println("Process finish!");
	}
}
