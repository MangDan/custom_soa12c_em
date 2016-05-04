package oracle.bpm.workspace.client.vo;

import java.util.ArrayList;
import java.util.HashMap;

public class FlowEntryVO {
	public HashMap<String, Object> flowEntry = new HashMap();
	public ArrayList<FlowEntryVO> childFlowEntries = new ArrayList();

	public HashMap<String, Object> getflowEntry() {
		return this.flowEntry;
	}

	public void setFlowEntry(HashMap<String, Object> flowEntry) {
		this.flowEntry = flowEntry;
	}

	public void addChild(FlowEntryVO flowEntryVO) {
		this.childFlowEntries.add(flowEntryVO);
	}

	public ArrayList<FlowEntryVO> getChildInstances() {
		return this.childFlowEntries;
	}

	public void setChildInstances(ArrayList<FlowEntryVO> flowEntryVO) {
		this.childFlowEntries = flowEntryVO;
	}

	public String getInstanceId() {
		return (String) this.flowEntry.get("instanceId");
	}

	public String getParentInstanceId() {
		return (String) this.flowEntry.get("parentInstanceId");
	}

	public boolean isRoot() {
		if (this.flowEntry.get("parentInstanceId") == null) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "FlowEntryVO [flowEntry=" + this.flowEntry + ", childFlowEntries=" + this.childFlowEntries;
	}
}