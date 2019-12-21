package com.furfel.cfccae;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AnimEditor {
	
	public static JFrame mainframe = new JFrame("Animation Editor");
	public static JButton loadBtn = new JButton("Load");
	public static JButton saveBtn = new JButton("Save");
	public static JTextField filename = new JTextField("/home/blackmonster/map.fcc");
	
	public static JList animationList = new JList();
	public static JList framesList = new JList();

	public static JComboBox frameTypes = new JComboBox(Values.animNames);
	public static JLabel data1l = new JLabel("data1:");
	public static JTextField data1tf = new JTextField();
	public static JLabel data2l = new JLabel("data2:");
	public static JTextField data2tf = new JTextField();
	public static JButton applyBtn = new JButton("Apply");
	public static JButton addBtn = new JButton("Add New");
	
	public static JTextField x = new JTextField("X");
	public static JTextField y = new JTextField("Y");
	public static JTextField obj = new JTextField("OBJ");
	public static JButton Apply = new JButton("Save Block");
	
	public static AnimatedBlock[] anims;
	
	public static int activeAnim=0;
	public static int activeFrame=0;
	
	public static int[][][] ids; public static int[][] bk; public static boolean[][] rg; public static int[][] ac; public static int px,py;
	
	public static void main(String[] args) {
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel fileloading = new JPanel();
		fileloading.add(filename);
		loadBtn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { loadMap(filename.getText()); } });
		fileloading.add(loadBtn);
		saveBtn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { saveMap(filename.getText()); } });
		fileloading.add(saveBtn);
		animationList.setPreferredSize(new Dimension(280,300));
		animationList.addListSelectionListener(new ListSelectionListener(){@Override public void valueChanged(ListSelectionEvent e) { if(!e.getValueIsAdjusting()) activeAnim=animationList.getSelectedIndex(); x.setText(Integer.toString(anims[activeAnim].mapx)); y.setText(Integer.toString(anims[activeAnim].mapy)); obj.setText(Integer.toString(anims[activeAnim].obj)); refreshFrames();} });
		framesList.setPreferredSize(new Dimension(180,300));
		framesList.addListSelectionListener(new ListSelectionListener(){@Override public void valueChanged(ListSelectionEvent e) { if(!e.getValueIsAdjusting()) {activeFrame=framesList.getSelectedIndex(); frameTypes.setSelectedIndex(anims[activeAnim].frames.get(activeFrame).type); data1tf.setText(Integer.toString(anims[activeAnim].frames.get(activeFrame).data1)); data2tf.setText(Integer.toString(anims[activeAnim].frames.get(activeFrame).data2)); } }});
		animationList.setBackground(new Color(255,255,150));
		JPanel lists = new JPanel();
		lists.add(animationList); lists.add(framesList);
		
		JPanel editor = new JPanel();
		editor.add(frameTypes);
		editor.add(data1l);
		data1tf.setPreferredSize(new Dimension(30,24));
		editor.add(data1tf);
		editor.add(data2l);
		data2tf.setPreferredSize(new Dimension(30,24));
		editor.add(data2tf);
		applyBtn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { anims[activeAnim].frames.get(activeFrame).data1=Integer.parseInt(data1tf.getText()); anims[activeAnim].frames.get(activeFrame).data2=Integer.parseInt(data2tf.getText()); anims[activeAnim].frames.get(activeFrame).type=frameTypes.getSelectedIndex(); refreshFrames(); } });
		editor.add(applyBtn);
		addBtn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { AnimationPhase tmp = new AnimationPhase(frameTypes.getSelectedIndex(),Integer.parseInt(data1tf.getText()),Integer.parseInt(data2tf.getText())); anims[activeAnim].frames.add(tmp); tmp=null; refreshFrames(); refreshAnims(); } });
		editor.add(addBtn);
		
		JPanel maindata = new JPanel();
		Apply.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { anims[activeAnim].mapx=Integer.parseInt(x.getText()); anims[activeAnim].mapy=Integer.parseInt(y.getText()); anims[activeAnim].obj=Integer.parseInt(obj.getText()); refreshAnims(); } });
		x.setPreferredSize(new Dimension(32,24)); y.setPreferredSize(new Dimension(32,24)); obj.setPreferredSize(new Dimension(32,24));
		maindata.add(x); maindata.add(y); maindata.add(obj); maindata.add(Apply);
		
		JPanel editor2 = new JPanel();
		editor2.add(editor,BorderLayout.NORTH);
		editor2.add(maindata,BorderLayout.SOUTH);
		
		mainframe.add(fileloading, BorderLayout.NORTH);
		mainframe.add(lists,BorderLayout.CENTER);
		mainframe.add(editor2, BorderLayout.SOUTH);
		mainframe.pack();
		mainframe.setVisible(true);
	}
	
	public static void loadMap(String fn) {
		System.out.println("Loading map "+fn);
		try {
		FileInputStream fis = new FileInputStream(fn);
		DataInputStream dis = new DataInputStream(fis);
		int bx=dis.readInt(); int by=dis.readInt(); px=dis.readInt(); py=dis.readInt();
		ids = new int[bx][by][5]; bk=new int[bx][by]; rg=new boolean[bx][by]; ac=new int[bx][by];
		for(int j=0;j<by;j++)
			for(int i=0;i<bx;i++) {
				for(int k=0;k<5;k++)
					ids[i][j][k]=dis.readInt();
				bk[i][j]=dis.readInt();
				rg[i][j]=dis.readBoolean();
				ac[i][j]=dis.readInt();
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
		} catch(Exception e) {System.out.println(e.getMessage());}
		System.out.println("Map loaded!");
		refreshAnims();
	}
	
	public static void saveMap(String fn) {
		System.out.println("saving map...");
		try {
		FileOutputStream fos = new FileOutputStream(fn);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeInt(ids.length);
		dos.writeInt(ids[0].length);
		dos.writeInt(px);
		dos.writeInt(py);
		for(int j=0;j<ids[0].length;j++)
			for(int i=0;i<ids.length;i++) {
				for(int k=0;k<5;k++)
					dos.writeInt(ids[i][j][k]);
				//dos.writeInt(Integer.parseInt(((blocking[i][j].up)?"1":"0")+((blocking[i][j].down)?"1":"0")+((blocking[i][j].left)?"1":"0")+((blocking[i][j].right)?"1":"0"), 2));
				//int tmp=0; if(blocking[i][j].up) tmp+=8; if(blocking[i][j].down) tmp+=4; if(blocking[i][j].left) tmp+=2; if(blocking[i][j].right) tmp+=1;
				dos.writeInt(bk[i][j]);
				dos.writeBoolean(rg[i][j]);
				dos.writeInt(ac[i][j]);
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
		dos.close();
		} catch(Exception e) {System.out.println(e.getMessage());}
		System.out.println("map saved!");
	}
	
	public static void refreshAnims() {
		String[] tmp=new String[anims.length];
		for(int i=0;i<anims.length;i++) {
			tmp[i]="";
			tmp[i]+=Integer.toString(i)+"->Sprite:"+Integer.toString(anims[i].obj)+" Frames:"+Integer.toString(anims[i].frames.size())+" Pos:"+Integer.toString(anims[i].mapx)+","+Integer.toString(anims[i].mapy);
		}
		animationList.setListData(tmp);
		activeFrame=0;
	}
	
	public static void refreshFrames() {
		String[] tmp=new String[anims[activeAnim].frames.size()];
		for(int i=0;i<anims[activeAnim].frames.size();i++) {
			tmp[i]="";
			tmp[i]+=Integer.toString(i)+"->"+Integer.toString(anims[activeAnim].frames.get(i).data1)+":"+Integer.toString(anims[activeAnim].frames.get(i).data2)+":"+Values.animNames[anims[activeAnim].frames.get(i).type];
		}
		framesList.setListData(tmp);
	}
}
