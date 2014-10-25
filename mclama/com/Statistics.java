package mclama.com;

public class Statistics {

	private Main game;
	
	private int items_Crafted=0;
	private int items_Bought=0;
	private int items_Bought_Upgraded=0;

	
	
	public int getItems_Bought() {
		return items_Bought;
	}

	public void setItems_Bought(int items_Bought) {
		this.items_Bought = items_Bought;
	}

	public int getItems_Bought_Upgraded() {
		return items_Bought_Upgraded;
	}

	public void setItems_Bought_Upgraded(int items_Bought_Upgraded) {
		this.items_Bought_Upgraded = items_Bought_Upgraded;
	}

	public Statistics(Main game) {
		this.game = game;
	}
	
	public int getItems_Crafted() {
		return items_Crafted;
	}

	public void setItems_Crafted(int items_Crafted) {
		this.items_Crafted = items_Crafted;
	}

}
