package thebombzen.tanks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.DeltaForced;
import thebombzen.tanks.object.projectile.Projectile;
import thebombzen.tanks.object.projectile.explosive.AntiPortalProjectile;
import thebombzen.tanks.object.projectile.explosive.DeltaForcedProjectile;
import thebombzen.tanks.object.projectile.explosive.ExplosiveProjectile;
import thebombzen.tanks.object.projectile.explosive.LandMineProjectile;
import thebombzen.tanks.object.projectile.explosive.ShredderProjectile;
import thebombzen.tanks.object.projectile.explosive.TimedExplosiveProjectile;
import thebombzen.tanks.object.projectile.magnet.ForwardMagnetProjectile;
import thebombzen.tanks.object.projectile.magnet.SpiralMagnetProjectile;
import thebombzen.tanks.object.projectile.teleport.PortalProjectile;
import thebombzen.tanks.object.projectile.teleport.TeleporterProjectile;
import thebombzen.tanks.object.projectile.terrain.CraterProjectile;
import thebombzen.tanks.object.projectile.terrain.DirtballProjectile;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 276365552805515202L;
	private static final ControlPanel controlPanel = new ControlPanel();

	public static ControlPanel getControlPanel() {
		return controlPanel;
	}

	private JComboBox<String> ammoSelectionComboBox = new JComboBox<String>();
	private JLabel angleLabel = new JLabel("Select Angle: 30");
	private JSlider angleSlider = new JSlider(JSlider.HORIZONTAL, 0, 180, 150);
	private Hashtable<Integer, JLabel> backwardAngleSliderLabels = new Hashtable<Integer, JLabel>();

	private List<Component> componentsToDisable = new ArrayList<Component>();

	private JButton fireButton = new JButton("Fire!");
	private JLabel firepowerLabel = new JLabel("Select Firepower: 70");
	private JSlider firepowerSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
			70);
	private Hashtable<Integer, JLabel> forwardAngleSliderLabels = new Hashtable<Integer, JLabel>();

	private volatile boolean leftPressed = false;
	private volatile boolean lockdown = false;

	private JComboBox<String> massSelectionBox = new JComboBox<String>();
	private Portal mostRecentlyFiredPortal = null;
	private Projectile mostRecentlyFiredProjectile = null;

	private JButton resetButton = new JButton("Reset");
	private volatile boolean rightPressed = false;

	private JLabel statusLabel = new JLabel("First player's turn.");

	private JProgressBar[] tankHealthBars;

	private JLabel windLabel = new JLabel();

	private boolean readyToDeltaForce = false;

	private ControlPanel() {

		tankHealthBars = new JProgressBar[2];
		tankHealthBars[0] = new JProgressBar(0, 100);
		tankHealthBars[1] = new JProgressBar(0, 100);

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(Box.createHorizontalStrut(10));
		
		Box content = Box.createVerticalBox();
		content.add(Box.createVerticalStrut(10));

		Box angleFireBox = Box.createHorizontalBox();
		
		//angleSlider.setMaximumSize(new Dimension(465, angleSlider
		//		.getPreferredSize().height));
		angleSlider.setMajorTickSpacing(30);
		angleSlider.setMinorTickSpacing(10);
		angleSlider.setFocusable(false);
		forwardAngleSliderLabels.put(0, new JLabel("180"));
		forwardAngleSliderLabels.put(30, new JLabel("150"));
		forwardAngleSliderLabels.put(60, new JLabel("120"));
		forwardAngleSliderLabels.put(90, new JLabel("90"));
		forwardAngleSliderLabels.put(120, new JLabel("60"));
		forwardAngleSliderLabels.put(150, new JLabel("30"));
		forwardAngleSliderLabels.put(180, new JLabel("0"));

		for (int i = 0; i <= 180; i += 30) {
			backwardAngleSliderLabels.put(i, new JLabel(Integer.toString(i)));
		}

		angleSlider.setLabelTable(forwardAngleSliderLabels);
		angleSlider.setPaintLabels(true);
		angleSlider.setPaintTicks(true);
		angleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				angleSliderChanged();
			}
		});
		componentsToDisable.add(angleSlider);
		
		Box angleLabelBox = Box.createHorizontalBox();
		angleLabelBox.add(angleLabel);
		angleLabelBox.add(Box.createHorizontalGlue());
		
		Box angleBox = Box.createVerticalBox();
		angleBox.add(angleLabelBox);
		angleBox.add(Box.createVerticalGlue());
		angleBox.add(angleSlider);
		
		angleFireBox.add(angleBox);
		angleFireBox.add(Box.createHorizontalStrut(10));

		//firepowerSlider.setFocusable(false);
		//firepowerSlider.setMaximumSize(new Dimension(465, firepowerSlider
		//		.getPreferredSize().height));
		firepowerSlider.setMajorTickSpacing(20);
		firepowerSlider.setMinorTickSpacing(5);
		firepowerSlider.setPaintLabels(true);
		firepowerSlider.setPaintTicks(true);
		firepowerSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				firepowerSliderChanged();
			}
		});
		componentsToDisable.add(firepowerSlider);
		
		Box firepowerLabelBox = Box.createHorizontalBox();
		firepowerLabelBox.add(firepowerLabel);
		firepowerLabelBox.add(Box.createHorizontalGlue());
		
		Box firepowerBox = Box.createVerticalBox();
		firepowerBox.add(firepowerLabelBox);
		firepowerBox.add(Box.createVerticalGlue());
		firepowerBox.add(firepowerSlider);
		
		angleFireBox.add(firepowerBox);

		content.add(angleFireBox);
		
		content.add(Box.createVerticalStrut(5));
		content.add(new JSeparator(SwingConstants.HORIZONTAL));
		content.add(Box.createVerticalStrut(5));

		setWindLabelText();

		Box windAmmoBox = Box.createHorizontalBox();
		
		windAmmoBox.add(windLabel);
		windAmmoBox.add(Box.createHorizontalGlue());

		DefaultComboBoxModel<String> massBoxModel = new DefaultComboBoxModel<String>();
		massBoxModel.addElement("Light");
		massBoxModel.addElement("Standard");
		massBoxModel.addElement("Heavy");
		massSelectionBox.setModel(massBoxModel);
		massSelectionBox.setSelectedIndex(1);
		massSelectionBox.setFocusable(false);
		componentsToDisable.add(massSelectionBox);

		Box massBox = Box.createVerticalBox();
		
		JLabel massLabel = new JLabel("Select Ammo Mass:");
		Box massLabelBox = Box.createHorizontalBox();
		massLabelBox.add(massLabel);
		massLabelBox.add(Box.createHorizontalGlue());
		
		massBox.add(massLabelBox);
		massBox.add(Box.createVerticalStrut(5));
		massBox.add(massSelectionBox);
		//massPanel.setMaximumSize(new Dimension(200, massPanel
		//		.getPreferredSize().height));
		//massPanel.setPreferredSize(massPanel.getMaximumSize());
		
		windAmmoBox.add(massBox);
		windAmmoBox.add(Box.createHorizontalStrut(10));

		DefaultComboBoxModel<String> ammoBoxModel = new DefaultComboBoxModel<String>();
		
		ammoBoxModel.addElement("Basic Explosive");
		ammoBoxModel.addElement("Timed Explosive");
		ammoBoxModel.addElement("Delta-forced Explosive");
		ammoBoxModel.addElement("Anti-portal Explosive");
		ammoBoxModel.addElement("Shredder");
		ammoBoxModel.addElement("Land Mine");
		ammoBoxModel.addElement("Attractive Magnet");
		ammoBoxModel.addElement("Repulsive Magnet");
		ammoBoxModel.addElement("Right Spiral Magnet");
		ammoBoxModel.addElement("Left Spiral Magnet");
		ammoBoxModel.addElement("Dirtball");
		ammoBoxModel.addElement("Crater");
		ammoBoxModel.addElement("Teleporter");
		ammoBoxModel.addElement("Portal");

		ammoSelectionComboBox.setModel(ammoBoxModel);
		ammoSelectionComboBox.setSelectedIndex(0);
		ammoSelectionComboBox.setMaximumRowCount(ammoBoxModel.getSize());
		ammoSelectionComboBox.setFocusable(false);

		Box ammoBox = Box.createVerticalBox();
		
		JLabel ammoLabel = new JLabel("Select Ammo Type:");
		Box ammoLabelBox = Box.createHorizontalBox();
		ammoLabelBox.add(ammoLabel);
		ammoLabelBox.add(Box.createHorizontalGlue());
		
		ammoBox.add(ammoLabelBox);
		
		ammoBox.add(Box.createVerticalStrut(5));
		ammoBox.add(ammoSelectionComboBox);
		//ammoPanel.setMaximumSize(new Dimension(200, ammoPanel
		//		.getPreferredSize().height));
		//ammoPanel.setPreferredSize(ammoPanel.getMaximumSize());
		windAmmoBox.add(ammoBox);
		windAmmoBox.add(Box.createHorizontalStrut(10));

		//fireButton.setMaximumSize(new Dimension(100, ammoPanel
		//		.getPreferredSize().height));
		//fireButton.setPreferredSize(fireButton.getMaximumSize());

		fireButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fireButtonPressed(ae);
			}
		});

		JPanel fireButtonPanel = new JPanel();
		fireButtonPanel.setLayout(new GridLayout());
		componentsToDisable.add(fireButton);
		fireButtonPanel.add(fireButton);
		windAmmoBox.add(fireButtonPanel);

		windAmmoBox.add(Box.createHorizontalGlue());

		componentsToDisable.add(ammoSelectionComboBox);

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				resetButtonPressed();
			}
		});

		windAmmoBox.add(resetButton);

		//wap.setMaximumSize(new Dimension(940, wap.getPreferredSize().height));

		content.add(windAmmoBox);
		content.add(Box.createVerticalStrut(5));
		content.add(new JSeparator(SwingConstants.HORIZONTAL));
		content.add(Box.createVerticalStrut(5));

		Box healthBox = Box.createHorizontalBox();
		
		Box firstPlayerHealthBox = Box.createVerticalBox();
		Box firstPlayerHealthLabelBox = Box.createHorizontalBox();
		firstPlayerHealthLabelBox.add(new JLabel("First player's health"));
		firstPlayerHealthLabelBox.add(Box.createHorizontalGlue());
		firstPlayerHealthBox.add(firstPlayerHealthLabelBox);
		firstPlayerHealthBox.add(Box.createVerticalStrut(5));
		firstPlayerHealthBox.add(tankHealthBars[0]);
		
		healthBox.add(firstPlayerHealthBox);
		healthBox.add(Box.createHorizontalStrut(10));
		
		Box secondPlayerHealthBox = Box.createVerticalBox();
		Box secondPlayerHealthLabelBox = Box.createHorizontalBox();
		secondPlayerHealthLabelBox.add(new JLabel("Second player's health"));
		secondPlayerHealthLabelBox.add(Box.createHorizontalGlue());
		secondPlayerHealthBox.add(secondPlayerHealthLabelBox);
		secondPlayerHealthBox.add(Box.createVerticalStrut(5));
		secondPlayerHealthBox.add(tankHealthBars[1]);
		healthBox.add(secondPlayerHealthBox);
		
		//healthPanel.setMaximumSize(new Dimension(940, healthPanel
		//		.getPreferredSize().height));
		content.add(healthBox);
		content.add(Box.createVerticalStrut(10));

		Box whoseMoveBox = Box.createHorizontalBox();
		whoseMoveBox.add(Box.createHorizontalGlue());
		whoseMoveBox.add(statusLabel);
		whoseMoveBox.add(Box.createHorizontalGlue());
		
		//movePanel.setPreferredSize(new Dimension(940, 20));
		content.add(whoseMoveBox);
		content.add(Box.createVerticalStrut(10));
		
		add(content);
		add(Box.createHorizontalStrut(10));
		
	}

	private void angleSliderChanged() {
		int degrees = 180 - angleSlider.getValue();
		Tanks.getTanks().getCurrentTank()
				.setFireAngle(-Constants.PI * degrees / 180D);
		angleLabel.setText("Select Angle: "
				+ (Tanks.getTanks().getTurnNumber() != 0 ? 180 - degrees
						: degrees));
	}

	public void clickedMouse(int x, int y) {
		if (mostRecentlyFiredPortal != null) {
			if (!Terrain.getTerrain().doesTerrainExistAt(new Vector(x, y))) {
				World.getWorld().addObject(
						new Portal(mostRecentlyFiredPortal, new Vector(x, y)));
				mostRecentlyFiredPortal = null;
				postFire();
			}
		}
		if (readyToDeltaForce
				&& mostRecentlyFiredProjectile instanceof DeltaForced) {
			((DeltaForced) mostRecentlyFiredProjectile).deltaForce();
			readyToDeltaForce = false;
			statusLabel.setText("Ammo successfully delta-forced.");
		}

	}

	private void fireButtonPressed(ActionEvent ae) {
		lock();

		Tank tank = Tanks.getTanks().getCurrentTank();
		tank.setSelectedAmmo(ammoSelectionComboBox.getSelectedIndex());
		tank.setSelectedMass(massSelectionBox.getSelectedIndex());
		tank.setFirePower(firepowerSlider.getValue());

		double mass;
		switch (massSelectionBox.getSelectedIndex()) {
		case 0:
			mass = Constants.INV_SQRT_2;
			break;
		case 1:
			mass = 1D;
			break;
		case 2:
			mass = Constants.SQRT_2;
			break;
		default:
			mass = 1D;
			break;
		}

		Vector position = tank.getPosition().add(
				Vector.polarToCartesian(25D, tank.getFireAngle()));
		Vector velocity = Vector.polarToCartesian(
				35D * Math.sqrt(firepowerSlider.getValue()) / mass,
				tank.getFireAngle()).add(tank.getVelocity());

		Projectile projectile;

		switch (ammoSelectionComboBox.getSelectedIndex()) {
		case 0:
			projectile = new ExplosiveProjectile(position, velocity, mass);
			break;
		case 1:
			projectile = new TimedExplosiveProjectile(position, velocity, mass);
			break;
		case 2:
			projectile = new DeltaForcedProjectile(position, velocity, mass);
			statusLabel.setText("Click to delta-force the projectile.");
			readyToDeltaForce = true;
			break;
		case 3:
			projectile = new AntiPortalProjectile(position, velocity, mass);
			break;
		case 4:
			projectile = new ShredderProjectile(position, velocity, mass);
			break;
		case 5:
			projectile = new LandMineProjectile(position, velocity, mass);
			break;
		case 6:
			projectile = new ForwardMagnetProjectile(position, velocity, mass,
					true);
			break;
		case 7:
			projectile = new ForwardMagnetProjectile(position, velocity, mass,
					false);
			break;
		case 8:
			projectile = new SpiralMagnetProjectile(position, velocity, mass,
					false);
			break;
		case 9:
			projectile = new SpiralMagnetProjectile(position, velocity, mass,
					true);
			break;
		case 10:
			projectile = new DirtballProjectile(position, velocity, mass);
			break;
		case 11:
			projectile = new CraterProjectile(position, velocity, mass);
			break;
		case 12:
			projectile = new TeleporterProjectile(position, velocity, mass,
					tank);
			break;
		case 13:
			projectile = new PortalProjectile(position, velocity, mass);
			break;
		default:
			projectile = null;
			break;
		}
		mostRecentlyFiredProjectile = projectile;
		World.getWorld().addObject(projectile);
		World.getWorld().markBlocked();
	}

	private void firepowerSliderChanged() {
		firepowerLabel.setText("Select Firepower: "
				+ firepowerSlider.getValue());
	}

	public Portal getMostRecentlyFiredPortal() {
		return mostRecentlyFiredPortal;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(960, 200);
	}

	public JProgressBar[] getTankHealthBars() {
		return tankHealthBars;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isLocked() {
		return lockdown;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	private void lock() {
		lockdown = true;
		for (Component component : componentsToDisable) {
			component.setEnabled(false);
		}
	}

	public void playerWon(int player) {
		lock();
		if (player == 0) {
			statusLabel.setText("First Player Won!");
		} else {
			statusLabel.setText("Second Player Won!");
		}
	}

	public void postFire() {
		if (mostRecentlyFiredPortal != null) {
			statusLabel.setText("Place second portal by clicking.");
			return;
		}
		Tanks.getTanks().cycleTurns();
		Tank newTank = Tanks.getTanks().getCurrentTank();
		if (newTank.getPlayerNumber() == 0) {
			angleSlider.setLabelTable(forwardAngleSliderLabels);
			statusLabel.setText("First player's turn.");
		} else {
			angleSlider.setLabelTable(backwardAngleSliderLabels);
			statusLabel.setText("Second player's turn.");
		}
		double fireAngle = - newTank.getFireAngle();
		while (fireAngle < 0){
			fireAngle += Constants.PI;
		}
		while (fireAngle > Constants.PI){
			fireAngle -= Constants.PI;
		}
		
		angleSlider.setValue(180 - (int) (180D / Constants.PI * fireAngle));

		firepowerSlider.setValue(newTank.getFirePower());
		massSelectionBox.setSelectedIndex(newTank.getSelectedMass());
		ammoSelectionComboBox.setSelectedIndex(newTank.getSelectedAmmo());
		Tanks.getTanks().randomizeWind();
		setWindLabelText();
		unlock();
	}

	private void resetButtonPressed() {
		Tanks.getTanks().resetAndInit();
		angleSlider.setLabelTable(forwardAngleSliderLabels);
		angleSlider.setValue(150);
		firepowerSlider.setMaximum(100);
		firepowerSlider.setValue(70);
		setWindLabelText();
		massSelectionBox.setSelectedIndex(1);
		ammoSelectionComboBox.setSelectedIndex(0);
		angleSliderChanged();
		firepowerSliderChanged();
		statusLabel.setText("First player's turn.");
		tankHealthBars[0].setValue(100);
		tankHealthBars[1].setValue(100);
		mostRecentlyFiredPortal = null;
		mostRecentlyFiredProjectile = null;
		unlock();
		Tanks.getTanks().start();
	}

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public void setMostRecentlyFiredPortal(Portal mostRecentlyFiredPortal) {
		this.mostRecentlyFiredPortal = mostRecentlyFiredPortal;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}

	private void setWindLabelText() {
		int displayWind = (int) (Tanks.getTanks().getWind());
		if (displayWind < 0) {
			windLabel.setText(String.format("Wind: << %d <<", -displayWind));
		} else {
			windLabel.setText(String.format("Wind: >> %d >>", displayWind));
		}
	}

	private void unlock() {
		for (Component component : componentsToDisable) {
			component.setEnabled(true);
		}
		lockdown = false;
	}
}
