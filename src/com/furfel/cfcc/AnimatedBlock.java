package com.furfel.cfcc;
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
	
	public void nextFrame() {
		if(frames.size()>0)
		if(mapx*Map.spriteSize==drawx && mapy*Map.spriteSize==drawy)
		{
			if(curframe==frames.size()) curframe=0;
			int type=frames.get(curframe).type;
			switch(type) {
				case AnimatedBlockConst.CHANGE_SPRITE: this.obj=frames.get(curframe).data1; break;
				case AnimatedBlockConst.MOVE_OBJECT: {this.mapx+=frames.get(curframe).data1; this.mapy+=frames.get(curframe).data2;} break;
				case AnimatedBlockConst.FREEZE: {} break;
			}
			curframe++;
		} else movement();
	}
	
	public void movement() {
		if(mapx*Map.spriteSize>drawx) drawx+=moveSpeed;
		if(mapx*Map.spriteSize<drawx) drawx-=moveSpeed;
		if(mapy*Map.spriteSize<drawy) drawy-=moveSpeed;
		if(mapy*Map.spriteSize>drawy) drawy+=moveSpeed;
	}
	
	public AnimatedBlock(int x, int y, int obj) {
		this.mapx=x; this.mapy=y; this.obj=obj; this.drawx=x*Map.spriteSize; this.drawy=y*Map.spriteSize;
	}
	
	public void draw(Graphics g) {
		if(obj!=0) {
			g.drawImage(Drawing.tileset1[(int) Math.round(Math.floor(obj/64))][obj%64], drawx - Map.vpx+320, drawy - Map.vpy+320, null);
			if(GameMain.showGrid) {g.setColor(Color.MAGENTA); g.drawRect(drawx - Map.vpx+320, drawy - Map.vpy+320, Map.spriteSize, Map.spriteSize);
			g.setColor(Color.RED); g.drawString(Integer.toString(obj), drawx+20-Map.vpx+320, drawy+30-Map.vpy+320);}
			}
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
