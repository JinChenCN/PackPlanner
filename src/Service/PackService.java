package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import model.Item;
import model.Pack;

public class PackService {
	private List<Item> inputItems;
	private Stack<Item> tempItems;
	private PackConfig config;
	private List<Pack> packs;
	private static int id = 1;

	public void process(File file) {
		inputItems = new ArrayList<Item>();
		config = new PackConfig();
		if (init(file) && inputItems.size() > 0) {
			if (!"NATURAL".equals(config.getSortOrder())) {
				ListSortUtil.sort(inputItems, config.getSortOrder());
			}
			storeInStack();
			packs = new ArrayList<Pack>();
			handlePack(new Pack(id++, config.getMaxPieces(),
					config.getMaxWeight()));
		}
		Iterator<Item> iterator = inputItems.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			System.out.println(item.getLength());
		}
	}

	private void storeInStack() {
		tempItems = new Stack<Item>();
		for (int i = inputItems.size() - 1; i > 0; i--) {
			Item item = inputItems.get(i);
			tempItems.push(item);
		}
	}

	private void handlePack(Pack pack) {
		while (!tempItems.isEmpty()) {
			Item item = tempItems.pop();
			handlePack(item, pack);
			if (item.getQuantity() > 0) {
				packs.add(pack);
				tempItems.push(item);
				handlePack(new Pack(id++, config.getMaxPieces(),
						config.getMaxWeight()));
			} else {
				if (pack.isFull()) {
					packs.add(pack);
					handlePack(new Pack(id++, config.getMaxPieces(),
							config.getMaxWeight()));
				} else {
					handlePack(tempItems.pop(), pack);
				}
			}
		}
	}

	private void handlePack(Item item, Pack pack) {
		if (pack.getAvailablePieces() >= item.getQuantity()) {
			if (pack.getAvailableWeight() >= item.getQuantity()
					* item.getWeight()) {
				pack.getItems().add(item);
				pack.setAvailablePieces(pack.getAvailablePieces()
						- item.getQuantity());
				pack.setAvailableWeight(pack.getAvailableWeight()
						- item.getQuantity() * item.getWeight());
				if (pack.getAvailablePieces() == 0
						|| pack.getAvailableWeight() == 0.0) {
					pack.setFull(true);
				}
				item.setQuantity(0);
			} else {
				int tempQuantity = (int) (pack.getAvailableWeight() / item
						.getWeight());
				if (tempQuantity >= 1) {
					Item tempItem = new Item();
					tempItem.setId(item.getId());
					tempItem.setLength(item.getLength());
					tempItem.setQuantity(tempQuantity);
					tempItem.setWeight(item.getWeight());
					pack.getItems().add(tempItem);
					pack.setAvailablePieces(pack.getAvailablePieces()
							- tempItem.getQuantity());
					pack.setAvailableWeight(pack.getAvailableWeight()
							- tempItem.getQuantity() * tempItem.getWeight());
					if (pack.getAvailablePieces() == 0
							|| pack.getAvailableWeight() == 0.0) {
						pack.setFull(true);
					}
					item.setQuantity(item.getQuantity()
							- tempItem.getQuantity());
				}
			}
		} else {
			Item tempItem = new Item();
			tempItem.setId(item.getId());
			tempItem.setLength(item.getLength());
			tempItem.setQuantity(item.getQuantity() - pack.getAvailablePieces());
			tempItem.setWeight(item.getWeight());
			item.setQuantity(pack.getAvailablePieces());
			handlePack(item, pack);
			if (item.getQuantity() > 0) {
				item.setQuantity(item.getQuantity() + tempItem.getQuantity());
			}
		}
	}

	private boolean checkPackSetting(String[] input) {
		String maxPieces = input[1].trim();
		try {
			int tempPieces = Integer.parseInt(maxPieces);
			if (tempPieces < 1) {
				System.out.println("Wrong config of Pack: " + maxPieces);
				System.out
						.println("[max pieces per pack] should be larger than 0");
				return false;
			} else {
				config.setMaxPieces(tempPieces);
			}
		} catch (Exception e) {
			System.out.println("Wrong config of Pack: " + maxPieces);
			System.out.println("[max pieces per pack] should be int type");
			return false;
		}
		String maxWeight = input[2].trim();
		try {
			float tempWeight = Float.parseFloat(maxWeight);
			if (tempWeight <= 0) {
				System.out.println("Wrong config of Pack: " + maxPieces);
				System.out
						.println("[max weight per pack] should be larger than 0.0");
				return false;
			} else {
				config.setMaxWeight(tempWeight);
			}
		} catch (Exception e) {
			System.out.println("Wrong config of Pack: " + maxWeight);
			System.out.println("[max weight per pack] should be float type");
			return false;
		}
		return true;
	}

	private boolean checkItemSetting(BufferedReader br) throws Exception {
		String line = "";
		String splitBy = ",";
		while ((line = br.readLine()) != null) {
			if ("".equals(line.trim())) {
				System.out.println("Finish reading the input file");
				return true;
			}
			String[] itemInput = line.split(splitBy);
			if (itemInput.length != 4) {
				System.out.println("Wrong config of Item: " + line);
				System.out.println("Please config item like this:");
				System.out
						.println("[item id],[item length],[item quantity],[piece weight]");
				return false;
			} else {
				Item item = new Item();
				String itemId = itemInput[0].trim();
				try {
					item.setId(Integer.parseInt(itemId));
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemId);
					System.out.println("[item id] should be int type");
					return false;
				}
				String itemLength = itemInput[1].trim();
				try {
					item.setLength(Float.parseFloat(itemLength));
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemLength);
					System.out.println("[item length] should be float type");
					return false;
				}
				String itemQuantity = itemInput[2].trim();
				try {
					item.setQuantity(Integer.parseInt(itemQuantity));
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemQuantity);
					System.out.println("[item quantity] should be int type");
					return false;
				}
				String pieceWeight = itemInput[3].trim();
				try {
					item.setWeight(Float.parseFloat(pieceWeight));
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + pieceWeight);
					System.out.println("[item weight] should be float type");
					return false;
				}
				inputItems.add(item);
			}
		}
		return true;
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
						if (!checkPackSetting(input)) {
							return false;
						}
						return checkItemSetting(br);
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
