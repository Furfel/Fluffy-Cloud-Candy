package com.furfel.fccmc;
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