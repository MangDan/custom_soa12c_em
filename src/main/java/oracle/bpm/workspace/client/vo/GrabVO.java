package oracle.bpm.workspace.client.vo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class GrabVO implements Serializable {
	private String instanceId;
	private String comment;
	private String mode;
	
	private List<ActivityInfo> openActivities;
	private List<VariableInfo> changeVariables;
	
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<ActivityInfo> getOpenActivities() {
		return openActivities;
	}

	public void setOpenActivities(List<ActivityInfo> openActivities) {
		this.openActivities = openActivities;
	}

	public List<VariableInfo> getChangeVariables() {
		return changeVariables;
	}

	public void setChangeVariables(List<VariableInfo> changeVariables) {
		this.changeVariables = changeVariables;
	}

	public class ActivityInfo {
		private String openActivityId;
		private String openActivityName;
		private String openActivityProcessId;
		private String targetActivityId;
		private String targetActivityName;
		private String targetActivityProcessId;
		
		public String getOpenActivityId() {
			return openActivityId;
		}
		
		public void setOpenActivityId(String openActivityId) {
			this.openActivityId = openActivityId;
		}
		
		public String getOpenActivityName() {
			return openActivityName;
		}
		
		public void setOpenActivityName(String openActivityName) {
			this.openActivityName = openActivityName;
		}
		
		public String getOpenActivityProcessId() {
			return openActivityProcessId;
		}
		
		public void setOpenActivityProcessId(String openActivityProcessId) {
			this.openActivityProcessId = openActivityProcessId;
		}
		
		public String getTargetActivityId() {
			return targetActivityId;
		}
		
		public void setTargetActivityId(String targetActivityId) {
			this.targetActivityId = targetActivityId;
		}
		
		public String getTargetActivityName() {
			return targetActivityName;
		}
		
		public void setTargetActivityName(String targetActivityName) {
			this.targetActivityName = targetActivityName;
		}
		
		public String getTargetActivityProcessId() {
			return targetActivityProcessId;
		}
		
		public void setTargetActivityProcessId(String targetActivityProcessId) {
			this.targetActivityProcessId = targetActivityProcessId;
		}
	}
	
	public class VariableInfo {
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
	}
}