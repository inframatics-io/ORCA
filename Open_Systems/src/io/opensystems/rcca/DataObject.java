/*
 * Copyright (c) 2013 Open Systems(WWW.OPENSYSTEMS.IO). All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
package io.opensystems.rcca;
//import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

/*
 * Created on Mar 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * 
 * @author's Payman Touliat
 * 		     
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataObject {
	public String CATEGORY  = "CATEGORY";
	public String GROUP     = "GROUP";
	public String ATTRIBUTE = "ATTRIBUTE";
	public JCheckBox myCheckBox;
	
    private int m_id;
    private int p_id;
	protected String m_Name, ref_URL, probability, difficulty, impact;
	private String nodeType;
//    private int TRL_Number; // change this with a list of action items
    protected String descriptionText;
    protected Vector<Integer> myActionIDs;
    
    //    protected Vector allowVector;
//    protected Vector disallowVector;
    //protected Vector vDescriptors;
    
	

    public DataObject(int id, String name){
		this(id,name, new Vector<Integer>(),"low", "low","low");
	}
    public DataObject(int id, String name,Vector<Integer> action_IDs){
    	this(id,name, action_IDs,"low", "low","low");
    }
    public DataObject(int id, String name,Vector<Integer> action_IDs, String _probability, String _difficulty, String _impact){
		m_id = id;
		setName(name);
		myActionIDs=action_IDs;
		probability = _probability;
		difficulty= _difficulty;
		impact= _impact;
	}
    public DataObject(int id,String name,String type){
        new DataObject(id,name);
        if (type.equalsIgnoreCase(CATEGORY)){
           setTypeToCategory();
        }else if(type.equalsIgnoreCase(GROUP)){
           setTypeToGroup(); 
        }else if(type.equalsIgnoreCase(ATTRIBUTE)){
            setTypeToAttribute();
        }else{
            //throw and error or something
        }
    }
	
	public int getId(){return m_id;}
	public int  getParentId(){return p_id;}
	public void setParentId(int id){p_id=id;}
	public String getType(){return nodeType;}
	public void setTypeToCategory(){nodeType=CATEGORY;}
	public void setTypeToGroup(){nodeType=GROUP;}
	public void setTypeToAttribute(){nodeType=ATTRIBUTE;}
	public void setDescription(String s){descriptionText=s;}

	public void setName(String n){m_Name=n;myCheckBox=new JCheckBox(m_Name);}
	public String getName(){return m_Name;}
	public String toString(){return m_Name;}
	/**
	 * This function return all the Action Item IDs
	 * @return Vector<Integer> 
	 */
	public Vector<Integer> getActionIDs(){return myActionIDs;}
	public String getActionIDsString(){
		String s="";
		for(int i=0; i<myActionIDs.size(); i++){
			s+=myActionIDs.get(i).toString()+",";
		}
		return s;
	}
	public void addActionID(int id){myActionIDs.add(id);}
	public void setActionIDs(Vector<Integer> aIDs){ myActionIDs=aIDs;}
	
	
	public void setdesctiptionText(String desc){descriptionText=desc;}
	public String getdesctiptionText(){return descriptionText;}
	
	public void setReferenceURL(String rf_url){ref_URL=rf_url;}
	public String getReferenceURL(){return ref_URL;}
	
	public void setProbability(String p){probability=p;}
	public String getProbability(){return probability;}
	
	public void setDifficulty(String dif){difficulty=dif;}
	public String getDifficulty(){return difficulty;}
	
	public void setImpact(String c){this.impact=impact;}
	public String getImpact(){return this.impact;}

	
//	public void linkActionItem(int actionID){
//		myActionIDs.add((Integer)actionID);
//	}
	
	public void copy(DataObject dataIn){
		m_id = dataIn.getId();
		m_Name = dataIn.getName();
		nodeType = dataIn.getType();
		myActionIDs = dataIn.getActionIDs();
	}
}
