package mclama.com;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mclama.com.Entity.Button;
import mclama.com.Entity.Entity;
import mclama.com.crafting.Craft;
import mclama.com.crafting.Item;

public class Main extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	//Crafting Grid
	private int craftGridWidth=12;
	private int craftGridHeight=9;
	private int craftGridSize=24;
	private int craftGridXStart=288;
	private int craftGridYStart=24;
	private int craftGridXOffset=0;
	private int craftGridXStartOffset=12;
	
	//crafting variables
	private int craft_workers=3;
	private int craft_wood_workers=0;
	private int craft_metal_workers=0;
	private int craft_misc_workers=0;
	private int craft_item_workers=0;

	//Base to the game
	private static Main client;
	private static JFrame frame;
	private boolean gameRunning=true;
	private long lastFpsTime;
	private int fps;
	
	//double buffering
	private Graphics db_g;
	private Image db_i;
	
	//Variables that i will use
	private int currentFps;
	private int mx;
	private int my;
	private int gridx;
	private int gridy;
	
	//Api classes
	private Craft craft;
	private Utility util;
	private Options opt;
	private Statistics stats;
	
	//Entitys
	protected List<Entity> Ents = new ArrayList<Entity>(1024);
	
	//utility
	private boolean debug_boxing=false;
	private int debug_boxing_x=0;
	private int debug_boxing_y=0;
	private int debug_boxing_endx=0;
	private int debug_boxing_endy=0;
	private boolean mouseOnScreen;
	
	
	//inventory related
	private List<Item> inv_items = new ArrayList<Item>(155);//
	private Item inv_item_selected=null;
	private Item inv_last_item_bought=null;
	private String inv_last_item_bought_name="";
	private String inv_last_item_bought_text="";
	private int inv_buy_item_level=0;





	public Main() {

		
		
//		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
//	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
//	    frame.setLocation(x, y);
//	    frame.setBounds(100, 100, 700, 400);
//	    
//	    frame.setResizable(false);
//	    frame.setTitle("Project Craft");
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame.setBounds(100, 100, 700, 400);
//
//	    frame.pack();
//	    frame.setVisible(true);
	    
	

		System.out.println("Executing game loop");
		//gameLoop();
	}
	
	@Override
	public void update(Graphics g) {
		
		//Begin double buffer
		if(db_i == null){
			db_i = createImage(this.getSize().width, this.getSize().height);
			db_g = db_i.getGraphics();
		}
		
		db_g.setColor(getBackground());
		db_g.fillRect(0,0,this.getSize().width, this.getSize().height);
		
		db_g.setColor(getForeground());
		paint(db_g);
		
		g.drawImage(db_i, 0, 0, this);
		//end double buffer
		
		
		
	}
	
	@Override
    public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		
		g.setColor(Color.RED);
		g.drawRect(0,0,getWidth()-1, getHeight()-1);
		
		
		g.setColor(Color.WHITE);
		//draw the grid
		int gridXEnd = craftGridXStart + (craftGridSize*craftGridWidth);
		int gridYEnd = craftGridYStart + (craftGridSize*craftGridHeight);
		
		for(int x=0; x<craftGridWidth+1; x++){
			g.drawLine(craftGridXStart+(x*craftGridSize), craftGridYStart, craftGridXStart+(x*craftGridSize), gridYEnd);
		}
		for(int y=0; y<craftGridHeight+1; y++){
			g.drawLine(craftGridXStart, craftGridYStart+(y*craftGridSize), gridXEnd, craftGridYStart+(y*craftGridSize));
		}
		
		int sidePanelWidth = 11*craftGridSize;
		int sidePanelHeight = 14*craftGridSize;
		
		g.drawRect(24,120,sidePanelWidth,sidePanelHeight);
		
		//Draw items on grid
		Item[][] itemGrid = craft.getItemGrid();
		int craftWidth = craft.getCraftWidth();
		int craftHeight = craft.getCraftHeight();
		
		for(int x=0; x<craftWidth; x++){
			for(int y=0; y<craftHeight; y++){
				g.drawImage(itemGrid[x][y].getImage(), craftGridXStart+(x*craftGridSize), craftGridYStart+(y*craftGridSize), this);
				//g.drawImage(itemGrid[x][y].getImage(), craftGridXStart+(x*craftGridSize), craftGridYStart+(y*craftGridSize), itemGrid[x][y].getImage().getWidth(this), itemGrid[x][y].getImage().getHeight(this),this);
			}
		}
		
		//Draw all entities.
		for(int i=Ents.size()-1; i>=0; i--) {
        	Entity e = Ents.get(i);
        	e.paint(g);
		}
		
		g.setColor(Color.GREEN);
		g.drawRect(debug_boxing_x,debug_boxing_y,debug_boxing_endx, debug_boxing_endy);
		
		g.setColor(Color.ORANGE);
		g.drawString("Item received: ", craftGridXStart, 432);
		g.drawRect(craftGridXStart-1, 432, 240, 25);
			
		for(int i=0; i<inv_items.size(); i++) {
        	Item e = inv_items.get(i);
        	int x = i % 11;
        	int y = (int) Math.ceil(i / 11);
        	g.drawImage(e.getImage(), 24+(x*craftGridSize), 120+(y*craftGridSize), this);
        	if(Options.debug_show_inventory_numbers) g.drawString((i+1)+"",26+(x*craftGridSize), 136+(y*craftGridSize));
        	//System.out.println(i + ",," + x + "," + y);
		}
		
		if(inv_last_item_bought!=null) {
			g.drawImage(inv_last_item_bought.getImage(), craftGridXStart,433,inv_last_item_bought.getImage().getWidth(this),inv_last_item_bought.getImage().getHeight(this), this);
			g.drawString(inv_last_item_bought_name, craftGridXStart+inv_last_item_bought.getImage().getWidth(this)+2, 444);
			g.drawString(inv_last_item_bought_text, craftGridXStart+inv_last_item_bought.getImage().getWidth(this)+2, 456);
		}
		
		
		g.drawString("fps: " + currentFps, 8, 16);
		g.drawString("mouse " + mx + "/" + my, 8, 32);
		g.drawString("grid " + gridx + "/" + gridy, 8, 48);
		g.drawString(debug_boxing_x + "," + debug_boxing_y + "," + debug_boxing_endx + "," + debug_boxing_endy, 8, 64);
		g.drawString("items in inventory " + inv_items.size(), 8, 80);
		
    }

	public static void main(String[] args) {
//		client = new Main();
//		frame = new JFrame();
//		frame.getContentPane().add(client, BorderLayout.CENTER);
//		frame.setSize(new Dimension(880,600));
//		frame.setVisible(true);
	}
	
	public void init(){
		//client = new Main();
		setSize(882,492);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		stats = new Statistics(this);
		craft = new Craft(this, craftGridWidth, craftGridHeight);
		util = new Utility(this);
		opt = new Options(this);
		
		//craftGridXStart = (Math.round((getWidth()/2)-((craftGridWidth*craftGridSize)/2)/craftGridSize))*craftGridSize;
		craftGridXStart = (getWidth()/2)-((craftGridWidth*craftGridSize)/2);
		craftGridXOffset= craftGridXStart % 24; //offset variable
		craftGridXStartOffset= Math.round(craftGridXStart/craftGridSize);
		
		
		
		Ents.add(new Button(this, 800, 48, 16, 16, setImg("images/skull.png")));
		for(int i=0; i<2; i++){
			Item newItem = buyNewItem(0);
			if(newItem!=null){
				inv_items.add(newItem);
			}
		}
		inv_last_item_bought = new Item("Core", "Trigger", 0, setImg("images/core_trigger"));
	}
	
	public void destroy(){
		//Ends applet
	}
	
	public void start(){
		//Starts applet
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop(){
		//
	}
	
	public void gameLoop()
	{
	   long lastLoopTime = System.nanoTime();
	   final int TARGET_FPS = opt.getTargetFps();
	   final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;   

	   // keep looping round til the game ends
	   while (gameRunning)
	   {
	      // work out how long its been since the last update, this
	      // will be used to calculate how far the entities should
	      // move this loop
	      long now = System.nanoTime();
	      long updateLength = now - lastLoopTime;
	      lastLoopTime = now;
	      double delta = updateLength / ((double)OPTIMAL_TIME);

	      // update the frame counter
	      lastFpsTime += updateLength;
	      fps++;
	      
	      // update our FPS counter if a second has passed since
	      // we last recorded
	      if (lastFpsTime >= 1000000000)
	      {
	         //System.out.println("(FPS: "+fps+")");
	    	 currentFps=fps;
	         lastFpsTime = 0;
	         fps = 0;
	      }
	      
	      // update the game logic
	      doGameUpdates(delta);
	      
	      // draw everyting
	      render();
	      
	      // we want each frame to take 10 milliseconds, to do this
	      // we've recorded when we started the frame. We add 10 milliseconds
	      // to this and then factor in the current time to give 
	      // us our final value to wait for
	      // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
	      try{
	    	  //Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
	    	  Thread.sleep(16);
	      }catch(InterruptedException e){
	    	  e.printStackTrace();
	      }
	      finally{
	    	  
	      }
	   }
	}

	private void render() {
		repaint();
	}

	private void doGameUpdates(double delta){
		
//	   for (int i = 0; i < stuff.size(); i++)
//	   {
//	      // all time-related values must be multiplied by delta!
//	      Stuff s = stuff.get(i);
//	      s.velocity += Gravity.VELOCITY * delta;
//	      s.position += s.velocity * delta;
//	      
//	      // stuff that isn't time-related doesn't care about delta...
//	      if (s.velocity >= 1000)
//	      {
//	         s.color = Color.RED;
//	      }
//	      else
//	      {
//	         s.color = Color.BLUE;
//	      }
//	   }
	}
	
	public Image setImg(String string) {
		URL url = null;
		if(!string.endsWith(".png")) string += ".png";
		try{
			url = getDocumentBase();
			return getImage(url, string);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getImage(url, "images/skull.png"); //error image
	}
	
	public Item buyNewItem(int forLevel){
		Random gen = new Random();
		int chance = gen.nextInt(100);
		if(chance<25) //25% chance of a Core
		{
			switch(gen.nextInt(Math.max(2+(forLevel/10),2))){ //A base minimum searchable items, to a maximum of the items
				case 0:
					return new Item("Core", "Trigger", forLevel, setImg("images/core_trigger.png"));
				case 1:
					return new Item("Core", "Handle", forLevel, setImg("images/core_handle.png"));
			}
		} else if(chance <70){ //45% chance of finding an item.
			switch(gen.nextInt(Math.max(4+(forLevel/10) + gen.nextInt(1),5))){  //A base minimum searchable items, to a maximum of the items
				case 0:
					return new Item("Wood", "Pole", forLevel, setImg("images/wood_pole.png"));
				case 1:
					return new Item("Wood", "Connector", forLevel, setImg("images/wood_connector.png"));
				case 2:
					return new Item("Metal", "pipe", forLevel, setImg("images/metal_pipe.png"));
				case 3:
					return new Item("Metal", "hallowed-plate", forLevel, setImg("images/metal_hallowed-plate.png"));
				case 4:
					return new Item("Metal", "sights", forLevel, setImg("images/metal_sights.png"));
			}
		}
		
		return null; //else a 30% chance of being left with scrap nothing.
	}
	
	@Override
	public void run() {
		gameLoop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_Z){ //Temp buy new item
			Item bought = buyNewItem(inv_buy_item_level);
			if(bought!=null){
				boolean inInv = isItemInInventory(bought);				
			}else{
				inv_last_item_bought_name="";
				inv_last_item_bought_text="Darn, Couldn't get an item.";
			}
		}
		
	}

	private boolean isItemInInventory(Item bought) {
		inv_last_item_bought_name=bought.getName();
		inv_last_item_bought = bought;
		for(int i=0; i<inv_items.size(); i++) {
        	Item e = inv_items.get(i);
        	if((e.getPrename() + e.getName()).equals(bought.getPrename() + bought.getName())){ //
        		System.out.println(e.getPrename() + e.getName() + " .. " + bought.getPrename() + bought.getName());
        		if(bought.getLevel()>e.getLevel()){ //higher level than the one in our inventory
        			e=bought;
        			inv_last_item_bought_text="Upgraded " + bought.getName() + " to level " + bought.getLevel() + ".";
        		}
        		else{
        			inv_last_item_bought_text="Found a duplicate level " + bought.getLevel() + " " + bought.getName() + ", Discarding.";
        		}
        		return true;
        	}
		}
		inv_last_item_bought_text="Found a new item! " + bought.getName() + " level " + bought.getLevel() + ".";
		inv_items.add(bought);
		return false;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		if(mouseOnScreen){ //Make sure mouse is on-screen
			gridx = Math.round((mx-craftGridXOffset)/craftGridSize) - craftGridXStartOffset;
			gridy = Math.round(my/craftGridSize) - 1;
		}
		
		//TODO Show info for item under mouse

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.isAltDown()){
			debug_boxing=true;
			debug_boxing_x=e.getX();
			debug_boxing_y=e.getY();
			debug_boxing_endx=(mx-debug_boxing_x);
			debug_boxing_endy=(my-debug_boxing_y);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(mouseOnScreen){ //Make sure mouse is on-screen
			mx = e.getX();
			my = e.getY();
			System.out.println("Mouse released at " + mx + "/" + my + " on grid location " + gridx + "/" + gridy);
			
			if(gridx>=0 && gridx < craftGridWidth && gridy>=0 && gridy < craftGridHeight){
				System.out.println("Return item: " + craft.getItem(gridx, gridy).getName());
			}
			
			
			for(int i=Ents.size()-1; i>=0; i--) {
	        	Entity en = Ents.get(i);
	        	if(en instanceof Button){
	        		if(((Button) en).inBounds(mx, my)){ //clicking in bounds?
	        			System.out.println("You clicked me");
	        		}
	        	}
			}
			if(debug_boxing){
				debug_boxing=false;
				debug_boxing_endx=(mx-debug_boxing_x);
				debug_boxing_endy=(my-debug_boxing_y);
				System.out.println(debug_boxing_x + "," + debug_boxing_y + "," + debug_boxing_endx + "," + debug_boxing_endy 
						+ " Distance: " + Utility.distance(debug_boxing_x,debug_boxing_y,debug_boxing_endx, debug_boxing_endy));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOnScreen=true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseOnScreen=false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		if(mouseOnScreen){ //Make sure mouse is on-screen
					gridx = Math.round((mx-craftGridXOffset)/craftGridSize) - craftGridXStartOffset;
					gridy = Math.round(my/craftGridSize) - 1;
					if(debug_boxing){
						debug_boxing_endx=(mx-debug_boxing_x);
						debug_boxing_endy=(my-debug_boxing_y);
					}
				}
	}


	
	

}
