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
/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import java.util.Vector;

public class DynamicTree extends JPanel {
    
	protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    protected Vector newOPTIONNodesAdded;
    protected Vector processedOptionNodes;
	public int catigoryIdCounter = 1000; // remove these counters and change
	public int groupIdCounter = 2000;	// them with something else
	public int attributIdCounter = 3000;
    
    
    public DynamicTree(String groupName, Font sentFont/*,int cIdC,int gIdC,int aIdC*/) {
        super(new GridLayout(1,0));
        
        rootNode = new DefaultMutableTreeNode(groupName);
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        

        tree = new JTree(treeModel);
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setFont(sentFont);
        
        
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
        newOPTIONNodesAdded= new Vector();
        processedOptionNodes=new Vector();
        
        catigoryIdCounter = 1000;
    	groupIdCounter = 2000;
    	attributIdCounter = 3000;
    }
    public JTree getJTree(){
    	return tree;
    }
    public DefaultTreeModel getTreeModel(){
    	return treeModel;
    }
    
    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        processedOptionNodes.clear();
        newOPTIONNodesAdded.clear();
        treeModel.reload();
    }

    //NEW
    public void expand() {
      int categoriesCounter = 0;
      int groupCounter =0;
      int attributCounter = 0;

      categoriesCounter = treeModel.getChildCount(treeModel.getRoot());
      for (int i=0;i< categoriesCounter;++i){
         DefaultMutableTreeNode nodeCategoryTemp = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
         groupCounter = treeModel.getChildCount(nodeCategoryTemp);

         for(int j =0; j < groupCounter; j++){
            DefaultMutableTreeNode nodeGroupTemp=(DefaultMutableTreeNode) treeModel.getChild(nodeCategoryTemp,j);
            attributCounter =  treeModel.getChildCount(nodeGroupTemp);

            if (attributCounter == 0)
               tree.scrollPathToVisible(new TreePath(nodeGroupTemp.getPath()));
            else{
                for (int k = 0; k<attributCounter; ++k){
                    DefaultMutableTreeNode nodeAttribut=(DefaultMutableTreeNode) treeModel.getChild(nodeGroupTemp,k);
                    tree.scrollPathToVisible(new TreePath(nodeAttribut.getPath()));
                    }
               }
           }
         }


    }   //end
//    public void collapse(){
//        tree.collapsePath()
//    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
        	DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
            	DataObject nodeToBeRemoved=(DataObject) currentNode.getUserObject();
            	if (nodeToBeRemoved.getType().equals(nodeToBeRemoved.ATTRIBUTE)){
            		removeAttribute(currentNode);
            	}else if(nodeToBeRemoved.getType().equals(nodeToBeRemoved.GROUP)){
            		int attributCounter = 0;
                 	attributCounter= treeModel.getChildCount(currentNode);
                 	for(int k=0;k<attributCounter;++k){
                 		DefaultMutableTreeNode nodeAttribut=(DefaultMutableTreeNode) treeModel.getChild(currentNode,k);
                 		removeAttribute(nodeAttribut);
            		}
            	}else{            		
            		int groupCounter =0;
            		int attributCounter = 0;
            		groupCounter = treeModel.getChildCount(currentNode);
        			for(int j=0; j<groupCounter;++j){
        				DefaultMutableTreeNode nodeGroupTemp=(DefaultMutableTreeNode) treeModel.getChild(currentNode,j);
        				attributCounter= treeModel.getChildCount(nodeGroupTemp);
        				for(int k=0;k<attributCounter;k++){
        					DefaultMutableTreeNode nodeAttribut=(DefaultMutableTreeNode) treeModel.getChild(nodeGroupTemp,k);
        					removeAttribute(nodeAttribut);
        				}
        			}
            	}
            	treeModel.removeNodeFromParent(currentNode);
            }
           return; 
        } 

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /** A helper Function that removes the cuttentNode from 
     * tee and allowed and disAllowed list of other attribute nodes 
     * @param currentNode
     */
	private void removeAttribute(DefaultMutableTreeNode currentNode){
		DataObject nodeToBeRemoved=(DataObject) currentNode.getUserObject();
		boolean rmStatus=newOPTIONNodesAdded.removeElement(nodeToBeRemoved);
		if (rmStatus){// optiono was new so no need to go to compatibility list 
			
		}else{
			rmStatus=processedOptionNodes.removeElement(nodeToBeRemoved);
			for(int i=0;i<processedOptionNodes.size();++i){
				DataObject nodeInfo =(DataObject)processedOptionNodes.get(i);
				rmStatus=nodeInfo.allowVector.remove(nodeToBeRemoved);
				if(!rmStatus){
					rmStatus=nodeInfo.disallowVector.remove(nodeToBeRemoved);
				}
			}
		}
	}
    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(DataObject child) {
                  
    	DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
            ///therefor the new node has to be catagory
            child.setTypeToCategory();
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
            if (parentNode.isRoot()){
                ///therefor the new node has to be catagory
            	child.setParentId(0);
                child.setTypeToCategory();
                }
            else{
                /// else the user  may be adding a option or a group. We have to check
                DataObject cascadedParentNode = (DataObject) parentNode.getUserObject();
                if (cascadedParentNode.getType().equals(cascadedParentNode.GROUP)){
                	child.setParentId(cascadedParentNode.getId());
					child.setTypeToAttribute();
					newOPTIONNodesAdded.add(child);
                }
                else{ 
                	child.setTypeToGroup();
                } /// later we can check to see if the user is adding more than what he should
            }
        }
        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            DataObject child) {
        DataObject cascadedParentNode = (DataObject) parent.getUserObject();
        if (cascadedParentNode.getType().equals(cascadedParentNode.GROUP)){
        	child.setParentId(cascadedParentNode.getId());
        	child.setTypeToAttribute();
            newOPTIONNodesAdded.add(child);
            }
            else{ 
            child.setTypeToGroup();
            } /// later we can check to see if the user is adding more than what he should
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            DataObject child, 
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = 
                new DefaultMutableTreeNode();
        childNode.setUserObject(child);

        if (parent == null) {
            parent = rootNode;
        }
      
        treeModel.insertNodeInto(childNode, parent, 
                                 parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }
    
    /** returns a vector that lists all new nodes that have 
     * been added and are type options. 
     **/
    public Vector getNewOPTIONNodes(){
    		return newOPTIONNodesAdded;
 	
    }

    /** resets the vector that lists all new nodes that have 
     *  been added and are type options. 
     **/
    public void resetNewOPIONNodes(){
    	Vector temp= new Vector(newOPTIONNodesAdded);
    	for (int i=0;i<temp.size();++i)
    	processedOptionNodes.add(temp.get(i));
    	newOPTIONNodesAdded.clear();
    	
    }

	class MyTreeModelListener implements TreeModelListener{
        public void treeNodesChanged(TreeModelEvent e) {
        	/*DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                     (e.getTreePath().getLastPathComponent());
            
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                       (node.getChildAt(index));
                
            } catch (NullPointerException exc) {}
           
            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());*/
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }      

}
//int categoriesCounter = 0;
//int groupCounter =0;
//int attributCounter = 0;
//categoriesCounter=treeModel.getChildCount(treeModel.getRoot());  
//for(int i=0; i< categoriesCounter;++i){
//	DefaultMutableTreeNode nodeCategoryTemp = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
//	groupCounter = treeModel.getChildCount(nodeCategoryTemp);
//	for(int j=0; j<groupCounter;++j){
//		DefaultMutableTreeNode nodeGroupTemp=(DefaultMutableTreeNode) treeModel.getChild(nodeCategoryTemp,j);
//		attributCounter= treeModel.getChildCount(nodeGroupTemp);
//		for(int k=0;k<attributCounter;++k){
//			DefaultMutableTreeNode nodeAttribut=(DefaultMutableTreeNode) treeModel.getChild(nodeGroupTemp,k);
//			 nodeInfo=(DataObject) nodeAttribut.getUserObject();
//		}
//	}
//}      
