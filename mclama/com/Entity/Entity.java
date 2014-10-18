package mclama.com.Entity;

import java.awt.Graphics;
import java.awt.Image;

import mclama.com.Main;

public class Entity {
	
	private Main game;
	
	private int x,y;
	private Image image;

	public Entity(Main game, int x, int y, Image image) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.image = image;
	}
	
	
	public void update(){                  //update ticks
		
	}
	
	public boolean inBounds(int x2, int y2){ //If we clicked the object
		if(x2 >= x && x2 <= x+image.getWidth(game)
		&& y2 >= y && y2 <= y+image.getHeight(game)){
			return true;
		}	
		return false;
	}
	
	public void paint(Graphics g){         //Paint image
		
	}

}
