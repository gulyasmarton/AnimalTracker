package hu.elte.animaltracker.controller.zones;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import hu.elte.animaltracker.controller.listeners.ZoneDesignerControllerListener;
import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.listeners.ZoneUnitListener;
import hu.elte.animaltracker.model.zones.Primitive;
import hu.elte.animaltracker.model.zones.SetExclusive;
import hu.elte.animaltracker.model.zones.SetIntersection;
import hu.elte.animaltracker.model.zones.SetOperator;
import hu.elte.animaltracker.model.zones.SetSubtraction;
import hu.elte.animaltracker.model.zones.SetUnion;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import hu.elte.animaltracker.view.zones.EditZoneUnitView;
import hu.elte.animaltracker.view.zones.SetSubtractionDialog;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.io.OpenDialog;
import ij.io.SaveDialog;

public class ZoneDesignerController {
	ImagePlus imp;
	DefaultTreeModel zonesModel;
	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");
	List<ZoneDesignerControllerListener> designerControllerListeners = new ArrayList<ZoneDesignerControllerListener>();

	private enum SetOperators {
		UNION, INTERSECTION, EXCLUSIVE, SUBTRACTION
	}

	public ZoneDesignerController(ImagePlus imp) {
		this.imp = imp;
		zonesModel = new DefaultTreeModel(rootNode);

	}

	public void addROI() {
		// TODO Auto-generated method stub
		Roi r = imp.getRoi();
		if (r == null) {
			IJ.error("No ROI");
			return;
		}

		Primitive primitive = Primitive.createPrimitive(r, true);

		if (primitive != null) {
			primitive.addZoneUnitListener(new ZoneUnitListener() {

				@Override
				public void onRoiChanged(ZoneUnit source) {
					// TODO Auto-generated method stub
					updateView();
				}
			});
			rootNode.add(new DefaultMutableTreeNode(primitive, true));
			zonesModel.reload();
			// roiModel.addElement(new SerializableRoi(r, true));
			updateView();
			imp.deleteRoi();
		} else {
			IJ.error("This type of ROI is not allowed!");
			return;
		}
	}

	private void updateView() {
		// TODO Auto-generated method stub
		Overlay ov = new Overlay();
		ov = updateView(ov, rootNode);
		// for (int i = 0; i < rootNode.getChildCount(); i++) {
		// ZoneUnit unit = (ZoneUnit) ((DefaultMutableTreeNode) rootNode
		// .getChildAt(i)).getUserObject();
		// if (unit.isVisible())
		// ov.add(unit.getRoi());
		/*
		 * SerializableRoi roi = (SerializableRoi) ((DefaultMutableTreeNode)
		 * rootNode .getChildAt(i)).getUserObject(); if (roi.isVisible())
		 * ov.add(roi.getRoi());
		 */
		// }
		imp.setOverlay(ov);
	}

	private Overlay updateView(Overlay ov, DefaultMutableTreeNode root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
					.getChildAt(i);
			ZoneUnit unit = (ZoneUnit) node.getUserObject();
			Roi r = unit.getRoi();
			if (r != null && unit.isVisible())
				ov.add(r);
			ov = updateView(ov, node);
		}
		return ov;
	}

	public DefaultTreeModel getZonesModel() {
		return zonesModel;
	}

	public void changeRoiVisibility(DefaultMutableTreeNode object) {
		// TODO Auto-generated method stub

		if (object.getUserObject() instanceof ZoneUnit) {
			ZoneUnit roi = (ZoneUnit) object.getUserObject();
			roi.setVisible(!roi.isVisible());
			updateView();
		}
	}

	private DefaultMutableTreeNode nodeGenerator(DefaultMutableTreeNode root,
			ZoneUnit unit) {
		DefaultMutableTreeNode nod = new DefaultMutableTreeNode(unit);

		if (unit instanceof SetOperator)
			for (ZoneUnit zoneUnit : ((SetOperator) unit).getElements())
				nod = nodeGenerator(nod, zoneUnit);

		root.add(nod);

		return root;
	}

	private List<ZoneUnit> getZoneUnit(TreeSelectionModel selectionModel,
			boolean remove) {
		List<ZoneUnit> units = new ArrayList<ZoneUnit>();
		for (TreePath treePath : selectionModel.getSelectionPaths()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
					.getLastPathComponent();
			if (node.getUserObject() instanceof ZoneUnit) {
				units.add((ZoneUnit) node.getUserObject());
				if (remove)
					zonesModel.removeNodeFromParent(node);
			}
		}
		return units;
	}

	private void setSet(TreeSelectionModel selectionModel,
			SetOperators sOperators) {
		List<ZoneUnit> units = getZoneUnit(selectionModel, true);
		if (units.size() == 0) {
			IJ.error("Please select elements");
			return;
		}
		SetOperator operator = null;

		switch (sOperators) {
		case UNION:
			operator = new SetUnion(units);
			break;
		case EXCLUSIVE:
			operator = new SetExclusive(units);
			break;
		case INTERSECTION:
			operator = new SetIntersection(units);
			break;
		case SUBTRACTION:
			SetSubtractionDialog dialog = new SetSubtractionDialog(units);
			dialog.setVisible(true);
			operator = new SetSubtraction(units);
			break;

		}
		operator.addZoneUnitListener(new ZoneUnitListener() {

			@Override
			public void onRoiChanged(ZoneUnit source) {
				// TODO Auto-generated method stub
				updateView();
			}
		});
		rootNode = nodeGenerator(rootNode, operator);
		zonesModel.reload();
		updateView();
	}

	public void setUnion(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		setSet(selectionModel, SetOperators.UNION);
	}

	public void setExclusive(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		setSet(selectionModel, SetOperators.EXCLUSIVE);
	}

	public void setIntersection(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		setSet(selectionModel, SetOperators.INTERSECTION);
	}

	public void setSubtraction(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		setSet(selectionModel, SetOperators.SUBTRACTION);
	}

	private void remove(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node) {
		parent.remove(node);

		if (parent.getUserObject() instanceof SetOperator) {
			SetOperator set = (SetOperator) parent.getUserObject();
			set.removeZoneUnit((ZoneUnit) node.getUserObject());

			if (set.getElements().size() == 0) {
				remove((DefaultMutableTreeNode) parent.getParent(),
						(DefaultMutableTreeNode) parent);

			}
		}

		/*
		 * if (node.getUserObject() instanceof SetOperator) { SetOperator set =
		 * (SetOperator) node.getUserObject(); for (ZoneUnit unit :
		 * set.getElements()) { parent.add(new DefaultMutableTreeNode(unit)); }
		 * if (parent.getUserObject() instanceof SetOperator) { if
		 * (parent.getChildCount() > 1){ ((SetOperator)
		 * parent.getUserObject()).RoiUpdate(); }else{
		 * remove((DefaultMutableTreeNode)parent.getParent(),parent); } }
		 * 
		 * }
		 */

	}

	public void remove(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub

		List<ZoneUnit> units = new ArrayList<ZoneUnit>();
		for (TreePath treePath : selectionModel.getSelectionPaths()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
					.getLastPathComponent();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
					.getParent();
			remove(parent, node);
			/*
			 * if (node.getUserObject() instanceof SetOperator) { SetOperator
			 * set = (SetOperator) node.getUserObject(); parent.remove(node);
			 * for (ZoneUnit unit : set.getElements()) { parent.add(new
			 * DefaultMutableTreeNode(unit)); } if (parent.getUserObject()
			 * instanceof SetOperator) { if (parent.getChildCount() > 1)
			 * ((SetOperator) parent.getUserObject()).RoiUpdate();
			 * 
			 * }
			 * 
			 * } else {
			 * 
			 * }
			 */
		}
		zonesModel.reload();
		updateView();
		/*
		 * if (node.getUserObject() instanceof ZoneUnit) { units.add((ZoneUnit)
		 * node.getUserObject()); zonesModel.removeNodeFromParent(node); }
		 */
	}

	public void edit(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		for (TreePath treePath : selectionModel.getSelectionPaths()) {
			ZoneUnit unit = (ZoneUnit) ((DefaultMutableTreeNode) treePath
					.getLastPathComponent()).getUserObject();
			unit.setVisible(false);
			EditZoneUnitView editPrimitiveView = new EditZoneUnitView(
					new EditZoneUnitController(imp, unit));
			editPrimitiveView.setVisible(true);
			updateView();
			return;
		}
	}

	private DefaultMutableTreeNode nodeGeneratorDuplicate(
			DefaultMutableTreeNode root, ZoneUnit unit) {
		DefaultMutableTreeNode nod = new DefaultMutableTreeNode(unit);

		if (unit instanceof SetOperator)
			for (ZoneUnit zoneUnit : ((SetOperator) unit).getElements())
				nod = nodeGenerator(nod, zoneUnit);

		root.add(nod);

		return root;
	}

	public void duplicate(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		List<ZoneUnit> units = getZoneUnit(selectionModel, false);
		for (ZoneUnit zoneUnit : units) {
			nodeGenerator(rootNode, zoneUnit.duplicate());
		}
		zonesModel.reload();
		updateView();

	}

	public void select(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		if (selectionModel == null
				|| selectionModel.getSelectionPaths() == null) {
			IJ.error("No element");
			return;
		}
		List<ZoneUnit> units = getZoneUnit(selectionModel, false);
		if (units.size() == 0) {
			IJ.error("No element");
			return;
		}
		firedSelected(units.get(0));
	}

	private void firedSelected(ZoneUnit unit) {
		for (ZoneDesignerControllerListener l : designerControllerListeners) {
			l.zoneSelected(unit);
		}
	}

	public void addZoneDesignerControllerListener(
			ZoneDesignerControllerListener listener) {
		designerControllerListeners.add(listener);
	}

	public void removeZoneDesignerControllerListener(
			ZoneDesignerControllerListener listener) {
		designerControllerListeners.remove(listener);
	}

	public void loadFile() {
		OpenDialog sd = new OpenDialog("Open atz file ...");

		String directory = sd.getDirectory();
		String fileName = sd.getFileName();
		if (fileName == null)
			return;
		List<ZoneUnit> list = TrackingIO.openZoneUnits(directory+File.separator+fileName);
		if(list==null || list.size()==0)
			return;
		
		rootNode = new DefaultMutableTreeNode("Root Node");
		
		for (ZoneUnit zoneUnit : list) {
			rootNode = nodeGenerator(rootNode, zoneUnit);
			zoneUnit.addZoneUnitListener(new ZoneUnitListener() {

				@Override
				public void onRoiChanged(ZoneUnit source) {
					updateView();
				}
			});
		}
		zonesModel.setRoot(rootNode);
		zonesModel.reload();
		updateView();
		/*
		try {
			FileInputStream fileIn = new FileInputStream(directory
					+ System.getProperty("file.separator") + fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
			
			
			rootNode = (DefaultMutableTreeNode) in.readObject();

			for (int i = 0; i < rootNode.getChildCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode
						.getChildAt(i);
				ZoneUnit unit = (ZoneUnit) node.getUserObject();
				unit.addZoneUnitListener(new ZoneUnitListener() {

					@Override
					public void onRoiChanged(ZoneUnit source) {
						// TODO Auto-generated method stub
						updateView();
					}
				});
			}

			zonesModel.setRoot(rootNode);
			zonesModel.reload();
			updateView();

			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("SPR class not found");
			c.printStackTrace();
			return;
		}
		*/
	}

	public void saveToFile() {
		// TODO Auto-generated method stub
		// if(zone == null || zone.getSize()==0) return;
		SaveDialog sd = new SaveDialog("Save atz file ...", "zone.atz", ".atz");
		String directory = sd.getDirectory();
		String fileName = sd.getFileName();
		if (fileName == null)
			return;
		try {
			FileOutputStream fileOut = new FileOutputStream(directory
					+ System.getProperty("file.separator") + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(rootNode);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public void showAsRoi(TreeSelectionModel selectionModel) {
		// TODO Auto-generated method stub
		List<ZoneUnit> units = getZoneUnit(selectionModel, false);
		if (units.size() == 0) {
			IJ.error("No element");
			return;
		}
		imp.setRoi(units.get(0).getRoi());
	}

	public void open() {
		// TODO Auto-generated method stub
		loadFile();
	}

	public void save() {
		// TODO Auto-generated method stub
		saveToFile();
	}

	public void rename(ZoneUnit zoneUnit, String name) {
		// TODO Auto-generated method stub
		zoneUnit.setName(name);

		zonesModel.reload();
	}

}
