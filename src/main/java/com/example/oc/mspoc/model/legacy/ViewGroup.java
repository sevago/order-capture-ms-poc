package com.example.oc.mspoc.model.legacy;

import java.util.LinkedList;
import java.util.List;

public class ViewGroup {
	private String groupName;
	private List<String> groupMembers = new LinkedList<String>();
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}
	
	public void addGroupMember(String groupMember) {
		this.groupMembers.add(groupMember);
	}

}
