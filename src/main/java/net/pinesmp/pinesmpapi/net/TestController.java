package net.pinesmp.pinesmpapi.net;

import net.pinesmp.pinesmpapi.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@RestController
public class TestController {
	@GetMapping("/test")
	public Response test(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Response(true, new LinkedList<String>(), new LinkedList<String>(), new LinkedHashMap<String, Object>());
	}
}
