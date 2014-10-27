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
		unlockGrid(craftWidth/2,craftHeight/2);
		unlockGrid(craftWidth/2-1,craftHeight/2);
		unlockGrid(craftWidth/2-1,craftHeight/2+1);
		unlockGrid(craftWidth/2,craftHeight/2+1);
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
	
	public void setEnabled(boolean value, int x, int y){
		this.enabledGrid[x][y] = value;
	}
	
	public Item getItem(int x, int y){
		return this.itemGrid[x][y];
	}
	
	public void setItem(Item item, int x, int y){
		this.itemGrid[x][y] = item;
		calculateCraftPrice(); 
	}

	public boolean isItemConnected(int xx, int yy) {
		try { if(enabledGrid[xx-1][yy]) return true; } catch (Exception e) {}
		try { if(enabledGrid[xx+1][yy]) return true; } catch (Exception e) {}
		try { if(enabledGrid[xx][yy+1]) return true; } catch (Exception e) {}
		try { if(enabledGrid[xx][yy-1]) return true; } catch (Exception e) {}
		return false;
	}

	public void unlockGrid(int xx, int yy) {
		itemGrid[xx][yy]=new Item("blank_enabled", game.setImg("images/blank_enabled.png"));
		setEnabled(true, xx, yy);
	}
	
	public boolean craftGridHasCore(){
		for(int x=0; x<craftWidth; x++){
			for(int y=0; y<craftHeight; y++){
				if(itemGrid[x][y].getPreName().equals("Core")){
					return true;
				}
			}
		}
		return false;
	}
	
	public double calculateCraftPrice(){
		Item coreP = null;
		int woodItems=0;
		int metalItems=0;
		int miscItems=0;
		for(int x=0; x<craftWidth; x++){
			for(int y=0; y<craftHeight; y++){
				if(itemGrid[x][y].getPreName().equals("Core")){
					coreP = itemGrid[x][y];
				}
				if(itemGrid[x][y].getPreName().equals("wood")){
					woodItems++;
				}
				if(itemGrid[x][y].getPreName().equals("metal")){
					metalItems++;
				}
				if(itemGrid[x][y].getPreName().equals("misc")){
					miscItems++;
				}
			}
		}
		double price = (3*(Math.pow(1.135,(coreP.getLevel())))) * Math.sqrt(Math.pow(1.075,(coreP.getLevel())));
		System.out.println(price);
		return price;
	}
	
	
}
