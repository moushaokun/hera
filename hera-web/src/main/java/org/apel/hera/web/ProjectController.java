package org.apel.hera.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@RequestMapping("/index")
	public String index(){
		return "project_index";
	}
	
}
