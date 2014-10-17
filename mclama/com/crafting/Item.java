package mclama.com.crafting;

import java.awt.Image;

public class Item {
	
	private String name;
	private Image image;

	public Item(String name, Image image) {
		this.name=name;
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
