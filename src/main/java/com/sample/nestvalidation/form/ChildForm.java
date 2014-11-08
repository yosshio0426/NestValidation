package com.sample.nestvalidation.form;

import java.util.ArrayList;
import java.util.List;

import org.seasar.struts.annotation.Required;

public class ChildForm {
	
	@Required
	public String name;

	@Required
	public String[] array = new String[0];
	
	@Required
	public List<String> list = new ArrayList<>();

}
