package org.spatch.edit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.spatch.lems.LEMSTree;
import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsComponentList;
import org.spatch.lems.LemsComponentType;
import org.spatch.lems.LemsModel;

public class ComponentSelectorEditor implements TreeSelectionListener, ActionListener, EditListener {

	
	LemsModel lemsModel;
	LemsComponentType lemsComponentType;
	LEMSTree componentTree;
	
	LemsComponentList parentList;

	LemsComponent active = null;
	LemsComponent potential = null;
	
	
	JPanel mainPanel = null;
	
	JPanel editContainer;
	JPanel viewContainer;
	
	
	JComboBox typeChoice = null;

	JLabel folderInfoLabel;
 	
	JButton applyButton;
	JButton editButton;
	JButton deleteButton;	
	 
	JPanel folderComponentCards;
	JPanel viewEditCards;
	
	boolean isFirst;
	
	
	
	
	
	public ComponentSelectorEditor(LemsComponentType lct, LemsModel m, boolean f) {
		lemsComponentType = lct;
		lemsModel = m;
		isFirst = f;
	}
	
	public JPanel getPanel() throws ContentError {
		if (mainPanel == null) {
			buildPanel();
		}
		return mainPanel;
	}
  
	 
	public void valueChanged(TreeSelectionEvent e) {
		  if (e.isAddedPath()) {
			  
			  Object obj = e.getNewLeadSelectionPath().getLastPathComponent();
			  if (obj instanceof LemsComponent) {
				  active = (LemsComponent)obj;
			  } else {
				  active = null;
				  if (obj instanceof LemsComponentList) {
					  parentList = (LemsComponentList)obj;
				  }
			  }
		  } else {
			  // presumably deleted
			  active = null;
		  }
		  itemSelected(active);
	}	
	

	public void showFolderPanel() {
		((CardLayout)(folderComponentCards.getLayout())).first(folderComponentCards);
	}
	
	public void showComponentPanel() {
		((CardLayout)(folderComponentCards.getLayout())).last(folderComponentCards); 
	}
	
	public void showEditPanel() {
		showComponentPanel();
		((CardLayout)(viewEditCards.getLayout())).last(viewEditCards);
	}
	
	public void showViewPanel() {
		showComponentPanel();
		((CardLayout)(viewEditCards.getLayout())).first(viewEditCards);
	}
	
		
 
	
	public String hwrap(String s) {
		return "<html>" + s + "</html>";
	}

	public String getGroupPropertyName() throws ContentError {
		String ret = null;
		// TODO generalize
		if (lemsComponentType.hasTextParameter("role")) {
			ret = "role";
		}
		return ret;
	}

	 
	public String getInitialText() {
		String txt = "<h2>" + lemsComponentType.getName() + "</h2>";
		txt += lemsComponentType.getAbout();
		return txt;
	}
	 
	 
	public String getTabLabel() {
		 return lemsComponentType.getName();
	}

    
	 
	public void buildPanel() throws ContentError {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.add(BorderLayout.CENTER, splitPane);
		
		
		ArrayList<Component> components = lemsModel.getComponents(lemsComponentType);
		
		componentTree = new LEMSTree(lemsComponentType.getName(), components);
		String gpn = getGroupPropertyName();
		if (gpn != null) {
			componentTree.groupBy(gpn);
		}

		JTree tree = new JTree(componentTree);		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane sp = new JScrollPane(tree);
		sp.setPreferredSize(new Dimension(200, 200));
		splitPane.add(sp);
		
		
		folderComponentCards = new JPanel(new CardLayout());
		splitPane.add(folderComponentCards);
		
		JPanel folderPanel = new JPanel();
		folderPanel.setLayout(new BorderLayout());
		JPanel creatorPanel = buildCreatorPanel();
		folderPanel.add(BorderLayout.NORTH, creatorPanel);
		
		folderInfoLabel = new JLabel(hwrap(getInitialText()));
	    folderInfoLabel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
	 	folderInfoLabel.setVerticalAlignment(SwingConstants.TOP);
		folderInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		folderInfoLabel.setPreferredSize(new Dimension(500, 200));
		folderPanel.add(BorderLayout.CENTER, topFlow(leftFlow(folderInfoLabel)));

		folderComponentCards.add(folderPanel, "Folder");
			
		
		viewEditCards = new JPanel(new CardLayout());
		folderComponentCards.add(new JScrollPane(viewEditCards), "Component");
 
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new BorderLayout(2, 2));
		
		editButton = new JButton("Edit");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);

		deleteButton = new JButton("Delete");
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		
		JPanel edbuts = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
		edbuts.add(editButton);
		edbuts.add(deleteButton);
		viewPanel.add(BorderLayout.NORTH, rightFlow(edbuts));
		
		viewEditCards.add(viewPanel, "Info");
		
		viewContainer = new JPanel(new BorderLayout());
		
		viewPanel.add(BorderLayout.CENTER, viewContainer);
		
		JPanel bpan = new JPanel();
		bpan.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 3));
	 		
		applyButton = new JButton("Select");
		bpan.add(applyButton);
		applyButton.setEnabled(false);
		
		JButton bback = new JButton("Back to main model");
		bpan.add(bback);
	
		if (isFirst) {
			// leave out apply and back
		} else {
			viewPanel.add(BorderLayout.SOUTH, bpan);
		}
		
		applyButton.setEnabled(false);
	
		tree.addTreeSelectionListener(this);
	 	
		
		editContainer = new JPanel();
		viewEditCards.add(editContainer, "Edit");
		editContainer.setLayout(new BorderLayout());
	
		LemsComponent lc = componentTree.getFirst();
		if (lc != null) {
			itemSelected(lc);
		}
	}
	
 
	public void itemSelected(LemsComponent lc) {
		active = lc;
		
		if (active == null) {
			  applyButton.setEnabled(false);
	 			  
		  } else {
			  applyButton.setEnabled(true);
 		}
		showActiveView();
	}

	
	
	
	public void editSaved() {
		
		if (potential != null) {
			// we're creating a new component, which isn't in the tree yet
			active = potential;
			try {
			lemsModel.addComponent(potential);
			E.info("Added new component " + potential.getID());
			} catch (Exception ex) {
				E.error("problem adding component: " + ex);
			}
			componentTree.addAndSelect(potential);
			itemSelected(potential);
			potential = null;
		}
		
		 showActiveView();
		 potential = null;
	}
	
	
	public void deleteActive() {
		lemsModel.removeComponent(active);
		componentTree.removeComponent(active);
		active = null;
		showActiveView();
	}
	
	
	
	public void editCancelled() {
		if (potential != null) {
			active = null;
			potential = null;
		}
		
		 showActiveView();
	}
	
	public void showActiveView() {
		try {
		if (active == null) {
			showFolderPanel();
			folderInfoLabel.setText(hwrap(getInitialText()));
			
		} else {
			viewContainer.removeAll();
			if (active.isDiagram()) {
				DiagramatticEditor de = new DiagramatticEditor(active, lemsModel.getLems());
				viewContainer.add(BorderLayout.CENTER, de.getPanel());
								 
			} else {
				JLabel infoLabel = new JLabel(hwrap(active.getInfo()));
			    infoLabel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
			 	infoLabel.setVerticalAlignment(SwingConstants.TOP);
				infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
				infoLabel.setPreferredSize(new Dimension(500, 200));
				viewContainer.add(BorderLayout.CENTER, infoLabel);
			}
			showViewPanel();
			 
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void editActive() {
		if (active != null) {
			editContainer.removeAll();
			
			LinearEditor ce = new LinearEditor(active, lemsModel.getLems());
			ce.setEditListener(this);
			
			editContainer.add(BorderLayout.CENTER, ce.getPanel());
			showEditPanel();
		}
	}
	 
	
	
	public JPanel buildCreatorPanel() throws ContentError {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
		
		ret.add(new JLabel("New " + lemsComponentType.getName()));
		
		String[] names = lemsModel.getSubtypeNames(lemsComponentType);
		
		if (names != null && names.length > 0) {
			JPanel ptyp = new JPanel();
	
			ptyp.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
			ptyp.add(BorderLayout.WEST, new JLabel("Type: "));
		    typeChoice = new JComboBox(names);
			ptyp.add(BorderLayout.CENTER, typeChoice);
		    
			ret.add(ptyp);
		}
	
		JButton createButton = new JButton("Create");
		ret.add(createButton);
		createButton.setActionCommand("create");
		createButton.addActionListener(this);
		return ret;
	}
	
	

	@SuppressWarnings("unused")
	private JPanel centerFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.CENTER);
	}
	
	private JPanel rightFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.RIGHT);
	}
	
	private JPanel leftFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.LEFT);
	}
	
	private JPanel topFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.LEADING);
	}
	
	private JPanel wrapFlow(JComponent p, int pos) {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout(pos, 0, 0));
		ret.add(p);
		return ret;
	}
	
	 
  

	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals("edit")) {
			editActive();
			
		
		} else if (cmd.equals("create")) {
			String tnm = null;
			if (typeChoice != null) {
				tnm = (String)(typeChoice.getSelectedItem());
			} else {
				tnm = lemsComponentType.getName();
			}
			try {
				int ext = lemsModel.getNewSuffix(tnm);
				String label = tnm + "-" + ext;
				createPotentialComponent(tnm, label, "");
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		} else if (cmd.equals("delete")) {
			
			String msg = "Really delete " + active.getID() + "?";
			String title = "Deleting a component";
			int ret = JOptionPane.showConfirmDialog(mainPanel, msg, title, JOptionPane.OK_CANCEL_OPTION, 0);
			if (ret == JOptionPane.OK_OPTION) {
				deleteActive();
			}
			
			
		} else {
			E.error("unrecognized: " + cmd);
		}
	}
	
	
	public void createPotentialComponent(String typeName, String label, String name) {
		LemsComponent lc = null;
		try {
			lc = lemsModel.createComponent(typeName, label, name);
			active = lc;
			potential = lc;
			editActive();
			
		} catch (Exception ex) {
			E.error("cant make cpt " + ex);
			ex.printStackTrace();
		}
	}
 
	
}
