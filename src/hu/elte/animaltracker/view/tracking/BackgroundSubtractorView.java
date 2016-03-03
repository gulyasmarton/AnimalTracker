package hu.elte.animaltracker.view.tracking;

import hu.elte.animaltracker.controller.tracking.BackgroundSubtractorController;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BackgroundSubtractorView extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4160493032605330378L;
	protected JPanel contentPane;
	protected JList list;
	protected JPanel buttonPanel;

	/**
	 * Create the frame.
	 */
	public BackgroundSubtractorView(final BackgroundSubtractorController medianImageController) {	
		setTitle("Background Subtractor");
		setBounds(100, 100, 335, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel(new GridLayout(1,1));
		JScrollPane scrollPane = new JScrollPane();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		list = new JList(medianImageController.getListModel());
		list.setVisibleRowCount(2);
		scrollPane.setViewportView(list);
		panel.add(scrollPane);
		contentPane.add(panel, BorderLayout.CENTER);

		

		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(buttonPanel, BorderLayout.WEST);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{120};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0,0.0, 0.0};
		buttonPanel.setLayout(gbl_panel_2);
		
		
		JButton btnSetImage = new JButton("Set image");
		btnSetImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medianImageController.setImage();
			}
		});	
		
		GridBagConstraints gbc_btnSetImage = new GridBagConstraints();
		gbc_btnSetImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetImage.insets = new Insets(0, 0, 15, 0);
		gbc_btnSetImage.gridx = 0;
		gbc_btnSetImage.gridy = 0;
		buttonPanel.add(btnSetImage, gbc_btnSetImage);
		
		
		JButton btnNewButton = new JButton("Add frame");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medianImageController.addFrame();
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		buttonPanel.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Remove frame");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medianImageController.removeFrame(list.getSelectedValues());
			}
		});
		
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 2;
		buttonPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Show filter");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medianImageController.show();
			}
		});

		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.insets = new Insets(10, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 3;
		buttonPanel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Done");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				closeFrame();
			}
		});

		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 4;
		buttonPanel.add(btnNewButton_3, gbc_btnNewButton_3);

		
	}

	protected Double getSigma() {
		Double s = null;
		String sigma = "";
		try {
			if (!sigma.equals(""))
				s = Double.parseDouble(sigma);
		} catch (NumberFormatException e) {

		}
		return s;

	}

	protected void closeFrame() {		
		super.dispose();
	}

}
