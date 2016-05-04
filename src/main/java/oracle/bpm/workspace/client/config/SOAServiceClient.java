package oracle.bpm.workspace.client.config;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpel.services.workflow.metadata.ITaskMetadataService;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.task.ITaskService;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import oracle.bpm.client.BPMServiceClientFactory;
import oracle.bpm.services.authentication.IBPMUserAuthenticationService;
import oracle.bpm.services.client.IBPMServiceClient;
import oracle.bpm.workspace.client.util.ContextCache;
import oracle.fabric.blocks.event.BusinessEventConnection;
import oracle.fabric.blocks.event.BusinessEventConnectionFactory;
import oracle.integration.platform.blocks.event.jms.JmsRemoteBusinessEventConnectionFactory;
import oracle.soa.management.facade.Locator;
import oracle.soa.management.facade.LocatorFactory;
import oracle.soa.management.facade.ServerManager;
import oracle.soa.management.facade.ServerManagerFactory;
import oracle.tip.pc.services.identity.BPMIdentityService;

public class SOAServiceClient {
	private String domainIdentity;
	private String serverURL;
	private String wsURL;
	private String userName;
	private String password;
	private String initialContextFactory;
	private String participateInClientTransaction;
	private static IWorkflowContext adminWorkflowContext = null;
	private static final String connFactName = "jms/fabric/EDNConnectionFactory";
	private static final String xaConnFactName = "jms/fabric/xaEDNConnectionFactory";
	private static final String queueName = "jms/fabric/EDNQueue";
	private Logger logger = LoggerFactory.getLogger(getClass());

	public String getDomainIdentity() {
		return this.domainIdentity;
	}

	public void setDomainIdentity(String domainIdentity) {
		this.domainIdentity = domainIdentity;
	}

	public String getServerURL() {
		return this.serverURL;
	}

	public String getWsURL() {
		return this.wsURL;
	}

	public void setWsURL(String wsURL) {
		this.wsURL = wsURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getInitialContextFactory() {
		return this.initialContextFactory;
	}

	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	public String getParticipateInClientTransaction() {
		return this.participateInClientTransaction;
	}

	public void setParticipateInClientTransaction(String participateInClientTransaction) {
		this.participateInClientTransaction = participateInClientTransaction;
	}

	public IWorkflowContext getAdminWorkflowContext() {
		return adminWorkflowContext;
	}

	public void setAdminWorkflowContext() {
		try {
			adminWorkflowContext = getWorkflowServiceClient().getTaskQueryService().authenticate(this.userName,
					this.password.toCharArray(), null);
		} catch (WorkflowException e) {
			e.printStackTrace();
		}
	}

	public InitialContext getInitialContext() throws NamingException {
		Properties props = new Properties();
		props.put("java.naming.provider.url", getServerURL());
		props.put("java.naming.factory.initial", getInitialContextFactory());
		props.put("java.naming.security.principal", getUserName());
		props.put("java.naming.security.credentials", getPassword());

		InitialContext context = new InitialContext(props);

		return context;
	}

	public BusinessEventConnection getWebLogicJMSConnection() throws Exception {
		InitialContext context = getInitialContext();
		UserTransaction userTransaction = (UserTransaction) context.lookup("javax.transaction.UserTransaction");
		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context
				.lookup("jms/fabric/EDNConnectionFactory");
		QueueConnectionFactory xaQueueConnectionFactory = (QueueConnectionFactory) context
				.lookup("jms/fabric/xaEDNConnectionFactory");
		Queue jmsQueue = (Queue) context.lookup("jms/fabric/EDNQueue");

		BusinessEventConnectionFactory factory = new JmsRemoteBusinessEventConnectionFactory(queueConnectionFactory,
				xaQueueConnectionFactory, jmsQueue, userTransaction);

		return factory.createBusinessEventConnection();
	}

	public Locator getLocator() {
		Hashtable<String, String> soaserver = new Hashtable();
		soaserver.put("java.naming.provider.url", getServerURL() + "/soa-infra");
		soaserver.put("java.naming.factory.initial", getInitialContextFactory());
		soaserver.put("java.naming.security.principal", getUserName());
		soaserver.put("java.naming.security.credentials", getPassword());
		soaserver.put("dedicated.connection", "true");
		Locator locator = null;
		try {
			locator = LocatorFactory.createLocator(soaserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locator;
	}

	public ServerManager getServerManager() {
		Hashtable<String, String> soaserver = new Hashtable();
		soaserver.put("java.naming.provider.url", getServerURL() + "/soa-infra");
		soaserver.put("java.naming.factory.initial", getInitialContextFactory());
		soaserver.put("java.naming.security.principal", getUserName());
		soaserver.put("java.naming.security.credentials", getPassword());
		soaserver.put("dedicated.connection", "true");

		ServerManager sm = null;
		ServerManagerFactory smf = ServerManagerFactory.getInstance();
		try {
			sm = smf.createServerManager(soaserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sm;
	}

	private BPMServiceClientFactory getBPMServiceClientFactory() {
		Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = new HashMap();

		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE, "REMOTE");
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, getServerURL());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL, getUserName());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS, getPassword());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_INITIAL_CONTEXT_FACTORY,
				getInitialContextFactory());

		return BPMServiceClientFactory.getInstance(properties, null, null);
	}

	public IBPMUserAuthenticationService getBPMUserAuthenticationService() {
		return getBPMServiceClientFactory().getBPMUserAuthenticationService();
	}

	public IBPMServiceClient getBPMServiceClient() {
		return getBPMServiceClientFactory().getBPMServiceClient();
	}
	
	public IBPMContext getBPMContext() throws Exception {
		IBPMContext bpmCtx;
		
		if(ContextCache.getContextCache().isEmpty("bpmContext")) {
			bpmCtx = getBPMServiceClientFactory().getBPMUserAuthenticationService().getBPMContextForAuthenticatedUser();
			ContextCache.getContextCache().put("bpmContext", bpmCtx);
			logger.debug("BPMContext put in cache...");
		} else {
			bpmCtx = (IBPMContext) ContextCache.getContextCache().get("bpmContext");
			logger.debug("BPMContext get from cache...");
		}
		
		return bpmCtx;
	}
	
	public IWorkflowServiceClient getWorkflowServiceClient() {
		Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = new HashMap();

		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE, "REMOTE");
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, getServerURL());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL, getUserName());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS, getPassword());
		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_INITIAL_CONTEXT_FACTORY,
				getInitialContextFactory());

		return WorkflowServiceClientFactory.getWorkflowServiceClient("REMOTE", properties, null);
	}

	public BPMIdentityService getBPMIdentityService() {
		Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = new HashMap();

		properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.SOAP_END_POINT_ROOT, getWsURL());

		return WorkflowServiceClientFactory.getSOAPIdentityServiceClient(getDomainIdentity(), properties, null);
	}

	public ITaskQueryService getTaskQueryService() {
		return getWorkflowServiceClient().getTaskQueryService();
	}

	public ITaskService getTaskService() {
		return getWorkflowServiceClient().getTaskService();
	}

	public ITaskMetadataService getTaskMetadataService() {
		return getWorkflowServiceClient().getTaskMetadataService();
	}
}
