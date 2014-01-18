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
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.tree.DefaultMutableTreeNode;




/**
 * @author Payman Touliat
 *
 * TODO  change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Open {
    
     // declare a file output object
	public String rootName;
	public DynamicTree m_treePanel;
	public String openTag="<Node>";
	public String closeTag="</Node>";
	private boolean previousTagWasOpen=false;
    private BufferedReader reader; // declare
    private StringBuffer strBuffer;
    
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
            rootName=returnFirstElement(strBuffer.toString(),"Root-Name");
        }
        catch(Exception e){
            System.out.println(" Error with the  xml Parser "+e);
        }
	}
	
	/**
	 * This method parses the XML data for Root Cause Tree Nodes
	 * It can be called only after {@link #Open(File)} constructor has been called. 
	 * @param m_TP is the Tree Panel which will hold the results
	 */
    public void parseXMLRootCause(DynamicTree m_TP){
        m_treePanel=m_TP;
        try {
        	for (int iGlobal=45; iGlobal<=strBuffer.toString().length()-closeTag.length();iGlobal++){
        		iGlobal=searchForNode(iGlobal,m_TP.rootNode);
        		previousTagWasOpen=false;
        	}
        	
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param sI
     * @param parent
     * @return
     */
    private int searchForNode(int sI,DefaultMutableTreeNode parent){
    	DefaultMutableTreeNode child;
    	
    	int i;
    	for(i=sI; i<=strBuffer.toString().length()-closeTag.length();i++){
    		if (strBuffer.toString().regionMatches(i, openTag, 0, openTag.length())){
    			if (previousTagWasOpen){
    				DataObject nodeInfo=processNode(sI, i);
    				if (parent.isRoot())
    					child =m_treePanel.addObject(nodeInfo);
    				else
    					child =m_treePanel.addObject(parent,nodeInfo);
    				
    				i=sI=searchForNode(i+1,child);
    			}else{
    				previousTagWasOpen=true;
    				sI=i;// mark the start of subSting
    			}
    				
    		}else if(strBuffer.toString().regionMatches(i, closeTag, 0, closeTag.length())){
    			if (previousTagWasOpen){
    				DataObject nodeInfo=processNode(sI, i);
    				if (parent.isRoot()) 
    					m_treePanel.addObject(nodeInfo);
    				else
    					m_treePanel.addObject(parent,nodeInfo);
    				previousTagWasOpen=false;
    			} else
    				return i;
    		}else
    			continue;
    	}
    	return i;
    }
    /**
     * This method reads the Node data in to an DataObjet 
     * @param startIndex
     * @param endIndex
     * @return
     */
    private DataObject processNode(int startIndex, int endIndex){
    	String str =strBuffer.toString().substring(startIndex, endIndex);
    	DataObject nodeInfo= new DataObject(0,"UNABLE TO READ");
	
    	try {
			String nameOfAttribute        =returnFirstElement(str,"Name");
			String IDOfAttribute          =returnFirstElement(str,"ID");
			String actions =returnFirstElement(str,"Actions");
			String str_selected       =returnFirstElement(str,"Selected");

			int intID=Integer.parseInt(IDOfAttribute);
			Boolean selected =Boolean.valueOf(str_selected);

			nodeInfo = new DataObject(intID ,nameOfAttribute);
			nodeInfo.myCheckBox.setSelected(selected.booleanValue());
			nodeInfo.setDescription(returnFirstElement(str,"Description"));
			nodeInfo.setReferenceURL(returnFirstElement(str,"Reference"));
			nodeInfo.setProbability(returnFirstElement(str,"Probability"));
			nodeInfo.setDifficulty(returnFirstElement(str,"Difficulty"));
			nodeInfo.setImpact(returnFirstElement(str,"Impact"));
			
			// Tokenizing the Action items into int
			StringTokenizer tokenizer= new StringTokenizer(actions,",");
			while (tokenizer.hasMoreTokens()){
		    	nodeInfo.addActionID(Integer.parseInt(tokenizer.nextToken()));
			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return nodeInfo;
    }

    /**
     * This method parses the XML data for ActionItem IDs associated 
     * with the Root Cause Nodes.
     * It can be called only after {@link #Open(File)} constructor has been called.
     * @param a ActionItems that contains the Tasks
     */
    public void parseXMLAction(ActionItems a) throws Exception
    {
    	String beginTagToSearch = "<" + "Task"+ ">";
    	String endTagToSearch = "</" + "Task"+ ">";

    	// Look for the first occurrence of begin tag
    	int index = strBuffer.toString().indexOf("<Action-Items>");
    	int indexEnd = strBuffer.toString().indexOf("</Action-Items>");
    	// TODO: Fix this later
    	if (index == -1){
    		System.out.print("didn't find any Task");
    	}else{
    		String xmlString = strBuffer.toString().substring(index,indexEnd);
    		
    		while(index != -1 && !xmlString.isEmpty())
    		{
    			index = xmlString.indexOf(beginTagToSearch);
    			// Look for end tag
    			// DOES NOT HANDLE <section Blah />
    			int lastIndex = xmlString.indexOf(endTagToSearch);
    			// Make sure there is no error
    			if((lastIndex == -1) || (lastIndex < index))
    			{throw new Exception("Parsing Error");}


    			// Add it to our list of tag values
    			Task temp = readTask(xmlString.substring(index, lastIndex));
    			a.add(temp);
    			// Try it again. Narrow down to the part of string which is not 
    			// processed yet.
    			try
    			{
    				xmlString = xmlString.substring(lastIndex + endTagToSearch.length());
    			}
    			catch(Exception e)
    			{
    				xmlString = "String Errot: Open Method";
    			}
    			// Start over again by searching the first occurrence of the begin tag 
    			// to continue the loop.
//    			index = xmlString.indexOf(beginTagToSearch);
    		}
    	}		

    }
    private Task readTask(String str){
    	// extract the substring
    	// converting Entity References
    	str =xmlFilter(str);
    	Task a = new Task(1001,"Team","UNABLE TO READ TASK","1/1/1","1/1/1","1/1/1",-1);
    	try{
    		String id = returnFirstElement(str,"ID");
    		int intID=Integer.parseInt(id);
    		String owner =returnFirstElement(str,"Owner");
    		String description =returnFirstElement(str,"Description");
    		String start =returnFirstElement(str,"Start-Date");
    		String due =returnFirstElement(str,"Due-Date");
    		String end =returnFirstElement(str,"Finish-Date");
//    		String status =returnFirstElement(str,"Status");
    		
    		a = new Task(intID,owner,description,start,due,end,0);
    		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}    
    	
    	return a; 
    }
	private String returnFirstElement(String xml,String attribute) throws Exception
	{
	    String xmlString = new String(xml);
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
		System.out.println("Unable to Find XML Attribute "+ 
				beginTagToSearch +" "+endTagToSearch);
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
