package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class Java3D extends SimpleApplication {

	private Boolean isRunning = true;
	private Spatial model;
	private float moveSpeed = 0.5f;
	private float q = 32.7868852f;
	public static ThreeDModel[] nodes = new ThreeDModel[150];
	public static Boolean animation3DStarted = false;
	public static Long startTime = new Long(0);
	public static int startSet = 1;
	public static int endSet = 1;
	public static int currentSet = 1;
	public static int entireDuration;
	public static int currentTarget = 2;
	public static int durationMinusPrevious = 0;
	public static Boolean started = false;
	public static Boolean isMoving = false;
	public static Boolean threeDRunning = false; // true the entire time 3D screen is open until it is shut
	public static BitmapText directions;
	public static BitmapText rotation;
	public static BitmapText animation;
	public static BitmapText zoomation;
	public static BitmapText fullScreen;
	public static BitmapText exit3D;
	public static BitmapText hide;
	public static Boolean isHUDVisible = true;
	public static Spatial marcherModel;

	@Override
	public void simpleInitApp() {

		initializeHUD();
		threeDRunning = true;

		setDisplayFps(false);
		setDisplayStatView(false);

		this.assetManager.registerLocator("/", ClasspathLocator.class);
		// Load the OBJ model
		model = assetManager.loadModel("field6.obj");

		model.scale(0.05f, 0.05f, 0.05f);
		model.rotate(0.0f, 0f, 0.0f);
		model.setLocalTranslation(15.25f, 0f, 8.12825001f);

		// Populate the scene
		// pull in and initialize durations
		float[] durations = new float[100];
		for (int i = 0; i < 100; i++) {
			durations[i] = Field.durations[i];
		}

		Java3D.animation3DStarted = false;
		Java3D.started = false;


		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				
				if(Top.modelStyle.equals("alien")) {
					 marcherModel = assetManager.loadModel("dude.obj");
				}else if (Top.modelStyle.equals("minion")) {
					marcherModel = assetManager.loadModel("minion.glb");
				}
				ThreeDModel node = new ThreeDModel(marcherModel, i);
				nodes[i] = node;

				Double xDouble = Field.balls[i].points[Main.set.getValue() - 1].getX() / q;
				float xFloat = xDouble.floatValue();
				Double yDouble = Field.balls[i].points[Main.set.getValue() - 1].getY() / q;
				float yFloat = yDouble.floatValue();

				node.setLocalTranslation(xFloat, 0f, yFloat);
				rootNode.attachChild(node);
			}
		}

		rootNode.attachChild(model);

		// Add a DirectionalLight to the scene
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		rootNode.addLight(sun);

		cam.setLocation(new Vector3f(15.166319f, 11.419062f, 26.613789f)); // Adjust the values as needed

		// Look at the center of the scene (0, 0, 0)
		Vector3f center = new Vector3f(15.25f, 0f, 8.12825001f);
		cam.lookAt(center, Vector3f.UNIT_Y);

		initKeys();

		getRootNode()
				.attachChild(SkyFactory.createSky(getAssetManager(), "SKY00.dds", SkyFactory.EnvMapType.SphereMap));

	}

	private BitmapText getBitmapText(String text, int line) {
		BitmapText bmt = new BitmapText(guiFont, false);
		bmt.setSize(guiFont.getCharSet().getRenderedSize());
		bmt.setColor(ColorRGBA.Yellow);
		bmt.setLocalTranslation(0, bmt.getLineHeight() * line, 0);
		bmt.setText(text);
		return bmt;
	}

	private void initializeHUD() {

		int line = 6;

		// hide
		hide = getBitmapText("Toggle Shortcut Display: H", line--);
		guiNode.attachChild(hide);

		// directions
		directions = getBitmapText("Down/Left/Right/Up:  S/A/D/W", line--);
		guiNode.attachChild(directions);

		// rotation
		rotation = getBitmapText("Rotate Camera: mouse", line--);
		guiNode.attachChild(rotation);

		// animation
		animation = getBitmapText("Animate: M (Move)", line--);
		guiNode.attachChild(animation);

		// zoomation
		zoomation = getBitmapText("Zoom in/out: 1 / 2", line--);
		guiNode.attachChild(zoomation);

		// fullScreen
		// fullScreen = getBitmapText("Toggle Full Screen Mode: F", line--);
		// guiNode.attachChild(fullScreen);

		// exit3D
		exit3D = getBitmapText("Close 3D Viewer: ESCAPE", line--);
		guiNode.attachChild(exit3D);

	}

	private void initKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("In", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("Out", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("Animate", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("ToggleHUD", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addListener(analogListener, "Left", "Right", "Up", "Down", "In", "Out");
		inputManager.addListener(actionListener, "Animate", "ToggleHUD");
	}

	final private ActionListener actionListener = new ActionListener() {

		private boolean isPlaying = false;

		@Override
		public void onAction(String name, boolean isPressed, float tpf) {

			if (name.equals("ToggleHUD") && isPressed) {

				if (isHUDVisible) {
					guiNode.detachChild(hide);
					guiNode.detachChild(directions);
					guiNode.detachChild(rotation);
					guiNode.detachChild(animation);
					guiNode.detachChild(zoomation);
					// guiNode.detachChild(fullScreen);
					guiNode.detachChild(exit3D);
					isHUDVisible = !isHUDVisible;
				} else {
					guiNode.attachChild(hide);
					guiNode.attachChild(directions);
					guiNode.attachChild(rotation);
					guiNode.attachChild(animation);
					guiNode.attachChild(zoomation);
					// guiNode.attachChild(fullScreen);
					guiNode.attachChild(exit3D);
					isHUDVisible = !isHUDVisible;
				}
			}

			if (name.equals("Animate")) {

				for (int i = 0; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						if (Field.balls[i].points[1] == null) {
							// Do nothing. There ain't anything to do.
							return;
						}
					}
				}

				if (isPressed && !isPlaying) {
					try {
						AudioInputStream ais = AudioSystem.getAudioInputStream(Top.soundFile);
						Bottom.clip = AudioSystem.getClip();
						Bottom.clip.open(ais);
					} catch (Exception ex) {

					}

					if (Bottom.clip == null || !Right.isPlaybackAudioChecked) {

					} else {
						int startOfClip = Frame.bottom.calculateClipStart();
						Bottom.clip.setMicrosecondPosition(startOfClip * 1000);
						Bottom.clip.start();
					}

					// Get the start set from the Right side bar and see if it is valid. If so, set
					// it to the start set.
					Integer startSetEntered = Right.playbackFrom;

					for (int i = 0; i < nodes.length; i++) {
						if (nodes[i] != null) {
							System.out.println("Not Null");
							if (nodes[i].points[startSetEntered - 1] != null
									&& nodes[i].points[startSetEntered] != null) {
								startSet = startSetEntered;
								break;
							} else {
								startSet = 1;
								if (Bottom.clip != null && Right.isPlaybackAudioChecked) {
									Bottom.clip.setMicrosecondPosition(0);
								}
							}
						}
					}

					// set endSet to the last set that has balls and durations.
					int lastSetWithPoints = 0;
					for (int i = 0; i < nodes.length; i++) {
						if (nodes[i] != null && Field.balls[i] != null) {
							for (int j = 0; j < Field.balls[i].points.length; j++) {
								if (Field.balls[i].points[j] != null) {
									lastSetWithPoints++;
								}
							}
							break;
						}
					}

					int lastSetWithDurations = 0;
					for (int i = 0; i < Field.durations.length; i++) {
						if (Field.durations[i] == -1) {
							break;
						}
						if (Field.durations[i] != -1) {
							lastSetWithDurations = i + 1;
						}
					}

					if (lastSetWithDurations < lastSetWithPoints) {
						endSet = lastSetWithDurations;
					} else {
						endSet = lastSetWithPoints;
					}
					System.out.println("The end set is: " + endSet);
					animation3DStarted = true;

					isPlaying = true;

				} else if (!isPressed) {
					isPlaying = false;
				}
			}
		}
	};

	@Override
	public void destroy() {
		threeDRunning = false;
		if (Bottom.clip != null) {
			Bottom.clip.stop();
			Bottom.stopPressed();
			Bottom.stop.setVisible(true);
		}
		super.destroy();
	}

	final private AnalogListener analogListener = new AnalogListener() {
		@Override
		public void onAnalog(String name, float value, float tpf) {
			if (isRunning) {

				if (name.equals("Rotate")) {
					model.rotate(0, value, 0);
				}
				if (name.equals("Right")) {
					moveCamera(-.1, 0, 0);
				}
				if (name.equals("Left")) {
					moveCamera(.1, 0, 0);
				}
				if (name.equals("Up")) {
					moveCamera(0, .1, 0);
				}
				if (name.equals("Down")) {
					moveCamera(0, -.1, 0);
				}
				if (name.equals("In")) {
					moveCamera(0, 0, .1);
				}
				if (name.equals("Out")) {
					moveCamera(0, 0, -.1);
				}
			} else {
			}
		}
	};

	private void moveCamera(double moveX, double moveY, double moveZ) {
		Vector3f camDir = cam.getDirection().clone();
		Vector3f camLeft = cam.getLeft().clone();
		Vector3f camUp = cam.getUp().clone();

		camDir.normalizeLocal().multLocal((float) (moveZ * moveSpeed));
		camLeft.normalizeLocal().multLocal((float) (moveX * moveSpeed));
		camUp.normalizeLocal().multLocal((float) (moveY * moveSpeed));

		cam.setLocation(cam.getLocation().add(camDir).add(camLeft).add(camUp));
	}

	@Override
	public void simpleUpdate(float tpf) {

		if (Java3D.animation3DStarted) {
			// This is going to happen just once.
			if (started == false) {
				startTime = System.currentTimeMillis();
				started = true;
				// this will set the length of the entire animation
				System.out.println(startSet + " " + endSet);
				entireDuration = entireDuration(startSet, endSet);
				currentSet = Java3D.startSet;
				currentTarget = Java3D.currentSet + 1;
			}
			// and this happens repeatedly the entire time
			long time = System.currentTimeMillis();
			long duration = time - Java3D.startTime;
			// This signals that the duration has elapsed and the timer is to stop
			if (duration > entireDuration) {
				duration = entireDuration;
				stopTimer();
				Java3D.started = false;

				// Set 1 is set at the end of the loop, isMoving flag is set false, and it is
				// repainted......but, you still move on
				// Frame.field.setSet(1);
				// currentSet = 1;
				Java3D.isMoving = false;
				Java3D.currentSet = (Java3D.currentTarget);
				return;
			}
			// You get here even after the clock has stopped. Hence the if(started) and
			// isMoving == true

			// And this signals the end of one set cycle, advances the sets/targets/etc.
			if (duration >= currentDurationCalculation(Java3D.currentTarget) && Java3D.isMoving == true) {
				duration = currentDurationCalculation(Java3D.currentTarget);

				if (Java3D.endSet != Java3D.currentTarget) {
					Java3D.currentSet += 1;
					Java3D.currentTarget += 1;
				}
			}
			Java3D.durationMinusPrevious = ((int) duration - (previous(Java3D.currentSet)));
			double progress = (double) Java3D.durationMinusPrevious
					/ (double) Field.durations[Java3D.currentSet - 1];
			if (Java3D.started) {
				for (int i = 0; i < Java3D.nodes.length; i++) {
					if (Java3D.nodes[i] != null) {

						Java3D.nodes[i].startPoint = Java3D.nodes[i].points[Java3D.currentSet - 1];
						Java3D.nodes[i].endPoint = (Java3D.nodes[i].points[Java3D.currentTarget - 1]);
						Java3D.isMoving = true;
						Java3D.nodes[i].update(progress);
					}
				}
			}
		}

	}

	public static int previous(int currentSet) {
		int answer = 0;

		for (int i = Java3D.startSet - 1; i < currentSet - 1; i++) {
			answer += Field.durations[i];
		}

		return answer;
	}

	public static int currentDurationCalculation(int currentTarget) {
		int time = 0;
		for (int i = Java3D.startSet - 1; i < currentTarget - 1; i++) {
			time += Field.durations[i];
		}
		return time;
	}

	public static int entireDuration(int startSet, int endSet) {
		int entireDuration = 0;
		for (int i = startSet - 1; i < endSet - 1; i++) {
			entireDuration += Field.durations[i];
		}

		return entireDuration;
	}

	public static void stopTimer() {
		Java3D.animation3DStarted = false;
	}
	
	public class ThreeDModel extends Node {

		private Vector3f[] points = new Vector3f[100];
		private Vector3f startPoint;
		private Vector3f endPoint;
		private Vector3f location;
		private Spatial marcher;

		public ThreeDModel(Spatial marcher, int index) {
			this.marcher = marcher;
			this.attachChild(marcher);

			this.marcher.scale(1f, 1f, 1f);
			for (int i = 0; i < this.points.length; i++) {
				if (Field.balls[index].points[i] != null) {
					double xDouble = Field.balls[index].points[i].getX() / q;
					double yDouble = Field.balls[index].points[i].getY() / q;
					this.points[i] = new Vector3f((float) xDouble, 0f, (float) yDouble);
				}
			}
			this.location = new Vector3f(0f, 0f, 0f);
			this.startPoint = new Vector3f(0f, 0f, 0f);
			this.endPoint = new Vector3f(0f, 0f, 0f);
		}

		public void update(double progress) {
			Double locationX = startPoint.x + ((endPoint.x - startPoint.x) * progress);
			Double locationZ = startPoint.z + ((endPoint.z - startPoint.z) * progress);
			float xFloat2 = locationX.floatValue();
			float zFloat2 = locationZ.floatValue();
			this.location.x = xFloat2;
			this.location.z = zFloat2;

			// Set the local translation of the node
			this.setLocalTranslation(new Vector3f(location.x, 0f, location.z));

		}
	}
}

