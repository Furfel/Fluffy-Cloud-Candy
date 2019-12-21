package com.fccape;
public class Values {
	public static final String DEBUG_CONTROLS1="G-show/hide grid W,S,A,D-put wall Q-toggle quickMove J,K,L,;,'-set layer 0-4 [,]-inc/dec selObj";
	public static final String DEBUG_CONTROLS2="\\-put selObj on layer R-ridge P-toggle anim O-nextAnimFrame F7-saveMap F8-loadMap";
	public static final String GAME_VERSION="Codename: Fluffy Cloud Candy Version 0.1 alpha Developer Preview";
	public static final int PLAYER_DOWN=1;
	public static final int PLAYER_LEFT=2;
	public static final int PLAYER_RIGHT=3;
	public static final int PLAYER_UP=4;
	
	public static final int ACTION_IDLE=0;
	public static final int ACTION_CHANGEINSTANCE=1;
	public static final int ACTION_CHANGEMAP=2;
	public static final int ACTION_CHANGEBLOCK=3;
	public static final int ACTION_CHANGEBLOCKING=4;
	public static final int ACTION_TELEPORTPLAYER=5;
	public static final int ACTION_ADDANIMATIONPHASE=6;
	public static final int ACTION_SPAWNANIMATEDBLOCK=7;
	public static final int ACTION_SETANIMATEDBLOCKATTRIBUTES=8;
	public static final int ACTION_SHOWMESSAGEBOX=9;
	public static final int ACTION_GIVEITEM=10;
	
	public static final String[] ActionNames = {"IDLE","CHINSTANCE","CHMAP","CHBLOCK","CHBLOCKING","TELEPORT","ADDANIMPHASE","SPAWNANIMBLOCK","SETANIMATTRIB","MESSAGE"};
	
	public static final int SET_LAYER_0 = 0;
	public static final int SET_LAYER_1 = 1;
	public static final int SET_LAYER_2 = 2;
	public static final int SET_LAYER_3 = 3;
	public static final int SET_LAYER_4 = 4;
	public static final int SET_BLOCKING_UP = 5;
	public static final int SET_BLOCKING_DOWN = 6;
	public static final int SET_BLOCKING_LEFT = 7;
	public static final int SET_BLOCKING_RIGHT = 8;
}
