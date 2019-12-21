package com.furfel.cfcc;

public class ActoAnim {
	
	InstructionSet[] sets;
	private int currentInst=0;
	private boolean multipass=false;
	
	public ActoAnim(int sets) {
		this.sets = new InstructionSet[sets];
	}
	
	public void doActoAnim() {
		InstructionSet tmp = sets[currentInst];
		for(int i=0;i<tmp.instructions.length;i++) {
			boolean conditionOk=false;
			switch(tmp.instructions[i].conditionType) {
				case Values.CONDITION_NONE: {conditionOk=true;} break;
				case Values.CONDITION_PLAYERAT: { if(Map.playerx==tmp.instructions[i].conditionData[0] && Map.playery==tmp.instructions[i].conditionData[1]) conditionOk=true; } break;
				case Values.CONDITION_MULTIPASS: { if(multipass) conditionOk=true; multipass=false; } break;
			}
			if(conditionOk)
			switch(tmp.instructions[i].type) {
				case Values.AA_NOTHING: {} break;
				case Values.AA_SETBLOCK: { Map.blocks[tmp.instructions[i].data[0]][tmp.instructions[i].data[1]][tmp.instructions[i].data[2]].id = tmp.instructions[i].data[3]; } break;
				case Values.AA_SETMULTIPASS: { multipass=true; } break;
			}
		}
		currentInst++; if(currentInst>=sets.length) currentInst=0;
	}
	
	public void newInstructionSet(int id,int instr) {
		sets[id] = new InstructionSet(instr);
	}
	
}

class InstructionSet {
	Instruction[] instructions;
	
	public InstructionSet(int num) {
		instructions = new Instruction[num];
		for(int i=0;i<num;i++) {
			instructions[i] = new Instruction();
		}
	}
}

class Instruction {
	int type;
	int[] data;
	int conditionType;
	int[] conditionData;
}