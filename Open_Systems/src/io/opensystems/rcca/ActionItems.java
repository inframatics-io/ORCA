package io.opensystems.rcca;

import java.util.Date;

public class ActionItems {

	
protected	Task actions[];
private int startingIDIndex;	

public ActionItems(int actionIDIndex, int size){
	startingIDIndex =actionIDIndex;
	actions= new Task[size];
}
public void add(int actionID, String owner, String description, Date start, Date due, Date end, boolean status){
	actions[startingIDIndex] = new Task(actionID, owner, description, start, due, end, false);
	startingIDIndex++;
}

}
