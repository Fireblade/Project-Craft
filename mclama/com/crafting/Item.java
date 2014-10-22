package mclama.com.crafting;

import java.awt.Image;
import java.net.URL;
import java.util.Random;

public class Item {
	
	private String prename;
	private String name;
	private Image image;
	private int level;

	public Item(String prename,String name, int level, Image image) {
		this.prename=prename;
		this.name=name;
		this.level = level;
		this.image = image;
	}
	
	public Item(String name,Image image) {
		this.prename=null;
		this.name=name;
		this.level = 0;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
