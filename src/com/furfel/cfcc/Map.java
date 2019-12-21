package com.furfel.cfcc;
import java.awt.*;
import java.io.*;

public class Map {
	
	static Block[][][] blocks = new Block[200][200][5];
	static Blocking[][] blocking = new Blocking[200][200];
	static AnimatedBlock[] anims = new AnimatedBlock[0];
	static boolean[][] ridges = new boolean[200][200];
	static int[][] actions = new int[200][200];
	static ActionPack[] actionpacks = new ActionPack[3];
	static ActoAnim[] actoanims = new ActoAnim[2];
	
	static boolean[] globalSwitches = new boolean[0];
	static int[] globalNumbers = new int[0];
	
	public static final int spriteSize = 64;
	public static int skyLayer=4;
	
	public static int playerx=3,playery=3,playerz=0,playerd=0,playeranim=0;
	public static int vpx=3*spriteSize,vpy=3*spriteSize,vpz=0;
	
	public static boolean moving=false; public static int movingdir=0;
	public static int playerSpeed=8;
	public static boolean nightfall=false; public static int daytime=0;
	
	public static boolean quickMove=false;
	public static int upTo=4;
	
	public Map() {
		upTo=blocks[0][0].length;
		for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
				for(int k=1;k<blocks[0][0].length;k++)
					blocks[i][j][k] = new Block(i*spriteSize, j*spriteSize, spriteSize, 0);
		for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
					blocks[i][j][0] = new Block(i*spriteSize, j*spriteSize, spriteSize, 8);
		for(int i=0;i<blocking.length;i++)
			for(int j=0;j<blocking[0].length;j++)
				blocking[i][j] = new Blocking(0);
		actionpacks[0] = new ActionPack();
		actionpacks[0].addAction(Values.ACTION_IDLE, null, null,0);
		actionpacks[1] = new ActionPack();
		actionpacks[1].addAction(Values.ACTION_TELEPORTPLAYER, new int[]{9,10}, null,0);
		actionpacks[2] = new ActionPack();
		actionpacks[2].autoTrigger=true;
		actionpacks[2].addAction(Values.ACTION_SHOWMESSAGEBOX, null, new String[]{"You have been teleported!"},0);
		actionpacks[2].addAction(Values.ACTION_TELEPORTPLAYER, new int[]{8,7}, null,0);
	}
	
	public void draw(Graphics g){
		/*for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
				for(int k=0;k<blocks[0][0].length-1;k++)
					if(k<=upTo) blocks[i][j][k].draw(g);*/
		for(int i=playerx-6;i<=playerx+6;i++)
			for(int j=playery-6;j<=playery+6;j++)
				for(int k=0;k<skyLayer;k++)
					if(k<=upTo) if(i>=0 && j>=0 && i<blocks.length && j<blocks[0].length) blocks[i][j][k].draw(g);
		g.drawImage(Drawing.tilesetp[playerd*8+playeranim], 320, 320-vpz ,null);
		/*for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
				if(blocks[0][0].length-1<=upTo) blocks[i][j][blocks[0][0].length-1].draw(g);*/
		for(int i=playerx-6;i<=playerx+6;i++)
			for(int j=playery-6;j<=playery+6;j++)
				for(int k=skyLayer;k<blocks[0][0].length;k++)
				if(k<=upTo) if(i>=0 && j>=0 && i<blocks.length && j<blocks[0].length) blocks[i][j][k].draw(g);
		
		moving=true;
		if(vpx<playerx*spriteSize) vpx+=playerSpeed;
		if(vpx>playerx*spriteSize) vpx-=playerSpeed;
		if(vpy<playery*spriteSize) vpy+=playerSpeed;
		if(vpy>playery*spriteSize) vpy-=playerSpeed;
		if(vpx==playerx*spriteSize && vpy==playery*spriteSize) {moving=false; playeranim=0;}
		if(vpz>playerz) vpz-=4;
		if(vpz<playerz) vpz+=4;
		
		if(anims.length>0)
		for(int i=0;i<anims.length;i++) {
			anims[i].draw(g);
		}
		
		//if(Map.nightfall) {daytime--; if(daytime<=0) nightfall=false;} else {daytime++; if(daytime>=100) nightfall=true;}
		//g.setColor(new Color(0,0,0,daytime));
		//g.fillRect(0, 0, GameMain.dm.width, GameMain.dm.height);
		g.setColor(Color.BLUE);
		if(GameMain.showGrid)
			for(int i=playerx-6;i<=playerx+6;i++)
				for(int j=playery-6;j<=playery+6;j++)
					if(i>=0 && j>=0 && i<blocks.length && j<blocks[0].length) {
					g.setColor(Color.BLUE);
					g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
					if(blocking[i][j].up) {g.drawLine(i*spriteSize+3-vpx+320, j*spriteSize+3-vpy+320, (i+1)*spriteSize-3-vpx+320, j*spriteSize+3-vpy+320); g.drawLine(i*spriteSize+3-vpx+320, j*spriteSize+4-vpy+320, (i+1)*spriteSize-3-vpx+320, j*spriteSize+4-vpy+320);}
					if(blocking[i][j].down) {g.drawLine(i*spriteSize+3-vpx+320, (j+1)*spriteSize-3-vpy+320, (i+1)*spriteSize-3-vpx+320, (j+1)*spriteSize-3-vpy+320); g.drawLine(i*spriteSize+3-vpx+320, (j+1)*spriteSize-4-vpy+320, (i+1)*spriteSize-3-vpx+320, (j+1)*spriteSize-4-vpy+320);}
					if(blocking[i][j].left) {g.drawLine(i*spriteSize+3-vpx+320, j*spriteSize+3-vpy+320, i*spriteSize+3-vpx+320, (j+1)*spriteSize-3-vpy+320); g.drawLine(i*spriteSize+4-vpx+320, j*spriteSize+3-vpy+320, i*spriteSize+4-vpx+320, (j+1)*spriteSize-3-vpy+320);}
					if(blocking[i][j].right) {g.drawLine((i+1)*spriteSize-3-vpx+320, j*spriteSize+3-vpy+320, (i+1)*spriteSize-3-vpx+320, (j+1)*spriteSize-3-vpy+320); g.drawLine((i+1)*spriteSize-4-vpx+320, j*spriteSize+3-vpy+320, (i+1)*spriteSize-4-vpx+320, (j+1)*spriteSize-3-vpy+320);}
					if(ridges[i][j]) {g.setColor(new Color(229, 255, 128)); g.drawLine(i*spriteSize+5-vpx+320, (j+1)*spriteSize-5-vpy+320, i*spriteSize+30-vpx+320, (j+1)*spriteSize-30-vpy+320); g.drawLine(i*spriteSize+30-vpx+320, (j+1)*spriteSize-30-vpy+320, (i+1)*spriteSize-5-vpx+320, (j+1)*spriteSize-5-vpy+320);}
					if(actions[i][j]>0) {g.setColor(new Color(43, 129, 154)); g.drawString(Integer.toString(actions[i][j]), (i+1)*spriteSize-30-vpx+320, (j+1)*spriteSize-3-vpy+320);}
				}
	}
	
	public static void movePlayer(int dir) {
			boolean canmove=true;
			if(!(vpx==playerx*spriteSize && vpy==playery*spriteSize)) canmove=false;
			if(dir==Values.PLAYER_UP){	
				if(blocking[playerx][playery].up)
					canmove=false;}
			else if(dir==Values.PLAYER_DOWN){
				if(blocking[playerx][playery].down)
					canmove=false;}
			else if(dir==Values.PLAYER_LEFT){
				if(blocking[playerx][playery].left)
					canmove=false;}
			else if(dir==Values.PLAYER_RIGHT){
				if(blocking[playerx][playery].right)
					canmove=false;}
			if(quickMove) canmove=true;
			if(canmove)
			{
				switch(dir) {
					case Values.PLAYER_UP: playery--; break;
					case Values.PLAYER_DOWN: playery++; break;
					case Values.PLAYER_LEFT: playerx--; break;
					case Values.PLAYER_RIGHT: playerx++; break;
				}
			}
			if(dir==0) playerd=dir; else playerd=dir-1;
			if(ridges[playerx][playery]) playerz=16; else if((!ridges[playerx][playery]) && playerz!=0) playerz=0;
			if(canmove) if(actionpacks[actions[playerx][playery]].autoTrigger) doAction();
			if(quickMove) {vpx=playerx*spriteSize; vpy=playery*spriteSize;}
	}
	
	public static void buildMode(int cmd, int data) {
		switch(cmd) {
			case Values.SET_LAYER_0: blocks[playerx][playery][0].id=data; break;
			case Values.SET_LAYER_1: blocks[playerx][playery][1].id=data; break;
			case Values.SET_LAYER_2: blocks[playerx][playery][2].id=data; break;
			case Values.SET_LAYER_3: blocks[playerx][playery][3].id=data; break;
			case Values.SET_LAYER_4: blocks[playerx][playery][4].id=data; break;
			case Values.SET_BLOCKING_UP: blocking[playerx][playery].up=!blocking[playerx][playery].up; break;
			case Values.SET_BLOCKING_DOWN: blocking[playerx][playery].down=!blocking[playerx][playery].down; break;
			case Values.SET_BLOCKING_LEFT: blocking[playerx][playery].left=!blocking[playerx][playery].left; break;
			case Values.SET_BLOCKING_RIGHT: blocking[playerx][playery].right=!blocking[playerx][playery].right; break;
		}
	}
	
	public static void putBlock(int x,int y, int block) {
		blocks[x][y][upTo].id=block;
	}
	
	public static void loadMap(String fn) {
		Drawing.systemMsg="Loading map...";
		Drawing.activeDraw=false; Drawing.animationPaused=true;
		System.out.println("Loading map "+fn);
		try {
		FileInputStream fis = new FileInputStream(fn);
		DataInputStream dis = new DataInputStream(fis);
		int bx = dis.readInt(),by = dis.readInt(), bz=dis.readInt(); skyLayer=dis.readInt();
		blocks = new Block[bx][by][5]; blocking = new Blocking[bx][by]; ridges = new boolean[bx][by]; actions = new int[bx][by];
		playerx=dis.readInt(); playery=dis.readInt(); vpx=playerx*spriteSize; vpy=playery*spriteSize;
		int gv = dis.readInt(); globalNumbers=new int[gv]; gv = dis.readInt(); globalSwitches = new boolean[gv];
		
		for(int j=0;j<by;j++)
			for(int i=0;i<bx;i++) {
				for(int k=0;k<5;k++)
					blocks[i][j][k] = new Block(i*spriteSize, j*spriteSize, spriteSize, dis.readInt());
				blocking[i][j] = new Blocking(dis.readInt());
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
		dis.close(); System.out.println("Reading acto-anims from "+fn+".aa");
		
		fis = new FileInputStream(fn+".aa");
		dis = new DataInputStream(fis);
		apl=dis.readInt();
		int dt=0;
		actoanims=new ActoAnim[apl];
			if(apl>0)
			for(int i=0;i<actoanims.length;i++) {
				actoanims[i] = new ActoAnim(dis.readInt());
				for(int j=0;j<actoanims[i].sets.length;j++) {
					actoanims[i].newInstructionSet(j, dis.readInt());
					if(actoanims[i].sets[j].instructions.length>0)
					for(int k=0;k<actoanims[i].sets[j].instructions.length;k++) {
						actoanims[i].sets[j].instructions[k].type=dis.readInt();
						dt=dis.readInt();
						actoanims[i].sets[j].instructions[k].data=new int[dt];
						if(dt>0)
						for(int l=0;l<dt;l++) {
							actoanims[i].sets[j].instructions[k].data[l]=dis.readInt();
						}
						actoanims[i].sets[j].instructions[k].conditionType=dis.readInt();
						dt=dis.readInt();
						actoanims[i].sets[j].instructions[k].conditionData=new int[dt];
						if(dt>0)
							for(int l=0;l<dt;l++) {
								actoanims[i].sets[j].instructions[k].data[l]=dis.readInt();
						}
					}
				}
			}
		dis.close();
		} catch(Exception e) {System.out.println("Map loader:"+e.getMessage());}
		Drawing.animationPaused=false; Drawing.activeDraw=true;
		System.out.println("Map loaded!");
		Drawing.systemMsg="";
	}
	
	public static void saveMap(String fn) {
		Drawing.activeDraw=false; Drawing.animationPaused=true;
		System.out.println("saving map...");
		try {
		FileOutputStream fos = new FileOutputStream(fn);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeInt(blocks.length);
		dos.writeInt(blocks[0].length);
		dos.writeInt(playerx);
		dos.writeInt(playery);
		for(int j=0;j<blocks[0].length;j++)
			for(int i=0;i<blocks.length;i++) {
				for(int k=0;k<5;k++)
					dos.writeInt(blocks[i][j][k].id);
				//dos.writeInt(Integer.parseInt(((blocking[i][j].up)?"1":"0")+((blocking[i][j].down)?"1":"0")+((blocking[i][j].left)?"1":"0")+((blocking[i][j].right)?"1":"0"), 2));
				int tmp=0; if(blocking[i][j].up) tmp+=8; if(blocking[i][j].down) tmp+=4; if(blocking[i][j].left) tmp+=2; if(blocking[i][j].right) tmp+=1;
				dos.writeInt(tmp);
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
				}
			}
		dos.close(); System.out.println("Writing acto-anims to "+fn+".aa");
		fos = new FileOutputStream(fn+".aa");
		dos = new DataOutputStream(fos);
		dos.writeInt(actoanims.length);
			for(int i=0;i<actoanims.length;i++) {
				dos.writeInt(actoanims[i].sets.length);
					for(int j=0;j<actoanims[i].sets.length;j++) {
						dos.writeInt(actoanims[i].sets[j].instructions.length);
							for(int k=0;k<actoanims[i].sets[j].instructions.length;k++) {
								dos.writeInt(actoanims[i].sets[j].instructions[k].type);
								dos.writeInt(actoanims[i].sets[j].instructions[k].data.length);
								for(int l=0;j<actoanims[i].sets[j].instructions[k].data.length;l++) {
									dos.writeInt(actoanims[i].sets[j].instructions[k].data[l]);
								}
								dos.writeInt(actoanims[i].sets[j].instructions[k].conditionType);
								dos.writeInt(actoanims[i].sets[j].instructions[k].conditionData.length);
								for(int l=0;j<actoanims[i].sets[j].instructions[k].conditionData.length;l++) {
									dos.writeInt(actoanims[i].sets[j].instructions[k].conditionData[l]);
								}
							}
					}
			}
		dos.close();
		} catch(Exception e) {System.out.println(e.getMessage());}
		Drawing.animationPaused=false; Drawing.activeDraw=true;
		System.out.println("map saved!");
	}
	
	public static void updateAnims() {
		if(anims.length>0)
		for(int i=0;i<anims.length;i++)
		anims[i].nextFrame();
		if(moving) if(playeranim>=3) playeranim=0; else playeranim+=1;
	}
	
	public static void doAction() {
		if(!Drawing.messageBox && !Drawing.inventoryBox) actionpacks[actions[playerx][playery]].processAction(); else Drawing.messageBox=false;
	}
	
	public static void clearMap(int w, int h, int a, int b) {
		Drawing.activeDraw=false; Drawing.animationPaused=true;
		blocks = new Block[w][h][5];
		blocking = new Blocking[w][h];
		anims = new AnimatedBlock[b];
		ridges = new boolean[w][h];
		actions = new int[w][h];
		actionpacks = new ActionPack[a];
		upTo=blocks[0][0].length;
		for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
				for(int k=1;k<blocks[0][0].length;k++)
					blocks[i][j][k] = new Block(i*spriteSize, j*spriteSize, spriteSize, 0);
		for(int i=0;i<blocks.length;i++)
			for(int j=0;j<blocks[0].length;j++)
					blocks[i][j][0] = new Block(i*spriteSize, j*spriteSize, spriteSize, 8);
		for(int i=0;i<blocking.length;i++)
			for(int j=0;j<blocking[0].length;j++)
				blocking[i][j] = new Blocking(0);
		for(int i=0;i<anims.length;i++) anims[i] = new AnimatedBlock(0,0,0);
		for(int i=0;i<actionpacks.length;i++) actionpacks[i] = new ActionPack();
		Drawing.activeDraw=true; Drawing.animationPaused=false;
	}
	
}
