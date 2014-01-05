package io.opensystems.rcca;

// should be changed to java.time
//import java.util.Date; 


public final class Task {
 public static int COMPLETE=1;
 public static int UNCOMPLETE=0;
 public static int DUE =-1;
 public static int OVERDUE=-2;
 
 public TeamMember Owner; // who is responsible for the action Item.
 public String Description; // description  of the task
 public String startDate; // date the ActionItem was assigned.
 public String dueDate; // date the actionItem is due.
 public String endDate; // date the actionItem was completed.
 public int Status; // should change to Completed Uncompleted suspended
 private int ActionID;

public Task(int actionID, String owner, String description, String start,
		String due, String end, int status) {
	super();
	ActionID = actionID;
	Owner = new TeamMember(owner,"@",1000); // fix this later;
	Description = description;
	startDate = start;
	dueDate = due;
	endDate = end;
	Status = status;
}

 
public TeamMember getOwner() {
	return Owner;
}

public void setOwner(TeamMember owner) {
	Owner = owner;
}

public String getDescription() {
	return Description;
}

public void setDescription(String description) {
	Description = description;
}

public String getStartDate() {
	return startDate;
}

public void setStartDate(String startDate) {
	this.startDate = startDate;
}

public String getDueDate() {
	return dueDate;
}

public void setDueDate(String dueDate) {
	this.dueDate = dueDate;
}

public String getEndDate() {
	return endDate;
}

public void setEndDate(String endDate) {
	this.endDate = endDate;
}

public int getStatus() {
	return Status;
}

public void setStatus(int status) {
	Status = status;
}

public int getID() {
	return this.ActionID;
}

public String toString() {
	return "ActionItem [ActionID=" + ActionID + ", Owner=" + Owner
			+ ", Description=" + Description + ", start Date =" + startDate + ", due Date="
			+ dueDate + ", end Date=" + endDate + ", Status=" + Status + "]";
} 
 
 
}
