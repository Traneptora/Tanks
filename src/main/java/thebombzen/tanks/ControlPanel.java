package thebombzen.tanks;

import java.awt.BorderLayout;
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
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.Terrain;
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

	private JPanel affp = new JPanel(); // angle firepower fire panel
	private JComboBox<String> ammoSelectionBox = new JComboBox<String>();
	private JLabel angleLabel = new JLabel("Select Angle: 30");
	private JSlider angleSlider = new JSlider(JSlider.HORIZONTAL, 0, 180, 150);
	private Hashtable<Integer, JLabel> backwardAngleSliderLabels = new Hashtable<Integer, JLabel>();

	private List<Component> componentsToDisable = new ArrayList<Component>();

	private JButton fireButton = new JButton("Fire!");
	private JLabel firepowerLabel = new JLabel("Select Firepower: 70");
	private JSlider firepowerSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
			70);
	private Hashtable<Integer, JLabel> forwardAngleSliderLabels = new Hashtable<Integer, JLabel>();
	private JPanel healthPanel = new JPanel(new GridLayout(2, 2, 5, 5));

	private volatile boolean leftPressed = false;
	private volatile boolean lockdown = false;

	private JComboBox<String> massSelectionBox = new JComboBox<String>();
	private Portal mostRecentlyFiredPortal = null;
	private Projectile mostRecentlyFiredProjectile = null;

	private JPanel movePanel = new JPanel();

	private JButton resetButton = new JButton("Reset");
	private volatile boolean rightPressed = false;

	private JLabel statusLabel = new JLabel("First player's turn.");

	private JProgressBar[] tankHealthBars;
	private JPanel wap = new JPanel(); // wind ammo panel

	private JLabel windLabel = new JLabel();

	private boolean readyToDeltaForce = false;

	private ControlPanel() {

		tankHealthBars = new JProgressBar[2];
		tankHealthBars[0] = new JProgressBar(0, 100);
		tankHealthBars[1] = new JProgressBar(0, 100);

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(5));

		affp.setLayout(new BoxLayout(affp, BoxLayout.X_AXIS));
		angleSlider.setMaximumSize(new Dimension(465, angleSlider
				.getPreferredSize().height));
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
		JPanel angleLabelPanel = new JPanel(new BorderLayout());
		angleLabelPanel.add(angleLabel);
		JPanel anglePanel = new JPanel();
		anglePanel.setLayout(new BoxLayout(anglePanel, BoxLayout.Y_AXIS));
		anglePanel.add(angleLabelPanel);
		anglePanel.add(Box.createVerticalGlue());
		anglePanel.add(angleSlider);
		affp.add(anglePanel);
		affp.add(Box.createHorizontalStrut(10));

		firepowerSlider.setFocusable(false);
		firepowerSlider.setMaximumSize(new Dimension(465, firepowerSlider
				.getPreferredSize().height));
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
		JPanel firepowerLabelPanel = new JPanel(new BorderLayout());
		firepowerLabelPanel.add(firepowerLabel);
		JPanel firepowerPanel = new JPanel();
		firepowerPanel
				.setLayout(new BoxLayout(firepowerPanel, BoxLayout.Y_AXIS));
		firepowerPanel.add(firepowerLabelPanel);
		firepowerPanel.add(Box.createVerticalGlue());
		firepowerPanel.add(firepowerSlider);
		affp.add(firepowerPanel);

		affp.setMaximumSize(new Dimension(940, affp.getPreferredSize().height));
		add(affp);
		add(Box.createVerticalGlue());

		setWindLabelText();

		wap.setLayout(new BoxLayout(wap, BoxLayout.X_AXIS));
		wap.add(windLabel);
		wap.add(Box.createHorizontalGlue());

		DefaultComboBoxModel<String> massBoxModel = new DefaultComboBoxModel<String>();
		massBoxModel.addElement("Light");
		massBoxModel.addElement("Standard");
		massBoxModel.addElement("Heavy");
		massSelectionBox.setModel(massBoxModel);
		massSelectionBox.setSelectedIndex(1);
		massSelectionBox.setFocusable(false);

		JPanel massPanel = new JPanel();
		massPanel.setLayout(new BoxLayout(massPanel, BoxLayout.Y_AXIS));
		JLabel weightLabel = new JLabel("Select Ammo Weight:");
		JPanel weightLabelPanel = new JPanel(new BorderLayout());
		weightLabelPanel.add(weightLabel);
		massPanel.add(weightLabelPanel);
		massPanel.add(Box.createVerticalStrut(5));
		massPanel.add(massSelectionBox);
		massPanel.setMaximumSize(new Dimension(200, massPanel
				.getPreferredSize().height));
		massPanel.setPreferredSize(massPanel.getMaximumSize());
		wap.add(massPanel);
		wap.add(Box.createHorizontalStrut(10));

		componentsToDisable.add(massSelectionBox);

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

		ammoSelectionBox.setModel(ammoBoxModel);
		ammoSelectionBox.setSelectedIndex(0);
		ammoSelectionBox.setMaximumRowCount(14);
		ammoSelectionBox.setFocusable(false);

		JPanel ammoPanel = new JPanel();
		ammoPanel.setLayout(new BoxLayout(ammoPanel, BoxLayout.Y_AXIS));
		JLabel ammoLabel = new JLabel("Select Ammo Type:");
		JPanel ammoLabelPanel = new JPanel(new BorderLayout());
		ammoLabelPanel.add(ammoLabel);
		ammoPanel.add(ammoLabelPanel);
		ammoPanel.add(Box.createVerticalStrut(5));
		ammoPanel.add(ammoSelectionBox);
		ammoPanel.setMaximumSize(new Dimension(200, ammoPanel
				.getPreferredSize().height));
		ammoPanel.setPreferredSize(ammoPanel.getMaximumSize());
		wap.add(ammoPanel);
		wap.add(Box.createHorizontalStrut(10));

		fireButton.setMaximumSize(new Dimension(100, ammoPanel
				.getPreferredSize().height));
		fireButton.setPreferredSize(fireButton.getMaximumSize());

		fireButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fireButtonPressed(ae);
			}
		});

		componentsToDisable.add(fireButton);
		wap.add(fireButton);

		wap.add(Box.createHorizontalGlue());

		componentsToDisable.add(ammoSelectionBox);

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				resetButtonPressed();
			}
		});

		wap.add(resetButton);

		wap.setMaximumSize(new Dimension(940, wap.getPreferredSize().height));

		add(wap);
		add(Box.createVerticalGlue());

		healthPanel.add(new JLabel("First player's health"));
		healthPanel.add(new JLabel("Second player's health"));
		healthPanel.add(tankHealthBars[0]);
		healthPanel.add(tankHealthBars[1]);
		healthPanel.setMaximumSize(new Dimension(940, healthPanel
				.getPreferredSize().height));
		add(healthPanel);
		add(Box.createVerticalGlue());

		movePanel.setPreferredSize(new Dimension(940, 20));
		movePanel.add(statusLabel);
		add(movePanel);
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
				&& mostRecentlyFiredProjectile instanceof DeltaForcedProjectile) {
			((DeltaForcedProjectile) mostRecentlyFiredProjectile).deltaForce();
			readyToDeltaForce = false;
			statusLabel.setText("Ammo successfully delta-forced.");
		}

	}

	private void fireButtonPressed(ActionEvent ae) {
		lock();

		Tank tank = Tanks.getTanks().getCurrentTank();
		tank.setSelectedAmmo(ammoSelectionBox.getSelectedIndex());
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

		switch (ammoSelectionBox.getSelectedIndex()) {
		case 0:
			projectile = new ExplosiveProjectile(position, velocity, mass);
			break;
		case 1:
			projectile = new TimedExplosiveProjectile(position, velocity, mass);
			break;
		case 2:
			projectile = new DeltaForcedProjectile(position, velocity, mass);
			statusLabel.setText("Click to delta-force the ammo.");
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
		ammoSelectionBox.setSelectedIndex(newTank.getSelectedAmmo());
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
		ammoSelectionBox.setSelectedIndex(0);
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
