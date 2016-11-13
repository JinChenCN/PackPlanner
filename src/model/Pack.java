package model;

import java.util.ArrayList;
import java.util.List;

public class Pack {
	private int id;
	private float length;
	private int maxPieces;
	private int availablePieces;
	private float maxWeight;
	private float availableWeight;
	private boolean isFull;
	private List<Item> items;
	
	public Pack(int id, int maxPieces, float maxWeight) {
		this.id = id;
		this.maxPieces = maxPieces;
		this.availablePieces = maxPieces;
		this.maxWeight = maxWeight;
		this.availableWeight = maxWeight;
		this.isFull = false;
		this.items = new ArrayList<Item>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getMaxPieces() {
		return maxPieces;
	}

	public void setMaxPieces(int maxPieces) {
		this.maxPieces = maxPieces;
	}

	public int getAvailablePieces() {
		return availablePieces;
	}

	public void setAvailablePieces(int availablePieces) {
		this.availablePieces = availablePieces;
	}

	public float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}

	public float getAvailableWeight() {
		return availableWeight;
	}

	public void setAvailableWeight(float availableWeight) {
		this.availableWeight = availableWeight;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
