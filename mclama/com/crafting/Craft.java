package mclama.com.crafting;

import java.awt.Graphics;

import mclama.com.Main;

public class Craft {

	private Main game;
	private int craftWidth;
	private int craftHeight;
	
	
	private Item itemGrid[][];
	private boolean enabledGrid[][];

	
	public Craft(Main game, int craftWidth, int craftHeight) {
		this.game = game;
		this.craftWidth=craftWidth;
		this.craftHeight=craftHeight;
		
		CreateGrid();
	}

	private void CreateGrid() {
		itemGrid = new Item[craftWidth][craftHeight];       //grid which contains the items
		enabledGrid = new boolean[craftWidth][craftHeight]; //grid lets us know which ones we can use.
		
		for(int x=0; x<craftWidth; x++){
			for(int y=0; y<craftHeight; y++){
				itemGrid[x][y] = new Item("blank", game.setImg("images/blank.png"));
				enabledGrid[x][y]=false;
			}
		}
		
		itemGrid[craftWidth/2][craftHeight/2] = new Item("blank_enabled", game.setImg("images/blank_enabled.png"));
		itemGrid[craftWidth/2-1][craftHeight/2] = new Item("blank_enabled", game.setImg("images/blank_enabled.png"));
		itemGrid[craftWidth/2-1][craftHeight/2+1] = new Item("blank_enabled", game.setImg("images/blank_enabled.png"));
		itemGrid[craftWidth/2][craftHeight/2+1] = new Item("blank_enabled", game.setImg("images/blank_enabled.png"));
	}

	public void paint(Graphics g){
		//g.drawImage
		
		
	}

	public int getCraftWidth() {
		return craftWidth;
	}

	public void setCraftWidth(int craftWidth) {
		this.craftWidth = craftWidth;
	}

	public int getCraftHeight() {
		return craftHeight;
	}

	public void setCraftHeight(int craftHeight) {
		this.craftHeight = craftHeight;
	}

	public Item[][] getItemGrid() {
		return itemGrid;
	}

	public void setItemGrid(Item[][] itemGrid) {
		this.itemGrid = itemGrid;
	}

	public boolean[][] getEnabledGrid() {
		return enabledGrid;
	}

	public void setEnabledGrid(boolean[][] enabledGrid) {
		this.enabledGrid = enabledGrid;
	}
	
	public boolean isEnabled(int x, int y){
		return this.enabledGrid[x][y];
	}
	
	public Item getItem(int x, int y){
		return this.itemGrid[x][y];
	}
	
	
}
