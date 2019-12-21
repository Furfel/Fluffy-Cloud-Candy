package com.fccape;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.fccape.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FCC {
	
	public static JList actions = new JList();
	public static JList integers = new JList();
	public static JList strings = new JList();
	public static JList conditions = new JList();
	
	public static JFrame newActionDesigner = new JFrame("New action designer");
	public static ArrayList<Integer> newInts = new ArrayList<Integer>();
	public static ArrayList<String> newStrings = new ArrayList<String>();
	public static int[] newIntsA;
	public static String[] newStringsA;
	public static int newAction;
	public static JComboBox newActionCb = new JComboBox(Values.ActionNames);
	public static JList newStringsL = new JList();
	public static JList newIntsL = new JList();
	public static JTextField tfc = new JTextField("0");
	
	public static JFrame frm = new JFrame("FCC-ActionPacks Editor");
	
	public static JTextField datafield = new JTextField("Data");
	public static JButton dataapplybutton = new JButton("Apply");
	
	public static JButton loadbutton = new JButton("Load");
	public static JButton savebutton = new JButton("Save");
	public static JTextField filename = new JTextField("file path");
	public static JLabel activePackL = new JLabel("Pack: 0");
	public static JCheckBox checkAuto = new JCheckBox("autoTrigger");
	
	public static int activeAction = Values.ACTION_IDLE;
	public static int activePack = 0;
	public static int activeAct = 0;
	public static int activeData = 0;
	public static boolean activeString=false;
	public static ActionPack[] actionpacks;
	
	public static void main(String[] args) {
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		actions.setPreferredSize(new Dimension(300,300));
		integers.setPreferredSize(new Dimension(200,300));
		strings.setPreferredSize(new Dimension(200,300));
		
		JPanel loadsave = new JPanel();
		JButton incpack = new JButton(">");
		JButton decpack = new JButton("<");
		JButton addpack = new JButton("+");
		JButton rempack = new JButton("-");
		JButton newAct = new JButton("New Action");
		JButton remAct = new JButton("Del Action");
		JButton remCond = new JButton("Del Cond.");
		JButton addCond = new JButton("New Cond.");
		incpack.setPreferredSize(new Dimension(48,24));
		decpack.setPreferredSize(new Dimension(48,24));
		incpack.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { if(activePack<actionpacks.length-1) activePack++; activePackL.setText("Pack: "+Integer.toString(activePack)+ " of "+Integer.toString(actionpacks.length-1)); refreshActions();}});
		decpack.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { if(activePack>0) activePack--; activePackL.setText("Pack: "+Integer.toString(activePack)+ " of "+Integer.toString(actionpacks.length-1)); refreshActions();}});
		remAct.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { actionpacks[activePack].actions.remove(activeAct); refreshActions(); }});
		newAct.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { newActionDesigner.setVisible(true); }});
		addpack.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { ActionPack[] tmpacks = new ActionPack[actionpacks.length]; tmpacks=actionpacks; actionpacks = new ActionPack[tmpacks.length+1]; for(int _i=0;_i<tmpacks.length;_i++) actionpacks[_i]=tmpacks[_i]; actionpacks[actionpacks.length-1]=new ActionPack(); tmpacks=null; refreshActions(); activePackL.setText("Pack: "+Integer.toString(activePack)+ " of "+Integer.toString(actionpacks.length-1)); }});
		rempack.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { ActionPack[] tmpacks = new ActionPack[actionpacks.length]; tmpacks=actionpacks; actionpacks = new ActionPack[tmpacks.length-1]; int ink=0; for(int _i=0;_i<actionpacks.length;_i++) {if(_i==activePack) ink=1; actionpacks[_i]=tmpacks[_i+ink];} tmpacks=null; refreshActions(); activePackL.setText("Pack: "+Integer.toString(activePack)+ " of "+Integer.toString(actionpacks.length-1)); }});
		loadbutton.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { loadPack(filename.getText());}});
		savebutton.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { savePack(filename.getText());}});
		filename.setPreferredSize(new Dimension(200,24));
		datafield.setPreferredSize(new Dimension(120,24));
		datafield.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		dataapplybutton.setPreferredSize(new Dimension(70,24));
		dataapplybutton.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) { if(activeString)actionpacks[activePack].actions.get(activeAct).datas[activeData]=datafield.getText(); else actionpacks[activePack].actions.get(activeAct).data[activeData]=Integer.parseInt(datafield.getText()); refreshDatas(); }});
		checkAuto.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { actionpacks[activePack].autoTrigger=checkAuto.isSelected(); }});
		loadsave.add(filename);
		loadsave.add(loadbutton);
		loadsave.add(savebutton);
		JSeparator sep = new JSeparator();
		sep.setSize(20, 24);
		loadsave.add(sep);
		loadsave.add(remAct);
		loadsave.add(newAct);
		loadsave.add(rempack);
		loadsave.add(decpack);
		loadsave.add(activePackL);
		loadsave.add(incpack);
		loadsave.add(addpack);
		loadsave.add(checkAuto);
		loadsave.add(datafield);
		loadsave.add(dataapplybutton);
		
		JPanel datapanel = new JPanel();
		actions.addListSelectionListener(actionsListener);
		integers.addListSelectionListener(integersListener);
		strings.addListSelectionListener(stringsListener);
		datapanel.add(actions);
		datapanel.add(integers);
		datapanel.add(strings);
		
		JPanel actionbuttons = new JPanel();
		final JButton btnIdle = new JButton("IDLE");
		final JButton btnInstance = new JButton("ChINSTANCE");
		final JButton btnChMap = new JButton("ChMAP");
		final JButton btnChBlock = new JButton("ChBLOCK");
		final JButton btnChBlocking = new JButton("ChBLOCKING");
		final JButton btnTeleport = new JButton("TELEPORTPLAYER");
		final JButton btnAddAnim = new JButton("ADDANIMATIONPHASE");
		final JButton btnSpawnAnim = new JButton("SPAWNANIMATEDBLOCK");
		final JButton btnSetAnim = new JButton("SETANIMATEDBLOCKATTRIBUTES");
		final JButton btnMessage = new JButton("SHOWMESSAGEBOX");
		Font fnt = new Font(Font.SANS_SERIF, Font.BOLD, 9);
		btnInstance.setFont(fnt);
		btnAddAnim.setFont(fnt);
		btnChBlock.setFont(fnt);
		btnChBlocking.setFont(fnt);
		btnTeleport.setFont(fnt);
		btnSpawnAnim.setFont(fnt);
		btnSetAnim.setFont(fnt);
		btnMessage.setFont(fnt);
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				if(src==btnIdle) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_IDLE; refreshActions(); }
				else if(src==btnInstance) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_CHANGEINSTANCE; refreshActions(); }
				else if(src==btnChMap) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_CHANGEMAP; refreshActions(); }
				else if(src==btnChBlock) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_CHANGEBLOCK; refreshActions(); }
				else if(src==btnChBlocking) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_CHANGEBLOCKING; refreshActions(); }
				else if(src==btnTeleport) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_TELEPORTPLAYER; refreshActions(); }
				else if(src==btnAddAnim) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_ADDANIMATIONPHASE; refreshActions(); }
				else if(src==btnSpawnAnim) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_SPAWNANIMATEDBLOCK; refreshActions(); }
				else if(src==btnSetAnim) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_SETANIMATEDBLOCKATTRIBUTES; refreshActions(); }
				else if(src==btnMessage) { actionpacks[activePack].actions.get(activeAct).type = Values.ACTION_SHOWMESSAGEBOX; refreshActions(); }
			}};
		btnIdle.setPreferredSize(new Dimension(100,24));
		btnIdle.addActionListener(al);
		btnInstance.setPreferredSize(new Dimension(100,24));
		btnInstance.addActionListener(al);
		btnChMap.setPreferredSize(new Dimension(100,24));
		btnChMap.addActionListener(al);
		btnChBlock.setPreferredSize(new Dimension(100,24));
		btnChBlock.addActionListener(al);
		btnChBlocking.setPreferredSize(new Dimension(100,24));
		btnChBlocking.addActionListener(al);
		btnTeleport.setPreferredSize(new Dimension(100,24));
		btnTeleport.addActionListener(al);
		btnAddAnim.setPreferredSize(new Dimension(100,24));
		btnAddAnim.addActionListener(al);
		btnSpawnAnim.setPreferredSize(new Dimension(100,24));
		btnSpawnAnim.addActionListener(al);
		btnSetAnim.setPreferredSize(new Dimension(100,24));
		btnSetAnim.addActionListener(al);
		btnMessage.setPreferredSize(new Dimension(100,24));
		btnMessage.addActionListener(al);
		actionbuttons.add(btnIdle);
		actionbuttons.add(btnInstance);
		actionbuttons.add(btnChMap);
		actionbuttons.add(btnChBlock);
		actionbuttons.add(btnChBlocking);
		actionbuttons.add(btnTeleport);
		actionbuttons.add(btnAddAnim);
		actionbuttons.add(btnSpawnAnim);
		actionbuttons.add(btnSetAnim);
		actionbuttons.add(btnMessage);
		
		frm.getContentPane().add(actionbuttons, BorderLayout.SOUTH);
		frm.getContentPane().add(datapanel, BorderLayout.CENTER);
		frm.getContentPane().add(loadsave, BorderLayout.NORTH);
		frm.pack();
		
		JPanel newActionPanel = new JPanel();
		newActionPanel.add(newActionCb);
		newIntsL.setPreferredSize(new Dimension(100,200)); newStringsL.setPreferredSize(new Dimension(200,200));
		JPanel boxes = new JPanel(); boxes.add(newIntsL); boxes.add(newStringsL); newActionDesigner.add(boxes, BorderLayout.CENTER);
		JPanel controls = new JPanel(); JButton createBtn = new JButton("Create"); final JTextField newField = new JTextField(""); newField.setPreferredSize(new Dimension(100,24));
		JButton newAddInt = new JButton("+Int"); JButton newRemInt = new JButton("-Int"); JButton newAddStr = new JButton("+Str"); JButton newRemStr = new JButton("-Str");
		newAddInt.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newInts.add(Integer.parseInt(newField.getText())); newIntsL.setListData(newInts.toArray()); }});
		newAddStr.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newStrings.add(newField.getText()); newStringsL.setListData(newStrings.toArray()); }});
		newRemInt.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { int s=newIntsL.getSelectedIndex(); newInts.remove(s); newIntsL.setListData(newInts.toArray()); }});
		newRemStr.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { int s=newStringsL.getSelectedIndex(); newStrings.remove(s); newStringsL.setListData(newStrings.toArray()); }});
		createBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { newStringsA =  new String[newStrings.size()]; int _i=0; for(String strr:newStrings) {newStringsA[_i]=strr; _i++;} newIntsA = new int[newInts.size()]; _i=0; for(Integer intt:newInts) {newIntsA[_i]=intt; _i++;}
		Action tmpaction = new Action(newActionCb.getSelectedIndex(), newIntsA, newStringsA, Integer.parseInt(tfc.getText())); actionpacks[activePack].actions.add(tmpaction); newActionDesigner.setVisible(false); refreshActions(); }});
		controls.add(newAddInt); controls.add(newRemInt); controls.add(newField); controls.add(newAddStr); controls.add(newRemStr);
		newActionPanel.add(createBtn);
		newActionDesigner.add(newActionPanel, BorderLayout.NORTH);
		newActionDesigner.add(controls,BorderLayout.SOUTH);
		newActionDesigner.pack();
		
		frm.setVisible(true);
	}
	
	public static void loadPack(String fn) {
		try {
		System.out.println("Reading actionpacks from "+fn+".act");
		FileInputStream fis = new FileInputStream(fn+".act");
		DataInputStream dis = new DataInputStream(fis);
		int apl=dis.readInt();
		actionpacks=new ActionPack[apl];
			for(int i=0;i<actionpacks.length;i++) {
				actionpacks[i]=new ActionPack();
				actionpacks[i].autoTrigger=dis.readBoolean();
				int as=dis.readInt();
				for(int j=0;j<as;j++) {
					int atype=dis.readInt();
					int dl=dis.readInt();
					int[] tmpd;
					if(dl>0) {
						tmpd=new int[dl];
						for(int k=0;k<dl;k++)
							tmpd[k]=dis.readInt();
					} else tmpd=null;
					dl=dis.readInt();
					String[] tmps;
					if(dl>0) {
						tmps=new String[dl];
						for(int k=0;k<dl;k++) {
							tmps[k]="";
							int sl=dis.readInt();
							for(int l=0;l<sl;l++) tmps[k]+=(char)(dis.readByte());
						}
					} else tmps=null;
					actionpacks[i].addAction(atype, tmpd, tmps, 0);
				}
			}
		dis.close();
		} catch(Exception e) {System.out.println(e.getMessage());}
		refreshActions();
	}
	public static void savePack(String fn) {
		try {
			FileOutputStream fos = new FileOutputStream(fn+".act");
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(actionpacks.length);
				for(int i=0;i<actionpacks.length;i++) {
					dos.writeBoolean(actionpacks[i].autoTrigger);
					dos.writeInt(actionpacks[i].actions.size());
					for(int j=0;j<actionpacks[i].actions.size();j++) {
						dos.writeInt(actionpacks[i].actions.get(j).type);
						if(actionpacks[i].actions.get(j).data!=null) {
							dos.writeInt(actionpacks[i].actions.get(j).data.length);
							for(int k=0;k<actionpacks[i].actions.get(j).data.length;k++) {
								dos.writeInt(actionpacks[i].actions.get(j).data[k]);
							}
						} else dos.writeInt(0);
						if(actionpacks[i].actions.get(j).datas!=null) {
							dos.writeInt(actionpacks[i].actions.get(j).datas.length);
							for(int k=0;k<actionpacks[i].actions.get(j).datas.length;k++) {
								dos.writeInt(actionpacks[i].actions.get(j).datas[k].length());
								dos.write(actionpacks[i].actions.get(j).datas[k].getBytes());
							}
						} else dos.writeInt(0);
					}
				}
			dos.close();
			} catch(Exception e) {System.out.println(e.getMessage());}
		}
	
	public static void refreshActions() {
		actions.removeAll();
		ArrayList<String> lists = new ArrayList<String>();
		for(Action act : actionpacks[activePack].actions) {
			String tmp = Values.ActionNames[act.type]+"->ints:"+Integer.toString((act.data!=null)?(act.data.length):0)+"-strings:"+Integer.toString((act.datas!=null)?(act.datas.length):0);
			lists.add(tmp);
		}
		actions.setListData(lists.toArray());
		checkAuto.setSelected(actionpacks[activePack].autoTrigger);
	}
	
	public static void refreshDatas() {
		integers.setListData(new Object[0]);
		strings.setListData(new Object[0]);
		Action act = actionpacks[activePack].actions.get(activeAct);
		ArrayList<String> lists = new ArrayList<String>();
		if(act.data!=null)
		for(int i=0;i<act.data.length;i++) {String tmp = Integer.toString(act.data[i]); lists.add(tmp);}
		integers.setListData(lists.toArray());
		if(act.datas!=null)strings.setListData(act.datas);
	}
	
	public static ListSelectionListener actionsListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				activeAct=actions.getSelectedIndex();
				if(activeAct>=0) refreshDatas();
			}
		}
	};
	
	public static ListSelectionListener integersListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				activeData=integers.getSelectedIndex();
				activeString=false;
				datafield.setText((String)integers.getSelectedValue());
			}
		}
	};
	
	public static ListSelectionListener stringsListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()) {
				activeData=strings.getSelectedIndex();
				activeString=true;
				datafield.setText((String)strings.getSelectedValue());
			}
		}
	};
}
