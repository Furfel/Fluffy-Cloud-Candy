package com.furfel.cfcc;
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;


public class Drawing extends JPanel implements Runnable {
	
	Thread thread = new Thread(this);
	
	Map map = new Map();
	
	public int animationDelayer=0; public int movementDelay=0; public static final int ANIMATION_DELAY=2; public static final int MOVEMENT_DELAY=3;
	
	public static boolean activeDraw=true;
	public static boolean animationPaused=false;
	public static boolean predefine=true;
	private static boolean notMoving=true;
	
	public static boolean messageBox=false;
	public static String message="";
	public static String systemMsg="";
	public static int messageGray=200;
	public static int messageOff=300;
	
	private static int framesDrawn=0, fps=0, drawCycles=0;
	
	public static boolean inventoryBox=false; public static int invCurX=0, invCurY=0; public static int inventoryOff=-240;
	
	public static int srx=0,sry=0;
	
	public static Image[][] tileset1 = new Image[2][64];
	public static Image[] tilesetp = new Image[64];
	public static Image[] tileseti = new Image[64];
	
	public Drawing() {
		for(int i=0;i<tileset1[0].length;i++) {
			tileset1[0][i] = new ImageIcon("res/sh.png").getImage();
			tileset1[0][i] = createImage(new FilteredImageSource(tileset1[0][i].getSource(), new CropImageFilter(i%8*64, (int) (Math.round(Math.floor(i/8))*64),64,64)));
		}
		for(int i=0;i<tileset1[0].length;i++) {
			tileset1[1][i] = new ImageIcon("res/sh2.png").getImage();
			tileset1[1][i] = createImage(new FilteredImageSource(tileset1[1][i].getSource(), new CropImageFilter(i%8*64, (int) (Math.round(Math.floor(i/8))*64),64,64)));
		}
		for(int i=0;i<tilesetp.length;i++) {
			tilesetp[i] = new ImageIcon("res/shp.png").getImage();
			tilesetp[i] = createImage(new FilteredImageSource(tilesetp[i].getSource(), new CropImageFilter(i%8*64, (int) (Math.round(Math.floor(i/8))*64),64,64)));
		}
		for(int i=0;i<tileseti.length;i++) {
			tileseti[i] = new ImageIcon("res/shi.png").getImage();
			tileseti[i] = createImage(new FilteredImageSource(tileseti[i].getSource(), new CropImageFilter(i%8*64, (int) (Math.round(Math.floor(i/8))*64),64,64)));
		}
		thread.start();
	}
	
	public void preDraw(Graphics g) {
		for(int i=0;i<tileset1.length;i++)
			for(int j=0;j<tileset1[0].length;j++)
				g.drawImage(tileset1[i][j], 0, 0, null);
		for(int i=0;i<tilesetp.length;i++)
				g.drawImage(tilesetp[i], 0, 0, null);
		for(int i=0;i<tileseti.length;i++)
			g.drawImage(tileseti[i], 0, 0, null);
	}
	
	public void paintComponent(Graphics g) {
		if(predefine) {preDraw(g); predefine=false;}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GameMain.dm.width, GameMain.dm.height);
		map.draw(g);
		if(systemMsg!="") {g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,32)); g.setColor(Color.GREEN); g.drawString(systemMsg,4,700);}
		if(messageBox) {g.setColor(Color.BLUE); g.drawRect(9, 623+messageOff, 685, 40); g.setColor(Color.CYAN); g.drawRect(8, 622+messageOff, 687, 41); g.setColor(Color.WHITE); g.fillRect(10, 624+messageOff, 684, 39); g.setColor(new Color(messageGray,messageGray,messageGray)); g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28)); g.drawString(message, 13, 653);}
		if(inventoryOff!=-240) {g.setColor(Color.BLACK); g.fillRect(52, inventoryOff, 600, 240); g.setColor(new Color(150,150,150)); g.fillRect(52, inventoryOff, 592, 232); g.setColor(new Color(30,30,30)); g.fillRect(60, inventoryOff+8, 584, 224);}
		if(inventoryBox && inventoryOff==0) {for(int i=0;i<GameMain.inventory.length;i++) for(int j=0;j<GameMain.inventory[0].length;j++) g.drawImage(tileseti[GameMain.inventory[i][j]], 72+i*72, 16+j*72, null); g.setColor(Color.WHITE); g.drawRect(72+invCurX*72, 16+invCurY*72, 64, 64); g.drawRect(70+invCurX*72, 14+invCurY*72, 68, 68);}
		if(GameMain.showGrid) {
			g.setColor(Color.GREEN);
			g.drawOval(srx*64, sry*64, 64, 64);
			g.drawOval(srx*64+2, sry*64+2, 60, 60);
			g.drawImage(tileset1[(int) Math.round(Math.floor(GameMain.selObj/64))][GameMain.selObj%64], srx*64+8, sry*64+8, (srx+1)*64-8, (sry+1)*64-8, 0, 0, 64, 64, null);
			g.setColor(Color.BLACK);
			g.fillRect(8, 0, 630, 44);
			g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
			g.setColor(Color.YELLOW);
			g.drawString("Layer: "+Map.upTo+"; quickMove: "+((Map.quickMove)?"true":"false")+"; selObj: "+GameMain.selObj+"; animation:"+((Drawing.animationPaused)?"paused":"yes")+"; Pos:"+Map.playerx+":"+Map.playery, 10, 16);
			g.setColor(Color.WHITE);
			g.drawString(Values.DEBUG_CONTROLS1, 10, 30);
			g.drawString(Values.DEBUG_CONTROLS2, 10, 42);
			}
		framesDrawn++;
		g.setColor(Color.RED);
		g.drawString(Integer.toString(fps), 2, 704);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(activeDraw) repaint();
			if(messageBox) {if(messageGray>0) messageGray-=10; if(messageOff>0) messageOff-=20;}
			if(inventoryBox) {if(inventoryOff<0) inventoryOff+=16;} else if(inventoryOff>-240) inventoryOff-=16;
			if(animationDelayer==ANIMATION_DELAY) {animationDelayer=0; if(!animationPaused)Map.updateAnims();} else animationDelayer++;
			if(!notMoving && Map.vpx==Map.playerx*Map.spriteSize && Map.vpy==Map.playery*Map.spriteSize) notMoving=true; else notMoving=false;
			if(GameMain.keyUp)
				{if(notMoving) Map.movePlayer(Values.PLAYER_UP);}
			else if(GameMain.keyDown)
				{if(notMoving) Map.movePlayer(Values.PLAYER_DOWN);}
			else if(GameMain.keyLeft)
				{if(notMoving) Map.movePlayer(Values.PLAYER_LEFT);}
			else if(GameMain.keyRight)
				{if(notMoving) Map.movePlayer(Values.PLAYER_RIGHT);}
			if(drawCycles>=39) {drawCycles=0; fps=framesDrawn; framesDrawn=0;} else drawCycles++;
			try {Thread.sleep(25);} catch(Exception e) {}
		}
	}
	
	public static void showMessage(String msg) {
		messageGray=250;
		messageOff=200;
		message=msg;
		messageBox=true;
	}
	
	public static void showInventory() {
		inventoryBox=!inventoryBox;
	}

}
