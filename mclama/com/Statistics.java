package mclama.com;

public class Statistics {

	private Main game;
	
	private int items_Crafted=0;

	
	
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
