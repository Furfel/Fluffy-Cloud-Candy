package com.furfel.cfcc;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.*;

import javax.swing.*;


public class GameMain extends JFrame {
	
	public static Dimension dm = new Dimension(704,704);
	public static KeyListener listener;
	public static MouseListener mousey;
	public static MouseWheelListener wheely;
	public static MouseMotionListener movey;
	public static boolean showGrid=false;
	public static int selObj=0;
	public static JFrame textframe;
	public static JTextField textfield;
	public static JTextField textfield2;
	public static JTextField textfield3;
	public static JTextField textfield4;
	public static JFrame keysframe;
	
	public static int[][] inventory = new int[8][3];
	
	public static boolean keyLeft=false, keyRight=false, keyUp=false, keyDown=false; 
	
	public static void main(String args[]) {
		System.out.println(Values.GAME_VERSION);
		
		keysframe = new JFrame("Keys help");
		JLabel imagelabel = new JLabel();
		try {imagelabel = new JLabel(new ImageIcon(ImageIO.read(new File("res/fcckeys.png"))));} catch (Exception e) {}
		imagelabel.setPreferredSize(new Dimension(400,300));
		keysframe.add(imagelabel);
		keysframe.pack();
		
		textframe=new JFrame("Data input");
		JPanel centerpanel=new JPanel();
		textfield=new JTextField("/home/blackmonster/Fcc/map.fcc");
		textfield2=new JTextField("height");
		textfield3=new JTextField("actionpacks");
		textfield4=new JTextField("animcount");
		textfield.setPreferredSize(new Dimension(300,16));
		textfield2.setPreferredSize(new Dimension(300,16));
		textfield3.setPreferredSize(new Dimension(300,16));
		textfield4.setPreferredSize(new Dimension(300,16));
		textframe.getContentPane().add(textfield,BorderLayout.NORTH);
		centerpanel.add(textfield2,BorderLayout.NORTH);
		centerpanel.add(textfield3,BorderLayout.SOUTH);
		textframe.getContentPane().add(centerpanel, BorderLayout.CENTER);
		textframe.getContentPane().add(textfield4,BorderLayout.SOUTH);
		textframe.pack();
		
		GameMain game = new GameMain();
		game.setTitle(Values.GAME_VERSION);
		game.setResizable(false);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Drawing drawing = new Drawing();
		drawing.setPreferredSize(dm);
		game.add(drawing);
		game.pack();
		game.setVisible(true);
	}
	
	public GameMain() {
		listener = new KL();
		mousey = new ML();
		wheely = new ML();
		movey=new ML();
		addKeyListener(listener);
		addMouseListener(mousey);
		addMouseWheelListener(wheely);
		addMouseMotionListener(movey);
		setFocusable(true);
	}
	
	public static int findFreePlace() {
		int place=-1;
			for(int i=0;i<inventory.length;i++)
				for(int j=0;j<inventory[0].length;j++)
					if(inventory[i][j]==0) place=j*8+i;
		return place;
	}
	
	public class KL implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_UP: if(!Drawing.messageBox){if(Drawing.inventoryBox) {if(Drawing.invCurY>0)Drawing.invCurY--; else Drawing.invCurY=2;} else {Map.moving=true; Map.movingdir=Values.PLAYER_UP; keyUp=true;}} break;
				case KeyEvent.VK_DOWN: if(!Drawing.messageBox){if(Drawing.inventoryBox) {if(Drawing.invCurY<2)Drawing.invCurY++; else Drawing.invCurY=0;} else {Map.moving=true; Map.movingdir=Values.PLAYER_DOWN; keyDown=true;}} break;
				case KeyEvent.VK_LEFT: if(!Drawing.messageBox){if(Drawing.inventoryBox) {if(Drawing.invCurX>0)Drawing.invCurX--; else Drawing.invCurX=7;} else {Map.moving=true; Map.movingdir=Values.PLAYER_LEFT; keyLeft=true;}} break;
				case KeyEvent.VK_RIGHT: if(!Drawing.messageBox){if(Drawing.inventoryBox) {if(Drawing.invCurX<7)Drawing.invCurX++; else Drawing.invCurX=0;} else {Map.moving=true; Map.movingdir=Values.PLAYER_RIGHT; keyRight=true;}} break;
				case KeyEvent.VK_ENTER: if(!Drawing.messageBox){Drawing.showInventory(); keyUp=false; keyDown=false; keyLeft=false; keyRight=false;} break;
				case KeyEvent.VK_G: showGrid=!showGrid; break;
				case KeyEvent.VK_W: Map.buildMode(Values.SET_BLOCKING_UP, 0); break;
				case KeyEvent.VK_S: Map.buildMode(Values.SET_BLOCKING_DOWN, 0); break;
				case KeyEvent.VK_A: Map.buildMode(Values.SET_BLOCKING_LEFT, 0); break;
				case KeyEvent.VK_D: Map.buildMode(Values.SET_BLOCKING_RIGHT, 0); break;
				case KeyEvent.VK_Q: Map.quickMove=!Map.quickMove; break;
				case KeyEvent.VK_J: Map.upTo=Values.SET_LAYER_0; break;
				case KeyEvent.VK_K: Map.upTo=Values.SET_LAYER_1; break;
				case KeyEvent.VK_L: Map.upTo=Values.SET_LAYER_2; break;
				case KeyEvent.VK_SEMICOLON: Map.upTo=Values.SET_LAYER_3; break;
				case KeyEvent.VK_QUOTE: Map.upTo=Values.SET_LAYER_4; break;
				case KeyEvent.VK_OPEN_BRACKET: selObj--; break;
				case KeyEvent.VK_CLOSE_BRACKET: selObj++; break;
				case KeyEvent.VK_MINUS: selObj-=10; break;
				case KeyEvent.VK_EQUALS: selObj+=10; break;
				case KeyEvent.VK_BACK_SLASH: Map.buildMode(Map.upTo, selObj); break;
				case KeyEvent.VK_SLASH: Map.actions[Map.playerx][Map.playery]=selObj; break;
				case KeyEvent.VK_P: Drawing.animationPaused=!Drawing.animationPaused; break;
				case KeyEvent.VK_O: Map.updateAnims(); break;
				case KeyEvent.VK_R: Map.ridges[Map.playerx][Map.playery]=!Map.ridges[Map.playerx][Map.playery]; break;
				case KeyEvent.VK_SPACE: Map.doAction(); break;
				case KeyEvent.VK_F7: Map.saveMap(textfield.getText()); break;
				case KeyEvent.VK_F8: Map.loadMap(textfield.getText()); break;
				case KeyEvent.VK_T: textframe.setVisible(!textframe.isVisible()); break;
				case KeyEvent.VK_C: {Map.clearMap(Integer.parseInt(textfield.getText()), Integer.parseInt(textfield2.getText()), Integer.parseInt(textfield3.getText()), Integer.parseInt(textfield4.getText()));} break;
				case KeyEvent.VK_9: keysframe.setVisible(true); break;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_UP: keyUp=false; break;
				case KeyEvent.VK_DOWN: keyDown=false; break;
				case KeyEvent.VK_LEFT: keyLeft=false; break;
				case KeyEvent.VK_RIGHT: keyRight=false; break;
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
	}
	
	public class ML implements MouseListener, MouseWheelListener, MouseMotionListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			int rot = e.getWheelRotation();
			if(rot>0) if(selObj>0) selObj--;
			if(rot<0) if(selObj<127) selObj++;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int btn=e.getButton();
			if(btn==MouseEvent.BUTTON1) {
				Map.putBlock(Map.playerx-5+Drawing.srx,Map.playery-5+Drawing.sry,selObj);
			}
			else if(btn==MouseEvent.BUTTON3) {
				Map.putBlock(Map.playerx-5+Drawing.srx,Map.playery-5+Drawing.sry,0);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			Point xy = e.getPoint();
			Drawing.srx = (int)Math.round(xy.getX()/64)-1;
			Drawing.sry = (int)Math.round(xy.getY()/64)-1;
		}
		
	}
	
}
