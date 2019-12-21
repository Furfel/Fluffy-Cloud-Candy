package com.furfel.cfccae;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class AnimatedBlock {
	public int drawx,drawy,mapx,mapy,obj;
	public int moveSpeed=16;
	public boolean overPlayer=false;
	public int curframe=0;
	ArrayList<AnimationPhase> frames = new ArrayList<AnimationPhase>();
	
	public void addPhase(int type, int data1, int data2) {
		AnimationPhase frame = new AnimationPhase(type,data1,data2);
		frames.add(frame);
	}
	
	public AnimatedBlock(int x, int y, int obj) {
		this.mapx=x; this.mapy=y; this.obj=obj; this.drawx=x*64; this.drawy=y*64;
	}
}

class AnimationPhase {
	public int type;
	public int data1,data2;
	public AnimationPhase(int type, int data1, int data2) {
		this.type=type;
		this.data1=data1;
		this.data2=data2;
	}
}

class AnimatedBlockConst {
	public final static int CHANGE_SPRITE=0;
	public final static int MOVE_OBJECT=1;
	public final static int FREEZE=2;
	public final static int DEFAULT_SPEED=16;
}
