package hu.elte.animaltracker.view.zones;

import hu.elte.animaltracker.controller.listeners.ZoneDesignerControllerListener;
import hu.elte.animaltracker.controller.zones.RoiTreeCellRenderer;
import hu.elte.animaltracker.controller.zones.ZoneDesignerController;
import hu.elte.animaltracker.model.zones.ZoneUnit;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Insets;

public class ZoneDesignerView extends JFrame {

	private JPanel contentPane;
	JTree tree;

	/**
	 * Create the frame.
	 */
	public ZoneDesignerView(final ZoneDesignerController zoneDesignerController) {
		setTitle("Zone Designer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 492, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(buttonPanel, BorderLayout.EAST);
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[] { 0, 0 };
		gbl_buttonPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_buttonPanel.columnWeights = new double[] { 120.0, Double.MIN_VALUE };
		gbl_buttonPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		buttonPanel.setLayout(gbl_buttonPanel);

		JButton btnAddRoi = new JButton("Add ROI");
		btnAddRoi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.addROI();				
			}
		});
		GridBagConstraints gbc_btnAddRoi = new GridBagConstraints();
		gbc_btnAddRoi.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddRoi.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddRoi.gridx = 0;
		gbc_btnAddRoi.gridy = 0;
		buttonPanel.add(btnAddRoi, gbc_btnAddRoi);
		
		JButton btnUnion = new JButton("Union");
		btnUnion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.setUnion(tree.getSelectionModel());
			
			}
		});
		GridBagConstraints gbc_btnUnion = new GridBagConstraints();
		gbc_btnUnion.insets = new Insets(10, 0, 5, 0);
		gbc_btnUnion.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUnion.gridx = 0;
		gbc_btnUnion.gridy = 6;
		buttonPanel.add(btnUnion, gbc_btnUnion);
		
		JButton btnIntersection = new JButton("Intersection");
		btnIntersection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.setIntersection(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnIntersection = new GridBagConstraints();
		gbc_btnIntersection.insets = new Insets(0, 0, 5, 0);
		gbc_btnIntersection.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIntersection.gridx = 0;
		gbc_btnIntersection.gridy = 7;
		buttonPanel.add(btnIntersection, gbc_btnIntersection);
		
		JButton btnSubtraction = new JButton("Subtraction");
		btnSubtraction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.setSubtraction(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnSubtraction = new GridBagConstraints();
		gbc_btnSubtraction.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubtraction.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubtraction.gridx = 0;
		gbc_btnSubtraction.gridy = 8;
		buttonPanel.add(btnSubtraction, gbc_btnSubtraction);
		
		JButton btnExclusive = new JButton("Exclusive");
		btnExclusive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.setExclusive(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnExclusive = new GridBagConstraints();
		gbc_btnExclusive.insets = new Insets(0, 0, 5, 0);
		gbc_btnExclusive.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExclusive.gridx = 0;
		gbc_btnExclusive.gridy = 9;
		buttonPanel.add(btnExclusive, gbc_btnExclusive);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.remove(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemove.gridx = 0;
		gbc_btnRemove.gridy = 3;
		buttonPanel.add(btnRemove, gbc_btnRemove);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.edit(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 5, 0);
		gbc_btnEdit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEdit.gridx = 0;
		gbc_btnEdit.gridy = 2;
		buttonPanel.add(btnEdit, gbc_btnEdit);
		
		JButton btnDuplicate = new JButton("Duplicate");
		btnDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.duplicate(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnDuplicate = new GridBagConstraints();
		gbc_btnDuplicate.insets = new Insets(0, 0, 5, 0);
		gbc_btnDuplicate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDuplicate.gridx = 0;
		gbc_btnDuplicate.gridy = 1;
		buttonPanel.add(btnDuplicate, gbc_btnDuplicate);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zoneDesignerController.select(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.insets = new Insets(0, 0, 5, 0);
		gbc_btnDone.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDone.gridx = 0;
		gbc_btnDone.gridy = 12;
		buttonPanel.add(btnDone, gbc_btnDone);
		
		JButton btnShowAsA = new JButton("Show as a Roi");
		btnShowAsA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.showAsRoi(tree.getSelectionModel());
			}
		});
		GridBagConstraints gbc_btnShowAsA = new GridBagConstraints();
		gbc_btnShowAsA.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowAsA.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnShowAsA.gridx = 0;
		gbc_btnShowAsA.gridy = 5;
		buttonPanel.add(btnShowAsA, gbc_btnShowAsA);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.open();
			}
		});
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.insets = new Insets(10, 0, 5, 0);
		gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpen.gridx = 0;
		gbc_btnOpen.gridy = 10;
		buttonPanel.add(btnOpen, gbc_btnOpen);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoneDesignerController.save();
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 11;
		buttonPanel.add(btnSave, gbc_btnSave);
		
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = tree.getLastSelectedPathComponent();
				if(obj==null)
					return;
				ZoneUnit zoneUnit = (ZoneUnit) ((DefaultMutableTreeNode)obj).getUserObject();
				
				 JPanel panel = new JPanel(new GridLayout(0, 2));
				 JTextField nameField = new JTextField(zoneUnit.toString());
				 panel.add(new JLabel("Name:"));
				 panel.add(nameField);
				 int result = JOptionPane.showConfirmDialog(null, panel, "Rename",
				            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				        if (result == JOptionPane.OK_OPTION) {
				           zoneDesignerController.rename(zoneUnit,nameField.getText());
				        }
				
				
			}
		});
		GridBagConstraints gbc_btnRename = new GridBagConstraints();
		gbc_btnRename.insets = new Insets(10, 0, 5, 0);
		gbc_btnRename.fill = GridBagConstraints.HORIZONTAL;		
		gbc_btnRename.gridx = 0;
		gbc_btnRename.gridy = 4;
		buttonPanel.add(btnRename, gbc_btnRename);

		JScrollPane scrollPane = new JScrollPane();
		tree = new JTree(zoneDesignerController.getZonesModel());
		tree.setCellRenderer(new RoiTreeCellRenderer());	
		tree.setRootVisible(false);
		tree.setVisibleRowCount(2);
		tree.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					zoneDesignerController.changeRoiVisibility((DefaultMutableTreeNode)(tree.getLastSelectedPathComponent()));
				
						
			}
		});	
		scrollPane.setViewportView(tree);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		
		zoneDesignerController.addZoneDesignerControllerListener(new ZoneDesignerControllerListener() {
			
			@Override
			public void zoneSelected(ZoneUnit zoneUnit) {
				// TODO Auto-generated method stub
				closeFrame();
			}
		});
		// contentPane.add(tree, BorderLayout.NORTH);
	}

	protected void closeFrame() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}
