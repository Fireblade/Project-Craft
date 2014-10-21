package mclama.com;

import java.text.DecimalFormat;

public class Utility {
	
	private Main game;
	
	private static DecimalFormat df = new DecimalFormat("#.##");

	public Utility(Main game) {
		this.game = game;
	}
	
	public static String makeString(Double make){
		return df.format(make);
	}
	
	public static float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

}
