package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Item;

public class PackService {
	private List<Item> items;
	private PackConfig config;

	public void process(File file) {
		items = new ArrayList<Item>();
		config = new PackConfig();
		if (init(file) && items.size() > 0) {
			if (!"NATURAL".equals(config.getSortOrder())) {
				ListSortUtil.sort(items, config.getSortOrder());
			}
		}
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			System.out.println(item.getLength());
		}
	}

	private boolean init(File file) {
		String line = "";
		String splitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] input = line.split(splitBy);
				String sortOrder = input[0].trim();
				if (!"NATURAL".equalsIgnoreCase(sortOrder)
						&& !"SHORT_TO_LONG".equalsIgnoreCase(sortOrder)
						&& !"LONG_TO_SHORT".equalsIgnoreCase(sortOrder)) {
					continue;
				} else {
					if (input.length != 3) {
						System.out.println("Wrong config of Pack: " + line);
						System.out.println("Please config pack like this:");
						System.out
								.println("[Sort order],[max pieces per pack],[max weight per pack]");
						return false;
					} else {
						config.setSortOrder(sortOrder.toUpperCase());
						String maxPieces = input[1].trim();
						try {
							config.setMaxPieces(Integer.parseInt(maxPieces));
						} catch (Exception e) {
							System.out.println("Wrong config of Pack: "
									+ maxPieces);
							System.out
									.println("[max pieces per pack] should be int type");
							return false;
						}
						String maxWeight = input[2].trim();
						try {
							config.setMaxWeight(Float.parseFloat(maxWeight));
						} catch (Exception e) {
							System.out.println("Wrong config of Pack: "
									+ maxWeight);
							System.out
									.println("[max weight per pack] should be float type");
							return false;
						}
						while ((line = br.readLine()) != null) {
							if ("".equals(line.trim())) {
								System.out
										.println("Finish reading the input file");
								return true;
							}
							String[] itemInput = line.split(splitBy);
							if (itemInput.length != 4) {
								System.out.println("Wrong config of Item: "
										+ line);
								System.out
										.println("Please config item like this:");
								System.out
										.println("[item id],[item length],[item quantity],[piece weight]");
								return false;
							} else {
								Item item = new Item();
								String itemId = itemInput[0].trim();
								try {
									item.setId(Integer.parseInt(itemId));
								} catch (Exception e) {
									System.out.println("Wrong config of Item: "
											+ itemId);
									System.out
											.println("[item id] should be int type");
									return false;
								}
								String itemLength = itemInput[1].trim();
								try {
									item.setLength(Float.parseFloat(itemLength));
								} catch (Exception e) {
									System.out.println("Wrong config of Item: "
											+ itemLength);
									System.out
											.println("[item length] should be float type");
									return false;
								}
								String itemQuantity = itemInput[2].trim();
								try {
									item.setQuantity(Integer
											.parseInt(itemQuantity));
								} catch (Exception e) {
									System.out.println("Wrong config of Item: "
											+ itemQuantity);
									System.out
											.println("[item quantity] should be int type");
									return false;
								}
								String pieceWeight = itemInput[3].trim();
								try {
									item.setWeight(Float
											.parseFloat(pieceWeight));
								} catch (Exception e) {
									System.out.println("Wrong config of Item: "
											+ pieceWeight);
									System.out
											.println("[item weight] should be float type");
									return false;
								}
								items.add(item);
							}
						}
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
