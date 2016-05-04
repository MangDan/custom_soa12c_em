package oracle.bpm.workspace.client.web;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

import oracle.bpm.workspace.client.config.SOAServiceClient;
import oracle.bpm.workspace.client.util.XmlUtility;
import oracle.bpm.workspace.client.vo.FlowEntryVO;
import oracle.fabric.blocks.event.BusinessEventConnection;
import oracle.integration.platform.blocks.event.BusinessEventBuilder;
import oracle.soa.common.util.XMLUtil;
import oracle.soa.management.facade.ComponentInstance;
import oracle.soa.management.facade.Composite;
import oracle.soa.management.facade.CompositeInstance;
import oracle.soa.management.facade.Fault;
import oracle.soa.management.facade.FaultAction;
import oracle.soa.management.facade.Locator;
import oracle.soa.management.facade.Partition;
import oracle.soa.management.facade.flow.CommonFault;
import oracle.soa.management.facade.flow.FlowInstance;
import oracle.soa.management.util.ComponentInstanceFilter;
import oracle.soa.management.util.CompositeFilter;
import oracle.soa.management.util.CompositeInstanceFilter;
import oracle.soa.management.util.FaultActionType;
import oracle.soa.management.util.FaultFilter;
import oracle.soa.management.util.flow.CommonFaultFilter;
import oracle.soa.management.util.flow.FlowInstanceFilter;
import oracle.soa.tracking.api.state.ExecutionState;
import oracle.soa.tracking.api.state.FaultState;
import oracle.soa.tracking.api.state.RecoveryState;
import oracle.soa.tracking.api.state.TrackingState;
import oracle.soa.tracking.api.state.TrackingStateFactory;

@Controller
public class CustomSOAController {
	@Resource(name = "soaClient")
	protected SOAServiceClient soaClient;
	private Logger logger = LoggerFactory.getLogger(this.getClass());	// Logger

	@RequestMapping("/soa/compositestree/listbyfilter")
	public String composites(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {
		List<Partition> partitions = this.soaClient.getServerManager().getPartitions();

		Locator locator = this.soaClient.getLocator();

		ArrayList<HashMap<String, Object>> partitionArray = new ArrayList();
		for (Partition partition : partitions) {
			HashMap<String, Object> partitionMap = new HashMap();

			CompositeFilter cf = new CompositeFilter();
			cf.setPartition(partition.getName());

			List<Composite> composites = locator.getComposites(cf);

			ArrayList<HashMap<String, String>> compositeArray = new ArrayList();
			for (Composite composite : composites) {
				HashMap<String, String> compositeMap = new HashMap();

				this.logger.debug("++++ put compositeName +++>" + composite.getCompositeDN().getCompositeName());
				this.logger.debug("++++ put revision +++>" + composite.getCompositeDN().getRevision());

				compositeMap.put("compositeName", composite.getCompositeDN().getCompositeName());
				compositeMap.put("revision", composite.getCompositeDN().getRevision());

				compositeArray.add(compositeMap);
				this.logger.debug("++++ put composite Array Size +++>" + compositeArray.size());
			}
			this.logger.debug("++++ put compositeArray into partition Map[" + partition.getName() + "] +++>"
					+ compositeArray.size());
			partitionMap.put(partition.getName(), compositeArray);

			partitionArray.add(partitionMap);
			this.logger.debug("++++ put partitionArray +++>" + partitionArray.size());
		}
		model.addAttribute("partitions", partitions);
		model.addAttribute("results", partitionArray);

		return "layouts/menu";
	}

	@RequestMapping("/soa/composite/instance/listbyfilter")
	public String composite_list(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {
		String partition = (String) params.get("partition");
		String composite = (String) params.get("composite");
		String revision = (String) params.get("revision");
		String flowId = (String) params.get("flowId");
		String state = (String) params.get("state");

		this.logger.debug("partition : " + partition);
		this.logger.debug("revision : " + revision);
		this.logger.debug("composite : " + composite);
		this.logger.debug("flowId : " + flowId);
		this.logger.debug("state : " + state);

		Locator locator = this.soaClient.getLocator();

		CompositeInstanceFilter filter = new CompositeInstanceFilter();

		filter.setStates(new int[] { 0, 1, 4 });
		if (!composite.equals("")) {
			filter.setCompositeDN(partition + "/" + composite + "!" + revision);
		}
		if (!flowId.equals("")) {
			filter.setId(flowId);
		}
		List<CompositeInstance> compositeInstances = locator.getCompositeInstances(filter);
		for (CompositeInstance compositeInstance : compositeInstances) {
			this.logger.debug("compositeInstance.getId() : " + compositeInstance.getId());
			this.logger.debug("compositeInstance.getCompositeDN() : " + compositeInstance.getCompositeDN());
			this.logger.debug("getStateAsString(compositeInstance.getState()) : "
					+ getStateAsString(compositeInstance.getState()));
			this.logger.debug("compositeInstance.getECID() : " + compositeInstance.getECID());
			this.logger.debug("compositeInstance.getFaultCount() : " + compositeInstance.getFaultCount());
		}
		model.addAttribute("result", compositeInstances);

		return "soa/home/subs/composites";
	}

	@RequestMapping("/soa/composite/flow/listbyfilter")
	public String flowInstances(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {
		String partition = (String) params.get("partition");
		String composite = (String) params.get("composite");
		String revision = (String) params.get("revision");
		String flowId = (String) params.get("flowId");
		String state = (String) params.get("state");

		this.logger.debug("partition : " + partition);
		this.logger.debug("revision : " + revision);
		this.logger.debug("composite : " + composite);
		this.logger.debug("flowId : " + flowId);
		this.logger.debug("state : " + state);

		FlowInstanceFilter fInstanceFilter = new FlowInstanceFilter();

		fInstanceFilter.setPageSize(10);
		fInstanceFilter.setPageStart(0);
		if (!composite.equals("")) {
			fInstanceFilter.setCompositeDN(partition + "/" + composite + "!" + revision);
		}
		if (!flowId.equals("")) {
			fInstanceFilter.setFlowId(Long.valueOf(Long.parseLong(flowId)));
		}
		List<FlowInstance> flowInstances = this.soaClient.getLocator().getFlowInstances(fInstanceFilter);
		for (FlowInstance flowInstance : flowInstances) {
			this.logger.debug("flowInstance.getId() : " + flowInstance.getFlowId());
			this.logger.debug("flowInstance.getFlowTrace() : " + flowInstance.getFlowTrace());
		}
		model.addAttribute("result", flowInstances);

		return "soa/home/subs/flowInstances";
	}
	
	@RequestMapping("/soa/component/instance/listbyfilter")
	public String ComponentInstances(HttpSession session, @RequestParam(required = false) Map<String, String> params,
			ModelMap model) throws Exception {
		
		Locator locator = this.soaClient.getLocator();
		
		ComponentInstanceFilter cif = new ComponentInstanceFilter();
		
		cif.setEngineType(Locator.SE_BPMN);
		
		if(params.get("pageSize") != null && !params.get("pageSize").equals(""))
			cif.setPageSize(Integer.parseInt(params.get("pageSize")));
		if(params.get("componentName") != null && !params.get("componentName").equals(""))
			cif.setComponentName(params.get("componentName"));
		if(params.get("flowId") != null && !params.get("flowId").equals(""))
			cif.setFlowId(Long.parseLong(params.get("flowId")));
		if(params.get("componentInstanceId") != null && !params.get("componentInstanceId").equals(""))
			cif.setId("bpmn:" + params.get("componentInstanceId"));
		if(params.get("state") != null && !params.get("state").equals(""))
			cif.setNormalizedState(Integer.parseInt(params.get("state")));		// 일반 state는 예전의 bpel의 state와 같음, component state로 바꿔주는 부분이 nomalized state.
		
		//(new int[] { Integer.parseInt(params.get("state")) });
		
		logger.debug("STATE_COMPLETED_SUCCESSFULLY : " + String.valueOf(ComponentInstance.STATE_COMPLETED_SUCCESSFULLY));
		logger.debug("STATE_FAULTED : " + String.valueOf(ComponentInstance.STATE_FAULTED));
		logger.debug("STATE_RECOVERED : " + String.valueOf(ComponentInstance.STATE_RECOVERED));
		logger.debug("STATE_RECOVERY_REQUIRED : " + String.valueOf(ComponentInstance.STATE_RECOVERY_REQUIRED));
		logger.debug("STATE_RUNNING : " + String.valueOf(ComponentInstance.STATE_RUNNING));
		logger.debug("STATE_STALE : " + String.valueOf(ComponentInstance.STATE_STALE));
		logger.debug("STATE_SUSPENDED : " + String.valueOf(ComponentInstance.STATE_SUSPENDED));
		logger.debug("STATE_TERMINATED_BY_USER : " + String.valueOf(ComponentInstance.STATE_TERMINATED_BY_USER));
		
		List<ComponentInstance> componentInstances = locator.getComponentInstances(cif);
		
		for(ComponentInstance componentInstance : componentInstances) {
			logger.debug("componentInstance.getComponentName() : " + componentInstance.getComponentName());
			logger.debug("componentInstance.getCreator() : " + componentInstance.getCreator());
			logger.debug("componentInstance.getFlowId() : " + componentInstance.getFlowId());
			logger.debug("componentInstance.getId() : " + componentInstance.getId());
			logger.debug("componentInstance.getState() : " + componentInstance.getState());
			logger.debug("getTrackingState(componentInstance.getState()) : " + getTrackingState(componentInstance.getState()));
			logger.debug("componentInstance.getServiceEngine() : " + componentInstance.getServiceEngine());
			logger.debug("componentInstance.getStatus() : " + componentInstance.getStatus());
			logger.debug("componentInstance.getCikey() : " + componentInstance.getCikey());
			logger.debug("componentInstance.getCikey() : " + componentInstance.getNormalizedState());
			logger.debug("componentInstance.getNormalizedStateAsString() : " + componentInstance.getNormalizedStateAsString());
			logger.debug("componentInstance.getNormalizedState() : " + componentInstance.getNormalizedState());
		}
		
		model.addAttribute("result", componentInstances);

		return "soa/home/subs/processInstances";
	}
	
	@RequestMapping("/soa/composite/get")
	public String composite_instance(HttpSession session, @RequestParam("") String compositedn, ModelMap model)
			throws Exception {
		Locator locator = this.soaClient.getLocator();

		Composite composite = locator.lookupComposite(compositedn);

		return null;
	}

	public void makeTreeFlowEntry(FlowEntryVO flowEntryVO) {
	}

	@RequestMapping("/soa/flow/flowTrace")
	public String flowTrace(HttpSession session, @RequestParam("") long flowId, ModelMap model) throws Exception {
		String audit_xml = (String) this.soaClient.getLocator().getFlowTrace(flowId);

		this.logger.debug("flow audit[Flow ID:" + flowId + "] : " + audit_xml);

		Element audit_xml_ele = XmlUtility.loadDocument(new StringReader(audit_xml));

		NodeList entryList = audit_xml_ele.getElementsByTagName("entry");

		ArrayList<FlowEntryVO> flowEntries = new ArrayList();

		HashMap<String, FlowEntryVO> flowEntryMap = new HashMap();
		HashMap<String, Object> instanceMap;
		for (int i = 0; i < entryList.getLength(); i++) {
			if (entryList.item(i).getNodeType() == 1) {
				this.logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

				instanceMap = new HashMap();

				Element entry = (Element) entryList.item(i);

				NamedNodeMap nodeAttrs = entry.getAttributes();
				for (int nodeAttrIdx = 0; nodeAttrIdx < nodeAttrs.getLength(); nodeAttrIdx++) {
					Node attrNode = nodeAttrs.item(nodeAttrIdx);

					instanceMap.put(attrNode.getNodeName(),
							attrNode.getNodeName().equals("state")
									? getTrackingState(Integer.parseInt(attrNode.getFirstChild().getNodeValue()))
									: attrNode.getFirstChild().getNodeValue());

					this.logger.debug(
							"++++++ " + attrNode.getNodeName() + " ++++> " + attrNode.getFirstChild().getNodeValue());
				}
				NodeList entryChilds = entry.getChildNodes();
				for (int j = 0; j < entryChilds.getLength(); j++) {
					if (entryChilds.item(j).getNodeType() == 1) {
						Element entryChild = (Element) entryChilds.item(j);
						String entryChildValue = entryChild.getTextContent();
						
						instanceMap.put(entryChild.getNodeName(), entryChildValue);
						this.logger.debug("++++++ " + entryChild.getNodeName() + " ++++> " + entryChildValue);
					}
				}
				FlowEntryVO flowEntryVO = new FlowEntryVO();
				flowEntryVO.setFlowEntry(instanceMap);

				flowEntryMap.put(flowEntryVO.getInstanceId(), flowEntryVO);
				if (flowEntryMap.containsKey(flowEntryVO.getParentInstanceId())) {
					((FlowEntryVO) flowEntryMap.get(flowEntryVO.getParentInstanceId())).childFlowEntries
							.add(flowEntryVO);
				} else {
					flowEntries.add(flowEntryVO);
				}
				this.logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			}
		}
		for (FlowEntryVO flowEntrie : flowEntries) {
			print(0, flowEntrie);
		}
		Gson gson = new Gson();
		String jsonStr = gson.toJson(flowEntries);

		System.out.println("++++++> JSON String : " + jsonStr);
		model.addAttribute("result", jsonStr);

		return "soa/home/subs/flowTrace";
	}
	
	private void print(int depth, FlowEntryVO entry) {
		this.logger.debug(indent(depth) + " - " + entry.getInstanceId());
		for (FlowEntryVO child : entry.childFlowEntries) {
			print(depth + 1, child);
		}
	}

	private String indent(int depth) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < depth * 4; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	@RequestMapping("/soa/composite/edn/cancelEvent")
	public String cancelEvent(HttpSession session, @RequestParam("composite_instance_id") String composite_instance_id,
			ModelMap model) throws Exception {
		String EmployeeEvent = "CancelEvent";
		String NameSpace = "http://schemas.oracle.com/events/edl/MaterialReqCancel";

		String xmlStr = "<CancelEvent xmlns='http://www.oracle.com/kr/soa/edn/CancelEvent'><InstanceID>"
				+ composite_instance_id + "</InstanceID></CancelEvent>";

		Element eventBody = null;
		try {
			eventBody = XMLUtil.parseDocumentFromXMLString(xmlStr.toString()).getDocumentElement();

			String a = XMLUtil.toString(eventBody);
			System.out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BusinessEventConnection conn = this.soaClient.getWebLogicJMSConnection();

			BusinessEventBuilder builder = BusinessEventBuilder.newInstance();
			builder.setEventName(new QName(NameSpace, EmployeeEvent));
			builder.setBody(eventBody);

			conn.publishEvent(builder.createEvent(), 4);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/soa/composite/faults")
	public String getCompositeFaults(HttpSession session, @RequestParam("flowId") long flowId, ModelMap model)
			throws Exception {
		ArrayList<Object> resultArray = new ArrayList();
		Locator locator = this.soaClient.getLocator();

		CommonFaultFilter cff = new CommonFaultFilter();
		FaultFilter ff = new FaultFilter();

		cff.setFlowId(Long.valueOf(flowId));

		List<CommonFault> cfaults = locator.getCommonFaults(cff);
		for (CommonFault cfault : cfaults) {
			HashMap<String, Object> resultMap = new HashMap();

			ff.setId(Long.toString(cfault.getFaultId()));

			List<Fault> faults = locator.getFaults(ff);
			for (Fault fault : faults) {
				this.logger.debug("+++ fault +++> " + fault);
				resultMap.put("fault", fault);
			}
			this.logger.debug("+++ cfault +++> " + cfault);
			resultMap.put("cfault", cfault);

			resultMap.put("recoveryState", RecoveryState.getRecoveryState(cfault.getState()));
			resultArray.add(resultMap);
		}
		model.addAttribute("results", resultArray);

		return "soa/home/subs/faults";
	}

	@RequestMapping("/soa/composite/faultdetail")
	public String getFaultVariables(HttpSession session, @RequestParam("flowId") long flowId,
			@RequestParam("faultId") long faultId, ModelMap model) throws Exception {
		Locator locator = this.soaClient.getLocator();

		CommonFaultFilter cff = new CommonFaultFilter();

		cff.setFlowId(Long.valueOf(flowId));

		List<CommonFault> cfaults = locator.getCommonFaults(cff);
		System.out.println("cfaults.size() : " + cfaults.size());

		HashMap<String, Object> resultMap = new HashMap();
		for (CommonFault cfault : cfaults) {
			this.logger.debug("+++ cfault +++> " + cfault);
			resultMap.put("cfault", cfault);
			if (cfault.getFaultId() == faultId) {
				try {
					String[] variableNames = locator.getVariableNames(cfault);
					resultMap.put("variableNames", variableNames);
					for (String variableName : variableNames) {
						resultMap.put(variableName, locator.getVariableValue(cfault, variableName, ""));
					}
					List<FaultActionType> actionTypes = locator.getFaultActionTypes(cfault);
					resultMap.put("faultActions", getFaultActions(actionTypes));
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("+++++ " + cfault.getFaultName() + "(Flow ID:" + flowId
							+ ") ++++> a variable can't update!!!");
				}
			}
			resultMap.put("recoveryState", RecoveryState.getRecoveryState(cfault.getState()));
		}
		model.addAttribute("results", resultMap);

		return "soa/home/subs/faultDetail";
	}

	private List<FaultAction> getFaultActions(List<FaultActionType> actionTypes) {
		List<FaultAction> faultActions = new ArrayList();
		if (actionTypes == null) {
			return faultActions;
		}
		for (FaultActionType actionType : actionTypes) {
			Fault.FaultActionType faultActionType = null;
			this.logger.debug("actionType : " + actionType.ordinal());
			switch (actionType.ordinal()) {
			case 1:
				faultActionType = Fault.FaultActionType.ACTION_ABORT;
				break;
			case 2:
				faultActionType = Fault.FaultActionType.ACTION_CONTINUE;
				break;
			case 3:
				faultActionType = Fault.FaultActionType.ACTION_JAVA;
				break;
			case 4:
				faultActionType = Fault.FaultActionType.ACTION_MANUAL;
				break;
			case 5:
				faultActionType = Fault.FaultActionType.ACTION_REPLAY_SCOPE;
				break;
			case 6:
				faultActionType = Fault.FaultActionType.ACTION_RETHROW_FAULT;
				break;
			case 7:
				faultActionType = Fault.FaultActionType.ACTION_RETRY;
			}
			if (faultActionType != null) {
				FaultAction faultAction = new FaultAction(faultActionType.name(), faultActionType.ordinal());

				faultActions.add(faultAction);
			}
		}
		return faultActions;
	}

	@RequestMapping("/soa/flow/faultRecover")
	public String faultRecover(HttpSession session, @RequestParam("flowId") long flowId,
			@RequestParam("faultId") long faultId, @RequestParam("faultAction") String faultAction,
			@RequestParam("variableName") String variableName, @RequestParam("variableValue") String variableValue,
			ModelMap model) throws Exception {
		this.logger.debug("++++ flowId ++++> " + flowId);
		this.logger.debug("++++ faultId ++++> " + faultId);
		this.logger.debug("++++ faultAction ++++> " + faultAction);
		this.logger.debug("++++ variableName ++++> " + variableName);
		this.logger.debug("++++ variableValue ++++> " + variableValue);

		Locator locator = this.soaClient.getLocator();
		if ((!variableName.equals("")) && (!variableValue.equals(""))) {
			CommonFaultFilter cff = new CommonFaultFilter();

			cff.setFlowId(flowId);

			List<CommonFault> cfaults = locator.getCommonFaults(cff);
			for (CommonFault cfault : cfaults) {
				if (cfault.getFaultId() == faultId) {
					locator.setVariableValue(cfault, variableName, "", variableValue);
				}
			}
		}
		
        for(FaultActionType faultActionType : FaultActionType.values())
        {
            logger.debug("+++ faultActionType +++> " + faultActionType);
            if(faultActionType.name().equals(faultAction))
            {
                logger.info("+++ start fault recovery +++");
                locator.recoverFault(faultId, faultActionType);
                logger.info("++++ fault("+faultId+") executed with "+faultAction+" action successfully");
            }
        }

		model.addAttribute("result","++++ fault(" + faultId + ") executed with " + faultAction + " action successfully");
		return "common/json_response";
	}

	@RequestMapping("/soa/flow/abortFlow")
	public String abortFlow(HttpSession session, @RequestParam("flowId") long flowId, ModelMap model) throws Exception {
		this.soaClient.getLocator().abortFlows(new long[] { flowId });
		model.addAttribute("result", "++++ fault(" + flowId + ") executed with abort action successfully");
		return "common/json_response";
	}

	private String getStateAsString(int state) {
		if (state == 2) {
			return "Completed";
		}
		if (state == 3) {
			return "Faulted";
		}
		if (state == 1) {
			return "Recovery";
		}
		if (state == 0) {
			return "Running";
		}
		if (state == 6) {
			return "Stale";
		}
		if (state == 4) {
			return "Aborted";
		}
		return "Unknown";
	}

	private static String getTrackingState(int state) {
		TrackingState tState = TrackingStateFactory.getTrackingState(state);

		Logger logger = LoggerFactory.getLogger(CustomSOAController.class);	// Logger ó��
		
		logger.debug("Tracking State : " + state);
		logger.debug("getExecutionState : " + tState.getExecutionState());
		logger.debug("getFaultState : " + tState.getFaultState());
		logger.debug("getRecoveryState : " + tState.getRecoveryState());
		logger.debug("getTransitionType : " + tState.getTransitionType());
		if ((tState.getExecutionState() == ExecutionState.RUNNING) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)) {
			return "Running";
		}
		if ((tState.getExecutionState() == ExecutionState.COMPLETED) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)) {
			return "Completed";
		}
		if ((tState.getExecutionState() == ExecutionState.COMPLETED) && (tState.getFaultState() == FaultState.FAULTED)
				&& (tState.getRecoveryState() == RecoveryState.NON_RECOVERABLE)) {
			return "Failed";
		}
		if ((tState.getRecoveryState() != null) && (tState.getRecoveryState().name().contains("RECOVERY_REQUIRED"))) {
			return "Recovery Required";
		}
		if ((tState.getExecutionState() == ExecutionState.RUNNING) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == RecoveryState.RECOVERED)) {
			return "Recovery Required";
		}
		if ((tState.getExecutionState() == ExecutionState.COMPLETED) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == RecoveryState.RECOVERED)) {
			return "Completed Recovered";
		}
		if ((tState.getExecutionState() == ExecutionState.SUSPENDED) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)) {
			return "Suspended";
		}
		if ((tState.getExecutionState() == ExecutionState.RUNNING) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)
				&& (tState.getTransitionType() == TrackingState.TransitionType.RESUME)) {
			return "Resumed";
		}
		if ((tState.getExecutionState() == ExecutionState.TERMINATED) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)) {
			return "Aborted";
		}
		if ((tState.getExecutionState() == ExecutionState.STALE) && (tState.getFaultState() == FaultState.CLEAR)
				&& (tState.getRecoveryState() == null)) {
			return "Stale";
		}
		return "Unknown";
	}
}
