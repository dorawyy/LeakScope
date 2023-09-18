package edu.osu.sec.vsa.backwardslicing;

import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.Block;

public class CallStackItem {
	SootMethod sm;
	Block blcok;
	Unit currentInstruction;
	Value returnTarget;

	public CallStackItem(SootMethod sm, Block blcok, Unit currentInstruction, Value returnTarget) {
		super();
		this.sm = sm;
		this.blcok = blcok;
		this.currentInstruction = currentInstruction;
		this.returnTarget = returnTarget;
	}

	public SootMethod getSootMethod() {
		return sm;
	}

	public void setSootMethod(SootMethod sm) {
		this.sm = sm;
	}

	public Block getBlcok() {
		return blcok;
	}

	public void setBlcok(Block blcok) {
		this.blcok = blcok;
	}

	public Unit getCurrentInstruction() {
		return currentInstruction;
	}

	public void setCurrentInstruction(Unit currentInstruction) {
		this.currentInstruction = currentInstruction;
	}

	public Value getReturnTarget() {
		return returnTarget;
	}

	public void setReturnTarget(Value returnTarget) {
		this.returnTarget = returnTarget;
	}
}
