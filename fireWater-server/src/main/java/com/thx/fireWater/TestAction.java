package com.thx.fireWater;


import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.thx.common.struts2.CURDAction;
import com.thx.file.model.File;

@Namespace("/upload")
@Action(value = "test",
results={
		@Result(name="list",location="/pages/upload/list.jsp"),
		@Result(name="streamAdd",location="/pages/upload/streamAdd.jsp"),
		@Result(name="streamMiniAdd",location="/pages/upload/streamMiniAdd.jsp")
		})
@Controller
@Scope("prototype")
public class TestAction extends CURDAction<File> {
	private static final long serialVersionUID = 6625749424673034439L;
	private String uploadifyId;
    public String toListPage(){
    	return "list";
    }
    public String toAddStreamPage(){
    	return "streamAdd";
    }
    public String toAddStreamMiniPage(){
    	return "streamMiniAdd";
    }
    public void  add(){
    	getResponse().setContentType("text/html;charset=UTF-8");
    	try {
			getResponse().getWriter().write("uploadifyId:"+uploadifyId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public String getUploadifyId() {
		return uploadifyId;
	}
	public void setUploadifyId(String uploadifyId) {
		this.uploadifyId = uploadifyId;
	}
    
}
