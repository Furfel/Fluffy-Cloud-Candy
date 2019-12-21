package com.furfel.fccmc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

public class MapConvert extends JFrame {
	
	public static JTextField mapLoad=new JTextField("/home/blackmonster/Fcc/");
	public static JButton btnLoad = new JButton("Load");
	public static JTextField mapSave=new JTextField("/home/blackmonster/Fcc/");
	public static JButton btnSave = new JButton("Save");
	public static JLabel info=new JLabel("Load map first");
	public static byte version;
	
	public static int width,height;
	public static int gb,gn,sky;
	
	public static int[][][] blocks;
	public static int[][] blocking;
	public static boolean[][] ridges;
	public static int[][] actions;
	public static int playerx,playery;
	public static AnimatedBlock[] anims;
	public static ActionPack[] actionpacks;
	
	public static final byte latest=1;
	
	public static void main(String[]args) {
		MapConvert mc = new MapConvert();
	}
	
	public MapConvert() {
		setTitle("FCC Map Convert");
		JPanel loading = new JPanel();
		loading.add(mapLoad);
		btnLoad.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { detectVersion(mapLoad.getText()); getSize(mapLoad.getText()); info.setText("Version: "+version+" Size: "+width+"x"+height); loadMap(mapLoad.getText()); } });
		loading.add(btnLoad);
		add(loading, BorderLayout.NORTH);
		add(info, BorderLayout.CENTER);
		JPanel saving = new JPanel();
		saving.add(mapSave);
		btnSave.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { saveMap(mapSave.getText()); }} );
		saving.add(btnSave);
		add(saving, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}
		
	public static void detectVersion(String fn) {
		try {
			FileInputStream is= new FileInputStream(fn);
			DataInputStream dis = new DataInputStream(is);
			version=dis.readByte();
			dis.close();
		} catch(Exception e) {}
	}
	
	public static void getSize(String fn) {
		try {
			FileInputStream is= new FileInputStream(fn);
			DataInputStream dis = new DataInputStream(is);
			if(version!=0) dis.readByte();
			width=dis.readInt();
			height=dis.readInt();
			dis.close();
		} catch(Exception e) {}
	}
	
	public static void loadMap(String fn) {
		System.out.println("Loading map "+fn);
		
		/*
		 * Map version 0
		 */
		
		if(version==0) {
		try {
		FileInputStream fis = new FileInputStream(fn);
		DataInputStream dis = new DataInputStream(fis);
		sky=4;
		int bx = dis.readInt(),by = dis.readInt();
		blocks = new int[bx][by][5];
		blocking = new int[bx][by];
		ridges = new boolean[bx][by];
		actions = new int[bx][by];
		playerx=dis.readInt(); playery=dis.readInt();
		for(int j=0;j<by;j++)
			for(int i=0;i<bx;i++) {
				for(int k=0;k<5;k++)
					blocks[i][j][k] = dis.readInt();
				blocking[i][j] = dis.readInt();
				ridges[i][j]=dis.readBoolean();
				actions[i][j]=dis.readInt();
			}
		int animl=dis.readInt();
		anims = new AnimatedBlock[animl];
		for(int i=0;i<animl;i++) {
			anims[i]=new AnimatedBlock(dis.readInt(), dis.readInt(), dis.readInt());
			anims[i].moveSpeed=dis.readInt();
			anims[i].curframe=dis.readInt();
			anims[i].drawx=dis.readInt();
			anims[i].drawy=dis.readInt();
			int frms = dis.readInt();
			for(int j=0;j<frms;j++) {
				anims[i].addPhase(dis.readInt(), dis.readInt(), dis.readInt());
			}
		}
		dis.close();
		System.out.println("Reading actionpacks from "+fn+".act");
		fis = new FileInputStream(fn+".act");
		dis = new DataInputStream(fis);
		int apl=dis.readInt();
		actionpacks=new ActionPack[apl];
			for(int i=0;i<actionpacks.length;i++) {
				actionpacks[i]=new ActionPack();
				actionpacks[i].autoTrigger=dis.readBoolean();
				int as=dis.readInt();
				for(int j=0;j<as;j++) {
					int atype=dis.readInt();
					int dl=dis.readInt();
					int[] tmpd;
					if(dl>0) {
						tmpd=new int[dl];
						for(int k=0;k<dl;k++)
							tmpd[k]=dis.readInt();
					} else tmpd=null;
					dl=dis.readInt();
					String[] tmps;
					if(dl>0) {
						tmps=new String[dl];
						for(int k=0;k<dl;k++) {
							tmps[k]="";
							int sl=dis.readInt();
							for(int l=0;l<sl;l++) tmps[k]+=(char)(dis.readByte());
						}
					} else tmps=null;
					actionpacks[i].addAction(atype, tmpd, tmps,0);
				}
			}
		dis.close();
		} catch(Exception e) {System.out.println("Map loader:"+e.getMessage());} }
		
		/*
		 * Map version 1
		 * */
		
		else if(version==1) {
			try {
				FileInputStream fis = new FileInputStream(fn);
				DataInputStream dis = new DataInputStream(fis);
				dis.readByte();
				int bx = dis.readInt(),by = dis.readInt(), bz=dis.readInt(); sky=dis.readInt();
				blocks = new int[bx][by][bz];
				blocking = new int[bx][by];
				ridges = new boolean[bx][by];
				actions = new int[bx][by];
				playerx=dis.readInt(); playery=dis.readInt();
				gb=dis.readInt(); gn=dis.readInt();
				for(int j=0;j<by;j++)
					for(int i=0;i<bx;i++) {
						for(int k=0;k<bz;k++)
							blocks[i][j][k] = dis.readInt();
						blocking[i][j] = dis.readInt();
						ridges[i][j]=dis.readBoolean();
						actions[i][j]=dis.readInt();
					}
				int animl=dis.readInt();
				anims = new AnimatedBlock[animl];
				for(int i=0;i<animl;i++) {
					anims[i]=new AnimatedBlock(dis.readInt(), dis.readInt(), dis.readInt());
					anims[i].moveSpeed=dis.readInt();
					anims[i].curframe=dis.readInt();
					anims[i].drawx=dis.readInt();
					anims[i].drawy=dis.readInt();
					int frms = dis.readInt();
					for(int j=0;j<frms;j++) {
						anims[i].addPhase(dis.readInt(), dis.readInt(), dis.readInt());
					}
				}
				dis.close();
				System.out.println("Reading actionpacks from "+fn+".act");
				fis = new FileInputStream(fn+".act");
				dis = new DataInputStream(fis);
				int apl=dis.readInt();
				actionpacks=new ActionPack[apl];
					for(int i=0;i<actionpacks.length;i++) {
						actionpacks[i]=new ActionPack();
						actionpacks[i].autoTrigger=dis.readBoolean();
						int as=dis.readInt();
						for(int j=0;j<as;j++) {
							int atype=dis.readInt();
							int dl=dis.readInt();
							int[] tmpd;
							if(dl>0) {
								tmpd=new int[dl];
								for(int k=0;k<dl;k++)
									tmpd[k]=dis.readInt();
							} else tmpd=null;
							dl=dis.readInt();
							String[] tmps;
							if(dl>0) {
								tmps=new String[dl];
								for(int k=0;k<dl;k++) {
									tmps[k]="";
									int sl=dis.readInt();
									for(int l=0;l<sl;l++) tmps[k]+=(char)(dis.readByte());
								}
							} else tmps=null;
							dl=dis.readInt(); //reading conditions
							actionpacks[i].addAction(atype, tmpd, tmps,dl);
							for(int k=0;k<dl;k++)
								{
									int ctype=dis.readInt();
									int cdl=dis.readInt();
									int[] cdata=new int[cdl];
									for(int l=0;l<cdl;l++) cdata[l]=dis.readInt();
									actionpacks[i].actions.get(actionpacks[i].actions.size()-1).makeCondition(k, ctype, cdata);
								}
						}
					}
				dis.close();
			} catch(Exception e) {System.out.println("Map loader:"+e.getMessage());} }
		System.out.println("Map loaded!");
	}
	
	public static void saveMap(String fn) {
		System.out.println("saving map...");
		try {
		FileOutputStream fos = new FileOutputStream(fn);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeByte(latest);
		dos.writeInt(blocks.length);
		dos.writeInt(blocks[0].length);
		dos.writeInt(blocks[0][0].length);
		dos.writeInt(sky);
		dos.writeInt(playerx);
		dos.writeInt(playery);
		dos.writeInt(gb); dos.writeInt(gn);
		for(int j=0;j<blocks[0].length;j++)
			for(int i=0;i<blocks.length;i++) {
				for(int k=0;k<5;k++)
					dos.writeInt(blocks[i][j][k]);
				//dos.writeInt(Integer.parseInt(((blocking[i][j].up)?"1":"0")+((blocking[i][j].down)?"1":"0")+((blocking[i][j].left)?"1":"0")+((blocking[i][j].right)?"1":"0"), 2));
				//int tmp=0; if(blocking[i][j].up) tmp+=8; if(blocking[i][j].down) tmp+=4; if(blocking[i][j].left) tmp+=2; if(blocking[i][j].right) tmp+=1;
				dos.writeInt(blocking[i][j]);
				dos.writeBoolean(ridges[i][j]);
				dos.writeInt(actions[i][j]);
				//dos.writeInt( ((blocking[i][j].up)?"1":"0")+((blocking[i][j].down)?"1":"0")+((blocking[i][j].left)?"1":"0")+((blocking[i][j].right)?"1":"0"));
			}
		dos.writeInt(anims.length);
		for(int i=0;i<anims.length;i++) {
			dos.writeInt(anims[i].mapx);
			dos.writeInt(anims[i].mapy);
			dos.writeInt(anims[i].obj);
			dos.writeInt(anims[i].moveSpeed);
			dos.writeInt(anims[i].curframe);
			dos.writeInt(anims[i].drawx);
			dos.writeInt(anims[i].drawy);
			dos.writeInt(anims[i].frames.size());
			for(int j=0;j<anims[i].frames.size();j++) {
				dos.writeInt(anims[i].frames.get(j).type);
				dos.writeInt(anims[i].frames.get(j).data1);
				dos.writeInt(anims[i].frames.get(j).data2);
			}
		}
		dos.close(); System.out.println("Writing actionpacks to "+fn+".act");
		fos = new FileOutputStream(fn+".act");
		dos = new DataOutputStream(fos);
		dos.writeInt(actionpacks.length);
			for(int i=0;i<actionpacks.length;i++) {
				dos.writeBoolean(actionpacks[i].autoTrigger);
				dos.writeInt(actionpacks[i].actions.size());
				for(int j=0;j<actionpacks[i].actions.size();j++) {
					dos.writeInt(actionpacks[i].actions.get(j).type);
					if(actionpacks[i].actions.get(j).data!=null) {
						dos.writeInt(actionpacks[i].actions.get(j).data.length);
						for(int k=0;k<actionpacks[i].actions.get(j).data.length;k++) {
							dos.writeInt(actionpacks[i].actions.get(j).data[k]);
						}
					} else dos.writeInt(0);
					if(actionpacks[i].actions.get(j).datas!=null) {
						dos.writeInt(actionpacks[i].actions.get(j).datas.length);
						for(int k=0;k<actionpacks[i].actions.get(j).datas.length;k++) {
							dos.writeInt(actionpacks[i].actions.get(j).datas[k].length());
							dos.write(actionpacks[i].actions.get(j).datas[k].getBytes());
						}
					} else dos.writeInt(0);
					dos.writeInt(actionpacks[i].actions.get(j).conditions.length);
					for(int k=0;k<actionpacks[i].actions.get(j).conditions.length;k++) {
						dos.writeInt(actionpacks[i].actions.get(j).conditions[k].type);
						dos.writeInt(actionpacks[i].actions.get(j).conditions[k].data.length);
						for(int l=0;l<actionpacks[i].actions.get(j).conditions[k].data.length;l++) {
							dos.writeInt(actionpacks[i].actions.get(j).conditions[k].data[l]);
						}
					}
				}
			}
		dos.close();
		} catch(Exception e) {System.out.println(e.getMessage());}
		System.out.println("map saved!");
	}
	
}
