package hu.elte.animaltracker.view.tracking;

import hu.elte.animaltracker.controller.tracking.ColorBackgroundSubtractorController;
import hu.elte.animaltracker.model.tracking.filtering.ColorBackgroundSubtractor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ColorBackgroundSubtractorView extends BackgroundSubtractorView {

	private static final long serialVersionUID = 3182344423014597471L;

	public static void main(String[] args) {

		ColorBackgroundSubtractor color = new ColorBackgroundSubtractor(null);

		ColorBackgroundSubtractorController controller = new ColorBackgroundSubtractorController(
				color);

		ColorBackgroundSubtractorView v = new ColorBackgroundSubtractorView(
				controller);
		v.setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public ColorBackgroundSubtractorView(
			final ColorBackgroundSubtractorController controller) {
		super(controller);
		setTitle("Color Background Subtractor");
		setBounds(100, 100, 335, 355);
		GridBagLayout gridBagLayout = (GridBagLayout) buttonPanel.getLayout();
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0 };

		shiftGrid(gridBagLayout,3,2);
		shiftGrid(gridBagLayout,4,2);
		
		JButton setThrBtn = new JButton("set threshold");
		setThrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setThreshold();
			}
		});

		GridBagConstraints gbc_setThrBtn = new GridBagConstraints();
		gbc_setThrBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_setThrBtn.insets = new Insets(10, 0, 5, 0);
		gbc_setThrBtn.gridx = 0;
		gbc_setThrBtn.gridy = 3;
		buttonPanel.add(setThrBtn, gbc_setThrBtn);
		
		JButton setColorBtn = new JButton("set background");
		setColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setBackground();
			}
		});

		GridBagConstraints gbc_setColorBtn = new GridBagConstraints();
		gbc_setColorBtn.fill = GridBagConstraints.HORIZONTAL;
		gbc_setColorBtn.insets = new Insets(0, 0, 5, 0);
		gbc_setColorBtn.gridx = 0;
		gbc_setColorBtn.gridy = 4;
		buttonPanel.add(setColorBtn, gbc_setColorBtn);


	}

	private void shiftGrid(GridBagLayout gridBagLayout, int idx,int shift) {
		GridBagConstraints c = gridBagLayout.getConstraints(buttonPanel
				.getComponent(idx));
		c.gridy+=shift;
		gridBagLayout.setConstraints(buttonPanel.getComponent(idx), c);
	}

}
