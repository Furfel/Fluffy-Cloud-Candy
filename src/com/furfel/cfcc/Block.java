package com.furfel.cfcc;
import java.awt.*;


public class Block extends Rectangle {

	public int id=0;
	private int tileset=0;
	
	public void draw(Graphics g) {
		if(id!=0) {
		g.setColor(Color.RED);
		g.drawImage(Drawing.tileset1[(int) Math.round(Math.floor(id/64))][id%64], x - Map.vpx+320, y - Map.vpy+320, null);
		if(GameMain.showGrid) {g.drawRect(x - Map.vpx+320, y - Map.vpy+320, width, height);
		if(id!=8) {g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12)); g.drawString(Integer.toString(id), x+20-Map.vpx+320, y+30-Map.vpy+320);}}
		}
	}
	
	public Block(int x, int y, int w, int id) {
		this.x=x;
		this.y=y;
		this.width=w;
		this.height=w;
		this.id=id;
		this.tileset=((id<64)?0:1);
	}
	
}
