package mclama.com.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import mclama.com.Main;
import mclama.com.Options;

public class Button extends Entity {
	
	protected int bwidth=0, bheight=0;

	public Button(Main game, int x, int y, int bwidth, int bheight, Image image) {
		super(game, x, y, image);
		this.bwidth = bwidth;
		this.bheight = bheight;
		System.out.println((x+bwidth) + "/" + (y+bheight));
	}
	
	public Button(Main game, int x, int y, Image image) {
		super(game, x, y, image);
		this.bwidth = image.getWidth(game);
		this.bheight = image.getHeight(game);
		
	}
	
	public boolean inBounds(int x2, int y2){ //If we clicked the object
		if(x2 >= x && x2 <= x+bwidth
		&& y2 >= y && y2 <= y+bheight){
			return true;
		}	
		return false;
	}
	
	public void paint(Graphics g){         //Paint image
		g.drawImage(image, x, y, game);
		if(Options.debug_show_collision_box){
			g.setColor(Color.RED);
			g.drawRect(x,y,bwidth,bheight);
		}
	}

}
