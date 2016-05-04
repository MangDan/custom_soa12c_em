package oracle.bpm.workspace.client.vo;

import com.google.gson.Gson;

public class Test {

	public static void main(String [] args) {
		String jsonstr = "{  \r\n" + 
				"   \"instanceId\":\"50005\",\r\n" + 
				"   \"comment\":\"\",\r\n" + 
				"   \"mode\":\"resume\",\r\n" + 
				"   \"openActivities\":[  \r\n" + 
				"      {  \r\n" + 
				"         \"openActivityId\":\"ACT15297479643117\",\r\n" + 
				"         \"openActivityName\":\"UserTask1\",\r\n" + 
				"         \"openActivityProcessId\":\"SampleProcess\",\r\n" + 
				"         \"targetActivityId\":\"ACT1529052210571\",\r\n" + 
				"         \"targetActivityName\":\"ServiceTask\",\r\n" + 
				"         \"targetActivityProcessId\":\"SampleProcess\"\r\n" + 
				"      },\r\n" + 
				"      {  \r\n" + 
				"         \"openActivityId\":\"test\",\r\n" + 
				"         \"openActivityName\":\"test\",\r\n" + 
				"         \"targetActivityId\":\"test\",\r\n" + 
				"         \"targetActivityName\":\"test\"\r\n" + 
				"      }\r\n" + 
				"   ],\r\n" + 
				"   \"changeVariables\":[  \r\n" + 
				"      {  \r\n" + 
				"         \"name\":\"sampleDO\",\r\n" + 
				"         \"value\":\"<sample xmlns=\\\"http://www.example.org\\\">\\n   <element1>test</element1>\\n   <element2>test</element2>\\n   <element3>test2</element3>\\n</sample>\\n\"\r\n" + 
				"      }\r\n" + 
				"   ]\r\n" + 
				"}";
		
		GrabVO grabvo = null;
		Gson gson = new Gson();  
		grabvo = gson.fromJson(jsonstr, GrabVO.class);  
	    //System.out.println(grabvo.getChangeVariables().get(0).getValue());  
	    System.out.println(gson.toJson(grabvo));  
	    
	}

}
