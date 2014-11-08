package com.sample.nestvalidation.form;

import java.util.ArrayList;
import java.util.List;

import org.seasar.struts.annotation.Required;

import com.sample.nestvalidation.annotation.Nest;
import com.sample.nestvalidation.annotation.NestArray;

public class ParentForm {
	
	@Required
	public String parentName;
	
	@Nest
	public ChildForm child = new ChildForm();
	
	@Required
	public List<String> list = new ArrayList<>();
	
	@Required
	public String[] array = new String[0];
	
	@NestArray(ChildrenForm.class)
	public List<ChildrenForm> childrenList = new ArrayList<>();
	
	@NestArray(ChildrenForm.class)
	public ChildrenForm[] childrenArray = new ChildrenForm[0];

}
