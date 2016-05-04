package oracle.bpm.workspace.client.web;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CustomAuthenticationController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());	// Logger
	
	@RequestMapping("/")
	public String main(HttpSession session, ModelMap model) throws Exception {
		logger.debug("call soa/home/main");
		return "soa/home/main";
	}

	@RequestMapping("/test")
	public String test(HttpSession session, @RequestParam(required = false) Map<String, String> params, ModelMap model)
			throws Exception {
		this.logger.debug((String) params.get("dn"));
		return null;
	}
}