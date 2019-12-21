package com.furfel.cfcc;

public class Blocking {
	public boolean up=false,down=false,left=false,right=false;
	
	public Blocking(int mode) {
		if(mode!=0) {
			String tmp = Integer.toBinaryString(mode);
			if(tmp.length()==3) tmp="0"+tmp; else if(tmp.length()==2) tmp="00"+tmp; else if(tmp.length()==1) tmp="000"+tmp;
			if(tmp.length()>=1) if(tmp.charAt(0)=='1') up=true;
			if(tmp.length()>=2) if(tmp.charAt(1)=='1') down=true;
			if(tmp.length()>=3) if(tmp.charAt(2)=='1') left=true;
			if(tmp.length()>=4) if(tmp.charAt(3)=='1') right=true;
		}
	}
}
