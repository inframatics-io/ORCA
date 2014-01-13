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
import java.awt.*;
//import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.KeyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
//import java.time.*
//import java.math.BigInteger;
import java.io.File;







//import java.util.Enumeration;
//import java.net.*;
//import java.util.Hashtable;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser;
//import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
//import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
//import javax.swing.event.ChangeListener;
//import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreeModel;




import layouts.*;




/**
 * @author's Payman Touliat
 * 		     
 */

public class RootCause extends JFrame implements ActionListener {

    protected File m_currentDir;
    private int newActionItemID=1;
    

    private static String ADD_COMMAND = "add";
    private static String REMOVE_COMMAND = "remove";
    private static String CLEAR_COMMAND = "clear";
    private static String EXPAND_COMMAND = "expand";

    private DefaultMutableTreeNode previousNode=null;
    private DynamicTree treePanel;

    protected JDesktopPane main_desktop;
	private JInternalFrame jifmakeActionItemsFrame;
	private JInternalFrame jifNewRootCauseFrame;
	private JInternalFrame jifDynamicFaultMatrixFrame,jifNewFilterFrame;
	private JSplitPane ptrSpLeft;
	

	protected JMenuBar menuBar;
	protected JMenu menuFile, menuView, menuTools, menuActionItems, menuHelp;
	protected JMenuItem menuItemOpen, menuItemClose, menuItemNew, menuItemExport, menuItemSave,
			  menuItemExit, menuViewProjector, menuViewDesktop, menuToolsTopsis, menuHelpAbout;
	protected JCheckBoxMenuItem menuActinItems;
	
	protected JButton createManageActionFrame, addActionItem,
			  makeFishbone,saveAttributes,backToMain,
			  backToCompatibility,resetActionItems,ref1Button;
	protected JTextField nameTextField, actionTextField, ref1TextField;
	protected JTextArea descriptionTextField,infoTextField;
	protected JTable compatMatrix, morphMatrix;

	protected JRadioButton lProbabilityRButton,mProbabilityRButton, hProbabilityRButton,
						lImpactRButton, mImpactRButton, hImpactRButton, 
						lDifficultyRButton, mDifficultyRButton, hDifficultyRButton;
	protected int maxNumberOfAttributes;
	protected String node_probability;
	protected String node_difficulty;
	protected String node_Impact;
	protected String selected;
	
	protected String errorMsg = "";
//	protected JList allowList, disallowList;GregorianCalendar(year + 1900, month, date)
	protected DataObject draggingListObject;
//	protected Vector vCheckBoxFilters, vFiltersVector=new Vector();//later we need to move this initialization to 
	protected ActionItems actionList; 
	//private JLabel possibelCombsLabel;
	JPanel possibleCombs;
	JPanel rightBorder;
	Color[] RootCauseColors;
	Dimension buttonDim;
	Font textFont;
	private boolean isAttributeSaved=true;
	private boolean isFileSaved=true; // true=YES; false=NO; 
	protected boolean ableToSave = false;
	protected boolean reToMorphMatrix = false;
	
// this is just a place holder 
	ActionListener checkBoxListener = new ActionListener(){
	    public void actionPerformed(ActionEvent e){
	        if(jifDynamicFaultMatrixFrame!= null &&jifDynamicFaultMatrixFrame.isVisible()){
	            jifDynamicFaultMatrixFrame.dispose();
	            makeDynamicRootMatrix();
	        }
	    }
	};

	public RootCause(){
		super("Open System Tool");
		setBounds(0,0,1000,700);

		main_desktop = new JDesktopPane();
		main_desktop.putClientProperty("JDesktopPane.dragMode","outline");



		makeMenu();
		buttonDim = new Dimension(150,35);
		textFont = new Font("TimesRoman",Font.ITALIC,16);
		RootCauseColors = new Color[15];

		RootCauseColors[0] = Color.decode("#DBDBE5");//"#EFEFEF");//"#9DACBD");//"#3AA4F4");//"#FFFFFF");//"#EFEFEF");//background
//		RootCauseColors[1] = Color.decode("#FFCC00");//YEllow low probability and low impact incomplete 
//		RootCauseColors[2] = Color.decode("#FF6600");//Orange Low Probability and High/Mid Impact incomplete

//		RootCauseColors[4] = Color.decode("#7384FF");//blue complete but unselected
//		RootCauseColors[5] = Color.decode("#CC0000");//Red: High/Mid Probability && High/Mid Impact) incomplete 
//		RootCauseColors[6] = Color.decode("#CD5C5C");//off incomplete but checked
		
		RootCauseColors[1] = Color.decode("#FFFF00"); //red low
		RootCauseColors[2] = Color.decode("#FFFF33");
		RootCauseColors[3] = Color.decode("#FFFF00");
		RootCauseColors[4] = Color.decode("#FF9900");
		RootCauseColors[5] = Color.decode("#FFCC00");
		RootCauseColors[6] = Color.decode("#FF9933");
		RootCauseColors[7] = Color.decode("#FF6600");
		RootCauseColors[8] = Color.decode("#FF5C33");
		RootCauseColors[9] = Color.decode("#FF3300");
		RootCauseColors[10] = Color.decode("#FF3333");
		RootCauseColors[11] = Color.decode("#FF0000");
		RootCauseColors[12] = Color.decode("#D11919");
		RootCauseColors[13] = Color.decode("#CC0000");//red high
		RootCauseColors[14] = Color.decode("#A1E09C");//Greenish complete and selected
		
		setJMenuBar(menuBar);
		getContentPane().add(main_desktop);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
// 	this function listens for all button pushing events
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (e.getSource() == menuItemNew){
		    //when File-New from the menubar is clicked
		    ableToSave = true;    //these things should later be modefied so 
			reToMorphMatrix=false;//once we have more than one frame they don't collide
			//Dialog box asking for a name
		    String rootName=(String) JOptionPane.showInputDialog(main_desktop,"Please Enter a name for your " +
			"Root Cause Investigation\n ex.  Name:...Gearbox Failure","Gearbox Failure");
		   // To Be Done: check input throughly
			if (rootName != null /* && rootName.length() >0 */ ){
				treePanel = new DynamicTree(rootName,textFont);
				newRootCauseFrame(true);
				isFileSaved = false;
			}			
		}else if (e.getSource() == menuViewDesktop){
		}else if (e.getSource() == menuItemExport){
			if (true){
				jifNewRootCauseFrame.setVisible(true);
			}
		}else if (e.getSource() == menuItemSave){
			// able to Save makes sure that some window is open 
			// latter we should do more qualified testing
			if (ableToSave){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				JFileChooser m_chooser=new JFileChooser();
				m_chooser.addChoosableFileFilter(new xmlFileFilter("xml","XML Data"));
				if (m_currentDir == null )
					m_chooser.setCurrentDirectory((new File(treePanel.rootNode.toString())));
				else
					m_chooser.setCurrentDirectory(m_currentDir);
				int result=m_chooser.showSaveDialog(main_desktop);
				repaint();
				if (result != JFileChooser.APPROVE_OPTION){
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				m_currentDir=m_chooser.getCurrentDirectory();
				File m_currentFile= m_chooser.getSelectedFile();
				Save saveFile = new Save(treePanel.treeModel, treePanel.tree,m_currentFile,actionList);
				if (saveFile.error.isEmpty()){
					isFileSaved=true;
					JOptionPane.showMessageDialog(jifNewRootCauseFrame, "File was successfully saved.");
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}else if (e.getSource() == menuItemOpen){
		    JFileChooser m_chooser=new JFileChooser();
		    m_chooser.addChoosableFileFilter(new xmlFileFilter("xml","XML Data"));
			if (m_currentDir == null )
				{m_chooser.setCurrentDirectory(new File("."));}
			else
				{m_chooser.setCurrentDirectory(m_currentDir);}
			int result=m_chooser.showOpenDialog(main_desktop);
			m_currentDir=m_chooser.getCurrentDirectory();
			File m_currentFile=m_chooser.getSelectedFile();
			if (result == JFileChooser.APPROVE_OPTION){
			    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Open openFile = new Open(m_currentFile);
				treePanel = new DynamicTree(openFile.rootName,textFont);
				newRootCauseFrame(false);
				openFile.doXMLparse(treePanel);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				ableToSave=true;
			}
			
		}
		else if (e.getSource() == menuHelpAbout){
			AboutBox dlg = new AboutBox(RootCause.this);
			dlg.setVisible(true);
		}
		else if(e.getSource() == menuItemExit){
		    // check to see if the data is saved
		    if (!isAttributeSaved || !isFileSaved){
			    int answer =JOptionPane.showConfirmDialog(jifNewRootCauseFrame,"Do you want to save the changes?");
			    switch (answer){
			    case JOptionPane.YES_OPTION:
			    	boolean statusOfSaveDataToTree = saveInputsToNode(previousNode);
					jifNewRootCauseFrame.repaint();
					if (ableToSave){
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						JFileChooser m_chooser=new JFileChooser();
						m_chooser.addChoosableFileFilter(new xmlFileFilter("xml","XML Data"));
						if (m_currentDir == null )
							m_chooser.setCurrentDirectory((new File(treePanel.rootNode.toString())));
						else
							m_chooser.setCurrentDirectory(m_currentDir);
						int result=m_chooser.showSaveDialog(main_desktop);
						repaint();
						if (result != JFileChooser.APPROVE_OPTION){
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							return;
						}
						m_currentDir=m_chooser.getCurrentDirectory();
						File m_currentFile= m_chooser.getSelectedFile();
						Save saveFile = new Save(treePanel.treeModel, treePanel.tree,m_currentFile,actionList);
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						if (saveFile.error.isEmpty())
							System.exit(NORMAL);
						else{
							answer = JOptionPane.showConfirmDialog(jifNewRootCauseFrame, 
									"Unable to Save: "+saveFile.error + " Exit anyway?");
							if (answer == JOptionPane.YES_OPTION)
								System.exit(NORMAL);
						}
							
					}
				case JOptionPane.NO_OPTION:
					System.exit(NORMAL);
				case JOptionPane.CANCEL_OPTION:;
			    }
			}else{
				int answer =JOptionPane.showConfirmDialog(jifNewRootCauseFrame, "Are you sure you want to quit?", "Exit?", JOptionPane.YES_NO_OPTION);
				switch (answer){
			    case JOptionPane.YES_OPTION:
			    	System.exit(NORMAL);
			    case JOptionPane.NO_OPTION:;
				}
			}
		    
		}
		else if (ADD_COMMAND.equals(command)) {
            //Add button clicked
			DefaultMutableTreeNode node =
		    	(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
			
			if (node == null){
				node =treePanel.rootNode; 
			}
			// get the ID based on Level: 
			// 1000-1999 level 1
			// 2000-2999 level 2
			// 3000-3999 level 3 ...
			int level=node.getLevel();
			if (level>= treePanel.ID.size())
				treePanel.ID.add(level*1000+1);
			else
				treePanel.ID.set(level, treePanel.ID.get(level)+1);
			
			DataObject newNode = new DataObject(treePanel.ID.get(level),  treePanel.ID.get(level)+" NewNode");
			// next few line can be moved to DataObject itself
			// or deleted... i think
			newNode.myCheckBox.setEnabled(true);
			newNode.myCheckBox.setSelected(false);
			newNode.myCheckBox.setBackground(RootCauseColors[newNode.getHeatIndex()]);
			treePanel.addObject(newNode);
		}

		else if (REMOVE_COMMAND.equals(command)) {
            //Remove button clicked
            treePanel.removeCurrentNode();
        }
		else if (CLEAR_COMMAND.equals(command)) {
            //Clear button clicked.
            treePanel.clear();
        }
		else if (EXPAND_COMMAND.equals(command)) {
            //Clear button clicked.
            treePanel.expand();
        }
		else if(e.getSource() == ref1Button){
			JFileChooser ref_chooser=new JFileChooser();
			if (m_currentDir == null ){ref_chooser.setCurrentDirectory(new File("."));}
			else{ref_chooser.setCurrentDirectory(m_currentDir);}
			int result=ref_chooser.showOpenDialog(main_desktop);
		    m_currentDir=ref_chooser.getCurrentDirectory();
		    if (result == JFileChooser.APPROVE_OPTION ){
		    	//System.out.println(ref_chooser.getSelectedFile().getName());
		    	ref1TextField.setText(ref_chooser.getCurrentDirectory()+ "\\" +
		    			ref_chooser.getSelectedFile().getName());
		    	saveAttributes.setEnabled(true);
		    	isAttributeSaved = false;
			}
		}
		else if (e.getSource() == saveAttributes){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			treePanel.tree.getLastSelectedPathComponent();
//			boolean statusOfSaveDataToTree = saveInputsToNode(node);
			saveInputsToNode(node);
			jifNewRootCauseFrame.repaint();
		}
		else if (e.getSource() == createManageActionFrame){
//		check to see if the data is saved
		    if (!isAttributeSaved){
		    	boolean statusOfSaveDataToTree=true;
		    	int answer =JOptionPane.showConfirmDialog(jifNewRootCauseFrame,"Do you want to save the changes?");
			    if (answer == JOptionPane.YES_OPTION) {
			        // User clicked YES.
					statusOfSaveDataToTree = saveInputsToNode(previousNode);
					jifNewRootCauseFrame.repaint();
			    } else if (answer == JOptionPane.NO_OPTION) {
			        // User clicked NO.
			    }
			    isAttributeSaved=statusOfSaveDataToTree;
			    saveAttributes.setEnabled(!statusOfSaveDataToTree);
			}
		    //initializeCompatibility(treePanel.getNewOPTIONNodes(),treePanel.processedOptionNodes);
//		    treePanel.resetNewOPIONNodes();
		    jifNewRootCauseFrame.setVisible(false);
			makeManageActionFrame();
		}
		else if (e.getSource() == addActionItem){
		    // check to see if the data is saved
//			boolean statusOfSaveDataToTree =true;
//			if (!isAttributeSaved){
//			    int answer =JOptionPane.showConfirmDialog(jifNewRootCauseFrame,"Do you want to save the changes?");
//			    switch (answer){
//			    case JOptionPane.YES_OPTION:
//			    	statusOfSaveDataToTree = saveInputsToNode(previousNode);
//					jifNewRootCauseFrame.repaint();
//				case JOptionPane.NO_OPTION:;
//				case JOptionPane.CANCEL_OPTION:;
//			    }
//			    isAttributeSaved=statusOfSaveDataToTree;
//			    saveAttributes.setVisible(!statusOfSaveDataToTree);
//			}

		    	makeAddActionFrame();
		   	    
		}
		else if (e.getSource() ==resetActionItems){ //Not sure what to do here
		
		}
		else if (e.getSource() == backToMain){
		    //problem!!
			//latterAdded = new Vector();
			//backToMorphTree = true;
			// main Morph Matrix frame is visible
			jifmakeActionItemsFrame.setVisible(false);
			ptrSpLeft.add(treePanel,BorderLayout.CENTER);
		    jifNewRootCauseFrame.setVisible(true);
		}
		else if (e.getSource() == makeFishbone){
		    	jifmakeActionItemsFrame.setVisible(false);	
		    	makeDynamicRootMatrix();

		}
		else if (e.getSource() == backToCompatibility){
		    	jifDynamicFaultMatrixFrame.dispose();
		    	jifmakeActionItemsFrame.setVisible(true);
		    	reToMorphMatrix=true;
		}
	}
//	this frame is created when the user selects File-New
//	and will make a new Root Cause
	public void newRootCauseFrame(boolean aNewFrame) {
		jifNewRootCauseFrame = new JInternalFrame(
				"Make Fault Tree",true,true,true,true);
		jifNewRootCauseFrame.setBounds(0,0,800,600);
		jifNewRootCauseFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
//------------------
// NOT SURE WHERE TO PUT THIS
// MAYBE WE SHOULD Delete IT		
		if (aNewFrame){
		actionList = new ActionItems(newActionItemID,10);
		
		actionList.add("Team", "Form the Team", "2014/1/31", "2014/1/31", "2014/1/20", Task.UNCOMPLETE);
		actionList.add("Team", "Develop the Meeting Schedule Ground Rules", "2014/1/21", "2014/1/31", "2014/1/31");
		actionList.add("Team", "Develop and Use An Action Item List", "2014/1/1", "2014/1/22");
		actionList.add("Team", "Develop the Root Cause Tree", "2014/1/1", "2014/1/23");
		actionList.add("Team", "Develop a Critical Path Schedule", "2014/1/1", "2014/1/24");
		actionList.add("Team", "Conduct the Event Investigation", "2014/1/1", "2014/1/25");
		actionList.add("Team", "Establish the Root Causes", "2014/1/1", "2014/1/26");
		actionList.add("Team", "Determine the Corrective Actions", "2014/1/1", "2014/1/27");
		actionList.add("Team", "DEstablish and Implement the Closure Plan", "2014/1/1", "2014/1/28");
		actionList.add("Team", "Final Out Brief", "2014/1/1", "2014/1/29");
		}
//------------------------------
		
		

		KeyListener akeyListener = new myKeyListener();

//		setting up of the tree
//		treePanel = new DynamicTree(rootName,textFont);
		treePanel.tree.addMouseListener(new TreeMouseListener());
        treePanel.setPreferredSize(new Dimension(500, 400));

//		panel where all of the descriptors are located on the Right Pane
//		This is using the ParagraphLayout Mgr site is listed below for more info
//		http://www.jhlabs.com/java/layout/index.html
		JPanel rightMainPanel = new JPanel(new BorderLayout());
		JPanel innerTopRightPanel = new JPanel(new ParagraphLayout());
		JPanel wrapper =new JPanel(new BorderLayout());
		JPanel innerMiddleRightPanel= new JPanel(new BorderLayout());// later we can make this the information panel
		
		innerMiddleRightPanel.setBorder(new TitledBorder(new EtchedBorder()));
		//innerMiddleRightPanel.setSize(new Dimension(300,30));
		innerTopRightPanel.setBorder(new TitledBorder(new EtchedBorder()));
		innerMiddleRightPanel.setBorder(new TitledBorder(new EtchedBorder()));
		
		// change the name for 

		
//		Name field
		JLabel nameLabel = new JLabel("Name: ");
		nameTextField = new JTextField(10);
		nameTextField.addKeyListener(akeyListener);
		

//		Action Button and Field
		addActionItem = new JButton("<HTML> <A HREF=URL>Action Items:</A></HTML> ");
		addActionItem.setOpaque(false);
		addActionItem.setMargin(new Insets(0, 0, 0, 0));
		addActionItem.addActionListener(this);
		actionTextField = new JTextField(10);
		actionTextField.addKeyListener(akeyListener);
		
		// Description Field
		JLabel descriptionTextLabel = new JLabel("Description: ");
		descriptionTextField = new JTextArea(" ",4,20);
		descriptionTextField.setLineWrap(true);
		descriptionTextField.addKeyListener(akeyListener);
		
		// Probability
		JLabel probLabel=new JLabel("Probability:");
		//Impact
		JLabel impactLabel=new JLabel("Impact:");
		//difficulty
		JLabel difficultyLabel=new JLabel("Difficulty:");

		//Reference 1
		ref1TextField=new JTextField(15);
		ref1Button = new JButton("<HTML><A HREF=URL>Reference</A>: </HTML>");
		ref1Button.setOpaque(false);
		ref1Button.setMargin(new Insets(0, 0, 0, 0));

		ref1Button.addActionListener(this);

		ButtonGroup Probability_group= new ButtonGroup();
		lProbabilityRButton  =new JRadioButton("Low");
		mProbabilityRButton  =new JRadioButton("Mid");
		hProbabilityRButton =new JRadioButton("High");
		Probability_group.add(lProbabilityRButton);
		Probability_group.add(mProbabilityRButton);
		Probability_group.add(hProbabilityRButton);
		lProbabilityRButton.setSelected(true);
		node_probability=DataObject.LOW;

		ButtonGroup impact_group= new ButtonGroup();
		lImpactRButton  =new JRadioButton("Low");
		mImpactRButton  =new JRadioButton("Mid");
		hImpactRButton =new JRadioButton("High");
		impact_group.add(lImpactRButton);
		impact_group.add(mImpactRButton);
		impact_group.add(hImpactRButton);
		lImpactRButton.setSelected(true);
		node_Impact=DataObject.LOW;
		
		ButtonGroup difficulty_group= new ButtonGroup();
		lDifficultyRButton  =new JRadioButton("Low");
		mDifficultyRButton  =new JRadioButton("Mid");
		hDifficultyRButton =new JRadioButton("High");
		difficulty_group.add(lDifficultyRButton);
		difficulty_group.add(mDifficultyRButton);
		difficulty_group.add(hDifficultyRButton);
		lDifficultyRButton.setSelected(true);
		node_difficulty=DataObject.LOW;
		ActionListener RadioButtonListener=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (lProbabilityRButton.isSelected()) node_probability = DataObject.LOW;
				else if (mProbabilityRButton.isSelected()) node_probability = DataObject.MID;
				else node_probability = DataObject.HIGH;
				
				if (lDifficultyRButton.isSelected()) node_difficulty = DataObject.LOW;
				else if (mDifficultyRButton.isSelected()) node_difficulty = DataObject.MID;
				else node_difficulty = DataObject.HIGH;
				
				if (lImpactRButton.isSelected()) node_Impact = DataObject.LOW;
				else if (mImpactRButton.isSelected()) node_Impact = DataObject.MID;
				else node_Impact = DataObject.HIGH;
				
				isAttributeSaved=false;
				saveAttributes.setEnabled(true);
			}
		};
		lProbabilityRButton.addActionListener(RadioButtonListener);
		mProbabilityRButton.addActionListener(RadioButtonListener);
		hProbabilityRButton.addActionListener(RadioButtonListener);
		lDifficultyRButton.addActionListener(RadioButtonListener);
		mDifficultyRButton.addActionListener(RadioButtonListener);
		hDifficultyRButton.addActionListener(RadioButtonListener);
		lImpactRButton.addActionListener(RadioButtonListener);
		mImpactRButton.addActionListener(RadioButtonListener);
		hImpactRButton.addActionListener(RadioButtonListener);
	
//		this is where all the features are added
		innerTopRightPanel.add(nameLabel, ParagraphLayout.NEW_PARAGRAPH);
		innerTopRightPanel.add(nameTextField);

		innerTopRightPanel.add(addActionItem, ParagraphLayout.NEW_PARAGRAPH);
		innerTopRightPanel.add(actionTextField);

		innerTopRightPanel.add(descriptionTextLabel, ParagraphLayout.NEW_PARAGRAPH_TOP);
		JScrollPane scrollPane = new JScrollPane(descriptionTextField);
		innerTopRightPanel.add(scrollPane);
		
		innerTopRightPanel.add(probLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerTopRightPanel.add(lProbabilityRButton);
		innerTopRightPanel.add(mProbabilityRButton);
		innerTopRightPanel.add(hProbabilityRButton);
		
		innerTopRightPanel.add(difficultyLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerTopRightPanel.add(lDifficultyRButton);
		innerTopRightPanel.add(mDifficultyRButton);
		innerTopRightPanel.add(hDifficultyRButton);
		
		innerTopRightPanel.add(impactLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerTopRightPanel.add(lImpactRButton);
		innerTopRightPanel.add(mImpactRButton);
		innerTopRightPanel.add(hImpactRButton);
		
		innerTopRightPanel.add(ref1Button,ParagraphLayout.NEW_PARAGRAPH);
	
		innerTopRightPanel.add(ref1TextField,ParagraphLayout.NEW_LINE);
		
		// Button to save the Attributes
		saveAttributes = new JButton("Save Attributes");
		saveAttributes.addActionListener(this);
		saveAttributes.setEnabled(false);
		
		//innerTopRightPanel.add();
		
		
		wrapper.add(innerTopRightPanel,BorderLayout.CENTER);
		wrapper.add(saveAttributes,BorderLayout.SOUTH);
		rightMainPanel.add(wrapper,BorderLayout.NORTH);
		
		JTabbedPane m_tab=new JTabbedPane();
		JPanel tab1=new JPanel(new BorderLayout()); //First Tab [Add New]
		JPanel tab2=new JPanel(new BorderLayout());
		m_tab.addTab("Instruction",tab1);
		m_tab.addTab("Actions",tab2);
		innerMiddleRightPanel.add(m_tab,BorderLayout.CENTER);
		innerMiddleRightPanel.setBorder(new EmptyBorder(5,5,5,5));
		rightMainPanel.add(innerMiddleRightPanel,BorderLayout.CENTER);

 		// This where the right button bar is made
		JPanel rightButtonBar = new JPanel(new FlowLayout());


		
		//innerMiddleRightPanel.add(saveAttributes);

		createManageActionFrame = new JButton("Manage Action Items ");
		createManageActionFrame.addActionListener(this);

//		rightButtonBar.add(addActionItem);
		rightButtonBar.add(createManageActionFrame);

//		this section deals with the left side of the panned window
//		Left buttons bar container
		JPanel leftButtonBar = new JPanel();
		leftButtonBar.setLayout(new FlowLayout());

//		add button
        JButton addButton = new JButton("Add");
        addButton.setActionCommand(ADD_COMMAND);
        addButton.addActionListener(this);

//		remove button
        JButton removeButton = new JButton("Remove");
        removeButton.setActionCommand(REMOVE_COMMAND);
        removeButton.addActionListener(this);

//    expand tree button
        JButton expandButton = new JButton("Expand");
        expandButton.setActionCommand(EXPAND_COMMAND);
        expandButton.addActionListener(this);


//		clear button
        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand(CLEAR_COMMAND);
        clearButton.addActionListener(this);

//		adding the buttons to the button bar
		leftButtonBar.add(addButton);
		leftButtonBar.add(removeButton);
		leftButtonBar.add(clearButton);
		leftButtonBar.add(expandButton);

//		creation of the split panes
//		left pane creation
		JSplitPane spLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spLeft.setDividerSize(8);
		spLeft.setDividerLocation(150);
		spLeft.setContinuousLayout(true);
		spLeft.setLayout(new BorderLayout());

		ptrSpLeft = spLeft;
//		right pane creation
		JSplitPane spRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spRight.setDividerSize(8);
		spRight.setDividerLocation(150);
		spRight.setContinuousLayout(true);
		spRight.setLayout(new BorderLayout());

//		main split pane creation
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,spLeft,spRight);
		sp.setDividerSize(8);
		sp.setDividerLocation(jifNewRootCauseFrame.getWidth()-360);
		sp.setResizeWeight(0.5);
		sp.setContinuousLayout(true);
		sp.setOneTouchExpandable(true);

//		adding the treePanel and Button Bar to the left Panel
		spLeft.add(treePanel,BorderLayout.CENTER);
		spLeft.add(leftButtonBar, BorderLayout.SOUTH);

//		adding components to the right pane
		spRight.add(rightMainPanel, BorderLayout.WEST);
		spRight.add(rightButtonBar, BorderLayout.SOUTH);

//		adding the panes and displaying the window
		jifNewRootCauseFrame.add(sp, BorderLayout.CENTER);
		main_desktop.add(jifNewRootCauseFrame);
		jifNewRootCauseFrame.setVisible(true);

	}
// this function creates the Action Item and (maybe Team) management panel
	public void makeManageActionFrame(){

	    jifmakeActionItemsFrame = new JInternalFrame(
	            treePanel.rootNode.toString()+": Manage Action Items",true,true,true,true);
		jifmakeActionItemsFrame.setBounds(0,0,980,500);
		jifmakeActionItemsFrame.setLayout(new BorderLayout());
		jifmakeActionItemsFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);

//		creation of the split panes
//		left pane creation
		JSplitPane spLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spLeft.setDividerSize(8);
		spLeft.setDividerLocation(120);
		spLeft.setContinuousLayout(true);
		spLeft.setLayout(new BorderLayout());

//		right pane creation
		JSplitPane spRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spRight.setDividerSize(8);
		spRight.setDividerLocation(120);
		spRight.setContinuousLayout(true);
		spRight.setLayout(new BorderLayout());

//		JSplitPane spRightVert = new JSplitPane(JSplitPane.VERTICAL_SPLIT,spRight,spRight);
		JPanel bottomButtonBar = new JPanel(new FlowLayout());

		backToMain = new JButton("<< Back");
		backToMain.addActionListener(this);
		resetActionItems=new JButton ("Reset");
		resetActionItems.addActionListener(this);
		makeFishbone = new JButton("Next >>");
		makeFishbone.addActionListener(this);
		bottomButtonBar.add(backToMain);
		bottomButtonBar.add(resetActionItems);
		bottomButtonBar.add(makeFishbone);
		jifmakeActionItemsFrame.add(bottomButtonBar, BorderLayout.SOUTH);

//		main split pane creation
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,spLeft,spRight);
		sp.setDividerSize(8);
		sp.setDividerLocation(jifNewRootCauseFrame.getWidth()/4);
		sp.setResizeWeight(0.5);
		sp.setContinuousLayout(true);
		sp.setOneTouchExpandable(true);

//		This is where the Action Item Table is created
		final ActionTableModel aTableM = new ActionTableModel();
		final JTable actionItemsTable = new JTable( aTableM); // maybe AtionTableModel should be global
		actionItemsTable.setAutoCreateRowSorter(true);
		actionItemsTable.setPreferredScrollableViewportSize(new Dimension(700, 200));
		actionItemsTable.getColumnModel().getColumn(0).setPreferredWidth(20);	
		actionItemsTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		actionItemsTable.getColumnModel().getColumn(2).setPreferredWidth(300);
		
		JScrollPane topRightSP= new JScrollPane(actionItemsTable);
		
// 		Create a Delete Button for Action Items
		JButton deleteActionButton = new JButton("Delete Actions");
		JButton addNewActionButton = new JButton("Create Actions");
		
		deleteActionButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                if (aTableM.getRowCount() > 0 && actionItemsTable.getSelectedRow() != -1 ){
                    aTableM.deleteRow(actionItemsTable.getSelectedRow());
                    actionItemsTable.repaint();
                }
            }
        });
		addNewActionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                    aTableM.addRow();
                    actionItemsTable.repaint();
            }
        });
        
		
//		this is the top right panel which has All the Action Items is created
		JPanel topRightPanel = new JPanel(new ParagraphLayout());
		topRightPanel.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED),"ACTION ITEMS"));
		topRightPanel.add(topRightSP,ParagraphLayout.NEW_PARAGRAPH);

//		this is the bottom right panel which has the Team Members
		JPanel bottomRightPanel = new JPanel(new FlowLayout());
		bottomRightPanel.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED),"Team Members"));
		bottomRightPanel.add(deleteActionButton,FlowLayout.LEFT);
		bottomRightPanel.add(addNewActionButton,FlowLayout.CENTER);

//		adding components to the right split panel
		spRight.add(topRightPanel,BorderLayout.NORTH);
		spRight.add(bottomRightPanel,BorderLayout.CENTER);


//		adding the treePanel and Button Bar to the left Panel
		spLeft.add(treePanel,BorderLayout.CENTER);

		jifmakeActionItemsFrame.add(sp, BorderLayout.CENTER);
		main_desktop.add(jifmakeActionItemsFrame);
		jifmakeActionItemsFrame.show();
	}
	
// this functions makes the dynamic fault matrix frame
	public void makeDynamicRootMatrix(){

		jifDynamicFaultMatrixFrame = new JInternalFrame(
				"Dynamic FaultMatrix",true,true,true,true);
		jifDynamicFaultMatrixFrame.setSize(main_desktop.getSize());
		jifDynamicFaultMatrixFrame.setLayout(new BorderLayout());
		jifDynamicFaultMatrixFrame.setBackground(Color.white);

		JSplitPane spLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spLeft.setDividerSize(8);
		spLeft.setDividerLocation(150);
		spLeft.setContinuousLayout(true);
		spLeft.setLayout(new BorderLayout());
		spLeft.setBackground(Color.white);

		JSplitPane spRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spRight.setDividerSize(8);
		spRight.setDividerLocation(150);
		spRight.setContinuousLayout(true);
		spRight.setLayout(new BorderLayout());
		spRight.setBackground(Color.white);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,spLeft,spRight);
		sp.setDividerSize(8);
		sp.setDividerLocation(jifDynamicFaultMatrixFrame.getWidth()-200);
		sp.setResizeWeight(0.5);
		sp.setContinuousLayout(true);
		sp.setOneTouchExpandable(true);
		sp.setBackground(Color.white);

		JPanel topBorder = new JPanel(new FlowLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout());

		backToCompatibility = new JButton("<< Back");
		backToCompatibility.addActionListener(this);
		buttonPanel.add(backToCompatibility);

		JTextArea txtTitle = new JTextArea(treePanel.rootNode.toString());
		txtTitle.setFont(textFont);
		txtTitle.setEditable(false);
		txtTitle.setBackground(jifDynamicFaultMatrixFrame.getBackground());
		topBorder.add(txtTitle);
		jifDynamicFaultMatrixFrame.add(topBorder, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new ParagraphLayout());
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftPanel.setBackground(RootCauseColors[0]);     

        spLeft.add(leftScrollPane, BorderLayout.CENTER);

		//DefaultMutableTreeNode  rootNode = new DefaultMutableTreeNode(treePanel.rootNode);
		
		DefaultTreeModel treeModel = treePanel.treeModel;
		
		int numDummyLabel =0;
		
		drawRootCauseTree(treeModel,treePanel.rootNode,numDummyLabel,leftPanel, false);
		
	    rightBorder = new JPanel(new BorderLayout());
////	    enableEvents(ComponentEvent.)
		JPanel lowerPanel =new JPanel(/*new ParagraphLayout()*/);
		lowerPanel.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED),"INFORMATION:"));
		infoTextField = new JTextArea(" ",15,16);// make it dynamic
		infoTextField.setLineWrap(true);
		//descriptionTextField.addKeyListener(akeyListener);
		JScrollPane scrollPane = new JScrollPane(infoTextField);
		lowerPanel.add(scrollPane/*,ParagraphLayout.NEW_PARAGRAPH*/);
		rightBorder.add(lowerPanel,BorderLayout.SOUTH);
	    spRight.add(rightBorder,BorderLayout.NORTH);
		jifDynamicFaultMatrixFrame.add(buttonPanel, BorderLayout.SOUTH);
		jifDynamicFaultMatrixFrame.add(sp, BorderLayout.CENTER);
		main_desktop.add(jifDynamicFaultMatrixFrame);
		
		jifDynamicFaultMatrixFrame.setVisible(true);
		
		// add RadioButtons to select low mid high probability
		// and other stuff such as difficulty and Impact
		// also selectors for table view (default), 
		// fish-bone view, plot view and  maybe list view
	}
//	this function makes the MenuBar for main_desktop
	public void makeMenu() {
//		Where the Menu Bar is created
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		menuView = new JMenu("View");
		menuBar.add(menuView);
		menuTools = new JMenu("Tools");
		menuBar.add(menuTools);
		menuActionItems = new JMenu("Action Items");
		menuBar.add(menuActionItems);
		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuHelp);

//		where the file submenu's are created
		menuItemNew = new JMenuItem("New");
		menuItemNew.addActionListener(this);
		menuFile.add(menuItemNew);
		menuItemOpen = new JMenuItem("Open");
		menuItemOpen.setToolTipText("Open XML only");
		menuFile.add(menuItemOpen);
		menuItemOpen.addActionListener(this);
		menuItemClose = new JMenuItem("Close");
		menuFile.add(menuItemClose);
		menuItemClose.addActionListener(this);
		menuItemExport = new JMenuItem("Export"); // will use Apache POI to export to Excel
		menuItemExport.setToolTipText("Export to Excel");
		menuFile.add(menuItemExport);
		menuItemExport.addActionListener(this);
		menuItemSave = new JMenuItem("Save");
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(this);
		
		menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_X);
		menuItemExit.setToolTipText("Exit OpenTool");
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(this);

//		where the views submenu's are created
		menuViewProjector = new JMenuItem("Projector");
		menuViewProjector.addActionListener(this);

		menuViewDesktop = new JMenuItem("Desktop");
		menuViewDesktop.addActionListener(this);

		menuView.add(menuViewDesktop);
		menuView.add(menuViewProjector);

//		where the tools submenu's are created
		menuToolsTopsis = new JMenuItem("TOPSIS");
		menuToolsTopsis.addActionListener(this);
		menuTools.add(menuToolsTopsis);

//      where the default submenu items for Action Item are created
//		Filter trlFilter=new Filter(newFilterSuffix++,"Min TRL",1,9);
		
//		trlFilter.mySlider.setMajorTickSpacing(1);
//		sliderChangeListener mySliderListener=new sliderChangeListener();
//		trlFilter.mySlider.addChangeListener(mySliderListener);
//		globalVectorOfFilters.insertElementAt(trlFilter,0);
		
		// adding the a filter to the Action Items Menu as a checkBox
		menuActinItems =new JCheckBoxMenuItem("Completed");
		menuActinItems.setSelected(true);
		menuActinItems.addActionListener(checkBoxListener);
//		vCheckBoxFilters= new Vector();
//		vCheckBoxFilters.add(menuActinItems);
		menuActionItems.add(menuActinItems);
		
//		where the Help submenu's are created
		menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
		menuHelpAbout.addActionListener(this);
	}
	/**
	 * this function makes the data vectors that allows the names to placed
	 * in the the spreadsheet table.  it is returning a boolean to notify
	 * the user if there is a category that does not have any groups associated
	 * with it.
	 * IT saves the data only if Action IDs are valid Integers and name is not 
	 * duplicate. 
	 * @param sNode
	 * @return
	 */
	public boolean saveInputsToNode(DefaultMutableTreeNode sNode){
//		String name = new String(((DataObject)sNode.getUserObject()).getName());
		String newName=nameTextField.getText();
		jifNewRootCauseFrame.repaint();
		isAttributeSaved=true;
		saveAttributes.setEnabled(false);
	    if (sNode == null || sNode.isRoot()) return false;

		DataObject nodeInfo = (DataObject)sNode.getUserObject();
		String newActions = actionTextField.getText();
		String newDesc = descriptionTextField.getText();
		String ref1 = ref1TextField.getText();
		
		
		Integer Id;
		ArrayList<Integer> tempIDs= new ArrayList<Integer>();
		boolean flag=false;
		StringTokenizer tokenizer= new StringTokenizer(newActions,",");
		// Tokenizing the Action IDs
		if (nameIDMatch(newName, ((DataObject)sNode.getUserObject()).getId())){
			try{
				while (tokenizer.hasMoreTokens()){
			    	Id= Integer.parseInt(tokenizer.nextToken());
			    	if (actionList.isValid(Id)){ // Check for valid action item ID
			    		tempIDs.add(Id);
			    	}else{
			    		
			    		JOptionPane.showMessageDialog(jifNewRootCauseFrame,
					            "Action ID: "+Id.toString()+" dosen't exist\n"+
			    				"Please enter a valid action ID");
			    		flag=true;
			    		break;
			    	}
			    }
				// if not issue then save the data
				if(!flag){
					nodeInfo.setActionIDs(tempIDs);
					nodeInfo.setdesctiptionText(newDesc);
					nodeInfo.setName(newName);
					nodeInfo.setReferenceURL(ref1);
					nodeInfo.setProbability(node_probability);
					nodeInfo.setDifficulty(node_difficulty);
					nodeInfo.setImpact(node_Impact);
					isFileSaved = false;
				}
			}
			catch (NumberFormatException nfex){
			    JOptionPane.showMessageDialog(jifNewRootCauseFrame,
			            " Please Enter A Numeric Value For Action Item ID #.");
			}
		
		}else {
			JOptionPane.showMessageDialog(null,"You have entered a Duplicate Node Name","Error!!"
					,JOptionPane.INFORMATION_MESSAGE);
    	}
		// this is done in case the user had put duplicate IDs.
		actionTextField.setText(nodeInfo.getActionIDsString());	
		return true;
	}

	public static void main(String[] args) {
		new RootCause();
	}
	

	/**
	 * This function searches the tree for name
	 *  (used by the last makeDaynamicFaultMatrix to find the 
	 *   node the user is pointing to)
	 * @param name of the node 
	 * @return the corresponding node to the name
	 */
    public DataObject search(String name){

    	DefaultTreeModel treeModel = treePanel.treeModel;
        DefaultMutableTreeNode root=(DefaultMutableTreeNode) treeModel.getRoot();
        DataObject nodeInfo;
        
        for (Enumeration e = root.depthFirstEnumeration(); e.hasMoreElements();) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (!node.isRoot()){
            	nodeInfo =(DataObject) node.getUserObject();
                if (nodeInfo.getName().equals(name)) {   
                	return nodeInfo;
                }
            }
            
        }
        return null;
    }
	/**
	 * This function searches the tree to see if the name already exists.
	 * if name and ID match 
	 * it may be unnecessary 
	 * @param name
	 * @param sentID
	 * @return true if the name and ID match otherwise false
	 */
	public boolean nameIDMatch(String name, int sentID){
		
		DataObject nodeInfo;
		nodeInfo =search(name);
		if (nodeInfo !=null)
			return nodeInfo.getId() == sentID;
		else
			return false;
	}

    public void updateDynamicMorphFrame(){
    	DefaultTreeModel treeModel = treePanel.treeModel;
    	updateRootCauseNodes(treeModel, (DefaultMutableTreeNode)treeModel.getRoot(),false);
    }
    private void updateRootCauseNodes(DefaultTreeModel model, DefaultMutableTreeNode node,boolean parentChecked){

    	if(!node.isRoot()){
    	DataObject nodeInfo=(DataObject) node.getUserObject();
    	nodeInfo.myCheckBox.setEnabled(!parentChecked);
    	if (nodeInfo.myCheckBox.isSelected())
    		nodeInfo.myCheckBox.setBackground(RootCauseColors[14]);
    	else 
    		nodeInfo.myCheckBox.setBackground(RootCauseColors[nodeInfo.getHeatIndex()]);
    	nodeInfo.myCheckBox.setEnabled(!parentChecked);
    	if (!parentChecked)
    		parentChecked =nodeInfo.myCheckBox.isSelected();
    	}
    	
    	for (int i=0; i<model.getChildCount(node);i++){
    		updateRootCauseNodes(model, (DefaultMutableTreeNode)model.getChild(node, i),parentChecked);
    	}
    }
/**
 * A recursive Function call to draw all the Root Cause Nodes in "Root Cause Tree" format 
 * @param model Tree model that will be drawn.
 * @param node is each Tree node
 * @param indent it counter to indent the Root Cause Nodes.
 * @param leftPanel 
 * @param parentChecked true is parent Node has been checked;false otherwise.
 */
    private void drawRootCauseTree(DefaultTreeModel model, DefaultMutableTreeNode node, int indent, JPanel leftPanel, boolean parentChecked){
    	// Fix the RootNode later!
    	if (node.isRoot()){
    		JButton rootDisplayButton = new JButton(node.toString());
    		rootDisplayButton.setPreferredSize(buttonDim);
    		rootDisplayButton.setBackground(RootCauseColors[13]);
    		leftPanel.add(rootDisplayButton,ParagraphLayout.NEW_PARAGRAPH);
    		--indent;
    	}
    	else{
    	
    		JButton indentionSpace = new JButton("");
    		indentionSpace.setEnabled(false);
    		indentionSpace.setBackground(RootCauseColors[0]);
    		indentionSpace.setBorderPainted(false);
    		leftPanel.add(indentionSpace);
        	leftPanel.add(indentionSpace,ParagraphLayout.NEW_PARAGRAPH);
        	for(int i=0; i<indent;++i){
        		
        		indentionSpace = new JButton("");
        		indentionSpace.setEnabled(false);
        		indentionSpace.setBackground(RootCauseColors[0]);
        		indentionSpace.setPreferredSize(buttonDim);
        		indentionSpace.setBorderPainted(false);
    			leftPanel.add(indentionSpace);  
        	}
        	
        	DataObject nodeInfo=(DataObject) node.getUserObject();
        	nodeInfo.myCheckBox.setPreferredSize(buttonDim);
        	if (nodeInfo.myCheckBox.isSelected())
        		nodeInfo.myCheckBox.setBackground(RootCauseColors[14]);
        	else 
        		nodeInfo.myCheckBox.setBackground(RootCauseColors[nodeInfo.getHeatIndex()]);
        	
        	nodeInfo.myCheckBox.setEnabled(!parentChecked);
        	parentChecked =nodeInfo.myCheckBox.isSelected();
        	cbItemListener myCB = new cbItemListener();
        	nodeInfo.myCheckBox.addItemListener(myCB);  
			nodeInfo.myCheckBox.addMouseListener(myCB);
        	leftPanel.add(nodeInfo.myCheckBox);
//        	System.out.println(space + node.toString());
        	}
    	
    	for (int i=0; i<model.getChildCount(node);i++){
    		drawRootCauseTree(model, (DefaultMutableTreeNode)model.getChild(node, i),indent + 1,leftPanel,parentChecked);
    	}
    }
    public void makeAddActionFrame(){
        jifNewFilterFrame = new JInternalFrame("Action Item"
	           ,false,true,false,false);
        
        jifNewFilterFrame.setBounds(200,100,750,320);/** Dimension **/
		jifNewFilterFrame.setLayout(new BorderLayout());
		jifNewFilterFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
		jifNewFilterFrame.setBackground(Color.white);
		JTabbedPane m_tab=new JTabbedPane();
		JPanel tab1=new JPanel(new BorderLayout()); //First Tab [Add New]
		JPanel tab2=new JPanel(new BorderLayout()); //Second Tab [Select From List]
		final JButton cancelAction= new JButton("Cancel");
		final JButton okayAction= new JButton("OK");
//		final JButton createNew= new JButton("Create New");

		
		//Tab 1 create the Add Action Item
		//Description of Action 
		JLabel actionDescriptionLabel = new JLabel("Description");
		final JTextArea actionDescriptionTextArea = new JTextArea("",2,12);
		//Owner of Action
		JLabel actionOwnerLabel = new JLabel("Owner");
		final JTextField actionOwnerText = new JTextField(12);
		//Start Date of Action
		JLabel actionSDateLabel = new JLabel("Start Date");
		final JTextField actionSDateText = new JTextField(8);
		actionSDateText.setText("14/01/01"); // todays date FIX IT
		//Owner of Action
		JLabel actionDDateLabel = new JLabel("Due Date");
		final JTextField actionDDateText = new JTextField(8);
		JPanel addPanel=new JPanel(new ParagraphLayout());
		addPanel.add(actionDescriptionLabel,ParagraphLayout.NEW_PARAGRAPH);
		addPanel.add(actionDescriptionTextArea);
		addPanel.add(actionOwnerLabel,ParagraphLayout.NEW_PARAGRAPH);
		addPanel.add(actionOwnerText);
		addPanel.add(actionSDateLabel,ParagraphLayout.NEW_PARAGRAPH);
		addPanel.add(actionSDateText);
		addPanel.add(actionDDateLabel,ParagraphLayout.NEW_PARAGRAPH);
		addPanel.add(actionDDateText);
		
		// Tab2: Create the  Action Item Table 
		final JTable actionItemsTable = new JTable( new ActionTableModel());
		//actionItemsTable.setAutoCreateRowSorter(true);
		//actionItemsTable.setPreferredScrollableViewportSize(new Dimension(700, 200));
		actionItemsTable.getColumnModel().getColumn(0).setPreferredWidth(20);	
		actionItemsTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		actionItemsTable.getColumnModel().getColumn(2).setPreferredWidth(300);
	//	actionItemsTable.getSelectionModel().addListSelectionListener(new RowListener());
		JScrollPane myTableSP= new JScrollPane(actionItemsTable);
		

		selected="";
		
		ActionListener lst=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==okayAction){
					//int[] a =actionItemsTable.getSelectedRows();
					for(int c: actionItemsTable.getSelectedRows()){
						selected += actionList.elementAt(c).getID()+",";
					}
					if(!selected.isEmpty()){
						actionTextField.setText(selected);
						isAttributeSaved=false;
						saveAttributes.setEnabled(true);
					}
					if(!actionDescriptionTextArea.getText().isEmpty()){
						int newActionID=actionList.add(actionOwnerText.getText(),actionDescriptionTextArea.getText(),
								actionSDateText.getText(),actionDDateText.getText());
						selected += newActionID;
						actionTextField.setText(selected);
						isAttributeSaved=false;
						saveAttributes.setEnabled(true);
					}
				
				}

				jifNewFilterFrame.dispose();
			}
		};
		    
		okayAction.addActionListener(lst);
		cancelAction.addActionListener(lst);
		JPanel addActionButtonBar= new JPanel(new FlowLayout());
		addActionButtonBar.add(okayAction);
		addActionButtonBar.add(cancelAction);
		
		tab2.add(myTableSP);
		tab1.add(addPanel);

		m_tab.addTab("Add New",tab1);
		m_tab.addTab("Select From List",tab2);
		JPanel p=new JPanel(new BorderLayout());
		p.add(m_tab,BorderLayout.CENTER);
		p.add(addActionButtonBar,BorderLayout.SOUTH);
		p.setBorder(new EmptyBorder(5,5,5,5));
		jifNewFilterFrame.add(p);

		main_desktop.add(jifNewFilterFrame);
		jifNewFilterFrame.setVisible(true);
    }
    // cbItemListenre is a checkbox listener. It is used in the DynamicFaultMatrix frame
    // to create a quick info is a user clicks on a Root cause.
    class  cbItemListener extends MouseMotionAdapter implements ItemListener, MouseInputListener {
    	public void itemStateChanged(ItemEvent ie) {

    		updateDynamicMorphFrame();
    	}

    	public void mouseEntered(MouseEvent evt){

    		DataObject nodeInfo=search(((JCheckBox)evt.getSource()).getText());
    		if(nodeInfo!=null){
    			infoTextField.setText("Name: "+" "+nodeInfo.getName()+'\n'+
    					"Action Items: "+" "+nodeInfo.getActionIDsString()+'\n'+
    					"Probability: "+nodeInfo.getProbability()+'\n'+
    					"Difficulty: "+nodeInfo.getDifficulty()+'\n'+
    					"Impact: "+nodeInfo.getImpact()+'\n'+
    					"Heat Score"+nodeInfo.getHeatIndex()+'\n'+
    					"Description:\n"+" "+nodeInfo.getdesctiptionText()); 
    		}
    	}

    	public void mouseClicked(MouseEvent e){}
    	public void mousePressed(MouseEvent e){}
    	public void mouseReleased(MouseEvent e){}
    	public void mouseExited(MouseEvent e){}
    }

    class TreeMouseListener extends MouseInputAdapter {
		// User clicked on the tree; Check if the information is saved
    	public void mousePressed(MouseEvent evt){
    		boolean statusOfSaveDataToTree=true;
    		if (jifNewRootCauseFrame.isSelected()){
			    if (!isAttributeSaved){
				    int answer = JOptionPane.showConfirmDialog(jifNewRootCauseFrame,"Do you want to save the changes?");
				    if (answer == JOptionPane.YES_OPTION) {
				    	statusOfSaveDataToTree = saveInputsToNode(previousNode);
				    	// User clicked YES.
				    } else if (answer == JOptionPane.NO_OPTION) {
				        // User clicked NO.
				    }
				    isAttributeSaved=statusOfSaveDataToTree;
				    saveAttributes.setEnabled(!statusOfSaveDataToTree);
				}

			    DefaultMutableTreeNode node =
			    	(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
// TO DO delete and make sure you save the discription
				if (node==null ||node.isRoot()) {
				 	nameTextField.setVisible(false);
				    actionTextField.setVisible(false);

				    descriptionTextField.setVisible(false);
				    return;
				}

			    nameTextField.setVisible(true);
		        actionTextField.setVisible(true);
		    	descriptionTextField.setVisible(true);

				DataObject nodeInfo = (DataObject)node.getUserObject();

//				if(nodeInfo.getType().equals(nodeInfo.ATTRIBUTE)){
//					addActionItem.setEnabled(true);
//				}else{
//					addActionItem.setEnabled(false);
//				}
				nameTextField.setText(nodeInfo.getName());
				// this is where the tree attributes gets updated
				String actions = new String();
				actions = nodeInfo.getActionIDsString();
				actionTextField.setText(actions);
				descriptionTextField.setText(nodeInfo.getdesctiptionText());
				ref1TextField.setText(nodeInfo.getReferenceURL());
				// setting the JRadioButton for Probability
				if (nodeInfo.getProbability().equals(DataObject.LOW)) lProbabilityRButton.setSelected(true);
				else if(nodeInfo.getProbability().equals(DataObject.MID)) mProbabilityRButton.setSelected(true);
				else if (nodeInfo.getProbability().equals(DataObject.HIGH)) hProbabilityRButton.setSelected(true);
				else System.out.println("Probability is not low, mid, or high: " +nodeInfo.getProbability());
				// setting the JRadioButton for Difficulty
				if (nodeInfo.getDifficulty().equals(DataObject.LOW)) lDifficultyRButton.setSelected(true);
				else if(nodeInfo.getDifficulty().equals(DataObject.MID)) mDifficultyRButton.setSelected(true);
				else if (nodeInfo.getDifficulty().equals(DataObject.HIGH)) hDifficultyRButton.setSelected(true);
				else System.out.println("Difficulty is not low, mid, or high: " +nodeInfo.getDifficulty());
				// setting the JRadioButton for Impact
				if (nodeInfo.getImpact().equals(DataObject.LOW)) lImpactRButton.setSelected(true);
				else if(nodeInfo.getImpact().equals(DataObject.MID)) mImpactRButton.setSelected(true);
				else if (nodeInfo.getImpact().equals(DataObject.HIGH)) hImpactRButton.setSelected(true);
				else System.out.println("Impact is not low mid, or high: " +nodeInfo.getImpact());

				//save the pointer to node for later
				previousNode=node;
			}
			else if (jifmakeActionItemsFrame.isSelected()){
				DefaultMutableTreeNode node =
					(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
				if (node==null ||node.isRoot()) {
				    return;
				}
//				DataObject nodeInfo = (DataObject)node.getUserObject();

			}

		}
	}
    /**
     * Removes all instances of the Action <b>ID</b> from the tree supplied by <b>model</b> 
     * @param model
     * @param node
     * @param ID
     */
    private void removeActionID(DefaultTreeModel model, DefaultMutableTreeNode node,Integer ID){

    	if(!node.isRoot()){
    		DataObject nodeInfo=(DataObject) node.getUserObject();
    		nodeInfo.removeActionID(ID);
    	}

    	for (int i=0; i<model.getChildCount(node);i++){
    		removeActionID(model, (DefaultMutableTreeNode)model.getChild(node, i),ID);
    	}
    }
	class myKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}
		public void keyReleased(KeyEvent e) {

		}
		public void keyTyped(KeyEvent e) {
		    char ent=e.getKeyChar();
		    boolean statusOfSaveDataToTree=false;
		    if(ent=='\n'){
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				treePanel.tree.getLastSelectedPathComponent();
				statusOfSaveDataToTree = saveInputsToNode(node);
				jifNewRootCauseFrame.repaint();
		        }
		    else if (jifNewRootCauseFrame.isSelected()) {
				isAttributeSaved = statusOfSaveDataToTree;
				saveAttributes.setEnabled(!statusOfSaveDataToTree);
				jifNewRootCauseFrame.repaint();
			}
		}
	}
	
	class xmlFileFilter extends FileFilter{
		private String m_description=null;
		private String m_extension=null;
		
		public xmlFileFilter(String extension,String description){
			m_description=description;
			m_extension=extension;
		}
		public String getDescription(){
			return m_description;
		}
		public boolean accept(File afile){
			if (afile==null)
				return false;
			if (afile.isDirectory())
				return true;
			return afile.getName().toLowerCase().endsWith(m_extension);
		}
	}
	class ActionTableModel extends AbstractTableModel {


		private String[] columnNames = {"#",
				"Owner",
				"Description",
				"Assigned",
				"Date Due",
				"Completed",
		"Status"};
		//private ActionItems data;


		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return actionList.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {

			switch (col){
			case 0: return new Integer(actionList.elementAt(row).getID());
			case 1: return actionList.elementAt(row).getOwner();
			case 2: return actionList.elementAt(row).getDescription();
			case 3: return actionList.elementAt(row).getStartDate();
			case 4: return actionList.elementAt(row).getDueDate();
			case 5: return actionList.elementAt(row).getEndDate();
			case 6: return (actionList.elementAt(row).getStatus()==1)? new Boolean(true): new Boolean(false);
			default: return new Boolean(false);
			}

		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}


		public boolean isCellEditable(int row, int col) {
			if (col == 0) {
				return false;  // user should not be able to change Task ID#.
			} else {
				return true;
			}
		}


		public void setValueAt(Object value, int row, int col) {

			switch (col){
			case 1: actionList.elementAt(row).setOwner(new TeamMember((String)value,"",1));
					fireTableCellUpdated(row, col);
					break;
			case 2: actionList.elementAt(row).setDescription((String)value);
					fireTableCellUpdated(row, col);
					break;
			case 3: actionList.elementAt(row).setStartDate((String)value);
					fireTableCellUpdated(row, col);
					break;
			case 4: actionList.elementAt(row).setDueDate((String)value);
					fireTableCellUpdated(row, col);
					break;
			case 5: actionList.elementAt(row).setEndDate((String) value);
					fireTableCellUpdated(row, col);
					break;

			case 6: actionList.elementAt(row).setStatus((boolean) value? 1: 0);
					fireTableCellUpdated(row, col);
					break;
			default : ;
			}

			
			// maybe set TableWasEdited to true;

		}
		public void deleteRow(int row){
			if (row >-1 && row<actionList.size()){
				DefaultTreeModel treeModel = treePanel.treeModel;
				removeActionID(treeModel, (DefaultMutableTreeNode)treeModel.getRoot(),
						actionList.elementAt(row).getID());
				actionList.remveAt(row);
				fireTableRowsDeleted(row,row);
			}
		}
		public void addRow(){
			actionList.add("Team", "Team", "2014/1/1", "2014/2/1");	
			fireTableRowsInserted(actionList.size()-1,actionList.size()-1);
		}

	}
}
class AboutBox extends JDialog {
	public AboutBox(Frame owner) {
		super(owner, "About", true);
		JLabel lbl = new JLabel(new ImageIcon("images/opensys.png"));
		JPanel p = new JPanel();
		Border b1 = new BevelBorder(BevelBorder.LOWERED);
		Border b2 = new EmptyBorder(5, 5, 5, 5);
		lbl.setBorder(new CompoundBorder(b1, b2));
		p.add(lbl);
		getContentPane().add(p, BorderLayout.WEST);

		String message = "Open Systems\n\n" +
				"Root Cause and Corrective Action Tool\n"+
				"Part of Open Systems(c) Toolkit\n\n"+
				"Please visit us:\n    www.opensystems.io";
		//TO BE DONE: <HTML><A HREF=\"www.open-systems.co\">www.opensystems.io</A>\n";
		JTextArea txt = new JTextArea(message);
		txt.setBorder(new EmptyBorder(5, 10, 5, 10));
		txt.setFont(new Font("Helvetica", Font.BOLD, 12));
		txt.setEditable(false);
		txt.setBackground(getBackground());
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(txt);

		message = "JVM version "+System.getProperty("java.version")+"\n"+
		" by "+System.getProperty("java.vendor");
		txt = new JTextArea(message);
		txt.setBorder(new EmptyBorder(5, 10, 5, 10));
		txt.setFont(new Font("Arial", Font.PLAIN, 12));
		txt.setEditable(false);
		txt.setLineWrap(true);
		txt.setWrapStyleWord(true);
		txt.setBackground(getBackground());
		p.add(txt);

		getContentPane().add(p, BorderLayout.CENTER);
		 
		final JButton btOK = new JButton("OK");
		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		btOK.addActionListener(lst);
		p = new JPanel();
		p.add(btOK);
		getRootPane().setDefaultButton(btOK);
		getRootPane().registerKeyboardAction(lst,
		KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
		JComponent.WHEN_IN_FOCUSED_WINDOW);
		getContentPane().add(p, BorderLayout.SOUTH);

		// That will transfer focus from first component upon dialog's show
		WindowListener wl = new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				btOK.requestFocus();
			}
		};
		addWindowListener(wl);

		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}

}
