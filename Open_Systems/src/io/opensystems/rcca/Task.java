package io.opensystems.rcca;

import java.util.Date;

public final class Task {
 public int ActionID;
 public TeamMember Owner; // who is responsible for the action Item.
 public String Description; // description  of the task
 public Date startDate; // date the ActionItem was assigned.
 public Date dueDate; // date the actionItem is due.
 public Date endDate; // date the actionItem was completed.
 public boolean Status; // should change to Completed Uncompleted suspended


public Task(int actionID, String owner, String description, Date start,
		Date due, Date end, boolean status) {
	super();
	ActionID = actionID;
	Owner = new TeamMember(owner,"@",1000); // fix this later;
	Description = description;
	startDate = start;
	dueDate = due;
	endDate = end;
	Status = status;
}
 
public String toString() {
	return "ActionItem [ActionID=" + ActionID + ", Owner=" + Owner
			+ ", Description=" + Description + ", start Date =" + startDate + ", due Date="
			+ dueDate + ", end Date=" + endDate + ", Status=" + Status + "]";
} 
 
 
 
}
