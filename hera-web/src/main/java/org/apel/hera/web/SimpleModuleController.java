package org.apel.hera.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simpleModule")
public class SimpleModuleController {

	@RequestMapping("/index")
	public String index(){
		return "simple_module_index";
	}
	
}
