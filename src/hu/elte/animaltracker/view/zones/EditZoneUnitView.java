package hu.elte.animaltracker.view.zones;

import hu.elte.animaltracker.controller.listeners.EditZoneUnitControllerListener;
import hu.elte.animaltracker.controller.zones.EditZoneUnitController;
import hu.elte.animaltracker.controller.zones.EditZoneUnitController.PolygonPoint;
import ij.IJ;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.BoxLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class EditZoneUnitView extends JFrame implements MouseWheelListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090317604309878989L;
	private EditZoneUnitController editPrimitiveController;
	private JPanel contentPane;
	private JToggleButton[] toggleButtons;
	private JTextField xTextField;
	private JTextField yTextField;
	private JTextField wTextField;
	private JTextField hTextField;
	private JTextField pxTextField;
	private JTextField pyTextField;
	private JLabel wLabel;
	private JLabel hLabel;
	private JList list;

	private class SelectAllOfFocus extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			if (!e.isTemporary()) {
				JTextField textField = (JTextField) e.getComponent();
				// This is needed to put the text field in edited mode, so that
				// its processFocusEvent doesn't
				// do anything. Otherwise, it calls setValue, and the selection
				// is lost.
				textField.setText(textField.getText());
				textField.selectAll();
			}
		}

	}

	/**
	 * Create the frame.
	 */
	public EditZoneUnitView(EditZoneUnitController controller) {

		// Default setting
		this.editPrimitiveController = controller;
		Point2D.Double point = editPrimitiveController.getAnchor();
		setTitle("Edit - " + editPrimitiveController.getPrimitiveName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 500);

		// main Panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0,
				java.lang.Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0,
				java.lang.Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btn_roiOverwrite = new JButton("Overwrite ROI");
		btn_roiOverwrite.setPreferredSize(new Dimension(150, 30));
		GridBagConstraints gbc_btn_roiOverwrite = new GridBagConstraints();
		gbc_btn_roiOverwrite.insets = new Insets(10, 10, 10, 10);
		gbc_btn_roiOverwrite.fill = GridBagConstraints.CENTER;
		gbc_btn_roiOverwrite.gridx = 0;
		gbc_btn_roiOverwrite.gridy = 0;
		if (editPrimitiveController.isPrimitive())
			contentPane.add(btn_roiOverwrite, gbc_btn_roiOverwrite);
		final EditZoneUnitView my = this;
		btn_roiOverwrite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(my,
						"Do you want to overwrite this roi?", "Overwriting",
						dialogButton);
				if (dialogResult != JOptionPane.YES_OPTION)
					return;
				editPrimitiveController.setRoi();
				Point2D.Double anchor = editPrimitiveController.getAnchor();
				xTextField.setText(anchor.x + "");
				yTextField.setText(anchor.y + "");
				wTextField.setText(editPrimitiveController.getWidth() + "");
				hTextField.setText(editPrimitiveController.getHeight() + "");
				wLabel.setText(editPrimitiveController.getwLabel());
				hLabel.setText(editPrimitiveController.gethLabel());

			}
		});

		JPanel linear_panel = new JPanel();
		linear_panel.setBorder(new TitledBorder(null, "Transforms",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_linear_panel = new GridBagConstraints();
		gbc_linear_panel.insets = new Insets(0, 0, 5, 0);
		gbc_linear_panel.fill = GridBagConstraints.BOTH;
		gbc_linear_panel.gridx = 0;
		gbc_linear_panel.gridy = 1;
		contentPane.add(linear_panel, gbc_linear_panel);
		GridBagLayout gbl_linear_panel = new GridBagLayout();
		gbl_linear_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_linear_panel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_linear_panel.columnWeights = new double[] { 0, 0, 0 };
		gbl_linear_panel.rowWeights = new double[] { 0, 0, 0, 0,
				java.lang.Double.MIN_VALUE };
		linear_panel.setLayout(gbl_linear_panel);

		GridBagConstraints gbc_linear1 = new GridBagConstraints();
		gbc_linear1.gridheight = 5;
		gbc_linear1.insets = new Insets(0, 0, 5, 0);
		gbc_linear1.fill = GridBagConstraints.BOTH;
		gbc_linear1.anchor = GridBagConstraints.CENTER;
		gbc_linear1.gridx = 0;
		gbc_linear1.gridy = 0;
		JPanel panelancherOut = new JPanel();
		JPanel anchor_panel = new JPanel();
		anchor_panel.setPreferredSize(new Dimension(80, 80));
		anchor_panel.setLayout(new GridLayout(3, 3));
		panelancherOut.add(anchor_panel);
		linear_panel.add(panelancherOut, gbc_linear1);

		GridBagConstraints gbc_linear2 = new GridBagConstraints();
		gbc_linear2.insets = new Insets(10, 15, 10, 0);
		gbc_linear2.fill = GridBagConstraints.BOTH;
		gbc_linear2.gridx = 1;
		gbc_linear2.gridy = 0;
		linear_panel.add(new JLabel("X"), gbc_linear2);

		GridBagConstraints gbc_linear3 = new GridBagConstraints();
		gbc_linear3.insets = new Insets(0, 15, 10, 0);
		gbc_linear3.fill = GridBagConstraints.BOTH;
		gbc_linear3.gridx = 1;
		gbc_linear3.gridy = 1;
		linear_panel.add(new JLabel("Y"), gbc_linear3);

		GridBagConstraints gbc_linear7 = new GridBagConstraints();
		gbc_linear7.insets = new Insets(0, 15, 10, 0);
		gbc_linear7.fill = GridBagConstraints.BOTH;
		gbc_linear7.gridx = 2;
		gbc_linear7.gridy = 0;
		xTextField = new JTextField(point.x + "");
		xTextField.addFocusListener(new SelectAllOfFocus());
		xTextField.setColumns(5);
		xTextField.addMouseWheelListener(this);
		xTextField.addKeyListener(this);
		linear_panel.add(xTextField, gbc_linear7);

		GridBagConstraints gbc_linear8 = new GridBagConstraints();
		gbc_linear8.insets = new Insets(0, 15, 10, 0);
		gbc_linear8.fill = GridBagConstraints.BOTH;
		gbc_linear8.gridx = 2;
		gbc_linear8.gridy = 1;
		yTextField = new JTextField(point.y + "");
		yTextField.addFocusListener(new SelectAllOfFocus());
		yTextField.setColumns(5);
		yTextField.addMouseWheelListener(this);
		yTextField.addKeyListener(this);
		linear_panel.add(yTextField, gbc_linear8);

		if (editPrimitiveController.isPrimitive()) {
			GridBagConstraints gbc_linear4 = new GridBagConstraints();
			gbc_linear4.insets = new Insets(0, 15, 10, 0);
			gbc_linear4.fill = GridBagConstraints.BOTH;
			gbc_linear4.gridx = 1;
			gbc_linear4.gridy = 2;
			wLabel = new JLabel(editPrimitiveController.getwLabel());
			linear_panel.add(wLabel, gbc_linear4);

			GridBagConstraints gbc_linear5 = new GridBagConstraints();
			gbc_linear5.insets = new Insets(0, 15, 10, 0);
			gbc_linear5.fill = GridBagConstraints.BOTH;
			gbc_linear5.gridx = 1;
			gbc_linear5.gridy = 3;
			hLabel = new JLabel(editPrimitiveController.gethLabel());
			linear_panel.add(hLabel, gbc_linear5);

			GridBagConstraints gbc_linear9 = new GridBagConstraints();
			gbc_linear9.insets = new Insets(0, 15, 10, 0);
			gbc_linear9.fill = GridBagConstraints.BOTH;
			gbc_linear9.gridx = 2;
			gbc_linear9.gridy = 2;
			wTextField = new JTextField(editPrimitiveController.getWidth() + "");
			wTextField.setColumns(5);
			wTextField.addMouseWheelListener(this);
			wTextField.addKeyListener(this);
			wTextField.addFocusListener(new SelectAllOfFocus());
			linear_panel.add(wTextField, gbc_linear9);

			GridBagConstraints gbc_linear10 = new GridBagConstraints();
			gbc_linear10.insets = new Insets(0, 15, 10, 0);
			gbc_linear10.fill = GridBagConstraints.BOTH;
			gbc_linear10.gridx = 2;
			gbc_linear10.gridy = 3;
			hTextField = new JTextField(editPrimitiveController.getHeight()
					+ "");
			hTextField.setColumns(5);
			hTextField.addMouseWheelListener(this);
			hTextField.addKeyListener(this);
			hTextField.addFocusListener(new SelectAllOfFocus());
			linear_panel.add(hTextField, gbc_linear10);
		}

		toggleButtons = new JToggleButton[9];
		for (int i = 0; i < 9; i++) {
			JToggleButton toggleButton = new JToggleButton("");
			toggleButtons[i] = toggleButton;
			final int idx = i;
			toggleButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					for (int j = 0; j < toggleButtons.length; j++)
						if (j != idx)
							toggleButtons[j].setSelected(false);
					editPrimitiveController.setAnchor(idx);
					System.out.println(idx);
				}
			});
			anchor_panel.add(toggleButton);
		}
		toggleButtons[0].setSelected(true);

		JPanel polygon_panel = new JPanel();
		if (editPrimitiveController.isPolygon()) {
			list = new JList(editPrimitiveController.getPolygonModel());
			polygon_panel.setBorder(new TitledBorder(null, "Polygon's Points",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_size_panel = new GridBagConstraints();
			gbc_size_panel.fill = GridBagConstraints.BOTH;
			gbc_size_panel.gridx = 0;
			gbc_size_panel.gridy = 2;

			contentPane.add(polygon_panel, gbc_size_panel);

			polygon_panel.setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			polygon_panel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			JPanel panelTop = new JPanel();
			panel.add(panelTop);
			JButton btnInsert2 = new JButton("Insert");
			btnInsert2.setPreferredSize(new Dimension(80, 22));
			btnInsert2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						double x = java.lang.Double.parseDouble(pxTextField
								.getText());
						double y = java.lang.Double.parseDouble(pyTextField
								.getText());

						editPrimitiveController.insertPolygonPoint((float) x,
								(float) y, list.getSelectedIndex());

					} catch (NumberFormatException e) {
						IJ.error("Number Format Exception");
					}
				}
			});
			panelTop.add(btnInsert2);

			JButton btnOverwrite2 = new JButton("Overwrite");
			btnOverwrite2.setPreferredSize(new Dimension(80, 22));
			btnOverwrite2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (list.getSelectedIndex() == -1) {
						IJ.error("No selected item");
						return;
					}
					try {
						double x = java.lang.Double.parseDouble(pxTextField
								.getText());
						double y = java.lang.Double.parseDouble(pyTextField
								.getText());

						PolygonPoint point = (PolygonPoint) list
								.getSelectedValue();
						point.setPoint(x, y);
						editPrimitiveController.updatePolygon(point
								.getPolygon());

					} catch (NumberFormatException ex) {
						IJ.error("Number Format Exception");
					}
				}
			});
			panelTop.add(btnOverwrite2);

			JPanel panelBottom = new JPanel();
			panel.add(panelBottom);
			JLabel lblX_1 = new JLabel("X");
			JLabel lblY_1 = new JLabel("Y");
			pxTextField = new JTextField();
			pxTextField.addFocusListener(new SelectAllOfFocus());
			pxTextField.setColumns(5);
			pyTextField = new JTextField();
			pyTextField.addFocusListener(new SelectAllOfFocus());
			pyTextField.setColumns(5);
			panelBottom.add(lblX_1);
			panelBottom.add(pxTextField);
			panelBottom.add(lblY_1);
			panelBottom.add(pyTextField);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new Dimension(100, 100));
			list.setVisibleRowCount(5);
			scrollPane.setViewportView(list);
			polygon_panel.add(scrollPane, BorderLayout.CENTER);

			JPanel panel_2 = new JPanel();
			polygon_panel.add(panel_2, BorderLayout.WEST);
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			gbl_panel_2.columnWidths = new int[] { 0 };
			gbl_panel_2.rowHeights = new int[] { 0, 0 };
			gbl_panel_2.columnWeights = new double[] { 0.0 };
			gbl_panel_2.rowWeights = new double[] { 0.0, 0.0 };
			panel_2.setLayout(gbl_panel_2);

			JButton btnAdd = new JButton("Add by pointer");
			btnAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					editPrimitiveController.addPoligonPointByPointer(list
							.getSelectedIndex());
				}
			});
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.anchor = GridBagConstraints.CENTER;
			gbc_btnAdd.insets = new Insets(10, 0, 5, 10);
			gbc_btnAdd.gridx = 0;
			gbc_btnAdd.gridy = 0;
			panel_2.add(btnAdd, gbc_btnAdd);

			JButton btnRemove = new JButton("Remove");
			GridBagConstraints gbc_btnRemove = new GridBagConstraints();
			gbc_btnRemove.anchor = GridBagConstraints.CENTER;
			gbc_btnRemove.insets = new Insets(10, 0, 5, 10);
			gbc_btnRemove.gridx = 0;
			gbc_btnRemove.gridy = 1;
			panel_2.add(btnRemove, gbc_btnRemove);

			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (list.getSelectedIndex() == -1) {
						IJ.error("No selected item");
						return;
					}
					editPrimitiveController
							.RemovePolygonPoint((PolygonPoint) list
									.getSelectedValue());
				}
			});

			list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					// TODO Auto-generated method stub
					if (list.getSelectedIndex() == -1)
						return;
					EditZoneUnitController.PolygonPoint point = (EditZoneUnitController.PolygonPoint) list
							.getSelectedValue();

					pxTextField.setText(point.getX() + "");
					pyTextField.setText(point.getY() + "");
				}
			});
		}
		editPrimitiveController
				.addEditPrimitiveControllerListener(new EditZoneUnitControllerListener() {

					@Override
					public void anchorChanged(Double anchor) {
						// TODO Auto-generated method stub
						xTextField.setText(anchor.x + "");
						yTextField.setText(anchor.y + "");
					}

					@Override
					public void overlayerChanged() {
						// TODO Auto-generated method stub

					}

					@Override
					public void roiWidthChanged(double width) {
						// TODO Auto-generated method stub
						wTextField.setText(width + "");
					}

					@Override
					public void roiHeightChanged(double heigth) {
						// TODO Auto-generated method stub
						hTextField.setText(heigth + "");
					}

					@Override
					public void roiLocationXChanged(double x) {
						// TODO Auto-generated method stub
						xTextField.setText(x + "");
					}

					@Override
					public void roiLocationYChanged(double y) {
						// TODO Auto-generated method stub
						yTextField.setText(y + "");
					}
				});
	}

	protected boolean increaseValue(JTextField field, double increase) {
		try {
			double value = java.lang.Double.parseDouble(field.getText());
			field.setText((value + increase) + "");
			return true;
		} catch (NumberFormatException e) {
			IJ.error("Number Format Exception");
		}
		return false;
	}

	protected java.lang.Double getValue(JTextField field) {
		try {
			double value = java.lang.Double.parseDouble(field.getText());
			return value;
		} catch (NumberFormatException e) {
			IJ.error("Number Format Exception");
		}
		return null;
	}

	protected void setTransform() {
		// TODO Auto-generated method stub
		try {
			double x = java.lang.Double.parseDouble(xTextField.getText());
			double y = java.lang.Double.parseDouble(yTextField.getText());
			double w = editPrimitiveController.isPrimitive() ? java.lang.Double
					.parseDouble(wTextField.getText()) : 0;
			double h = editPrimitiveController.isPrimitive() ? java.lang.Double
					.parseDouble(hTextField.getText()) : 0;
			editPrimitiveController.setTransform(x, y, w, h);
			// if(editPrimitiveController.isPolygon())
			// list.setModel(editPrimitiveController.getPolygonModel());

		} catch (NumberFormatException e) {
			IJ.error("Number Format Exception");
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int notches = e.getWheelRotation() * 5;
		JTextField field = (JTextField) e.getSource();
		if (!field.isEnabled())
			return;
		if (field.equals(xTextField) || field.equals(yTextField)) {
			if (!increaseValue(field, notches))
				return;
			setTransform();
		} else if (field.equals(wTextField) || field.equals(hTextField)) {
			if (!increaseValue(field, notches))
				return;
			setTransform();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		JTextField field = (JTextField) e.getSource();
		if (!field.isEnabled())
			return;
		if (e.getKeyCode() != KeyEvent.VK_ENTER)
			return;
		if (field.equals(xTextField) || field.equals(yTextField)) {
			setTransform();
		} else if (field.equals(wTextField) || field.equals(hTextField)) {
			setTransform();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
