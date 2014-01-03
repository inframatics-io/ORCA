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
import java.awt.*;
import java.awt.dnd.*;
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
import java.math.BigInteger;
import java.util.Vector;
import java.io.File;
import java.util.Enumeration;
//import java.net.*;
import java.util.Hashtable;












import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import layouts.*;




/**
 * @author's Daniel R. Zentner
 * 		     Payman S. Touliat
 */

public class MorphMatrix extends JFrame implements ActionListener {

    protected File m_currentDir;
	private int newNodeSuffix = 1;
    private int newFilterSuffix=1;
    

    private static String ADD_COMMAND = "add";
    private static String REMOVE_COMMAND = "remove";
    private static String CLEAR_COMMAND = "clear";
    private static String EXPAND_COMMAND = "expand";

    private DefaultMutableTreeNode previousNode=null;
    private DynamicTree treePanel;

    protected JDesktopPane main_desktop;
	private JInternalFrame jifMakeCompatMatrixFrame;
	private JInternalFrame jifNewMorphMatrixFrame;
	private JInternalFrame jifDynamicFaultMatrixFrame,jifNewFilterFrame;
	private JSplitPane ptrSpLeft;
	

	protected JMenuBar menuBar;
	protected JMenu menuFile, menuView, menuTools, menuFilter, menuHelp;
	protected JMenuItem menuItemOpen, menuItemClose, menuItemNew, menuItemEdit, menuItemSave,
			  menuItemExit, menuViewCove, menuViewDesktop, menuToolsTopsis, menuHelpAbout;
	protected JCheckBoxMenuItem menuTrlFilter;
	
	protected JButton createCompatMatrix, addActionItem,
			  makeMorphMatrix,saveAttributes,backToMain,
			  backToCompatibility,resetCompatibility,ref1Button;
	protected JTextField nameTextField, trlTextField, ref1TextField;
	protected JTextArea descriptionTextField,infoTextField;
	protected JTable compatMatrix, morphMatrix;

	protected JRadioButton lProbabilityRButton,mProbabilityRButton, hProbabilityRButton,
						lCostRButton, mCostRButton, hCostRButton, 
						lDifficultyRButton, mDifficultyRButton, hDifficultyRButton;
	protected int maxNumberOfAttributes;
	protected String node_probability;
	protected String node_difficulty;
	protected String node_cost;
	
	protected String errorMsg = "";
	protected JList allowList, disallowList;
	protected DataObject draggingListObject;
	protected Vector vCheckBoxFilters, vFiltersVector=new Vector();//later we need to move this inizaization to 
	protected Vector globalVectorOfFilters=new Vector();  
	private JLabel possibelCombsLabel;
	JPanel possibleCombs;
	JPanel rightBorder;
	Color[] dynamicMorphColors;
	// Color choices for dynamicMorph
	// 0=background
	// 1=Catigory
	// 2=groups
	// 3=attributes
	// 4=options
	// 5=inactive attribute
    // 6=inactive attribute due to filter
	Dimension buttonDim;
	Font textFont;
	private boolean isAttributeSaved=true;
	private boolean isFileSaved=false;
	protected boolean ableToSave = false;
	protected boolean reToMorphMatrix = false;
	

	ActionListener checkBoxListener = new ActionListener(){
	    public void actionPerformed(ActionEvent e){
	        if(jifDynamicFaultMatrixFrame!= null &&jifDynamicFaultMatrixFrame.isVisible()){
	            jifDynamicFaultMatrixFrame.dispose();
	            makeDynamicFaultMatrix();
	            infoTextField.setText("Please click on the new Filter to apply the setting");
	        }
	    }
	};

	public MorphMatrix(){
		super("Open System Tool");
		setBounds(0,0,1000,700);

		main_desktop = new JDesktopPane();
		main_desktop.putClientProperty("JDesktopPane.dragMode","outline");



		makeMenu();
		buttonDim = new Dimension(150,35);
		textFont = new Font("TimesRoman",Font.ITALIC,16);
		// Color choices for dynamicMorph
		// 0=background
		// 1=Catigory
		// 2=groups
		// 3=attributes
		// 4=options
		// 5=inactive attribute due to incompatibility
		// 6=inactive attribute due to filter
		dynamicMorphColors = new Color[7];

		dynamicMorphColors[0] = Color.decode("#DBDBE5");//"#EFEFEF");//"#9DACBD");//"#3AA4F4");//"#FFFFFF");//"#EFEFEF");//background
		dynamicMorphColors[1] = Color.decode("#FFCC00");//cats
		dynamicMorphColors[2] = Color.decode("#FF6600");//groups
		dynamicMorphColors[3] = Color.decode("#A1E09C");//attrib
		dynamicMorphColors[4] = Color.decode("#7384FF");//options
		dynamicMorphColors[5] = Color.decode("#CC0000");//off
		dynamicMorphColors[6] = Color.decode("#CD5C5C");//off due to filter 


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
				newMorphMatrixFrame(true);
			}			
		}else if (e.getSource() == menuViewDesktop){
		}else if (e.getSource() == menuItemEdit){
			if (true){
				jifNewMorphMatrixFrame.setVisible(true);
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
				Save saveFile = new Save(treePanel.treeModel, treePanel.tree,m_currentFile);
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
				newMorphMatrixFrame(false);
				openFile.doXMLparse(treePanel);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				ableToSave=true;
			}
			
		}
		else if (e.getSource() == menuHelpAbout){
			AboutBox dlg = new AboutBox(MorphMatrix.this);
			dlg.setVisible(true);
		}
		else if(e.getSource() == menuItemExit){
		    // check to see if the data is saved
		    if (!isAttributeSaved || !isFileSaved){
			    int answer =JOptionPane.showConfirmDialog(jifNewMorphMatrixFrame,"Do you want to save the changes?");
			    switch (answer){
			    case JOptionPane.YES_OPTION:
			    	boolean statusOfSaveDataToTree = saveInputsToNode(previousNode);
					jifNewMorphMatrixFrame.repaint();
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
						Save saveFile = new Save(treePanel.treeModel, treePanel.tree,m_currentFile);
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						isFileSaved=true;
						System.exit(NORMAL);
					}
				case JOptionPane.NO_OPTION:
					System.exit(NORMAL);
				case JOptionPane.CANCEL_OPTION:;
			    }
			}
		    
			
			
		}
		else if (ADD_COMMAND.equals(command)) {
            //Add button clicked
			DefaultMutableTreeNode node =
		    	(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();

			if (node == null || node.isRoot()){
				DataObject newNode = new DataObject(++treePanel.catigoryIdCounter,"NewNode" + newNodeSuffix++,2);
				treePanel.addObject(newNode);
			}
			else if ( ((DataObject)node.getUserObject()).getType()==((DataObject)node.getUserObject()).CATEGORY){
				DataObject newNode = new DataObject(++treePanel.groupIdCounter,"NewNode" + newNodeSuffix++,2);
				
				treePanel.addObject(newNode);
			}
			else if ( ((DataObject)node.getUserObject()).getType() == ((DataObject)node.getUserObject()).GROUP){
				DataObject newNode = new DataObject(++treePanel.attributIdCounter,"NewNode" + newNodeSuffix++,2);
				// next few linew can be moved to DataObject itself
				newNode.myCheckBox.setEnabled(true);
				newNode.myCheckBox.setSelected(false);
				newNode.myCheckBox.setBackground(dynamicMorphColors[3]);
				treePanel.addObject(newNode);
			}
			else if ( ((DataObject)node.getUserObject()).getType() == ((DataObject)node.getUserObject()).ATTRIBUTE){
				System.out.println("error can't add more than 3 levels");
			}
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
		    	saveAttributes.setVisible(true);
		    	isAttributeSaved = false;
			}
		}
		else if (e.getSource() == saveAttributes){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			treePanel.tree.getLastSelectedPathComponent();
			boolean statusOfSaveDataToTree = saveInputsToNode(node);
			jifNewMorphMatrixFrame.repaint();
		}
		else if (e.getSource() == createCompatMatrix){
//		check to see if the data is saved
		    if (!isAttributeSaved){
			    int answer =JOptionPane.showConfirmDialog(jifNewMorphMatrixFrame,"Do you want to save the changes?");
			    if (answer == JOptionPane.YES_OPTION) {
			        // User clicked YES.
					boolean statusOfSaveDataToTree = saveInputsToNode(previousNode);
					jifNewMorphMatrixFrame.repaint();
			    } else if (answer == JOptionPane.NO_OPTION) {
			        // User clicked NO.
			    }
			    isAttributeSaved=true;
			    saveAttributes.setVisible(false);
			}
		    initializeCompatibility(treePanel.getNewOPTIONNodes(),treePanel.processedOptionNodes);
		    treePanel.resetNewOPIONNodes();
		    jifNewMorphMatrixFrame.setVisible(false);
			makeCompatMatrixFrame();
		}
		else if (e.getSource() == addActionItem){
		    // check to see if the data is saved
		    if (!isAttributeSaved){
			    int answer =JOptionPane.showConfirmDialog(jifNewMorphMatrixFrame,"Do you want to save the changes?");
			    switch (answer){
			    case JOptionPane.YES_OPTION:
			    	boolean statusOfSaveDataToTree = saveInputsToNode(previousNode);
					jifNewMorphMatrixFrame.repaint();
				case JOptionPane.NO_OPTION:;
				case JOptionPane.CANCEL_OPTION:;
			    }
			    isAttributeSaved=true;
			    saveAttributes.setVisible(false);
			}
		    //
		    

		    // make a new frame to ask for inputs
		    //if (newFilterSuffix==2)// Remember the Min TRL filter automatically was added 
		    //{
		    	makeFilterFrame();
		    //	}   // therefore the first filter that the user will add has a suffix of 2
		    //else
		    //{ jifNewFilterFrame.setVisible(true);}
		   	    
		}
		else if (e.getSource() ==resetCompatibility){
		    DataObject nodeInfo;
		    for(int i=0; i<treePanel.processedOptionNodes.size();++i){
		        nodeInfo= (DataObject) treePanel.processedOptionNodes.get(i);
		        for (int j=0; j<nodeInfo.disallowVector.size();++j){
		         nodeInfo.allowVector.add(nodeInfo.disallowVector.get(j));
		         nodeInfo.disallowVector.clear();
		        }
		    }	        
		}
		else if (e.getSource() == backToMain){
		    //problem!!
			//latterAdded = new Vector();
			//backToMorphTree = true;
			// main Morph Matrix frame is visible
			jifMakeCompatMatrixFrame.setVisible(false);
			ptrSpLeft.add(treePanel,BorderLayout.CENTER);
		    jifNewMorphMatrixFrame.setVisible(true);
		}
		else if (e.getSource() == makeMorphMatrix){
		    	jifMakeCompatMatrixFrame.setVisible(false);	
		    	makeDynamicFaultMatrix();

		}
		else if (e.getSource() == backToCompatibility){
		    	jifDynamicFaultMatrixFrame.dispose();
		    	jifMakeCompatMatrixFrame.setVisible(true);
		    	reToMorphMatrix=true;
		}
	}
//	this frame is created when the user selects File-New
//	and will make a new Morph matrix
	public void newMorphMatrixFrame(boolean aNewFrame) {
		jifNewMorphMatrixFrame = new JInternalFrame(
				"Make Fault Tree",true,true,true,true);
		jifNewMorphMatrixFrame.setBounds(0,0,750,400);
		jifNewMorphMatrixFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);


		KeyListener akeyListener = new myKeyListener();

//		setting up of the tree
//		treePanel = new DynamicTree(rootName,textFont);
		treePanel.tree.addMouseListener(new TreeMouseListener());
        treePanel.setPreferredSize(new Dimension(400, 300));

//		panel where all of the descriptors are located on the Right Pane
//		This is using the ParagraphLayout Mgr site is listed below for more info
//		http://www.jhlabs.com/java/layout/index.html
		JPanel rightMainPanel = new JPanel(new BorderLayout());
		JPanel innerRightPanel = new JPanel(new ParagraphLayout());
		JPanel middleRightPanel= new JPanel(new FlowLayout());// later we can make this the information panel
		middleRightPanel.setBorder(new TitledBorder(new EtchedBorder()));
		//middleRightPanel.setSize(new Dimension(300,50));
		innerRightPanel.setBorder(new TitledBorder(new EtchedBorder()));
		
		// change the name for 

		
//		Name field
		JLabel nameLabel = new JLabel("Name: ");
		nameTextField = new JTextField(10);
		nameTextField.addKeyListener(akeyListener);
		

//		Trl Field
		// change all of trllable to Action Items
		JLabel trlLabel = new JLabel("Action Items: ");
		trlTextField = new JTextField(10);
		trlTextField.addKeyListener(akeyListener);
		
//		Description Field
		JLabel descriptionTextLabel = new JLabel("Description: ");
		descriptionTextField = new JTextArea(" ",4,20);
		descriptionTextField.setLineWrap(true);
		descriptionTextField.addKeyListener(akeyListener);
		
		//Reference 1
		
		//ref1Label=new JLabel("<HTML><A HREF=URL>Reference</A>: </HTML>");
		ref1TextField=new JTextField(15);// have to make it Global
		
		// Probability
		JLabel probLabel=new JLabel("Probability:");
		//Cost
		JLabel costLabel=new JLabel("Cost:");
		//difficulty
		JLabel difficultyLabel=new JLabel("Difficulty:");

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
		node_probability="low";

		ButtonGroup cost_group= new ButtonGroup();
		lCostRButton  =new JRadioButton("Low");
		mCostRButton  =new JRadioButton("Mid");
		hCostRButton =new JRadioButton("High");
		cost_group.add(lCostRButton);
		cost_group.add(mCostRButton);
		cost_group.add(hCostRButton);
		lCostRButton.setSelected(true);
		node_cost="low";
		
		ButtonGroup difficulty_group= new ButtonGroup();
		lDifficultyRButton  =new JRadioButton("Low");
		mDifficultyRButton  =new JRadioButton("Mid");
		hDifficultyRButton =new JRadioButton("High");
		difficulty_group.add(lDifficultyRButton);
		difficulty_group.add(mDifficultyRButton);
		difficulty_group.add(hDifficultyRButton);
		lDifficultyRButton.setSelected(true);
		node_difficulty="low";
		ActionListener RadioButtonListener=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (lProbabilityRButton.isSelected()) node_probability = "low";
				else if (mProbabilityRButton.isSelected()) node_probability = "mid";
				else node_probability = "high";
				
				if (lDifficultyRButton.isSelected()) node_difficulty = "low";
				else if (mDifficultyRButton.isSelected()) node_difficulty = "mid";
				else node_difficulty = "high";
				
				if (lCostRButton.isSelected()) node_cost = "low";
				else if (mCostRButton.isSelected()) node_cost = "mid";
				else node_cost = "high";
				
				isAttributeSaved=false;
				saveAttributes.setVisible(true);
			}
		};
		lProbabilityRButton.addActionListener(RadioButtonListener);
		mProbabilityRButton.addActionListener(RadioButtonListener);
		hProbabilityRButton.addActionListener(RadioButtonListener);
		lDifficultyRButton.addActionListener(RadioButtonListener);
		mDifficultyRButton.addActionListener(RadioButtonListener);
		hDifficultyRButton.addActionListener(RadioButtonListener);
		lCostRButton.addActionListener(RadioButtonListener);
		mCostRButton.addActionListener(RadioButtonListener);
		hCostRButton.addActionListener(RadioButtonListener);
	
//		this is where all the features are added
		innerRightPanel.add(nameLabel, ParagraphLayout.NEW_PARAGRAPH);
		innerRightPanel.add(nameTextField);

		innerRightPanel.add(trlLabel, ParagraphLayout.NEW_PARAGRAPH);
		innerRightPanel.add(trlTextField);

		innerRightPanel.add(descriptionTextLabel, ParagraphLayout.NEW_PARAGRAPH_TOP);
		JScrollPane scrollPane = new JScrollPane(descriptionTextField);
		innerRightPanel.add(scrollPane);
		
		innerRightPanel.add(probLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerRightPanel.add(lProbabilityRButton);
		innerRightPanel.add(mProbabilityRButton);
		innerRightPanel.add(hProbabilityRButton);
		
		innerRightPanel.add(difficultyLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerRightPanel.add(lDifficultyRButton);
		innerRightPanel.add(mDifficultyRButton);
		innerRightPanel.add(hDifficultyRButton);
		
		innerRightPanel.add(costLabel,ParagraphLayout.NEW_PARAGRAPH);
		innerRightPanel.add(lCostRButton);
		innerRightPanel.add(mCostRButton);
		innerRightPanel.add(hCostRButton);
		
		innerRightPanel.add(ref1Button,ParagraphLayout.NEW_PARAGRAPH);
	
		innerRightPanel.add(ref1TextField,ParagraphLayout.NEW_LINE);
		

		rightMainPanel.add(innerRightPanel,BorderLayout.NORTH);
		rightMainPanel.add(middleRightPanel,BorderLayout.CENTER);

// 		This where the right button bar is made
		JPanel rightButtonBar = new JPanel(new FlowLayout());
		//button to add  Descriptor
		addActionItem = new JButton("Add Action Item");
		addActionItem.setEnabled(false);
		addActionItem.addActionListener(this);

		// Button to save the Attributes
		saveAttributes = new JButton("Save Attributes");
		saveAttributes.addActionListener(this);
		saveAttributes.setVisible(false);
		middleRightPanel.add(saveAttributes);

		createCompatMatrix = new JButton("Manage Action Items ");
		createCompatMatrix.addActionListener(this);

		rightButtonBar.add(addActionItem);
		rightButtonBar.add(createCompatMatrix);

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
		sp.setDividerLocation(jifNewMorphMatrixFrame.getWidth()/2);
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
		jifNewMorphMatrixFrame.add(sp, BorderLayout.CENTER);
		main_desktop.add(jifNewMorphMatrixFrame);
		jifNewMorphMatrixFrame.setVisible(true);

	}
// this function creates the compatiblity matrix
	public void makeCompatMatrixFrame(){

	    jifMakeCompatMatrixFrame = new JInternalFrame(
	            treePanel.rootNode.toString()+": Make Compatibility Matrix",true,true,true,true);
		jifMakeCompatMatrixFrame.setBounds(0,0,750,500);
		jifMakeCompatMatrixFrame.setLayout(new BorderLayout());
		jifMakeCompatMatrixFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);

//		creation of the split panes
//		left pane creation
		JSplitPane spLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spLeft.setDividerSize(8);
		spLeft.setDividerLocation(150);
		spLeft.setContinuousLayout(true);
		spLeft.setLayout(new BorderLayout());

//		right pane creation
		JSplitPane spRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		spRight.setDividerSize(8);
		spRight.setDividerLocation(150);
		spRight.setContinuousLayout(true);
		spRight.setLayout(new BorderLayout());

		//JSplitPane spRightVert = new JSplitPane(JSplitPane.VERTICAL_SPLIT,spRight,spRight);
		JPanel bottomButtonBar = new JPanel(new FlowLayout());

		backToMain = new JButton("<< Back");
		backToMain.addActionListener(this);
		resetCompatibility=new JButton ("Reset");
		resetCompatibility.addActionListener(this);
		makeMorphMatrix = new JButton("Next >>");
		makeMorphMatrix.addActionListener(this);
		bottomButtonBar.add(backToMain);
		bottomButtonBar.add(resetCompatibility);
		bottomButtonBar.add(makeMorphMatrix);
		jifMakeCompatMatrixFrame.add(bottomButtonBar, BorderLayout.SOUTH);

//		main split pane creation
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,spLeft,spRight);
		sp.setDividerSize(8);
		sp.setDividerLocation(jifNewMorphMatrixFrame.getWidth()/2);
		sp.setResizeWeight(0.5);
		sp.setContinuousLayout(true);
		sp.setOneTouchExpandable(true);

//		this is where the lists are created and where they are added to
//		the listener and the scroll pane
		myDropTargetListener DnD = new myDropTargetListener();
		allowList = new JList();
		disallowList = new JList();
		allowList.setDragEnabled(true);
		allowList.setDropTarget(new DropTarget(this,DnD));
		//disallowList.
		disallowList.setDragEnabled(true);
		disallowList.setDropTarget(new DropTarget(this,DnD));
		JScrollPane topRightSP= new JScrollPane(allowList);
		topRightSP.setPreferredSize(new Dimension(300,350));// make this dynamic
		JScrollPane bottomRightSP= new JScrollPane(disallowList);
		bottomRightSP.setPreferredSize(new Dimension(300,350));// make this size dynamic
		

//		this is the top right panel which has the compatible jlist object in it
		JPanel topRightPanel = new JPanel(new ParagraphLayout());
		topRightPanel.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED),"COMPATIBLE"));
		topRightPanel.add(topRightSP,ParagraphLayout.NEW_PARAGRAPH);

//		this is the bottom right panel which has the incompatible jlist object in it
		JPanel bottomRightPanel = new JPanel(new ParagraphLayout());
		bottomRightPanel.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED),"INCOMPATIBLE"));
		bottomRightPanel.add(bottomRightSP,ParagraphLayout.NEW_PARAGRAPH);

//		adding components to the right split panel
		spRight.add(topRightPanel,BorderLayout.NORTH);
		spRight.add(bottomRightPanel,BorderLayout.CENTER);


//		adding the treePanel and Button Bar to the left Panel
		spLeft.add(treePanel,BorderLayout.CENTER);

		jifMakeCompatMatrixFrame.add(sp, BorderLayout.CENTER);
		main_desktop.add(jifMakeCompatMatrixFrame);
		jifMakeCompatMatrixFrame.show();
	}
// this functions makes the dynamic fault matrix frame

	public void makeDynamicFaultMatrix(){

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
        leftPanel.setBackground(dynamicMorphColors[0]);     

        spLeft.add(leftScrollPane, BorderLayout.CENTER);

		DefaultMutableTreeNode  rootNode = new DefaultMutableTreeNode(treePanel.rootNode);
		
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		treeModel = treePanel.treeModel;
		
		
		int numDummyLabel =0;

//		DefaultMutableTreeNode nodeT = (DefaultMutableTreeNode) treeModel.getRoot();
//		DataObject nodeInfo=(DataObject) nodeT.getUserObject();
		for(int i=0; i<treePanel.processedOptionNodes.size();++i){
            DataObject nodeInfo= (DataObject)treePanel.processedOptionNodes.get(i);
            System.out.println(nodeInfo.getName());
		}
		printAllNodes(treeModel,treeModel.getRoot(),numDummyLabel,leftPanel);
		
		


      
//      categoriesCounter = 0;
//      groupCounter =0;
//      attributCounter = 0;   
//
//		categoriesCounter = treeModel.getChildCount(treeModel.getRoot());
//	    JButton[] categoryButton = new JButton[categoriesCounter];
//	    cbItemListener myCB = new cbItemListener();  
//
//	    for (int i=0;i< categoriesCounter;++i){
//            DefaultMutableTreeNode nodeT = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
//            DataObject nodeInfo=(DataObject) nodeT.getUserObject();
//            categoryButton[i] = new JButton (nodeInfo.toString());
//            categoryButton[i].setEnabled(true);
//            categoryButton[i].addMouseListener(myCB);  
//            categoryButton[i].setBackground(dynamicMorphColors[1]);
//            categoryButton[i].setPreferredSize(buttonDim);
//            leftPanel.add(categoryButton[i], ParagraphLayout.NEW_PARAGRAPH);
//			groupCounter = treeModel.getChildCount(treeModel.getChild(treeModel.getRoot(),i));
//	        JButton[] groupButton = new JButton[groupCounter];
//	        int total=groupCounter;
//
//			for(int j =0; j < groupCounter; j++){
//
//		        nodeT = (DefaultMutableTreeNode) treeModel.getChild((treeModel.getChild(treeModel.getRoot(),i)),j);
//		        nodeInfo= (DataObject) nodeT.getUserObject();
//		        groupButton[j] = new JButton (nodeInfo.toString());
//		        groupButton[j].setBackground(dynamicMorphColors[2]);
//		        groupButton[j].setPreferredSize(buttonDim);
//		        groupButton[j].addMouseListener(myCB); 
//		        leftPanel.add(groupButton[j]);
//		        total--;
//		   	  	attributCounter =  treeModel.getChildCount(treeModel.getChild((
//									treeModel.getChild(treeModel.getRoot(),i)),j));
//				for(int k = 0; k< attributCounter;k++){
//
//					nodeT = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getChild(
//                    treeModel.getChild(treeModel.getRoot(),i),j),k);
//					nodeInfo= (DataObject)nodeT.getUserObject();
//
//					nodeInfo.myCheckBox.setPreferredSize(buttonDim);
//					nodeInfo.myCheckBox.addItemListener(myCB);  
//					nodeInfo.myCheckBox.addMouseListener(myCB); 
//					leftPanel.add(nodeInfo.myCheckBox);
//					// by not restting the color  of the JCheckBox we
//					// can retain the status of Dyamic Morph Matrix
//  				}
//				if (total>=1){
//					JButton sameCategory = new JButton("");
//					sameCategory.setEnabled(false);
//					sameCategory.setBackground(dynamicMorphColors[0]);
//					sameCategory.setPreferredSize(buttonDim);
//					sameCategory.setBorderPainted(false);
//					leftPanel.add(sameCategory, ParagraphLayout.NEW_PARAGRAPH);
//				}
//				else
//				break;
//			}
//	    }
	    rightBorder = new JPanel(new BorderLayout());
//	    possibleCombs = new JPanel(new BorderLayout());
//		possibleCombs.setBackground(Color.yellow);
//		possibleCombs.setBorder(new TitledBorder(new SoftBevelBorder(5),"# of Technologies"));
//		possibelCombsLabel = new JLabel();
//		updateComboFrame();
//		possibleCombs.add(possibelCombsLabel,BorderLayout.SOUTH);
//		possibleCombs.add(new JLabel("# of Possible Combinations"),BorderLayout.CENTER);
//		String nOfTech="<HTML><FONT SIZE=4>"+treePanel.processedOptionNodes.size()+"</FONT></HTML>";
//		possibleCombs.add(new JLabel(nOfTech),BorderLayout.NORTH);
//		
//		rightBorder.add(possibleCombs,BorderLayout.NORTH);
//		
//		updateSlideBarsFrame();
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
		updateDynamicMorphFrame(); // this line should be removed latter
								   // For sure
		jifDynamicFaultMatrixFrame.setVisible(true);
		
		// add RadioButtons to select low mid high probability
		// and other stuff such as difficulty and cost
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
		menuFilter = new JMenu("Action Items");
		menuBar.add(menuFilter);
		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuHelp);

//		where the file submenu's are created
		menuItemNew = new JMenuItem("New");
		menuItemNew.addActionListener(this);
		menuFile.add(menuItemNew);
		menuItemOpen = new JMenuItem("Open");
		menuFile.add(menuItemOpen);
		menuItemOpen.addActionListener(this);
		menuItemClose = new JMenuItem("Close");
		menuFile.add(menuItemClose);
		menuItemClose.addActionListener(this);
		menuItemEdit = new JMenuItem("Edit");
		menuFile.add(menuItemEdit);
		menuItemEdit.addActionListener(this);
		menuItemSave = new JMenuItem("Save");
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(this);
		
		menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_X);
		menuItemExit.setToolTipText("Exit OpenTool");
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(this);

//		where the views submenu's are created
		menuViewCove = new JMenuItem("Cove");
		menuViewCove.addActionListener(this);

		menuViewDesktop = new JMenuItem("Desktop");
		menuViewDesktop.addActionListener(this);

		menuView.add(menuViewDesktop);
		menuView.add(menuViewCove);

//		where the tools submenu's are created
		menuToolsTopsis = new JMenuItem("TOPSIS");
		menuToolsTopsis.addActionListener(this);
		menuTools.add(menuToolsTopsis);

//      where the DEFULT submenu items for Filter are created
		Filter trlFilter=new Filter(newFilterSuffix++,"Min TRL",1,9);
		
		trlFilter.mySlider.setMajorTickSpacing(1);
		sliderChangeListener mySliderListener=new sliderChangeListener();
		trlFilter.mySlider.addChangeListener(mySliderListener);
		globalVectorOfFilters.insertElementAt(trlFilter,0);
		
		// adding the a filter to the Filter Menu as a checkBox
		menuTrlFilter =new JCheckBoxMenuItem(trlFilter.getName());
		menuTrlFilter.setSelected(true);
		menuTrlFilter.addActionListener(checkBoxListener);
		vCheckBoxFilters= new Vector();
		vCheckBoxFilters.add(menuTrlFilter);
		menuFilter.add(menuTrlFilter);
		
//		where the Help submenu's are created
		menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
		menuHelpAbout.addActionListener(this);
	}
	//this function makes the data vectors that allows the names to placed
	//in the the spreadsheet table.  it is returning a boolean to notify
	//the user if there is a category that does not have any groups associated
	//with it.
// either makeDataVectors or initialize
	public void updateSlideBarsFrame(){

	    //	  Sliders are created to work as a filter
		JPanel filterPane= new JPanel(new ParagraphLayout());
		JScrollPane filterScrollPane =new JScrollPane(filterPane);
	    filterScrollPane.setPreferredSize(new Dimension(100,465));
	    
	    
	    for (int i=0; i<vCheckBoxFilters.size();++i){
			if(((JCheckBoxMenuItem)vCheckBoxFilters.get(i)).isSelected()){
				filterPane.add(((Filter)globalVectorOfFilters.get(i)).mySlider,ParagraphLayout.NEW_PARAGRAPH);
			}
		}
	    rightBorder.add(filterScrollPane,BorderLayout.CENTER);
	    rightBorder.repaint();
	    jifDynamicFaultMatrixFrame.repaint();
	}
	public void initializeCompatibility(Vector sentAttributes,Vector oldAttributes){
		for(int i=0;i< sentAttributes.size();++i){
	    	DataObject currentOption = (DataObject)sentAttributes.get(i);
			for(int j=i+1; j< sentAttributes.size();++j){
				DataObject nodeInfo = (DataObject) sentAttributes.get(j);
				if (nodeInfo.getParentId()==currentOption.getParentId()){
					nodeInfo.adddisallowVector(currentOption);
					currentOption.adddisallowVector(nodeInfo);
				}else{
					nodeInfo.addAllowVector(currentOption);
					currentOption.addAllowVector(nodeInfo);
				}
			}
	    }
		if (oldAttributes !=null && !oldAttributes.isEmpty()){
			for(int i=0;i< sentAttributes.size();++i){
				DataObject currentOption = (DataObject)sentAttributes.get(i);
				for(int j=0; j< oldAttributes.size();++j){
					DataObject nodeInfo = (DataObject)oldAttributes.get(j);
					if (nodeInfo.getParentId()==currentOption.getParentId()){
						nodeInfo.adddisallowVector(currentOption);
						currentOption.adddisallowVector(nodeInfo);
					}else{
						nodeInfo.addAllowVector(currentOption);
						currentOption.addAllowVector(nodeInfo);
					}
				}
		    }
		}
	}
	public boolean saveInputsToNode(DefaultMutableTreeNode sNode){
		String name = new String(((DataObject)sNode.getUserObject()).getName());
		String newName=nameTextField.getText();
		jifNewMorphMatrixFrame.repaint();
		isAttributeSaved=true;
		saveAttributes.setVisible(false);
	    if (sNode == null || sNode.isRoot()) return false;

		DataObject nodeInfo = (DataObject)sNode.getUserObject();
		String newtrl = trlTextField.getText();
		String newDesc = descriptionTextField.getText();
		String ref1 = ref1TextField.getText();
		
		
		int INTtrl;
		try{
		    INTtrl=Integer.parseInt(newtrl);
		    if (INTtrl > 10 || INTtrl < 0) 
		        JOptionPane.showMessageDialog(jifNewMorphMatrixFrame,
	            " Please Enter A Numeric Value Between 0 and 10");
		    else {
		    	if (search(newName, ((DataObject)sNode.getUserObject()).getId())){
					nodeInfo.setTRL_Numver(INTtrl);
					nodeInfo.setdesctiptionText(newDesc);
					nodeInfo.setName(newName);
					nodeInfo.setReferenceURL(ref1);
					nodeInfo.setProbability(node_probability);
					nodeInfo.setDifficulty(node_difficulty);
					nodeInfo.setCost(node_cost);

				}
				else {
					JOptionPane.showMessageDialog(null,"You have entered a Duplicate Node Name","Error!!"
							,JOptionPane.INFORMATION_MESSAGE);
		    	}
		    }
		}
		catch (NumberFormatException nfex){
		    JOptionPane.showMessageDialog(jifNewMorphMatrixFrame,
		            " Please Enter A Numeric Value For TRL.");
		}
		return true;
	}
//	public BigInteger calculatedPossibilities(){

//	}
	public static void main(String[] args) {
		new MorphMatrix();
	}
	public boolean search(String name, int sentID){
		boolean rValue = true;
		DefaultMutableTreeNode  rootNode = new DefaultMutableTreeNode(treePanel.rootNode);
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		treeModel = treePanel.treeModel;

		int categoriesCounter = 0;
		int groupCounter =0;
		int attributCounter = 0;

		categoriesCounter = treeModel.getChildCount(treeModel.getRoot());
	    String tempName;
	    for (int i=0;i< categoriesCounter;++i){
	    	DefaultMutableTreeNode nodeT = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
			groupCounter = treeModel.getChildCount(treeModel.getChild(treeModel.getRoot(),i));
			tempName = ((DataObject)nodeT.getUserObject()).getName();
			if (tempName.equals(name) && sentID != ((DataObject)nodeT.getUserObject()).getId() ){
				rValue = false;
				break;
			}
			for(int j =0; j < groupCounter; j++){
				nodeT = (DefaultMutableTreeNode) treeModel.getChild((treeModel.getChild(treeModel.getRoot(),i)),j);
				tempName = ((DataObject)nodeT.getUserObject()).getName();
				if (tempName.equals(name) && sentID != ((DataObject)nodeT.getUserObject()).getId() ){
					rValue = false;
					break;
				}
		   	  	attributCounter =  treeModel.getChildCount(treeModel.getChild((
									treeModel.getChild(treeModel.getRoot(),i)),j));
				for(int k = 0; k< attributCounter;k++){
					nodeT = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getChild(
		                    treeModel.getChild(treeModel.getRoot(),i),j),k);
					tempName = ((DataObject)nodeT.getUserObject()).getName();
					if (tempName.equals(name) && sentID != ((DataObject)nodeT.getUserObject()).getId() ){
						rValue = false;
						break;
					}
  				}

			}
	    }
		return rValue;
	}
    public DataObject search(String name){
        DefaultTreeModel treeModel = treePanel.treeModel;
        int categoriesCounter = 0;
        int groupCounter =0;
        int attributCounter = 0;
        DataObject nodeInfo;
        categoriesCounter=treeModel.getChildCount(treeModel.getRoot());  
        for(int i=0; i< categoriesCounter;++i){
            DefaultMutableTreeNode nodeCategoryTemp = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(),i);
            nodeInfo=(DataObject)nodeCategoryTemp.getUserObject();
            if(nodeInfo.getName().equals(name)){
                return nodeInfo;
            }
            groupCounter = treeModel.getChildCount(nodeCategoryTemp);
            for(int j=0; j<groupCounter;++j){
                DefaultMutableTreeNode nodeGroupTemp=(DefaultMutableTreeNode) treeModel.getChild(nodeCategoryTemp,j);
                nodeInfo=(DataObject)nodeGroupTemp.getUserObject();
                if(nodeInfo.getName().equals(name)){
                    return nodeInfo;
                }
                attributCounter= treeModel.getChildCount(nodeGroupTemp);
                for(int k=0;k<attributCounter;++k){
                    DefaultMutableTreeNode nodeAttribut=(DefaultMutableTreeNode) treeModel.getChild(nodeGroupTemp,k);
                    nodeInfo=(DataObject) nodeAttribut.getUserObject();
                    if(nodeInfo.getName().equals(name)){
                        return nodeInfo;
                    }
                }
            }
        }   
        return null;
    }
	public void applyFilter(int filterValue,String filterName,int filterNumber){
		Vector vOfFilteredNodes= new Vector();
		if(filterName.equals("Min TRL")){
            for(int i=0; i<treePanel.processedOptionNodes.size();++i){
                DataObject nodeInfo= (DataObject)treePanel.processedOptionNodes.get(i);
                if(nodeInfo.getTRL_Number()< filterValue){
                    vOfFilteredNodes.add(nodeInfo);
                }
            }
        }else{
        	  for(int i=0; i<treePanel.processedOptionNodes.size();++i){
                DataObject nodeInfo= (DataObject)treePanel.processedOptionNodes.get(i);
                //checking to see if the nodes has this filter
                for(int j=0;j<nodeInfo.vDescriptors.size();++j){
                	Descriptor aDescriptor=(Descriptor)nodeInfo.vDescriptors.get(j);
                	if (aDescriptor.getName().equals(filterName)){
                		// now evaluate the filter
                		if(aDescriptor.getValue()>filterValue){
                			vOfFilteredNodes.add(nodeInfo);
                		}
                	}
                }
            }
        }
		// since we will be adding and re evalute some of these Filter
		// we need to use add or set fucntion
        if (vFiltersVector !=null){
            if(filterNumber> vFiltersVector.size()){
                vFiltersVector.add(vOfFilteredNodes);
            }else{
                vFiltersVector.set(filterNumber-1,vOfFilteredNodes);   
            }
        }
        
        updateDynamicMorphFrame();
       // updateComboFrame();
    }
    public void updateDynamicMorphFrame(){
   	  	for( int i=0;i<treePanel.processedOptionNodes.size();++i){
	  		((DataObject)treePanel.processedOptionNodes.get(i)).myCheckBox.setBackground(dynamicMorphColors[3]);
	  		((DataObject)treePanel.processedOptionNodes.get(i)).myCheckBox.setEnabled(true);
	  	}
	  	for( int i=0;i<treePanel.processedOptionNodes.size();++i){
	  		if (((DataObject)treePanel.processedOptionNodes.get(i)).myCheckBox.isSelected()){
	  			DataObject nodeInfo=(DataObject)treePanel.processedOptionNodes.get(i);
	  			for(int j=0;j<nodeInfo.disallowVector.size();++j){
	  				((DataObject)nodeInfo.disallowVector.get(j)).myCheckBox.setBackground(dynamicMorphColors[5]);
    	  			((DataObject)nodeInfo.disallowVector.get(j)).myCheckBox.setEnabled(false);
    	  		}
			}
	  	}
        
	  	if(!vFiltersVector.isEmpty()){
	  	    for (int j=0; j<vFiltersVector.size();++j){
	  		  	Vector HoldList= (Vector)vFiltersVector.elementAt(j);    	//DefaultMutableTreeNode  nodeT = new DefaultMutableTreeNode(node);
	  	    	
	  	    	//DataObject nodeInfo=(DataObject) nodeT.getUserObject();
	  	    	//System.out.println(nodeInfo.getName());
	  		  	if(HoldList!= null){
	  		        for(int i=0; i<HoldList.size();++i){    	//DefaultMutableTreeNode  nodeT = new DefaultMutableTreeNode(node);
	  		      	
	  		      	//DataObject nodeInfo=(DataObject) nodeT.getUserObject();
	  		      	//System.out.println(nodeInfo.getName());
	  		            DataObject nodeInfo=(DataObject)HoldList.get(i);
	  		            if(nodeInfo.myCheckBox.isSelected()){// later if we be able to make a fram that containes the list of these nodes and alowing the user to select which node to pass the filter
	  		                int answer = JOptionPane.showConfirmDialog(jifDynamicFaultMatrixFrame,nodeInfo.getName()+
	  		                        ", selected by the user does not pass through the TRL filter.\n" +
	  		                		"Do you wan to by pass the filter? ");
	  					    if (answer == JOptionPane.YES_OPTION) {
	  					    		HoldList.remove(nodeInfo);
	  					    		continue;
	  					    } else if (answer == JOptionPane.NO_OPTION) {
	  					        	nodeInfo.myCheckBox.setSelected(false);
	  					        	updateDynamicMorphFrame();
	  					        	break;
	  					    } else if (answer == JOptionPane.CANCEL_OPTION){
	  					        
	  					    }
	  		            }
	  		            nodeInfo.myCheckBox.setEnabled(false);
	  		            nodeInfo.myCheckBox.setBackground(dynamicMorphColors[6]);
	  		        }// end of iner for loop    
	  	        }
	  	    }// end of for loop
	  	}

    }
//    public void updateComboFrame(){
//    }
    private void printAllNodes(DefaultTreeModel model, Object node, int indent, JPanel leftPanel){
    	JButton indentionSpace = new JButton("");
		indentionSpace.setEnabled(false);
		indentionSpace.setBackground(dynamicMorphColors[0]);
		indentionSpace.setBorderPainted(false);
		leftPanel.add(indentionSpace);
    	leftPanel.add(indentionSpace,ParagraphLayout.NEW_PARAGRAPH);
    	for(int i=0; i<indent;++i){
    		
    		indentionSpace = new JButton("");
    		indentionSpace.setEnabled(false);
    		indentionSpace.setBackground(dynamicMorphColors[0]);
    		indentionSpace.setPreferredSize(buttonDim);
    		indentionSpace.setBorderPainted(false);
			leftPanel.add(indentionSpace);  
    	}
    	
    	//DefaultMutableTreeNode  nodeT = new DefaultMutableTreeNode(node);
    	//DataObject nodeInfo=(DataObject) nodeT.getUserObject();
    	//System.out.println(nodeInfo.getName());
    	
    	leftPanel.add(new JButton (node.toString()));
//    	System.out.println(space + node.toString());
    	for (int i=0; i<model.getChildCount(node);i++){
    		printAllNodes(model, model.getChild(node, i),indent + 1,leftPanel);
    	}
    }
    public void makeFilterFrame(){
        jifNewFilterFrame = new JInternalFrame("Action Item"
	           ,false,true,false,false);
        
        jifNewFilterFrame.setBounds(200,100,450,320);/** Dimension **/
		jifNewFilterFrame.setLayout(new BorderLayout());
		jifNewFilterFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
		jifNewFilterFrame.setBackground(Color.white);
		JTabbedPane m_tab=new JTabbedPane();
		JPanel tab1=new JPanel(new BorderLayout()); //First Tab [Add]
		JPanel tab2=new JPanel(new BorderLayout()); //Second Tab [Edit]
		final JButton cancelFilter= new JButton("Cancel");
		final JButton okayFilter= new JButton("OK");

		ActionListener lst=new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        if(e.getSource()==okayFilter){}

		        jifNewFilterFrame.dispose();
		    	}
		    };
		    
		okayFilter.addActionListener(lst);
		cancelFilter.addActionListener(lst);
		JPanel filterButtonBar= new JPanel(new FlowLayout());
		filterButtonBar.add(okayFilter);
		filterButtonBar.add(cancelFilter);
		
		
		m_tab.addTab("Add",tab1);
		m_tab.addTab("Edit",tab2);
		JPanel p=new JPanel(new BorderLayout());
		p.add(m_tab,BorderLayout.CENTER);
		p.add(filterButtonBar,BorderLayout.SOUTH);
		p.setBorder(new EmptyBorder(5,5,5,5));
		jifNewFilterFrame.add(p);

		main_desktop.add(jifNewFilterFrame);
		jifNewFilterFrame.setVisible(true);
    }
    class  cbItemListener extends MouseMotionAdapter implements ItemListener, MouseInputListener {
    	  public void itemStateChanged(ItemEvent ie) {
 
    		updateDynamicMorphFrame();
    		//updateComboFrame();
    	  }

    	  public void mouseEntered(MouseEvent evt){
    	  	JCheckBox temp = new JCheckBox();
    	  	JButton temp2 = new JButton();
    	  	if (evt.getSource().getClass() == temp.getClass()){
    	  		DataObject nodeInfo=search(((JCheckBox)evt.getSource()).getText());
    	  	    if(nodeInfo!=null){
    	  	      infoTextField.setText("Name: "+" "+nodeInfo.getName()+'\n'+
    	  	              "TRL: "+" "+nodeInfo.getTRL_Number()+'\n'+
	  					  "Description:\n"+" "+nodeInfo.getdesctiptionText());   
    	  	    }
    	  	}
    	  	else if (evt.getSource().getClass() ==  temp2.getClass()){
    	  	    DataObject nodeInfo=search(((JButton)evt.getSource()).getText());
    	  	    if(nodeInfo!=null){
    	  	        infoTextField.setText("Name:\n"+" "+nodeInfo.getName()+'\n'+
  	  	              "TRL:\n"+" "+nodeInfo.getTRL_Number()+'\n'+
	  					  "Description:\n"+" "+nodeInfo.getdesctiptionText());   
  	  	    	}
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
			if (jifNewMorphMatrixFrame.isSelected()){
			    if (!isAttributeSaved){
				    int answer = JOptionPane.showConfirmDialog(jifNewMorphMatrixFrame,"Do you want to save the changes?");
				    if (answer == JOptionPane.YES_OPTION) {
				    	boolean statusOfSaveDataToTree = saveInputsToNode(previousNode);
				    	// User clicked YES.
				    } else if (answer == JOptionPane.NO_OPTION) {
				        // User clicked NO.
				    }
				    isAttributeSaved=true;
				    saveAttributes.setVisible(false);
				}

			    DefaultMutableTreeNode node =
			    	(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
// TO DO delete and make sure you save the discription
				if (node==null ||node.isRoot()) {
				 	nameTextField.setVisible(false);
				    trlTextField.setVisible(false);

				    descriptionTextField.setVisible(false);
				    return;
				}

			    nameTextField.setVisible(true);
		        trlTextField.setVisible(true);
		    	descriptionTextField.setVisible(true);

				DataObject nodeInfo = (DataObject)node.getUserObject();

				if(nodeInfo.getType().equals(nodeInfo.ATTRIBUTE)){
					addActionItem.setEnabled(true);
				}else{
					addActionItem.setEnabled(false);
				}
				nameTextField.setText(nodeInfo.getName());
				// this is where the tree attributes gets updated
				String trl = new String();
				trl = ""+nodeInfo.getTRL_Number();
				trlTextField.setText(trl);
				descriptionTextField.setText(nodeInfo.getdesctiptionText());
				ref1TextField.setText(nodeInfo.getReferenceURL());
				// setting the JRadioButton for Probability
				if (nodeInfo.getProbability().contains("low")) lProbabilityRButton.setSelected(true);
				else if(nodeInfo.getProbability().contains("mid")) mProbabilityRButton.setSelected(true);
				else if (nodeInfo.getProbability().contains("high")) hProbabilityRButton.setSelected(true);
				else System.out.println("Probability is not low, mid, or high: " +nodeInfo.getProbability());
				// setting the JRadioButton for Difficulty
				if (nodeInfo.getDifficulty().contains("low")) lDifficultyRButton.setSelected(true);
				else if(nodeInfo.getDifficulty().contains("mid")) mDifficultyRButton.setSelected(true);
				else if (nodeInfo.getDifficulty().contains("high")) hDifficultyRButton.setSelected(true);
				else System.out.println("Difficulty is not low, mid, or high: " +nodeInfo.getDifficulty());
				// setting the JRadioButton for Cost
				if (nodeInfo.getCost().contains("low")) lCostRButton.setSelected(true);
				else if(nodeInfo.getCost().contains("mid")) mCostRButton.setSelected(true);
				else if (nodeInfo.getCost().contains("high")) hCostRButton.setSelected(true);
				else System.out.println("Cost is not low mid, or high: " +nodeInfo.getCost());

				//save the pointer to node for later
				previousNode=node;
			}
			else if (jifMakeCompatMatrixFrame.isSelected()){
				DefaultMutableTreeNode node =
					(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
				if (node==null ||node.isRoot()) {
				    return;
				}
				DataObject nodeInfo = (DataObject)node.getUserObject();
				// the code only allows for attributes to be 
				// compatibile or incompatible
				if(nodeInfo.getType().equals(nodeInfo.ATTRIBUTE)){
				    allowList.setVisible(true);
				    disallowList.setVisible(true);
				    Vector data = nodeInfo.allowVector;
				    allowList.setListData(nodeInfo.allowVector);
				    disallowList.setBackground(Color.white);
				    disallowList.setListData(nodeInfo.disallowVector);
				}else{
				    allowList.setVisible(false);
				    disallowList.setVisible(false);
				    
				}
			}

		}
	}
	class myKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {

		}
		public void keyReleased(KeyEvent e) {

		}
		public void keyTyped(KeyEvent e) {
		    char ent=e.getKeyChar();
		    if(ent=='\n'){
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				treePanel.tree.getLastSelectedPathComponent();
				boolean statusOfSaveDataToTree = saveInputsToNode(node);
				jifNewMorphMatrixFrame.repaint();
		        }
		    else if (jifNewMorphMatrixFrame.isSelected()) {
				isAttributeSaved = false;
				saveAttributes.setVisible(true);
				jifNewMorphMatrixFrame.repaint();
			}
		}
	}
	class myDropTargetListener implements DropTargetListener {

		public void dragEnter(DropTargetDragEvent dtde) {
			System.out.println("dragEnter");
		}
		public void dragOver(DropTargetDragEvent dtde) {

		}
		public void dropActionChanged(DropTargetDragEvent dtde) {
			System.out.println("dropActionChanged");
		}

		public void dragExit(DropTargetEvent dte) {
			System.out.println("dragExit");
		}

		public void drop(DropTargetDropEvent dtde) {

			DropTargetContext data = dtde.getDropTargetContext();
			DefaultMutableTreeNode node =
		    	(DefaultMutableTreeNode)treePanel.tree.getLastSelectedPathComponent();
			DataObject nodeInUse = (DataObject)node.getUserObject();
			//and then we need to find the group it is associtated with at take it from that
			//-fix the errors.
			if (node != null){
			    //data.
				if (data.getComponent() == disallowList && data.getComponent() != allowList){
				    DataObject value = (DataObject)allowList.getSelectedValue();
					System.out.println("Allow To Disallow " + value.m_Name);
					if(value.myCheckBox.isSelected()&&nodeInUse.myCheckBox.isSelected()){// rewite this message 
				        JOptionPane.showInternalMessageDialog(jifMakeCompatMatrixFrame,"Both Technology are selcted.\n" +
				        		"please make corrections by deselcting one of them","Compatibility Error",JOptionPane.INFORMATION_MESSAGE);

					}else{
						nodeInUse.disallowVector.add(value);
						value.disallowVector.add(nodeInUse);
						disallowList.setListData(nodeInUse.disallowVector);
	
	
						nodeInUse.allowVector.removeElementAt(allowList.getSelectedIndex());
						value.allowVector.remove(nodeInUse);
						allowList.setListData(nodeInUse.allowVector);
					}
				}
				else{
					DataObject value = (DataObject)disallowList.getSelectedValue();
					//System.out.println("DisAllow To allow");

					nodeInUse.allowVector.add(value);
					value.allowVector.add(nodeInUse);
					allowList.setListData(nodeInUse.allowVector);


					nodeInUse.disallowVector.removeElementAt(disallowList.getSelectedIndex());
					value.disallowVector.remove(nodeInUse);
					disallowList.setListData(nodeInUse.disallowVector);
				}
			}
			updateDynamicMorphFrame();
    		//updateComboFrame();
		}

	}
	class sliderChangeListener implements ChangeListener{
	    public void stateChanged(ChangeEvent e){
	        JSlider source = (JSlider)e.getSource();	        
	        if (!source.getValueIsAdjusting()) {
	            infoTextField.setText("Filter Name:  '"+source.getName()+"' \n"+"Current Value: '"+source.getValue()+"'");
	        	String nameOfSlider=source.getName();	
	            Filter aFilter;
	            int	   fN=-1;
	        	for (int i=0;i<globalVectorOfFilters.size();++i){	
	        		aFilter=(Filter)globalVectorOfFilters.get(i);
	        		if(aFilter.getName().equals(nameOfSlider)){
	        			fN=aFilter.FilterNumber;
	        			break;
	        		}
	        	}
	            int minTrl = (int)source.getValue();
	            if(fN<0){
	            	infoTextField.setText("Error: Filter could not be found!!");
	            }else{
	            	applyFilter(minTrl,nameOfSlider,fN);
	            }
	        } 
	    }
	}
//	class myActionListener  implements ActionListener{
//	    public void actionPerformed(ActionEvent evn){
//	       
//			
//	    }
//	}
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
