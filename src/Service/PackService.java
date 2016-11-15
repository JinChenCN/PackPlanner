package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import model.Item;
import model.Pack;

public class PackService {
	private List<Item> inputItems;
	private Stack<Item> tempInputItems;
	private PackConfig config;
	private List<Pack> packs;
	private static int id = 1;
	private double maxItemWeight = 0.0;
	private String splitBy = ",";

	public void process(File file) {
		inputItems = new ArrayList<Item>();
		config = new PackConfig();
		if (!init(file)) {
			return;
		} else {
			if (!config.getSortOrder().equals(SortOrderEnum.NATURAL.getName())) {
				ListSortUtil.sort(inputItems, config.getSortOrder());
			}
			storeInStack();
			if (!tempInputItems.isEmpty()) {
				packs = new ArrayList<Pack>();
				startPacking(tempInputItems.pop(), new Pack(id++, config.getMaxPieces(), config.getMaxWeight()));
			}
			
		}
		Iterator<Pack> iterator = packs.iterator();
		while (iterator.hasNext()) {
			Pack pack = iterator.next();
			System.out.println("Pack Number: " + pack.getId());
			Stack<Item> items = pack.getItems();
			Stack<Item> resultItems = reverseStack(items);
			while (!resultItems.isEmpty()) {
				Item item = resultItems.pop();
				System.out.println(item.getId() + ","
						+ item.getLength() + "," + item.getQuantity() + ","
						+ item.getWeight());
			}
			System.out.println("Pack Length: " + pack.getLength() + ", Pack Weight:" 
					+ new BigDecimal(""+pack.getMaxWeight()).subtract(new BigDecimal(""+pack.getAvailableWeight())));
		}
	}

	private Stack<Item> reverseStack(Stack<Item> items) {
		Stack<Item> resultItems = new Stack<Item>();
		while (!items.isEmpty()) {
			resultItems.push(items.pop());
		}
		return resultItems;
	}
	
	private void storeInStack() {
		tempInputItems = new Stack<Item>();
		for (int i = inputItems.size() - 1; i >= 0; i--) {
			Item item = inputItems.get(i);
			tempInputItems.push(item);
		}
	}
	
	private void pack(Item item, Pack pack, int quantity) {
		Item tempItem = new Item();
		tempItem.setId(item.getId());
		tempItem.setLength(item.getLength());
		tempItem.setQuantity(quantity);
		tempItem.setWeight(item.getWeight());
		pack.getItems().push(tempItem);
		pack.setAvailablePieces(pack.getAvailablePieces() - tempItem.getQuantity());
		pack.setAvailableWeight(new BigDecimal("" + pack.getAvailableWeight())
				.subtract(new BigDecimal("" + tempItem.getQuantity())
				.multiply(new BigDecimal("" + tempItem.getWeight())))
				.doubleValue());
		if (tempItem.getLength() > pack.getLength()) {
			pack.setLength(tempItem.getLength());
		}
	}
	
	private void startPacking(Item item, Pack pack) {
		if (pack.getAvailablePieces() >= item.getQuantity()) {
			int availableQuantity = item.getQuantity();
			if (pack.getAvailableWeight() >= new BigDecimal(""+availableQuantity).multiply(new BigDecimal(""+item.getWeight())).doubleValue()) {
				pack(item, pack, availableQuantity);
				if (pack.getAvailablePieces() == 0) {
					pack.setFull(true);
					packs.add(pack);
					if (!tempInputItems.isEmpty()) {
						startPacking(tempInputItems.pop(), new Pack(id++, config.getMaxPieces(), config.getMaxWeight()));
					}
				} else {
					if (!tempInputItems.isEmpty()) {
						startPacking(tempInputItems.pop(), pack);
					} else {
						packs.add(pack);
					}
				}
			} else {
				availableQuantity = (int) (pack.getAvailableWeight() / item.getWeight());
				if (availableQuantity >= 1) {
					pack(item, pack, availableQuantity);
					item.setQuantity(item.getQuantity() - availableQuantity);
				} 
				pack.setFull(true);
				packs.add(pack);
				startPacking(item, new Pack(id++, config.getMaxPieces(), config.getMaxWeight()));
			}
		} else {
			int availableQuantity = pack.getAvailablePieces();
			if (pack.getAvailableWeight() >= new BigDecimal(""+availableQuantity).multiply(new BigDecimal(""+item.getWeight())).doubleValue()) {
				pack(item, pack, availableQuantity);
				pack.setFull(true);
				packs.add(pack);
				item.setQuantity(item.getQuantity()-availableQuantity);
				startPacking(item, new Pack(id++, config.getMaxPieces(), config.getMaxWeight()));
			} else {
				availableQuantity = (int) (pack.getAvailableWeight() / item.getWeight());
				if (availableQuantity >= 1) {
					pack(item, pack, availableQuantity);
					item.setQuantity(item.getQuantity()-availableQuantity);
				} 
				pack.setFull(true);
				packs.add(pack);
				startPacking(item, new Pack(id++, config.getMaxPieces(), config.getMaxWeight()));
			}
		}
	}

	private boolean checkPackInput(String[] input) {
		String maxPieces = input[1].trim();
		try {
			int tempPieces = Integer.parseInt(maxPieces);
			if (tempPieces < 1) {
				System.out.println("The max items limit in the Pack is inputed as: " + maxPieces);
				System.out.println("[max pieces per pack] should be larger than 0");
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
			double tempWeight = Double.parseDouble(maxWeight);
			if (tempWeight <= 0) {
				System.out.println("Wrong config of Pack: " + maxPieces);
				System.out.println("[max weight per pack] should be larger than 0.0");
				return false;
			} else {
				config.setMaxWeight(tempWeight);
			}
		} catch (Exception e) {
			System.out.println("Wrong config of Pack: " + maxWeight);
			System.out.println("[max weight per pack] should be numeric type");
			return false;
		}
		return true;
	}

	private boolean checkItemInput(BufferedReader br) throws Exception {
		String line = "";
		while ((line = br.readLine()) != null) {
			if ("".equals(line.trim())) {
				System.out.println("Finish reading the input file");
				return true;
			}
			String[] itemInput = line.split(splitBy);
			if (itemInput.length != 4) {
				System.out.println("Wrong config of Item: " + line);
				System.out.println("Please config item like this:");
				System.out.println("[item id],[item length],[item quantity],[piece weight]");
				return false;
			} else {
				Item item = new Item();
				String itemId = itemInput[0].trim();
				try {
					int tempId = Integer.parseInt(itemId);
					if (tempId < 1) {
						System.out.println("Wrong config of Item: " + itemId);
						System.out.println("[item id] should be larger than 0");
						return false;
					} else {
						item.setId(tempId);
					}
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemId);
					System.out.println("[item id] should be int type");
					return false;
				}
				String itemLength = itemInput[1].trim();
				try {
					double tempLength = Double.parseDouble(itemLength);
					if (tempLength <= 0) {
						System.out.println("Wrong config of Item: " + tempLength);
						System.out
								.println("[item length] should be larger than 0.0");
						return false;
					} else {
						item.setLength(tempLength);
					}
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemLength);
					System.out.println("[item length] should be numeric type");
					return false;
				}
				String itemQuantity = itemInput[2].trim();
				try {
					int tempQuantity = Integer.parseInt(itemQuantity);
					if (tempQuantity < 1) {
						System.out.println("Wrong config of Item: " + itemQuantity);
						System.out.println("[item quantity] should be larger than 0");
						return false;
					} else {
						item.setQuantity(tempQuantity);
					}
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + itemQuantity);
					System.out.println("[item quantity] should be numeric type");
					return false;
				}
				String pieceWeight = itemInput[3].trim();
				try {
					double tempWeight = Double.parseDouble(pieceWeight);
					if (tempWeight <= 0) {
						System.out.println("Wrong config of Item: " + pieceWeight);
						System.out.println("[item weight] should be larger than 0.0");
						return false;
					} else {
						if (maxItemWeight < tempWeight) {
							maxItemWeight = tempWeight;
						}
						item.setWeight(tempWeight);
					}
				} catch (Exception e) {
					System.out.println("Wrong config of Item: " + pieceWeight);
					System.out.println("[item weight] should be numeric type");
					return false;
				}
				inputItems.add(item);
			}
		}
		if (inputItems.size() == 0) {
			System.out.println("Can't find any item!");
			return false;
		}
		if (config.getMaxWeight() < maxItemWeight) {
			System.out.println("Max weight per pack should larger than max weight per item!");
			return false;
		}
		return true;
	}

	private boolean init(File file) {
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] input = line.split(splitBy);
				String sortOrder = input[0].trim();
				if (!sortOrder.equalsIgnoreCase(SortOrderEnum.NATURAL.getName())
						&& !sortOrder.equalsIgnoreCase(SortOrderEnum.ASC.getName())
						&& !sortOrder.equalsIgnoreCase(SortOrderEnum.DESC.getName())) {
					continue;
				} else {
					if (input.length != 3) {
						System.out.println("Wrong config of Pack: " + line);
						System.out.println("Please config pack like this:");
						System.out.println("[Sort order],[max pieces per pack],[max weight per pack]");
						return false;
					} else {
						config.setSortOrder(sortOrder.toUpperCase());
						if (!checkPackInput(input)) {
							return false;
						}
						return checkItemInput(br);
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
