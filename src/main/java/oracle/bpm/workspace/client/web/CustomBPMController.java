package oracle.bpm.workspace.client.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.repos.Column;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpm.services.instancemanagement.model.IActivityInfo;
import oracle.bpm.services.instancemanagement.model.IFlowChangeItem;
import oracle.bpm.services.instancemanagement.model.IGrabInstanceContextRequest;
import oracle.bpm.services.instancemanagement.model.IGrabInstanceContextResponse;
import oracle.bpm.services.instancemanagement.model.IGrabInstanceRequest;
import oracle.bpm.services.instancemanagement.model.IGrabInstanceResponse;
import oracle.bpm.services.instancemanagement.model.IInstanceContextConfiguration;
import oracle.bpm.services.instancemanagement.model.IInstanceSummary;
import oracle.bpm.services.instancemanagement.model.IOpenActivityInfo;
import oracle.bpm.services.instancemanagement.model.IProcessInstance;
import oracle.bpm.services.instancemanagement.model.IVariableItem;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.ActivityInfo;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.FlowChangeItem;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.GrabInstanceContextRequest;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.GrabInstanceRequest;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.InstanceContextConfiguration;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.LocationInfo;
import oracle.bpm.services.instancemanagement.model.impl.alterflow.VariableItem;
import oracle.bpm.services.instancequery.IColumnConstants;
import oracle.bpm.services.instancequery.IInstanceQueryInput;
import oracle.bpm.services.instancequery.IInstanceQueryService;
import oracle.bpm.services.instancequery.impl.InstanceQueryInput;
import oracle.bpm.workspace.client.config.SOAServiceClient;
import oracle.bpm.workspace.client.constants.OracleConstants;
import oracle.bpm.workspace.client.vo.GrabVO;
import oracle.bpm.workspace.client.vo.InstanceVO;

@Controller
public class CustomBPMController {
	@Resource(name = "soaClient")
	protected SOAServiceClient soaClient;
	private Logger logger = LoggerFactory.getLogger(this.getClass()); // Logger

	@RequestMapping("/bpm/process/instance/listbyfilter")
	public String process_instances(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {

		IBPMContext bpmCtx = soaClient.getBPMContext();

		logger.debug("process_instances.flowid : " + (String) params.get("flow_id"));
		logger.debug("process_instances.instanceId : " + (String) params.get("instanceId"));
		logger.debug("process_instances.componentname : " + (String) params.get("componentName"));
		logger.debug("process_instances.state : " + (String) params.get("state"));
		logger.debug("process_instances.assignmentFilter : " + (String) params.get("assignmentFilter"));
		logger.debug("process_instances.maxRows : " + (String) params.get("maxRows"));

		String flowid = (String) params.get("flowid");
		String instanceid = (String) params.get("instanceId");
		String componentname = (String) params.get("componentname");
		String cpage = "";
		String rowSize = "";

		if (params.get(OracleConstants.PARAM.PAGE.CURRENT_PAGE) == null)
			cpage = "0";
		else
			cpage = (String) params.get(OracleConstants.PARAM.PAGE.CURRENT_PAGE);

		if (params.get(OracleConstants.PARAM.PAGE.CURRENT_PAGE) == null)
			rowSize = "10";
		else
			rowSize = (String) params.get(OracleConstants.PARAM.PAGE.MAX_ROWS);

		int startRow = Integer.parseInt(cpage) * Integer.parseInt(rowSize) + 1;
		int endRow = startRow + Integer.parseInt(rowSize) - 1;

		List<Column> displayColumns = new ArrayList<Column>();
		displayColumns.add(IColumnConstants.PROCESS_FLOWID_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_NUMBER_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_INSTANCEID_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_COMPONENTNAME_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_COMPONENTTYPE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_COMPOSITEDN_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_COMPOSITEINSTANCEID_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ACTIVITYID_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ACTIVITYNAME_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_ACTIVITYTYPE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ID_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_PROCESSNAME_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_VERSION_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_STATE_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_TITLE_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_CREATOR_COLUMN);
		displayColumns.add(IColumnConstants.PROCESS_CREATEDDATE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ASSIGNEE_TASKID_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_DUEDATE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ENDDATE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_UPDATEDDATE_COLUMN);
		// displayColumns.add(IColumnConstants.PROCESS_ASSIGNEES_COLUMN);

		/*
		 * other columns....
		 * displayColumns.add(IColumnConstants.PROCESS_AGROOT_ID);
		 * displayColumns.add(IColumnConstants.PROCESS_APPLICATIONCONTEXT_COLUMN
		 * );
		 * displayColumns.add(IColumnConstants.PROCESS_APPLICATIONNAME_COLUMN);
		 * 
		 * displayColumns.add(IColumnConstants.PROCESS_ASSIGNEEATTRIBUTE_COLUMN)
		 * ; ; displayColumns.add(IColumnConstants.
		 * PROCESS_ASSIGNEESDISPLAYNAME_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_ASSIGNEETYPEATTRIBUTE_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_ASSIGNMENTCONTEXT_COLUMN)
		 * ;
		 * 
		 * displayColumns.add(IColumnConstants.PROCESS_COMPOSITENAME_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_COMPOSITEVERSION_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_CONVERSATIONID_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_DUEDATE_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_ENDDATE_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_EXPIRATIONDATE_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_EXPIRATIONDURATION_COLUMN
		 * );
		 * displayColumns.add(IColumnConstants.PROCESS_IDENTITYCONTEXT_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_INSTANCE_PRIORITY_COLUMN)
		 * ; displayColumns.add(IColumnConstants.PROCESS_ISGROUP_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_MAIN_THREAD_REAL_STATE);
		 * displayColumns.add(IColumnConstants.PROCESS_NUMBER_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_NUMBEROFTIMESMODIFIED_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_OWNERGROUP_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_OWNERROLE_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_OWNERUSER_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_PARENTCOMPONENTINSTANCEID_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_PARENTID_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_PARENTVERSION_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_PARENTTHREAD_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_PREVIOUS_STATE_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_PROCESSDEFINITIONID_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_PROCESSDEFINITIONNAME_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_STEP_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_THREAD_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_UPDATEDBY_COLUMN);
		 * displayColumns.add(IColumnConstants.
		 * PROCESS_UPDATEDBYDISPLAYNAME_COLUMN);
		 * displayColumns.add(IColumnConstants.PROCESS_USER_COMMENTS);
		 * displayColumns.add(IColumnConstants.PROCESS_VERSIONREASON_COLUMN);
		 */

		Ordering ordering = new Ordering(IColumnConstants.PROCESS_INSTANCEID_COLUMN, false, true);
		Predicate pred = null;

		pred = new Predicate(IColumnConstants.PROCESS_COMPONENTTYPE_COLUMN, Predicate.OP_EQ, "BPMN");

		logger.debug("instance flowid : " + IColumnConstants.PROCESS_FLOWID_COLUMN.getName());
		// flowId 조회
		if (!"".equals(params.get(IColumnConstants.PROCESS_FLOWID_COLUMN.getName().toLowerCase()))
				&& params.get(IColumnConstants.PROCESS_FLOWID_COLUMN.getName().toLowerCase()) != null)
			pred.addClause(Predicate.AND, IColumnConstants.PROCESS_FLOWID_COLUMN, Predicate.OP_EQ,
					params.get(IColumnConstants.PROCESS_FLOWID_COLUMN.getName().toLowerCase()));

		logger.debug("instance state : " + IColumnConstants.PROCESS_STATE_COLUMN.getName());
		// 상태 조회
		if (!"".equals(params.get(IColumnConstants.PROCESS_STATE_COLUMN.getName()))
				&& params.get(IColumnConstants.PROCESS_STATE_COLUMN.getName()) != null)
			pred.addClause(Predicate.AND, IColumnConstants.PROCESS_STATE_COLUMN, Predicate.OP_EQ,
					params.get(IColumnConstants.PROCESS_STATE_COLUMN.getName()));

		logger.debug("instance title : " + IColumnConstants.PROCESS_TITLE_COLUMN.getName());
		// 제목조회
		if (!"".equals(params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()))
				&& params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()) != null)
			pred.addClause(Predicate.AND, IColumnConstants.PROCESS_TITLE_COLUMN, Predicate.OP_LIKE,
					"%" + params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()) + "%");

		logger.debug("componentname : " + IColumnConstants.PROCESS_COMPONENTNAME_COLUMN.getName());
		if (!"".equals(params.get(IColumnConstants.PROCESS_COMPONENTNAME_COLUMN.getName()))
				&& params.get(IColumnConstants.PROCESS_COMPONENTNAME_COLUMN.getName()) != null)
			// pred.addClause(Predicate.AND,
			// IColumnConstants.PROCESS_PROCESSNAME_COLUMN, Predicate.OP_EQ,
			// processname);
			pred.addClause(Predicate.AND, IColumnConstants.PROCESS_COMPONENTNAME_COLUMN, Predicate.OP_EQ,
					params.get(IColumnConstants.PROCESS_COMPONENTNAME_COLUMN.getName()));

		logger.debug("pred : " + pred.getString());
		/*
		 * 안됨... (Title로 검색하는 방식으로 변경함..) logger.debug("instanceId Column : " +
		 * IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()); logger.debug(
		 * "instanceId Value : " +
		 * params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()));
		 * if(!"".equals(params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.
		 * getName())) &&
		 * params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()) !=
		 * null) //pred.addClause(Predicate.AND,
		 * IColumnConstants.PROCESS_PROCESSNAME_COLUMN, Predicate.OP_EQ,
		 * processname); pred.addClause(Predicate.AND,
		 * IColumnConstants.PROCESS_INSTANCEID_COLUMN, Predicate.OP_EQ,
		 * params.get(IColumnConstants.PROCESS_INSTANCEID_COLUMN.getName()));
		 */

		IInstanceQueryInput input = new InstanceQueryInput();
		if (params.get("assignmentFilter") == null || params.get("assignmentFilter").equals("ALL"))
			input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.ALL);
		else if (params.get("assignmentFilter").equals("MY"))
			input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.MY);
		else if (params.get("assignmentFilter").equals("GROUP"))
			input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.GROUP);
		else if (params.get("assignmentFilter").equals("MY_AND_GROUP"))
			input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.MY_AND_GROUP);
		else
			input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.ADMIN);

		input.setStartRow(startRow);
		input.setEndRow(endRow);

		// Size 체크할 경우를 위해서 페이징 제거 파라미터 추가 (unlimited)
		/*
		 * if(params.get("pageSize") == null ||
		 * params.get("pageSize").equals("PAGING")) {
		 * input.setStartRow(startRow); //신한은행 PoC 땜에 주석 by kdh (140528)
		 * input.setEndRow(endRow); }
		 */

		// state : OPEN, COMPLETED, ERRORED, CANCELED, ABORTED, STALE
		// title : %%
		// WHERE STATE = ? AND TITLE LIKE ? AND COMPONENT='BPMN' and
		// assignmentFilter=ALL (모두)

		List<IProcessInstance> instances = soaClient.getBPMServiceClient().getInstanceQueryService()
				.queryInstances(bpmCtx, displayColumns, pred, ordering, null);

		List<InstanceVO> result = new ArrayList<InstanceVO>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		for (IProcessInstance instance : instances) {

			InstanceVO instanceVO = new InstanceVO();
			instanceVO.setPartition(instance.getProcessDN().substring(0, instance.getProcessDN().indexOf("/")));
			// instanceVO.setNumber(instance.getSystemAttributes().getProcessNumber());
			// //PROCESS_NUMBER_COLUMN
			instanceVO.setFlowId(instance.getSca().getFlowId());
			instanceVO.setInstanceId(instance.getCubeInstanceId());
			// instanceVO.setProcessInstanceId(instance.getSystemAttributes().getProcessInstanceId());
			instanceVO.setComponentName(instance.getSca().getComponentName());
			// instanceVO.setComponentType(instance.getSystemAttributes().getComponentType());
			instanceVO.setCompositeDN(instance.getSca().getCompositeDN());
			instanceVO.setProcessDN(instance.getProcessDN());
			instanceVO.setCompositeInstanceId(instance.getSca().getCompositeInstanceId());
			// instanceVO.setActivityName(instance.getSystemAttributes().getActivityName());
			// instanceVO.setAccessKey(instance.getSystemAttributes().getActivityType());
			// // PROCESS_ACTIVITYTYPE_COLUMN
			// instanceVO.setTaskId(instance.getSystemAttributes().); //
			// PROCESS_ID_COLUMN
			instanceVO.setProcessName(instance.getProcessName());
			// instance.getSystemAttributes().getStep();
			// instanceVO.setVersion(instance.getSystemAttributes().getVersion());
			instanceVO.setState(instance.getSystemAttributes().getState());
			instanceVO.setTitle(instance.getTitle());
			instanceVO.setCreator(instance.getCreator());
			// instanceVO.setActivityName(OBPMUtility.getProcessActivityNameKo(instance.getSystemAttributes().getActivityName()));

			// BGF PoC를 위해 사용자만 가져오도록 수정
			// BPM 에서 나오는 사용자 정보는 모든 롤과 사용자를 다 가져옴 --> 이상함(?)
			// instanceVO.setAssigneesDisplayName(WorkflowUtility.getAssigneeStringFromIdentityType(instance.getSystemAttributes().getAssignees()));

			// instanceVO.setCreatedDate(WorkflowUtility.getTimeZoneBasedDateString(df,
			// instance.getSystemAttributes().getCreatedDate(),
			// ctx.getTimeZone()));
			// instanceVO.setProcessDueDate(WorkflowUtility.getTimeZoneBasedDateString(df,
			// instance.getSystemAttributes().getProcessDueDate(),
			// bpmCtx.getTimeZone()));
			// instanceVO.setEndDate(WorkflowUtility.getTimeZoneBasedDateString(df,
			// instance.getSystemAttributes().getUpdatedDate(),
			// bpmCtx.getTimeZone())); // not c
			// instanceVO.setUpdatedDate(WorkflowUtility.getTimeZoneBasedDateString(df,
			// instance.getSystemAttributes().getUpdatedDate(),
			// bpmCtx.getTimeZone()));

			logger.debug("getComponentInstanceId : " + instance.getSca().getComponentInstanceId());
			logger.debug("getComponentName : " + instance.getSca().getComponentName());
			// logger.debug("getProcessNumber :
			// "+instance.getSystemAttributes().getProcessNumber());
			logger.debug("getComponentType : " + instance.getSystemAttributes().getComponentType());
			logger.debug("getActivityType : " + instance.getSystemAttributes().getActivityType());
			result.add(instanceVO);
		}

		model.addAttribute("result", result);

		return "soa/home/subs/processInstances";
	}

	@RequestMapping("/bpm/process/grab/info")
	public String grab_info(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {

		IBPMContext bpmCtx = soaClient.getBPMContext();

		String instanceId = (String) params.get("instanceId");
		IInstanceQueryService iqs = soaClient.getBPMServiceClient().getInstanceQueryService();
		// find the instance
		IProcessInstance instance = iqs.getProcessInstance(bpmCtx, instanceId);
		
		try {
			IGrabInstanceContextResponse grabContext = createGrabContext(bpmCtx, iqs, instance);
		
			if (instance == null) {
				logger.error("alterFlowForInstance() instance not found:" + instanceId);
				throw new Exception("alterFlowForInstance() instance not found:" + instanceId);
			}
	
			String processName = instance.getSca().getComponentName();
			String instanceState = instance.getSystemAttributes().getState();
	
			if (!(instanceState.equalsIgnoreCase(IInstanceQueryInput.PROCESS_STATE_OPEN)
					|| instanceState.equalsIgnoreCase(IInstanceQueryInput.PROCESS_STATE_SUSPENDED))) {
				logger.error("Instance is in state:" + instanceState + " state must be either OPEN or SUSPENDED");
				throw new Exception("Instance is in state:" + instanceState + " state must be either OPEN or SUSPENDED");
			}
	
			
			ArrayList<Object> openActivities = new ArrayList<Object>();
			// source : target = 1:n (id, name, path정보를 담아 반환한다.)
			for (IFlowChangeItem flowChange : grabContext.getAvailableFlowChanges()) { // Sources...
				HashMap<String, Object> activityMap = new HashMap<String, Object>();
				ArrayList<Object> targetActivities = new ArrayList<Object>();
	
				IOpenActivityInfo openActivity = flowChange.getSourceActivity();
				Boolean isSrcContainer = openActivity.isContainerActivity();
				String openActivityDisplayName = openActivity.getDisplayName();
				String openActivityId = openActivity.getId();
				String openActivityPath = openActivity.getStringPath();
				logger.debug("FlowChange Open Activity[isSrcContainer:" + isSrcContainer + "][display:"
						+ openActivityDisplayName + "][id:" + openActivityId + "][path:" + openActivityPath + "]");
	
				for (IActivityInfo targetActivity : flowChange.getValidGrabTargetActivities()) { // Targets....
	
					String targetName = targetActivity.getDisplayName();
					String targetId = targetActivity.getId();
	
					logger.debug("Checking Valid target [name:" + targetName + "][id:" + targetId + "]");
	
					targetActivities.add(targetActivity);
				}
	
				activityMap.put("openActivity", openActivity);
				activityMap.put("targetActivities", targetActivities);
	
				openActivities.add(activityMap);
			}
			
			// 변수 정보를 담아 반환한다.
			Iterable<IVariableItem> availableVariables = grabContext.getGrabInstanceContext().getAvailableVariables();
			for (IVariableItem availableVariable : availableVariables) {
				logger.debug("\t" + availableVariable.getName() + ":" + availableVariable.getValue());
			}
	
			model.addAttribute("openActivities", openActivities);
			model.addAttribute("availableVariables", availableVariables);
			model.addAttribute("processName", processName);
		} catch(Exception e) {
			logger.error("getGrabInfoException occurred while getting grab info! [please check this instance state(only possible running or suspend instance]");
			throw new Exception("GetGrabInfoException occurred while getting grab info! [please check this instance state(only possible running or suspend instance]");
		}
		
		return "soa/home/subs/alterFlow";
	}

	@RequestMapping("/bpm/process/grab/action")
	public String grab_action(HttpSession session, @RequestBody GrabVO grabVO, ModelMap model) throws Exception {
		IBPMContext bpmCtx = soaClient.getBPMContext();
		
		String success = "false";
		String errorCode = "";
		String errorMessage = "";
		String jsonResult = "";
		
		Gson gson = new Gson();
		logger.debug("grabVO.json : " + gson.toJson(grabVO));
		
		String instanceId = grabVO.getInstanceId();
		IInstanceQueryService iqs = soaClient.getBPMServiceClient().getInstanceQueryService();
		// find the instance
		IProcessInstance instance = iqs.getProcessInstance(bpmCtx, instanceId);
		//IGrabInstanceContextResponse grabContext = createGrabContext(bpmCtx, iqs, instance);

		if (instance == null) {
			success = "false";
			errorCode = "";
			errorMessage = "alterFlowForInstance() instance not found:" + instanceId;
			
			logger.error("alterFlowForInstance() instance not found:" + instanceId);
			//throw new Exception("alterFlowForInstance() instance not found:" + instanceId);
		}

		String instanceState = instance.getSystemAttributes().getState();

		if (!(instanceState.equalsIgnoreCase(IInstanceQueryInput.PROCESS_STATE_OPEN)
				|| instanceState.equalsIgnoreCase(IInstanceQueryInput.PROCESS_STATE_SUSPENDED))) {
			
			success = "false";
			errorCode = "";
			errorMessage = "Instance is in state:" + instanceState + " state must be either OPEN or SUSPENDED";
			logger.error("Instance is in state:" + instanceState + " state must be either OPEN or SUSPENDED");
			
		}

		List<GrabVO.ActivityInfo> openActivities = grabVO.getOpenActivities();
		List<GrabVO.VariableInfo> changeVariables = grabVO.getChangeVariables();
		String comment = grabVO.getComment();
		boolean action = (grabVO.getMode().equals("resume") ? true : false);

		final Set<IFlowChangeItem> flowChangeItemSet = new HashSet<IFlowChangeItem>();
		final Set<IVariableItem> variableItemSet = new HashSet<IVariableItem>();

		for (GrabVO.ActivityInfo openActivity : openActivities) {
			flowChangeItemSet.add(FlowChangeItem.create(
					ActivityInfo.create(openActivity.getOpenActivityId(), openActivity.getOpenActivityName(),
							openActivity.getOpenActivityProcessId()),
					ActivityInfo.create(openActivity.getTargetActivityId(), openActivity.getTargetActivityName(),
							openActivity.getTargetActivityProcessId())));
		}

		for (GrabVO.VariableInfo changeVariable : changeVariables) {
			variableItemSet.add(VariableItem.create(changeVariable.getName(), changeVariable.getValue()));
		}

		IGrabInstanceRequest request = new GrabInstanceRequest();
		request.setProcessInstance(instance);
		request.setResumeInstanceIfRequired(action); // false일때는 저장만? true일때
														// Grab? 테스트 해봐야 함.
		request.setComments(comment); // set Comment..
		request.setValidateValue(true);
		request.setRequestedVariableValueChanges(variableItemSet);
		request.setRequestedFlowChanges(flowChangeItemSet);

		IGrabInstanceResponse grabResponse = soaClient.getBPMServiceClient().getInstanceManagementService()
				.grabInstance(bpmCtx, request);
		IInstanceSummary instanceSummary = grabResponse.getInstanceSummary();
		boolean isUpdated = instanceSummary.isSuccessfullyUpdated();
		String message = instanceSummary.getMessage();
		String exceptionMessage = instanceSummary.getExceptionMessage();
		logger.debug("Grab Response: [isUpdated:" + isUpdated + "][message:" + message + "][exception:" + exceptionMessage + "]");
		
		if(isUpdated)
			success = "true";
		
		if(exceptionMessage != null)
			errorMessage = exceptionMessage;
		
		model.addAttribute("success", success);
		model.addAttribute("errorCode", errorCode);
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("result", jsonResult);
		
		return "common/json_response";
	}

	private static IGrabInstanceContextResponse createGrabContext(IBPMContext ctx, IInstanceQueryService iqs,
			IProcessInstance instance) throws Exception {
		InstanceContextConfiguration.Builder builder = new InstanceContextConfiguration.Builder();
		builder.includeOpenActivities().build();
		builder.includeProcessDataObjects().build();

		IInstanceContextConfiguration configuration = builder.build();

		// 두번째 파라미터 : 필요하면 Instance Suspend 후에 진행한다.
		IGrabInstanceContextRequest ctxReq = GrabInstanceContextRequest.create(instance, false,
				LocationInfo.ROOT_LOCATION, configuration);
		
		IGrabInstanceContextResponse ctxResp = iqs.createGrabInstanceContext(ctx, ctxReq);
		// IGrabInstanceContext context = ctxResp.getGrabInstanceContext();

		return ctxResp;
	}
}
