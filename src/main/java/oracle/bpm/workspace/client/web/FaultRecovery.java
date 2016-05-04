package oracle.bpm.workspace.client.web;

import java.util.Hashtable;
import java.util.List;

import oracle.soa.management.facade.Component;
import oracle.soa.management.facade.Fault;
import oracle.soa.management.facade.FaultAction;
import oracle.soa.management.facade.Locator;
import oracle.soa.management.facade.LocatorFactory;
import oracle.soa.management.facade.Partition;
import oracle.soa.management.facade.ServerManager;
import oracle.soa.management.facade.ServerManagerFactory;
import oracle.soa.management.facade.bpel.BPELServiceEngine;
import oracle.soa.management.facade.bpm.BPMInvokeMessage;
import oracle.soa.management.facade.flow.CommonFault;
import oracle.soa.management.facade.flow.FlowInstance;
import oracle.soa.management.util.ComponentFilter;
import oracle.soa.management.util.FaultFilter;
import oracle.soa.management.util.MessageFilter;
import oracle.soa.management.util.flow.CommonFaultFilter;
import oracle.soa.management.util.flow.FlowInstanceFilter;
import oracle.soa.tracking.api.state.RecoveryState;

public class FaultRecovery {
	private Locator locator = null;
	private BPELServiceEngine mBPELServiceEngine;

	public FaultRecovery() {
		this.locator = getLocator();
		try {
			this.mBPELServiceEngine = ((BPELServiceEngine) this.locator.getServiceEngine("bpel"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Hashtable getJndiProps() {
		Hashtable jndiProps = new Hashtable();
		jndiProps.put("java.naming.provider.url", "t3://172.17.1.67:7003/soa-infra");
		jndiProps.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		jndiProps.put("java.naming.security.principal", "weblogic");
		jndiProps.put("java.naming.security.credentials", "YoGurt01");
		jndiProps.put("dedicated.connection", "true");
		return jndiProps;
	}

	public Locator getLocator() {
		try {
			return LocatorFactory.createLocator(getJndiProps());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getPartitions() {
		Hashtable<String, String> soaserver = new Hashtable();
		soaserver.put("java.naming.provider.url", "t3://172.17.1.100:7003/soa-infra");
		soaserver.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		soaserver.put("java.naming.security.principal", "weblogic");
		soaserver.put("java.naming.security.credentials", "YoGurt01");
		soaserver.put("dedicated.connection", "true");

		ServerManager sm = null;
		ServerManagerFactory smf = ServerManagerFactory.getInstance();
		try {
			sm = smf.createServerManager(soaserver);

			List<Partition> partitions = sm.getPartitions();
			System.out.println(partitions.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recoverFaults() {
		System.out.println("+++++++++++++++++++> " + RecoveryState.ABORTED);
		System.out.println("+++++++++++++++++++> " + RecoveryState.ABORTED.name());
		System.out.println("+++++++++++++++++++> " + RecoveryState.BPEL_CALLBACK_MESSAGE_RECOVERY_REQUIRED.name());
		try {
			List<FlowInstance> localList = this.locator.getFlowInstances(new FlowInstanceFilter());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			System.out.println("Get Recoverable Faults");

			CommonFaultFilter cff = new CommonFaultFilter();
			cff.setFlowId(Long.valueOf(30005L));

			List<CommonFault> cfaults = this.locator.getCommonFaults(cff);

			System.out.println("cfaults.size() : " + cfaults.size());
			for (CommonFault cfault : cfaults) {
				System.out.println(cfault.getMessage());
				System.out.println(cfault.getComponentName());
				System.out.println(cfault.getFaultTime());
				System.out.println(cfault.getState());
				System.out.println(cfault.getType());
				System.out.println(cfault.getRetryCount());
				System.out.println(cfault.getFaultId());

				System.out.println("111111111");
				String xml = "<CompleteInstancesCollection xmlns=\"http://xmlns.oracle.com/pcbpel/adapter/db/top/insCompleteInstancesDB\"><CompleteInstances><cikey>3</cikey></CompleteInstances></CompleteInstancesCollection>";
				System.out.println("22222222222");
			}
			FaultFilter filter = new FaultFilter();

			filter.setCompositeInstanceId("30005");
			filter.setRecoverable(true);
			filter.setId("30007");

			List<Fault> faultList = this.mBPELServiceEngine.getFaults(filter);
			for (Fault fault : faultList) {
				System.out.println(
						"=============================================================================================================");
				System.out.println("         Composite DN: " + fault.getCompositeDN().getStringDN());
				System.out.println("Composite Instance ID: " + fault.getCompositeInstanceId());
				System.out.println("       Component Name: " + fault.getComponentName());
				System.out.println("Component Instance ID: " + fault.getComponentInstanceId());
				System.out.println("        Activity Name: " + fault.getLabel());
				System.out.println("             Fault ID: " + fault.getId());
				System.out.println("             TenantId ID: " + fault.getTenantId());
				System.out.println("           Fault Name: " + fault.getName());
				System.out.println("     Recoverable flag: " + fault.isRecoverable());
				System.out.println("        Fault Message: " + fault.getMessage());
				System.out.println("fault : " + fault);
				System.out.println(this.mBPELServiceEngine.getEngineType());
				System.out.println(fault.getEngineType());

				List<FaultAction> faultActions = fault.getFaultActions();
				for (FaultAction faultAction : faultActions) {
					System.out.println("faultAction.getName() : " + faultAction.getName());
					System.out.println("faultAction.getType() : " + faultAction.getType());
				}
				MessageFilter mf = new MessageFilter();
				List<BPMInvokeMessage> messages = this.mBPELServiceEngine.getInvokeMessages(mf);
				for (BPMInvokeMessage m : messages) {
					System.out.println(m.getId());
				}
				ComponentFilter f = new ComponentFilter();
				f.setEngineType("bpel");

				List<Component> components = this.mBPELServiceEngine.getDeployedComponents(f);
				for (Component component : components) {
					System.out.println(component.getNumberOfRecoverableInstances());
				}
				System.out.println(fault.isRecoverable());

				this.mBPELServiceEngine.getVariableNames(fault);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FaultRecovery faultRecovery = new FaultRecovery();

		faultRecovery.getPartitions();
	}
}
