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
 * @author's Daniel R. Zentner
 * 		     Payman Toulyiat
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataObject {
	public String CATEGORY  = "CATEGORY";
	public String GROUP     = "GROUP";
	public String ATTRIBUTE = "ATTRIBUTE";
	public JCheckBox myCheckBox;
	public Vector blockedBy;
	
    private int m_id;
    private int p_id;
	protected String m_Name, ref_URL, probability, difficulty, cost;
	private String nodeType;
    private int TRL_Number; // change this with a list of action items
    protected String descriptionText;
    protected Vector allowVector;
    protected Vector disallowVector;
    protected Vector vDescriptors;
    
	

    public DataObject(int id, String name,int trl){
		m_id = id;
		setName(name);
		setTRL_Numver(trl);
		allowVector = new Vector();
		disallowVector = new Vector();
		vDescriptors=new Vector();
		probability ="low"; 
		difficulty="low"; 
		cost="low";
	}
    public DataObject(int id, String name,int trl, String _probability, String _difficulty, String _cost){
		m_id = id;
		setName(name);
		setTRL_Numver(trl);
		allowVector = new Vector();
		disallowVector = new Vector();
		vDescriptors=new Vector();
		probability = _probability;
		difficulty= _difficulty;
		cost= _cost;
	}
    public DataObject(int id,String name,int trl,String type){
        new DataObject(id,name,trl);
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
	public int getTRL_Number(){return TRL_Number;}
	public void setTRL_Numver(int trl){TRL_Number=trl;}
	public void setdesctiptionText(String desc){descriptionText=desc;}
	public String getdesctiptionText(){return descriptionText;}
	public void setReferenceURL(String rf_url){ref_URL=rf_url;}
	public String getReferenceURL(){return ref_URL;}
	public void setProbability(String p){probability=p;}
	public String getProbability(){return probability;}
	public void setDifficulty(String dif){difficulty=dif;}
	public String getDifficulty(){return difficulty;}
	public void setCost(String c){cost=c;}
	public String getCost(){return cost;}

	
	
	public Vector getallowVector(){return allowVector;}
	public Vector getdisallowVector(){return disallowVector;}
	
	
	public void addAllowVector(DataObject newD){
		allowVector.add(newD);
	}
	
	public void adddisallowVector(DataObject newD){
		disallowVector.add(newD);	
	}
	
	public void addAllAllowVectors(Vector vIn){
		if (vIn.size() == 0){
			allowVector.removeAllElements();
		}
		else{
			for (int i = 0; i < vIn.size(); i++){
				allowVector.add(vIn.get(i));
			}
		}
	}
	public void addAllDisallowVectors(Vector vIn){
		if (vIn.size() == 0){
			disallowVector.removeAllElements();
		}
		else{
			for (int i = 0; i < vIn.size(); i++){
				disallowVector.add(vIn.get(i));
			}
		}
	}
	
	public void copy(DataObject dataIn){
		m_id = dataIn.getId();
		m_Name = dataIn.getName();
		nodeType = dataIn.getType();
		TRL_Number = dataIn.getTRL_Number();
	}
}
