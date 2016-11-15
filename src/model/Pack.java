package model;

import java.util.Stack;

public class Pack {
	private int id;
	private double length;
	private int maxPieces;
	private int availablePieces;
	private double maxWeight;
	private double availableWeight;
	private boolean isFull;
	private Stack<Item> items;
	
	public Pack(int id, int maxPieces, double maxWeight) {
		this.id = id;
		this.maxPieces = maxPieces;
		this.availablePieces = maxPieces;
		this.maxWeight = maxWeight;
		this.availableWeight = maxWeight;
		this.isFull = false;
		this.items = new Stack<Item>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
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

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getAvailableWeight() {
		return availableWeight;
	}

	public void setAvailableWeight(double availableWeight) {
		this.availableWeight = availableWeight;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public Stack<Item> getItems() {
		return items;
	}

	public void setItems(Stack<Item> items) {
		this.items = items;
	}

}
