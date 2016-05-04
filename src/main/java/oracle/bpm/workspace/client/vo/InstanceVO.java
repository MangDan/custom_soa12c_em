package oracle.bpm.workspace.client.vo;

import java.io.Serializable;

import oracle.bpm.project.model.processes.BpmnType;

@SuppressWarnings("serial")
public class InstanceVO implements Serializable {
	private String partition;
	private int number;
	private String activityId;
	private String activityName;
	private BpmnType accessKey;
	private String agRootId;
	private String applicationContext;
	private String applicationName;
	private String taskId;
	private String assignee;
	private String assignees;
	private String assigneesDisplayName;
	private String assigneeType;
	private String assignmentContext;
	private String creator;
	private String createdDate;
	private String componentName;
	private String componentType;
	private String compositeDN;
	private String processDN;
	private long flowId;
	private String compositeInstanceId;
	private String compositeName;
	private String compositeVersion;
	private String processInstanceId;
	private String conversationId;
	private String processDueDate;
	private String endDate;
	private String expirationDate;
	private String expirationDuration;
	private String identityContext;
	private String priority;
	private String instanceId;
	private String isGroup;
	private String callbackContext;
	private int taskNumber;
	private String numberOfTimesModified;
	private String ownerGroup;
	private String ownerRole;
	private String ownerUser;
	private String parentComponentInstanceId;
	private String parentTaskId;
	private String parentThread;
	private String parentTaskVersion;
	private String subState;
	private String taskDefinitionId;
	private String taskDefinitionName;
	private String processName;
	private String state;
	private String step;
	private String thread;
	private String title;
	private String updatedBy;
	private String updatedByDisplayName;
	private String updatedDate;
	private String userComment;
	private int version;
	private String versionReason;
	
	public String getPartition() {
		return partition;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public BpmnType getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(BpmnType accessKey) {
		this.accessKey = accessKey;
	}
	public String getAgRootId() {
		return agRootId;
	}
	public void setAgRootId(String agRootId) {
		this.agRootId = agRootId;
	}
	public String getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(String applicationContext) {
		this.applicationContext = applicationContext;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAssignees() {
		return assignees;
	}
	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}
	public String getAssigneesDisplayName() {
		return assigneesDisplayName;
	}
	public void setAssigneesDisplayName(String assigneesDisplayName) {
		this.assigneesDisplayName = assigneesDisplayName;
	}
	public String getAssigneeType() {
		return assigneeType;
	}
	public void setAssigneeType(String assigneeType) {
		this.assigneeType = assigneeType;
	}
	public String getAssignmentContext() {
		return assignmentContext;
	}
	public void setAssignmentContext(String assignmentContext) {
		this.assignmentContext = assignmentContext;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	public String getCompositeDN() {
		return compositeDN;
	}
	public void setCompositeDN(String compositeDN) {
		this.compositeDN = compositeDN;
	}
	public String getProcessDN() {
		return processDN;
	}
	public void setProcessDN(String processDN) {
		this.processDN = processDN;
	}
	public long getFlowId() {
		return flowId;
	}
	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}
	public String getCompositeInstanceId() {
		return compositeInstanceId;
	}
	public void setCompositeInstanceId(String compositeInstanceId) {
		this.compositeInstanceId = compositeInstanceId;
	}
	public String getCompositeName() {
		return compositeName;
	}
	public void setCompositeName(String compositeName) {
		this.compositeName = compositeName;
	}
	public String getCompositeVersion() {
		return compositeVersion;
	}
	public void setCompositeVersion(String compositeVersion) {
		this.compositeVersion = compositeVersion;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getProcessDueDate() {
		return processDueDate;
	}
	public void setProcessDueDate(String processDueDate) {
		this.processDueDate = processDueDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getExpirationDuration() {
		return expirationDuration;
	}
	public void setExpirationDuration(String expirationDuration) {
		this.expirationDuration = expirationDuration;
	}
	public String getIdentityContext() {
		return identityContext;
	}
	public void setIdentityContext(String identityContext) {
		this.identityContext = identityContext;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	public String getCallbackContext() {
		return callbackContext;
	}
	public void setCallbackContext(String callbackContext) {
		this.callbackContext = callbackContext;
	}
	public int getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}
	public String getNumberOfTimesModified() {
		return numberOfTimesModified;
	}
	public void setNumberOfTimesModified(String numberOfTimesModified) {
		this.numberOfTimesModified = numberOfTimesModified;
	}
	public String getOwnerGroup() {
		return ownerGroup;
	}
	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}
	public String getOwnerRole() {
		return ownerRole;
	}
	public void setOwnerRole(String ownerRole) {
		this.ownerRole = ownerRole;
	}
	public String getOwnerUser() {
		return ownerUser;
	}
	public void setOwnerUser(String ownerUser) {
		this.ownerUser = ownerUser;
	}
	public String getParentComponentInstanceId() {
		return parentComponentInstanceId;
	}
	public void setParentComponentInstanceId(String parentComponentInstanceId) {
		this.parentComponentInstanceId = parentComponentInstanceId;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getParentThread() {
		return parentThread;
	}
	public void setParentThread(String parentThread) {
		this.parentThread = parentThread;
	}
	public String getParentTaskVersion() {
		return parentTaskVersion;
	}
	public void setParentTaskVersion(String parentTaskVersion) {
		this.parentTaskVersion = parentTaskVersion;
	}
	public String getSubState() {
		return subState;
	}
	public void setSubState(String subState) {
		this.subState = subState;
	}
	public String getTaskDefinitionId() {
		return taskDefinitionId;
	}
	public void setTaskDefinitionId(String taskDefinitionId) {
		this.taskDefinitionId = taskDefinitionId;
	}
	public String getTaskDefinitionName() {
		return taskDefinitionName;
	}
	public void setTaskDefinitionName(String taskDefinitionName) {
		this.taskDefinitionName = taskDefinitionName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getThread() {
		return thread;
	}
	public void setThread(String thread) {
		this.thread = thread;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedByDisplayName() {
		return updatedByDisplayName;
	}
	public void setUpdatedByDisplayName(String updatedByDisplayName) {
		this.updatedByDisplayName = updatedByDisplayName;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getVersionReason() {
		return versionReason;
	}
	public void setVersionReason(String versionReason) {
		this.versionReason = versionReason;
	}
}