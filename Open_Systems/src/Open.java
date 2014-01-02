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
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

/*
 * Created on Apr 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Payman S. Touliat
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Open {
    
     // declare a file output object
    private BufferedReader reader; // declare
    private StringBuffer strBuffer;
	public String rootName;
	public DynamicTree m_treePanel;
	private DefaultMutableTreeNode c_category;
	private DefaultMutableTreeNode c_group;
	private Vector notProcessedYet = new Vector();
	private int AttributeIDCounter, GroupIDCounter, CatagoryIDCounter;
	
    
	
	public Open(File openf){    
        strBuffer=new StringBuffer(10000);
        String temp=null;
		try {
        	reader = new BufferedReader(new FileReader(openf));
        	while((temp=reader.readLine())!=null){
        	    	strBuffer.append(temp);
//        	    	System.out.println(strBuffer.capacity());
        		}
		}
    	catch (Exception e){
    		    System.out.println("Error in opening file "+e);
    	}
        try{
            Vector v_rootName=xmlParser(strBuffer.toString(),"Root-Name");
            rootName=(String) v_rootName.elementAt(0);
        }
        catch(Exception e){
            System.out.println(" Error with the  xml Parser "+e);
        }
	}
	
	
    public void doXMLparse(DynamicTree m_TP){
        m_treePanel=m_TP;
        try {
            Vector m_categories=xmlParser(strBuffer.toString(),"Category");
            addCategories(m_categories);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        m_treePanel.resetNewOPIONNodes();
        m_treePanel.newOPTIONNodesAdded=notProcessedYet;
    }
    private void addCategories(Vector v_category){
        DataObject nodeInfo;
        for(int i=0; i<v_category.size();++i){
           try {
            String nameOfCategory =returnFirstElement((String)v_category.elementAt(i),"Name");
            String IDOfCategory   =returnFirstElement((String)v_category.elementAt(i),"ID");
            String TRLOfCategory  =returnFirstElement((String)v_category.elementAt(i),"TRL");
            String DescriptionOfCategory =returnFirstElement((String)v_category.elementAt(i),"Description");
            String reference_URL =returnFirstElement((String)v_category.elementAt(i),"Reference");
            
            

            int intID=Integer.parseInt(IDOfCategory);
            m_treePanel.catigoryIdCounter=intID;
            int intTRL=Integer.parseInt(TRLOfCategory);
        
            
            nodeInfo =new DataObject(intID ,nameOfCategory,intTRL);
            nodeInfo.setDescription(xmlFilter(DescriptionOfCategory));
            nodeInfo.setReferenceURL(reference_URL);
            
            c_category=m_treePanel.addObject(nodeInfo);
            Vector v_groups=xmlParser((String)v_category.elementAt(i),"Group");
            addGroups(v_groups);
           
           } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	       }
           
        }
    }
     
    private void addGroups(Vector v_group){
        DataObject nodeInfo;
        for(int i=0; i<v_group.size();++i){
           try {
            String nameOfGroup =returnFirstElement((String)v_group.elementAt(i),"Name");
            String IDOfGroup  =returnFirstElement((String)v_group.elementAt(i),"ID");
            String TRLOfGroup  =returnFirstElement((String)v_group.elementAt(i),"TRL");
            String DescriptionOfGroup =returnFirstElement((String)v_group.elementAt(i),"Description");
            String reference_URL =returnFirstElement((String)v_group.elementAt(i),"Reference");

            int intID=Integer.parseInt(IDOfGroup);
            m_treePanel.groupIdCounter=intID;
            int intTRL=Integer.parseInt(TRLOfGroup);
        
            
            nodeInfo =new DataObject(intID ,nameOfGroup,intTRL);
            nodeInfo.setDescription(xmlFilter(DescriptionOfGroup));
            nodeInfo.setReferenceURL(reference_URL);
            c_group=m_treePanel.addObject(c_category,nodeInfo);
            Vector v_attributes=xmlParser((String)v_group.elementAt(i),"Attribute");
            addAttributes(v_attributes);
           
           } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	       }
           
        }        
    }
   
    private void addAttributes(Vector v_attribute){
        DataObject nodeInfo;
        for(int i=0; i<v_attribute.size();++i){
           try {
            String nameOfAttribute        =returnFirstElement((String)v_attribute.elementAt(i),"Name");
            String IDOfAttribute          =returnFirstElement((String)v_attribute.elementAt(i),"ID");
            String TRLOfAttribute  		  =returnFirstElement((String)v_attribute.elementAt(i),"TRL");
            String DescriptionOfAttribute =returnFirstElement((String)v_attribute.elementAt(i),"Description");
            String reference_URL 		  =returnFirstElement((String)v_attribute.elementAt(i),"Reference");
            String str_selected       =returnFirstElement((String)v_attribute.elementAt(i),"Selected");
           
            int intID=Integer.parseInt(IDOfAttribute);
            m_treePanel.attributIdCounter=intID;
            int intTRL=Integer.parseInt(TRLOfAttribute);
            Boolean selected =Boolean.valueOf(str_selected);
            
            nodeInfo =new DataObject(intID ,nameOfAttribute,intTRL);
            nodeInfo.myCheckBox.setSelected(selected.booleanValue());
            nodeInfo.setDescription(xmlFilter(DescriptionOfAttribute));
            nodeInfo.setReferenceURL(reference_URL);
            nodeInfo.setProbability(returnFirstElement((String)v_attribute.elementAt(i),"Probability"));
            nodeInfo.setDifficulty(returnFirstElement((String)v_attribute.elementAt(i),"Difficulty"));
            nodeInfo.setCost(returnFirstElement((String)v_attribute.elementAt(i),"Cost"));
            
            String s_Incompatibility=returnFirstElement((String) v_attribute.elementAt(i),"Incompatible");
            doCompatibility(s_Incompatibility,nodeInfo);
           } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	       }
           
        }           
    }
    // delte doCompatibility
    public void doCompatibility(String s_Incompatibility,DataObject nodeInfo){
    	try{
	    	Vector v_Incompatibility=xmlParser(s_Incompatibility,"Name");
	    	for(int i=0;i<m_treePanel.newOPTIONNodesAdded.size();++i){    
    			DataObject nodeTemp=(DataObject)m_treePanel.newOPTIONNodesAdded.get(i);
    			boolean found=false;
    			for(int j=0;j<v_Incompatibility.size();++j){
    	    	    String nameOfAIncompNode=(String)v_Incompatibility.get(j);
    	    	    if(nameOfAIncompNode.equals(nodeTemp.getName())){	
            			found=true;
            			break;
            		}    
    			}
    			if(found){
        			nodeInfo.disallowVector.add(nodeTemp);
        			nodeTemp.disallowVector.add(nodeInfo);
    			}else{
    			    nodeInfo.allowVector.add(nodeTemp);        
			        nodeTemp.allowVector.add(nodeInfo);
    			}
        	}
	        m_treePanel.addObject(c_group,nodeInfo);
    	}catch (Exception e){
    		System.out.println(e);
    	}
    }
	private Vector xmlParser(String xml,String attribute) throws Exception
	{
	    String xmlString = new String(xml);
		Vector v = new Vector();
		String beginTagToSearch = "<" + attribute + ">";
		String endTagToSearch = "</" + attribute + ">";
		
		// Look for the first occurrence of begin tag
		int index = xmlString.indexOf(beginTagToSearch);
		while(index != -1)
		{
            // Look for end tag
			// DOES NOT HANDLE <section Blah />
			int lastIndex = xmlString.indexOf(endTagToSearch);
			// Make sure there is no error
			if((lastIndex == -1) || (lastIndex < index))
				{throw new Exception("Parsing Error");}
			
			// extract the substring
			String subs = xmlString.substring((index + beginTagToSearch.length()), lastIndex) ;
            // converting Entity References
			subs =xmlFilter(subs);
			// Add it to our list of tag values
			v.addElement(subs);
            // Try it again. Narrow down to the part of string which is not 
            // processed yet.
			try
			{
				xmlString = xmlString.substring(lastIndex + endTagToSearch.length());
			}
			catch(Exception e)
			{
				xmlString = "";
			}
             // Start over again by searching the first occurrence of the begin tag 
             // to continue the loop.
			index = xmlString.indexOf(beginTagToSearch);
		}		
		
		return v;	
	}
	private String returnFirstElement(String xml,String attribute) throws Exception
	{
	    String xmlString = new String(xml);
		Vector v = new Vector();
		String beginTagToSearch = "<" + attribute + ">";
		String endTagToSearch = "</" + attribute + ">";
		
		// Look for the first occurrence of begin tag
		int index = xmlString.indexOf(beginTagToSearch);
		if(index != -1)
		{
            // Look for end tag
			// DOES NOT HANDLE <section Blah />
			int lastIndex = xmlString.indexOf(endTagToSearch);
			// Make sure there is no error
			if((lastIndex == -1) || (lastIndex < index))
				{throw new Exception("Parsing Error");}
			
			// extract the substring
			String subs = xmlString.substring((index + beginTagToSearch.length()), lastIndex) ;
            // converting Entity References
			subs =xmlFilter(subs);
			// Add it to our list of tag values
			return subs;    
		}		
		System.out.println("not found "+ attribute);
		return "";
	
	}
	// converting Entity References
	private String xmlFilter(String st){
	    if (st!=null){
	    st=st.replace("&lt;","<");
	    st=st.replace("&gt;",">");
	    st=st.replace("&amp;","&");
	    st=st.replace("&apos;","'");
	    // do one for "
	    }
	    return st;    
	}
}
