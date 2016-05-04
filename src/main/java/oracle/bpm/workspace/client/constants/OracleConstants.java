package oracle.bpm.workspace.client.constants;

import java.util.HashMap;
import java.util.Map;

public class OracleConstants {
	public static class VALUE {
		public static final int MAX_ROWS_PER_PAGE = 10;
		public static String ADMIN_ROLE_NAME = "";
		public static String ALL_USER_PASSWORD = "";
	}

	public static class SERVLET {
	}

	public static class PREFIX {
		public static final String USER_PREF = "usrPrf";
	}

	public static class PREFERENCE {
		public static final Map<String, Object> USER_DEFAULTS = new HashMap();
		public static final Map<String, Object> APPLICATION_DEFAULTS;
		public static final Map<String, Object> APPLICATION_LABELS;

		static {
			USER_DEFAULTS.put("usrPrfmaxPageRows", String.valueOf(10));
			USER_DEFAULTS.put("usrPrfdisplayChart", String.valueOf(false));
			USER_DEFAULTS.put("usrPrfdisplayFolders", String.valueOf(true));
			USER_DEFAULTS.put("usrPrfpageHeight", "550");
			USER_DEFAULTS.put("usrPrfinboxSortField", "taskNumber");
			USER_DEFAULTS.put("usrPrfinboxSortOrder", "ASC");
			USER_DEFAULTS.put("usrPrfshowActions", String.valueOf(true));

			USER_DEFAULTS.put("usrPrfinboxCol0", "taskNumber");
			USER_DEFAULTS.put("usrPrfinboxCol1", "title");

			USER_DEFAULTS.put("usrPrfinboxCol3", "assigneeUsers");

			USER_DEFAULTS.put("usrPrfinboxCol5", "state");
			USER_DEFAULTS.put("usrPrfinboxCol6", "createdDate");
			USER_DEFAULTS.put("usrPrfinboxCol7", "expirationDate");

			APPLICATION_DEFAULTS = new HashMap();
			APPLICATION_LABELS = new HashMap();

			APPLICATION_DEFAULTS.put("resourceBundle",
					"egovframework.oracle.bpm.client.resource.WorklistResourceBundle");
		}
	}

	public static class PARAM {
		public static final String MENU_ID = "menuId";
		public static final String SUB_MENU_ID = "subMenuId";
		public static final String FILE = "file";
		public static final String ENCODING = "encoding";

		public static class ACTIVITY {
			public static final String LABEL = "activity_label";
			public static final String STATE = "activity_state";
			public static final String CREATION_DATE = "activity_creation_date";
			public static final String EXPIRATION_DATE = "activity_expiration_Date";
		}

		public static class APP_PREF {
			public static final String RESOURCE_BUNDLE = "resourceBundle";
		}

		public static class PAGE {
			public static final String KEYWORDS = "keywords";
			public static final String TYPE = "pageType";
			public static final String ACTION = "action";
			public static final String SORT_FIELD = "sortField";
			public static final String SORT_ORDER = "sortOrder";
			public static final String MAX_ROWS = "maxRows";
			public static final String FIRST_ROW = "startRow";
			public static final String LAST_ROW = "endRow";
			public static final String CURRENT_PAGE = "curPage";
			public static final String TOTAL_ROWS = "totalRows";

			public static class ACTIONS {
				public static final String UPDATE = "update";
				public static final String DELETE = "delete";
				public static final String CREATE = "create";
				public static final String VIEW = "view";
				public static final String FORM = "form";
			}
		}

		public static class PROCESS {
			public static final String HOME_DIR = "homeDir";
			public static final String DOMAIN_ID = "domainId";
			public static final String DOMAIN_REF = "domainRef";
			public static final String PROCESS_ID = "processId";
			public static final String PROCESS_GUID = "processGuid";
			public static final String SUITCASE_ID = "suitcaseID";
			public static final String PROCESS_VERSION = "processVersion";
			public static final String INSTANCE_ID = "instanceId";
			public static final String REFERENCE_ID = "referenceId";

			public static class ACTIONS {
				public static final String UPDATE = "update";
				public static final String DELETE = "delete";
				public static final String CREATE = "create";
			}
		}

		public static class TASK {
			public static final String ACTIONS = "taskActions";
		}

		public static class TASK_TYPE {
			public static final String ID = "Id";
			public static final String NAME = "Name";
			public static final String TITLE = "Title";
			public static final String DOMAIN_ID = "DomainId";
			public static final String PROCESS_ID = "ProcessId";
			public static final String PROCESS_NAME = "ProcessName";
			public static final String PROCESS_VERSION = "ProcessVersion";
			public static final String PROCESS_DESCRIPTION = "ProcessDescription";
			public static final String PROCESS_URI = "ProcessUri";
			public static final String KEYWORDS = "taskTypeKeyword";
			public static final String SORT_FIELD = "taskTypeSortField";
			public static final String SORT_ORDER = "taskTypeSortOrder";
		}

		public static class USER_PREF {
			public static final String MAX_PAGE_ROWS = "usrPrfmaxPageRows";
			public static final String DISPLAY_CHART = "usrPrfdisplayChart";
			public static final String SHOW_FOLDERS = "usrPrfdisplayFolders";
			public static final String PAGE_HEIGHT = "usrPrfpageHeight";
			public static final String INBOX_SORT_FIELD = "usrPrfinboxSortField";
			public static final String INBOX_SORT_ORDER = "usrPrfinboxSortOrder";
			public static final String INBOX_COL = "usrPrfinboxCol";
			public static final String SHOW_ACTIONS = "usrPrfshowActions";
		}

		public static class VACATION {
			public static final String ACTIVE = "vacActive";
			public static final String START_DATE = "vacStartDate";
			public static final String END_DATE = "vacEndDate";
		}
	}

	public static class PAGE {
		public static class ADMINISTRATION {
			public static final String AUTHORIZATION_MANAGEMENT = "authorizationManagement";
			public static final String RULES = "rules";
			public static final String CHARGE_MANAGEMENT = "chargeManagement";
		}

		public static class ANALYSIS {
			public static final String TASK_STATE = "taskState";
			public static final String PROCESS_DETAIL = "processDetail";
			public static final String INSTANCE_STATE = "instanceState";
			public static final String INSTANCE_EXECUTION_TIME = "instanceExecutionTime";
			public static final String PROCESS_PERFORMANCE = "processPerformance";
		}

		public static class COMMUNICATION {
			public static final String NOTICE = "notice";
			public static final String NEWLIBRARY = "newlibrary";
			public static final String QNA = "qna";
		}

		public static class MONITORING {
			public static final String INSTANCE = "instance";
		}

		public static class PREFERENCE {
			public static final String VACATION = "vacation";
			public static final String MY_RULES = "myRules";
		}

		public static class TASKS {
			public static final String INBOX = "inbox";
			public static final String INITIATED = "initiated";
			public static final String DELAY = "delay";
			public static final String DELEGATED = "delegated";
			public static final String PORTLET = "portlet";
		}
	}

	public static class MENU {
		public static class ROOT {
			public static final String HOME = "home";
			public static final String MY_TASKS = "myTasks";
			public static final String ANALYSIS = "analysis";
			public static final String PREFERENCE = "preference";
			public static final String MONITORING = "monitoring";
			public static final String ADMINSTRATION = "administration";
			public static final String COMMUNICATION = "communication";
		}
	}

	public static class CODE {
		public static class SORT_ORDER {
			public static final String DESCENDING = "DESC";
			public static final String ASCENDING = "ASC";
		}
	}

	public static class APPLICATION {
		public static final String RESOURCE_BUNDLE = "egovframework.oracle.bpm.client.resource.WorklistResourceBundle";
	}
}
