package model;

import java.util.List;

public class Pack {
	private int id;
	private float length;
	private int maxPieces;
	private float maxWeight;
	private List<Item> items;
	
	public Pack(int id, int maxPieces, float maxWeight) {
		this.id = id;
		this.maxPieces = maxPieces;
		this.maxWeight = maxWeight;
	}
	
	public Item addItem(Item item) {
		return null;
	}
}
