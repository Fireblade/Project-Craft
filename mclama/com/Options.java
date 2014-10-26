package mclama.com;

public class Options {
	
	public static boolean debug_show_collision_box=true;
	public static boolean debug_show_crafting_numbers=true;
	public static boolean debug_show_inventory_numbers=true;
	public static boolean debug_show_mouse_click_info=false;
	
	
	private Main game;
	
	
	private int TargetFps=60; //requires restart if changed

	public Options(Main game) {
		this.game = game;
	}
	
	public int getTargetFps() {
		return TargetFps;
	}

	public void setTargetFps(int targetFps) {
		TargetFps = targetFps;
	}

}
