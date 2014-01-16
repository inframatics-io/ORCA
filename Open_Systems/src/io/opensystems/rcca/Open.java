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
import javax.swing.tree.DefaultMutableTreeNode;




/**
 * @author Payman Touliat
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
	private boolean previousTagWasOpen=false;
	
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
	
	
    public void parseXMLRootCause(DynamicTree m_TP){
        m_treePanel=m_TP;
//        String openTag="<Node>";
    	String closeTag="</Node>";
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
    private int searchForNode(int sI,DefaultMutableTreeNode parent){
    	String openTag="<Node>";
    	String closeTag="</Node>";
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
    private DataObject processNode(int startIndex, int endIndex){
    	String str =strBuffer.toString().substring(startIndex, endIndex);
    	DataObject nodeInfo= new DataObject(0,"UNABLE TO READ");
	
    	try {
			String nameOfAttribute        =returnFirstElement(str,"Name");
			String IDOfAttribute          =returnFirstElement(str,"ID");
			String DescriptionOfAttribute =returnFirstElement(str,"Description");
			String reference_URL 		  =returnFirstElement(str,"Reference");
			String str_selected       =returnFirstElement(str,"Selected");

			int intID=Integer.parseInt(IDOfAttribute);
			Boolean selected =Boolean.valueOf(str_selected);

			nodeInfo = new DataObject(intID ,nameOfAttribute);
			nodeInfo.myCheckBox.setSelected(selected.booleanValue());
			nodeInfo.setDescription(xmlFilter(DescriptionOfAttribute));
			nodeInfo.setReferenceURL(reference_URL);
			nodeInfo.setProbability(returnFirstElement(str,"Probability"));
			nodeInfo.setDifficulty(returnFirstElement(str,"Difficulty"));
			nodeInfo.setImpact(returnFirstElement(str,"Impact"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return nodeInfo;
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
