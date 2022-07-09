package net.pinesmp.pinesmpapi.net;

import net.pinesmp.pinesmpapi.util.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Controller
@ResponseBody
@RequestMapping(value = "/api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {
	@RequestMapping(value = "")
	public Response test() {
		return new Response(true, new LinkedList<String>(), new LinkedList<String>(), new LinkedHashMap<String, Object>());
	}
}
