package edu.osu.sec.vsa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.osu.sec.vsa.base.StmtPoint;
import edu.osu.sec.vsa.utility.Logger;
import soot.SootField;

public class HeapObject implements IDGNode {
	DGraph dg;

	SootField sootField;


	boolean inited = false;
	boolean solved = false;
	ArrayList<ValuePoint> vps;
	HashSet<ValuePoint> solvedVps = new HashSet<ValuePoint>();

	ArrayList<HashMap<Integer, HashSet<String>>> result = new ArrayList<HashMap<Integer, HashSet<String>>>();
	static HashMap<String, HeapObject> hos = new HashMap<String, HeapObject>();

	private HeapObject(DGraph dg, SootField sootField) {
		this.dg = dg;
		this.sootField = sootField;
	}

	public static HeapObject getInstance(DGraph dg, SootField sootField) {
		String str = sootField.toString();
		if (!hos.containsKey(str)) {
			hos.put(str, new HeapObject(dg, sootField));
		}
		return hos.get(str);
	}

	@Override
	public Set<IDGNode> getDependents() {
		HashSet<IDGNode> dps = new HashSet<IDGNode>();
		for (ValuePoint vp : vps) {
			dps.add(vp);
		}
		return dps;

	}

	@Override
	public int getUnsovledDependentsCount() {
		int count = 0;
		for (IDGNode vp : getDependents()) {
			if (!vp.hasSolved()) {
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean hasSolved() {
		return solved;
	}

	@Override
	public void solve() {
		solved = true;
		Logger.print("[HEAP SOLVE]" + sootField + " (" + this.hashCode() + ")");
		// Logger.print("[HEAP SOLVE] soot field has " + vps.size() + " value points");

		for (ValuePoint vp : vps) {
			// Logger.print("[HEAP SOLVE] vp result is " + vp.toString());
			ArrayList<HashMap<Integer, HashSet<String>>> vpResult = vp.getResult();
			for (HashMap<Integer, HashSet<String>> res : vpResult) {
				if (res.containsKey(-1)) {
					result.add(res);
				}
			}
		}
		Logger.print("[HEAP SOLVE]" + sootField + " heap result is: " + result.toString());
	}

	@Override
	public boolean canBePartiallySolve() {
		boolean can = false;
		for (ValuePoint vp : vps) {
			if (!solvedVps.contains(vp) && vp.hasSolved()) {
				solvedVps.add(vp);
				can = true;
				for (HashMap<Integer, HashSet<String>> res : vp.getResult()) {
					if (res.containsKey(-1)) {
						result.add(res);
					}
				}
			}
		}
		if (can) {
			solved = true;
		}
		return can;
	}

	@Override
	public void initIfHavenot() {
		vps = new ArrayList<ValuePoint>();
		ValuePoint tmp;
		List<StmtPoint> sps = StmtPoint.findSetter(sootField);
		for (StmtPoint sp : sps) {
			tmp = new ValuePoint(dg, sp.getMethod_location(), sp.getBlock_location(), sp.getInstruction_location(), Collections.singletonList(-1));
			vps.add(tmp);
		}
		Logger.print("[HEAP INIT]" + sootField + " has " + StmtPoint.findSetter(sootField).size() + " setter");
		inited = true;

	}

	@Override
	public boolean inited() {
		return inited;
	}

	@Override
	public ArrayList<HashMap<Integer, HashSet<String>>> getResult() {
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sootField == null) ? 0 : sootField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeapObject other = (HeapObject) obj;
		if (sootField == null) {
			if (other.sootField != null)
				return false;
		} else if (!sootField.equals(other.sootField))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (!inited)
			return super.toString();
		StringBuilder sb = new StringBuilder();
		sb.append("===========================");
		sb.append(this.hashCode());
		sb.append("===========================\n");
		sb.append("Field: " + sootField + "\n");
		sb.append("Solved: " + hasSolved() + "\n");
		sb.append("Depend: ");
		for (IDGNode var : this.getDependents()) {
			sb.append(var.hashCode());
			sb.append(", ");
		}
		sb.append("\n");
		sb.append("ValueSet: \n");
		for (HashMap<Integer, HashSet<String>> resl : result) {
			sb.append("  ");
			for (int i : resl.keySet()) {
				sb.append(" |" + i + ":");
				for (String str : resl.get(i)) {
					sb.append(str + ",");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public SootField getSootField() {
		return sootField;
	}

	public void setSootField(SootField sootField) {
		this.sootField = sootField;
	}

}
