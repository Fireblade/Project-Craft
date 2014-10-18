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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	protected List<Entity> Entities = new ArrayList<Entity>(1024);

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
		g.fillRect(0,0,this.getSize().width-1, this.getSize().height-1);
		
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
		
		
		
		
		g.setColor(Color.RED);
		g.drawRect(0,0,this.getSize().width-1, this.getSize().height-1);
		
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
		
		g.drawString("fps: " + currentFps, 8, 16);
		g.drawString("mouse " + mx + "/" + my, 8, 32);
		g.drawString("grid " + gridx + "/" + gridy, 8, 48);
		
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
	    	  Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
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
		try{
			url = getDocumentBase();
			return getImage(url, string);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getImage(url, "images/skull.png"); //error image
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
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse released at " + mx + "/" + my + " on grid location " + gridx + "/" + gridy);
		
		if(gridx>=0 && gridx < craftGridWidth && gridy>=0 && gridy < craftGridHeight){
			System.out.println("Return item: " + craft.getItem(gridx, gridy).getName());
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		
		gridx = Math.round(mx/craftGridSize) - 12;
		gridy = Math.round(my/craftGridSize) - 1;
	}
	
	

}
