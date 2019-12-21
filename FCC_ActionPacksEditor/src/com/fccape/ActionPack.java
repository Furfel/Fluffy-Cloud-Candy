package com.fccape;
import java.util.ArrayList;


public class ActionPack {
	ArrayList<Action> actions = new ArrayList<Action>();
	
	public boolean autoTrigger=false;
	
	public void addAction(int type, int[] data, String[] datas, int cnd) {
		Action tmp = new Action(type,data,datas,cnd);
		actions.add(tmp);
		tmp=null;
	}
	
	/*public void processAction() {
		for(Action action : actions) {
			switch(action.type) {
				case Values.ACTION_IDLE: {} break;
				case Values.ACTION_CHANGEBLOCK: {Map.blocks[action.data[0]][action.data[1]][action.data[2]].id=action.data[3];} break;
				case Values.ACTION_CHANGEBLOCKING: {Map.blocking[action.data[0]][action.data[1]]=new Blocking(action.data[2]);} break;
				case Values.ACTION_TELEPORTPLAYER: {Map.playerx=action.data[0]; Map.playery=action.data[1]; Map.vpx=Map.playerx*Map.spriteSize;  Map.vpy=Map.playery*Map.spriteSize;} break;
				case Values.ACTION_CHANGEINSTANCE: {Map.actions[action.data[0]][action.data[1]]=action.data[2];} break;
				case Values.ACTION_ADDANIMATIONPHASE: {Map.anims[action.data[0]].addPhase(action.data[1], action.data[2], action.data[3]);} break;
				case Values.ACTION_SPAWNANIMATEDBLOCK: {Map.anims[action.data[0]].mapx=action.data[1]; Map.anims[action.data[0]].mapy=action.data[2]; Map.anims[action.data[0]].drawx=Map.anims[action.data[0]].mapx*Map.spriteSize; Map.anims[action.data[0]].drawy=Map.anims[action.data[0]].mapy*Map.spriteSize;} break;
				case Values.ACTION_SETANIMATEDBLOCKATTRIBUTES: {Map.anims[action.data[0]].obj=action.data[1]; Map.anims[action.data[0]].moveSpeed=action.data[2];} break;
				case Values.ACTION_SHOWMESSAGEBOX: {Drawing.showMessage(action.datas[0]);} break;
			}
		}
	}*/
	
}

class Action {
	public int type;
	public String[] datas;
	public int[] data;
	public Condition[] conditions;
	
	public Action(int type, int[] data, String[] datas, int conditions) {
		this.type=type;
		
		if(datas!=null){
		this.datas=new String[datas.length];
		for(int i=0;i<datas.length;i++) {
			this.datas[i]=datas[i];
		}}
		
		if(data!=null){
		this.data=new int[data.length];
		for(int i=0;i<data.length;i++) {
			this.data[i]=data[i];
		}}
		
		this.conditions = new Condition[conditions];
	}
}

class Condition {
	public int type;
	public int[] data;
}