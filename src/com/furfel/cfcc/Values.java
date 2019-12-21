package com.furfel.cfcc;

public class Values {
	public static final String DEBUG_CONTROLS1="G-show/hide grid W,S,A,D-put wall Q-toggle quickMove J,K,L,;,'-set layer 0-4 [,]-inc/dec selObj";
	public static final String DEBUG_CONTROLS2="\\-put selObj on layer R-ridge P-toggle anim O-nextAnimFrame F7-saveMap F8-loadMap";
	public static final String GAME_VERSION="Codename: Fluffy Cloud Candy Version 0.2 alpha Developer Preview";
	public static final byte MAP_VERSION=1;
	
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
	public static final int GLOBALVARS_SUBSTRACT=11;
	public static final int GLOBALVARS_ADD=12;
	public static final int GLOBALVARS_FALSE=13;
	public static final int GLOBALVARS_TRUE=14;
	public static final int GLOBALVARS_NOT=15;
	public static final int GLOBALVARS_SET=16;
	public static final int GLOBALVARS_MULTIPLY=17;
	
	public static final int AA_NOTHING=0;
	public static final int AA_SETBLOCK=1;
	public static final int AA_SETMULTIPASS=2;
	
	public static final int CONDITION_NONE=0;
	public static final int CONDITION_PLAYERAT=1;
	public static final int CONDITION_MULTIPASS=2;
	public static final int CONDITION_PLAYERHASITEM=3;
	public static final int CONDITION_PLAYERHASNOITEM=4;
	public static final int CONDITION_GLOBAL_EQUALS=5;
	public static final int CONDITION_GLOBAL_LESS=6;
	public static final int CONDITION_GLOBAL_GREATER=7;
	public static final int CONDITION_GLOBAL_LEQUAL=8;
	public static final int CONDITION_GLOBAL_GEQUAL=9;
	public static final int CONDITION_GLOBAL_ISTRUE=10;
	public static final int CONDITION_GLOBAL_ISFALSE=11;
	public static final int CONDITION_COMPARE_EQUALS=12;
	public static final int CONDITION_COMPARE_LESS=13;
	public static final int CONDITION_COMPARE_GREATER=14;
	public static final int CONDITION_COMPARE_LEQUAL=15;
	public static final int CONDITION_COMPARE_GEQUAL=16;
	public static final int CONDITION_COMPARE_AND=17;
	public static final int CONDITION_COMPARE_NOT=18;
	
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
