package com.summerschool.friendfinderapplication;

import com.parse.ParseObject;

public class Task extends ParseObject {
	
	public Task() {
		
	}
	
	public boolean isCompleted() {
		return getBoolean("completed");
	}
	
	public void setComleted(boolean complete) {
		put("completed", complete);
	}
	
	public String getDescription() {
		return getString("textDescription");
	}
	
	public void setDescription(String description) {
		put("desciption", description);
	}
}
