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
	
	public boolean inBounds(int x, int y){ //If we clicked the object
		
		return false;
	}
	
	public void paint(Graphics g){         //Paint image
		
	}

}
