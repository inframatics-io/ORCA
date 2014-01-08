package io.opensystems.rcca;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/*
 * Created on Apr 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * 
 * @author Payman S. Touliat
 * 		   
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Save {
	private DefaultTreeModel treeModel;
//	private JTree tree;
	protected File m_currentDir;
	protected File m_currentFile;
	protected PrintStream p; // declare a print stream object
	public String error;
	
	public Save(DefaultTreeModel treeModelIn, JTree treeIN, File fChoosen){
		treeModel = treeModelIn;
		//tree = treeIN;
		error ="";
		if(fChoosen.getName().toLowerCase().endsWith(".xml"))
			m_currentFile=fChoosen;
		else{
			System.out.println(fChoosen.getName());
			m_currentFile=new File(fChoosen.getName()+".xml");
			System.out.println(m_currentFile.getName());	
		}
		saveFile();
	}


	protected void saveFile(){
		FileOutputStream out; // declare a file output object
        
        
		try {
			out = new FileOutputStream(m_currentFile);
        	p = new PrintStream( out );
       
        	p.println("<Root>" +"<Root-Name>"+ m_currentFile.getName() +"</Root-Name>");   
        	printNodeStructure();
    		p.println("</Root>");
    		p.close();
  		}
		catch (Exception e){
                System.err.println ("Error writing to file");
                error="Error writing to file";
        }
	}
	private void printNodeStructure(){
	    int categoriesCounter = 0;
		int groupCounter =0;
		int attributCounter = 0;
		
		//triple for loop, saves each value of the tree into 
		//there respective Vector structure 
		//DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
		categoriesCounter = treeModel.getChildCount(treeModel.getRoot());
		 		
		
		for (int i=0;i< categoriesCounter;++i){
		// Next two lines extracts the DataObject form the tree    
		    DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
		    DataObject nodeInfo= (DataObject) nodeTemp.getUserObject();
		    
		    
		    groupCounter = treeModel.getChildCount(treeModel.getChild(treeModel.getRoot(),i));
		    nodePrint(nodeInfo, groupCounter ,"<Category>",p);
		    
		    for(int j =0; j < groupCounter; j++){
			    nodeTemp = (DefaultMutableTreeNode) treeModel.getChild((treeModel.getChild(treeModel.getRoot(),i)),j);
			    nodeInfo= (DataObject) nodeTemp.getUserObject();
			    nodePrint(nodeInfo,groupCounter,"<Group>",p);   
			    
				attributCounter =  treeModel.getChildCount(treeModel.getChild((
									treeModel.getChild(treeModel.getRoot(),i)),j));
				for(int k = 0; k< attributCounter;k++){
				    nodeTemp = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getChild(
							treeModel.getChild(treeModel.getRoot(),i),j),k);
				    nodeInfo= (DataObject) nodeTemp.getUserObject();
				    nodePrint(nodeInfo,attributCounter,"<Attribute>",p);		
				}
				p.println("  </Group>");
			}
		    p.println(" </Category>");
		}
	}
	// converting Entity References
	private String xmlFilter(String st){
	    if (st!=null){
	    st=st.replace("<","&lt;");
	    st=st.replace(">","&gt;");
	    st=st.replace("&","&amp;");
	    st=st.replace("'","&apos;");
	    }
	    return st;    
	}
	
	private void nodePrint(DataObject nodeIn, int counter, String command, PrintStream p){
		//Put in XML or TXT write capibilities here
		String value = new String("");
		if (command == "<Category>"){
			value =" "+ "<Category>" +
							"<Name>" + xmlFilter(nodeIn.getName()) + "</Name>"+
							"<ID>" + nodeIn.getId()+"</ID>" +
							"<ACTIONS>"+ nodeIn.getActionIDsString()+"</ACTIONS>"+
							"<Description>"+ xmlFilter(nodeIn.getdesctiptionText()) + "</Description>"+
							"<Probability>"+ nodeIn.getProbability()+ "</Probability>"+
							"<Difficulty>"+ nodeIn.getDifficulty()+ "</Difficulty>"+
							"<Cost>"+ nodeIn.getCost()+ "</Cost>"+
							"<Reference>"+ xmlFilter(nodeIn.getReferenceURL()) + "</Reference>";
							
							//+"<Parent-ID>"+nodeIn.getParentId()+"</Parent-ID>";
			p.println(value);
		}
		else if (command == "<Group>"){
			value = "  "  + "<Group>" + 
							"<Name>"+xmlFilter(nodeIn.getName()) + "</Name>" +
							"<ID>"+nodeIn.getId() +"</ID>" +
							"<ACTIONS>"+nodeIn.getActionIDsString()+"</ACTIONS>" +
							"<Description>"+ xmlFilter(nodeIn.getdesctiptionText()) + "</Description>"+
							"<Probability>"+ nodeIn.getProbability()+ "</Probability>"+
							"<Difficulty>"+ nodeIn.getDifficulty()+ "</Difficulty>"+
							"<Cost>"+ nodeIn.getCost()+ "</Cost>"+
							"<Reference>"+ xmlFilter(nodeIn.ref_URL) + "</Reference>";
			p.println(value);
		}
		else if (command == "<Attribute>"){
			value = "   " + "<Attribute>" + 
							"<Name>"+xmlFilter(nodeIn.getName()) +"</Name>"+ 
							"<ID>"+nodeIn.getId()+"</ID>"+
							"<ACTIONS>"+nodeIn.getActionIDsString()+"</ACTIONS>" +  
							"<Description>"+ xmlFilter(nodeIn.getdesctiptionText())+"</Description>" +
							"<Probability>"+ nodeIn.getProbability()+ "</Probability>"+
							"<Difficulty>"+ nodeIn.getDifficulty()+ "</Difficulty>"+
							"<Cost>"+ nodeIn.getCost()+ "</Cost>"+
							"<Reference>"+ xmlFilter(nodeIn.ref_URL) + "</Reference>"+
							"<Selected>"+nodeIn.myCheckBox.isSelected()+"</Selected>";
			p.println(value);
  			p.println("   <Incompatible>"+DataObjectVectorToString(nodeIn.myActionIDs)+"</Incompatible>"+"</Attribute>");
			
			}
	}

	private String DataObjectVectorToString(Vector<Integer> vIn){
		String value = new String("");
		for (int i = 0; i < vIn.size(); i++){
			value += "<ActionID>"+xmlFilter(vIn.elementAt(i).toString())+"</ActionID>";
		}
		
		return value;
	}
	
	
}
