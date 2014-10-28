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
				itemGrid[x][y] = new Item("","blank", 0,game.setImg("images/blank.png"));
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
		itemGrid[xx][yy]=new Item("", "blank_enabled", 0, game.setImg("images/blank_enabled.png"));
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
				//System.out.println(x + ", " + y);
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
		double price = (3 * coreP.getLevel()) * Math.sqrt(Math.pow(1.1,(coreP.getLevel()))); //Base price for any Core.
		price *= coreModifier(coreP, woodItems, metalItems, miscItems);
		System.out.println("Craft item sells for: " + price);
		return price;
	}

	private double coreModifier(Item coreP, int woodItems, int metalItems,
			int miscItems) {
		double mod=1;
		
		switch(coreP.getName()){
		case "Trigger":
			mod += (woodItems*-0.15) + (metalItems*0.15);
			mod += findItem("MetalSights", 0.15f);
			mod += findItem("MetalPipe", 0.05f);
			mod += findItem("MetalHallowed-Plate", 0.05f);
			break;
		
		case "Handle":
			mod += (woodItems*0.15) + (metalItems*-0.15);
			break;
		}
		
		return mod;
	}

	private double findItem(String string, float value) {
		for(int x=0; x<craftWidth; x++){
			for(int y=0; y<craftHeight; y++){
				//System.out.println(x + ", " + y);
				if((itemGrid[x][y].getPreName() + itemGrid[x][y].getName()).equals(string)){
					return value;
				}
			}
		}
		return 0;
	}
	
	
}
