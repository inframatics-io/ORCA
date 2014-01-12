package io.opensystems.rcca;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;




/**
 * 
 * @author Payman Touliat
 * 		   
 *
 * Created on January 11,2014
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Save {
	private DefaultTreeModel treeModel;
	protected File m_currentDir;
	protected File m_currentFile;
	protected PrintStream p; // declare a print stream object
	public String error;
	protected ActionItems aList;

	public Save(DefaultTreeModel treeModelIn, JTree treeIN, File fChoosen,ActionItems alist){
		treeModel = treeModelIn;
		aList =alist;
		error ="";
		if(fChoosen.getName().toLowerCase().endsWith(".xml"))
			m_currentFile=fChoosen;
		else{

			m_currentFile=new File(fChoosen.getName()+".xml");

		}
		saveFile();
	}

	protected void saveFile(){
		FileOutputStream out; // declare a file output object


		try {
			out = new FileOutputStream(m_currentFile);
			p = new PrintStream( out );

			p.println("<Root>" +"<Root-Name>"+ m_currentFile.getName() +"</Root-Name>");   
			p.println("<Root-Cause-Tree>");
			printNodes((DefaultMutableTreeNode) treeModel.getRoot(),"  ");
			p.println("</Root-Cause-Tree>");
			p.println("<Action-Items>");
			printActions();
			p.println("</Action-Items>");
			p.println("</Root>");

			p.close();
		}
		catch (Exception e){
			System.err.println ("Error writing to file");
			error="Error writing to file";
		}
	}
	/**
	 *  A recursive call to print data for each node
	 * @param node
	 * @param indent
	 */
	private void printNodes(DefaultMutableTreeNode node,String indent){
		boolean imALastChild=true;
		String value;
		if(!node.isRoot()){
			indent +=" ";

			DataObject nodeInfo=(DataObject) node.getUserObject();
			value = indent + "<Node>" + 
					"<Name>"+xmlFilter(nodeInfo.getName()) +"</Name>"+ 
					"<ID>"+nodeInfo.getId()+"</ID>"+
					"<ACTIONS>"+nodeInfo.getActionIDsString()+"</ACTIONS>" +  
					"<Description>"+ xmlFilter(nodeInfo.getdesctiptionText())+"</Description>" +
					"<Probability>"+ nodeInfo.getProbability()+ "</Probability>"+
					"<Difficulty>"+ nodeInfo.getDifficulty()+ "</Difficulty>"+
					"<Impact>"+ nodeInfo.getImpact()+ "</Impact>"+
					"<Reference>"+ xmlFilter(nodeInfo.ref_URL) + "</Reference>"+
					"<Selected>"+nodeInfo.myCheckBox.isSelected()+"</Selected>";
			p.print(value);
		}
		for (int i=0; i<treeModel.getChildCount(node);i++){
			printNodes((DefaultMutableTreeNode)treeModel.getChild(node, i),indent);
			imALastChild =false;
		}
		if(!node.isRoot()){
			if (imALastChild)
				p.println("</Node>");
			else
				p.println(indent+"</Node>");
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
	
	/**
	 * Prints all the Action Items in the <b>alist</b>-<i>ActionList</i>
	 * 
	 */
	private void printActions(){
		String value;
		for(int i=0; i<aList.size();++i){
			value ="<Action>"+"<ID>"+aList.elementAt(i).getID()+"</ID>"+
					"<Owner>"+aList.elementAt(i).getOwner()+"</Owner>"+
					"<Description>"+aList.elementAt(i).getDescription()+"</Description>"+
					"<Start-Date>"+aList.elementAt(i).getStartDate()+"</Start-Date>"+
					"<Due-Date>"+aList.elementAt(i).getDueDate()+"</Due-Date>"+
					"<Finish-Date>"+aList.elementAt(i).getEndDate()+"</Finish-Date>"+
					"<Status>"+aList.elementAt(i).getStatus()+"</Status>"+
					"</Action>";
			p.println(value);
		}
	}
}