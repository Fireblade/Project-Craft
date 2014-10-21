package mclama.com.Entity;

import java.awt.Graphics;
import java.awt.Image;

import mclama.com.Main;

public class Entity {
	
	protected Main game;
	
	protected int x,y;
	protected Image image;

	public Entity(Main game, int x, int y, Image image) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.image = image;
	}
	
	
	public void update(){                  //update ticks
		
	}
	
	
	public void paint(Graphics g){         //Paint image
		g.drawImage(image, x, y, game);
	}

}
