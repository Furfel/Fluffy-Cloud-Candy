package com.furfel.cfcc;
import java.util.ArrayList;


public class ActionPack {
	ArrayList<Action> actions = new ArrayList<Action>();
	
	public boolean autoTrigger=false;
	private boolean conditionSatisfied=true;
	
	public void addAction(int type, int[] data, String[] datas, int conditions) {
		Action tmp = new Action(type,data,datas,conditions);
		actions.add(tmp);
		tmp=null;
	}
	
	public void processAction() {
		for(Action action : actions) {
			if(action.conditions.length>0) {
				for(int i=0;i<action.conditions.length;i++) {
					if(conditionSatisfied)
					switch(action.conditions[i].type) {
						case Values.CONDITION_NONE: { conditionSatisfied=true; } break;
						case Values.CONDITION_PLAYERAT: { if(Map.playerx==action.conditions[i].data[0] && Map.playery==action.conditions[i].data[1]) conditionSatisfied=true; else conditionSatisfied=false; } break;
						
						case Values.CONDITION_COMPARE_EQUALS: { if(Map.globalNumbers[action.conditions[i].data[0]]==Map.globalNumbers[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_LEQUAL: { if(Map.globalNumbers[action.conditions[i].data[0]]<=Map.globalNumbers[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_GEQUAL: { if(Map.globalNumbers[action.conditions[i].data[0]]>=Map.globalNumbers[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_LESS: { if(Map.globalNumbers[action.conditions[i].data[0]]<Map.globalNumbers[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_GREATER: { if(Map.globalNumbers[action.conditions[i].data[0]]>Map.globalNumbers[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_AND: { if(Map.globalSwitches[action.conditions[i].data[0]]==Map.globalSwitches[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						case Values.CONDITION_COMPARE_NOT: { if(Map.globalSwitches[action.conditions[i].data[0]]!=Map.globalSwitches[action.conditions[i].data[1]]) conditionSatisfied=true; } break;
						
						case Values.CONDITION_GLOBAL_EQUALS: { if(Map.globalNumbers[action.conditions[i].data[0]]==action.conditions[i].data[1]) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_LEQUAL: { if(Map.globalNumbers[action.conditions[i].data[0]]<=action.conditions[i].data[1]) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_GEQUAL: { if(Map.globalNumbers[action.conditions[i].data[0]]>=action.conditions[i].data[1]) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_LESS: { if(Map.globalNumbers[action.conditions[i].data[0]]<action.conditions[i].data[1]) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_GREATER: { if(Map.globalNumbers[action.conditions[i].data[0]]>action.conditions[i].data[1]) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_ISFALSE: { if(Map.globalSwitches[action.conditions[i].data[0]]==false) conditionSatisfied=true; } break;
						case Values.CONDITION_GLOBAL_ISTRUE: { if(Map.globalSwitches[action.conditions[i].data[0]]==true) conditionSatisfied=true; } break;
					}
				}
			} else conditionSatisfied=true;
			if(conditionSatisfied)
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
				case Values.ACTION_CHANGEMAP: {Map.loadMap(action.datas[0]);} break;
				case Values.ACTION_GIVEITEM: {int fp=GameMain.findFreePlace(); if(fp!=-1) GameMain.inventory[fp%8][(int)Math.round(Math.floor(fp/8))]=action.data[0]; else Drawing.showMessage("No free place in inventory!");} break;
				case Values.GLOBALVARS_SUBSTRACT: { Map.globalNumbers[action.data[0]]-=action.data[1]; } break;
				case Values.GLOBALVARS_ADD: { Map.globalNumbers[action.data[0]]+=action.data[1]; } break;
				case Values.GLOBALVARS_MULTIPLY: { Map.globalNumbers[action.data[0]]*=action.data[1]; } break;
				case Values.GLOBALVARS_FALSE: { Map.globalSwitches[action.data[0]]=false; } break;
				case Values.GLOBALVARS_TRUE: { Map.globalSwitches[action.data[0]]=true; } break;
				case Values.GLOBALVARS_NOT: { Map.globalSwitches[action.data[0]]=!Map.globalSwitches[action.data[0]]; } break;
				case Values.GLOBALVARS_SET: { Map.globalNumbers[action.data[0]]=action.data[1]; }; break;
			}
			conditionSatisfied=true;
		}
	}
	
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
	
	public void makeCondition(int index, int type, int[] data) {
		conditions[index].type=type;
		conditions[index].data=data;
	}
}

class Condition {
	public int type;
	public int[] data;
}