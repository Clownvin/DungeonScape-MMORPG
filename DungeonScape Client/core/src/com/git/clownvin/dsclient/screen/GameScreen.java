package com.git.clownvin.dsclient.screen;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.git.clownvin.dsapi.config.Config;
import com.git.clownvin.dsapi.packet.ActionPacket;
import com.git.clownvin.dsapi.packet.MessagePacket;
import com.git.clownvin.dsapi.packet.VelocityPacket;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.character.ClientCharacter;
import com.git.clownvin.dsclient.texture.Textures;
import com.git.clownvin.dsclient.ui.InventorySlot;
import com.git.clownvin.math.MathUtil;

public class GameScreen extends AbstractScreen {
	
	public static final float CHAT_WIDTH = .33f;
	public static final float CHAT_HEIGHT = .33f;
	public static final float CF_HEIGHT = .033f;
	public static final float INVENTORY_WIDTH = .44f;
	public static final float EQUIP_WIDTH = .22f;
	public static final float INVENTORY_HEIGHT = .66f;
	
	private TextField chatField;
	private ScrollPane chatScrollPane;
	private InventorySlot[] inventorySlots;
	private Table inventoryTable;
	private ScrollPane inventoryScrollPane;
	private VerticalGroup chatGroup;
	private LinkedList<Label> chatMessages;
	private long lastChunkRequest;
	private FPSLogger fpsLogger;

	public GameScreen(DSGame game) {
		super(game);
	}
	
	@Override
	public void show() {
		super.show();
		actionTimer = ACTION_DELAY * 100;
	}
	
	public void setInventory(int[] ids, String[] names, long[] amounts) {
		for (int i = 0; i < inventorySlots.length; i++) {
			inventorySlots[(Config.INVENTORY_SIZE -1) - i].setID(ids[i]);
			inventorySlots[(Config.INVENTORY_SIZE -1) - i].setName(names[i]);
			inventorySlots[(Config.INVENTORY_SIZE -1) - i].setAmount(amounts[i]);
		}
		System.out.println("Finished setting inventory.");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Skin skin = game.getSkin();
		fpsLogger = new FPSLogger();
		inventoryTable = new Table();
		inventorySlots = new InventorySlot[Config.INVENTORY_SIZE];
		for (int i = 0; i < inventorySlots.length; i++) {
			inventorySlots[(Config.INVENTORY_SIZE -1) - i] = new InventorySlot((Config.INVENTORY_SIZE -1) - i, skin);
			inventoryTable.add(inventorySlots[(Config.INVENTORY_SIZE -1) - i]);
			if ((i + 1) % 10 == 0)
				inventoryTable.row();
		}
		inventoryScrollPane = new ScrollPane(inventoryTable, skin);
		inventoryScrollPane.setVisible(false);
		
		chatMessages = new LinkedList<Label>();
		chatField = new TextField("", skin);
		chatField.setMessageText("Enter message");
		chatField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				if (c == (char) 13) {
					stage.setKeyboardFocus(null);
					enterTimer = System.currentTimeMillis();
					if (textField.getText().length() > 0) {
					game.getClient().getConnection().send(new MessagePacket(textField.getText(), game.getSelfID(), MessagePacket.WHITE));
					textField.setText("");
					}
				}
				//System.out.println("Char: "+c+", "+(byte)c);
			}
			
		});
		chatGroup = new VerticalGroup();
		chatGroup.left();
		chatGroup.columnAlign(Align.left);
		chatScrollPane = new ScrollPane(chatGroup, skin);
		chatScrollPane.setFadeScrollBars(false);
		stage.addActor(chatScrollPane);
		stage.addActor(chatField);
		stage.addActor(inventoryScrollPane);
		camera.lookAt(0, 0, camera.position.z);
		//camera.zoom += 25f;
		camera.zoom -= .25f;
		lastChunkRequest = System.currentTimeMillis();
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void addChatMessage(String message, Color color) {
		Label messageLabel = new Label(message, game.getSkin());
		messageLabel.setColor(color);
		//messageLabel.setAlignment(Align.left);
		if (chatMessages.size() > 100)
			chatGroup.removeActor(chatMessages.removeFirst());
		chatMessages.add(messageLabel);
		//chatGroup.align(Align.left);
		chatGroup.addActor(messageLabel);
		chatScrollPane.layout();
		if (!chatScrollPane.isDragging() && !chatScrollPane.isFlinging() && !chatScrollPane.isPanning() && chatScrollPane.getY() > 0)
			chatScrollPane.scrollTo(chatScrollPane.getX(), 0, 0, 0);
	}
	
	private float velocityX = 0, velocityY = 0, lastVelocityX = 0, lastVelocityY = 0, lastLookX = 0.0f, lastLookY = 0.0f, lookX = 0.0f, lookY = 0.0f;
	private byte gear = 0, lastGear = 0;
	private static final int ACTION_DELAY = 64;
	private long actionTimer = 0;
	private long enterTimer = 0;
	private boolean rc = false, lc = false;
	private final byte PLAY = 0;
	private final byte EDIT = 1;
	private byte mode = 0;
	private int prevX = 0;
	private int prevY = 0;
	private boolean next = false;
	
	public void handleInput() {
		if (stage.getKeyboardFocus() != null) {
			return;
		}
		lastVelocityX = velocityX;
		lastVelocityY = velocityY;
		lastLookX = lookX;
		lastLookY = lookY;
		lastGear = gear;
		boolean left = false, right = false, up = false, down = false, shift = false, space = false;
		left = Gdx.input.isKeyPressed(Keys.A);
		right = Gdx.input.isKeyPressed(Keys.D);
		up = Gdx.input.isKeyPressed(Keys.W);
		down = Gdx.input.isKeyPressed(Keys.S);
		shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		space = Gdx.input.isKeyPressed(Keys.SPACE);
		if (Gdx.input.isKeyJustPressed(Keys.I)) {
			inventoryScrollPane.setVisible(!inventoryScrollPane.isVisible());
		}
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			mode = mode == PLAY ? EDIT : PLAY;
			addChatMessage("Set mode: "+(mode == PLAY ? "play" : "edit"), Color.FIREBRICK);
		}
		if (left && !right)
			velocityX = -1.0f;
		else if (right && !left)
			velocityX = 1.0f;
		else
			velocityX = 0.0f;
		if (up && !down)
			velocityY = 1.0f;
		else if (down && !up)
			velocityY = -1.0f;
		else
			velocityY = 0.0f;
		gear = VelocityPacket.FIFTH_GEAR;
		if (shift && space)
			gear = VelocityPacket.SECOND_GEAR;
		else if (space)
			gear = VelocityPacket.FOURTH_GEAR;
		else if (shift)
			gear = VelocityPacket.THIRD_GEAR;
		if (velocityX != lastVelocityX || velocityY != lastVelocityY || gear != lastGear) {
			game.getClient().getConnection().send(new VelocityPacket(velocityX, velocityY, gear));
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && System.currentTimeMillis() - enterTimer >= ACTION_DELAY) {
			stage.setKeyboardFocus(chatField);
			enterTimer = System.currentTimeMillis();
		}
		Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		lookX = vec.x;
		lookY = vec.y;
		Actor hit = stage.hit(Gdx.input.getX(), Gdx.input.getY(), false);
		if (rc && !Gdx.input.isButtonPressed(Buttons.RIGHT))
			rc = false;
		if (lc && !Gdx.input.isButtonPressed(Buttons.LEFT))
			lc = false;
		//System.out.println("hit: "+hit);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1) && mode != PLAY) {
			int rx = MathUtil.ard((lookX / Tile.WIDTH) + game.getCharacters().getCharacter(game.getSelfID()).getX());
			int ry = MathUtil.ard((lookY / Tile.HEIGHT) + game.getCharacters().getCharacter(game.getSelfID()).getY());
			//addChatMessage("Want to rep tile at "+rx+" "+ry+"?", Color.GREEN);
			game.getClient().getConnection().send(new MessagePacket("/rtile "+rx+" "+ry, game.getSelfID(), (byte) 0));
			game.getChunks().clearAll();
			return;
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2) && mode != PLAY) {
			int rx = MathUtil.ard((lookX / Tile.WIDTH) + game.getCharacters().getCharacter(game.getSelfID()).getX());
			int ry = MathUtil.ard((lookY / Tile.HEIGHT) + game.getCharacters().getCharacter(game.getSelfID()).getY());
			//addChatMessage("Want to rep tile at "+rx+" "+ry+"?", Color.GREEN);
			game.getClient().getConnection().send(new MessagePacket("/aobj "+rx+" "+ry, game.getSelfID(), (byte) 0));
			//game.getChunks().clearAll();
			return;
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_3) && mode != PLAY) {
			int rx = MathUtil.ard((lookX / Tile.WIDTH) + game.getCharacters().getCharacter(game.getSelfID()).getX());
			int ry = MathUtil.ard((lookY / Tile.HEIGHT) + game.getCharacters().getCharacter(game.getSelfID()).getY());
			//addChatMessage("Want to rep tile at "+rx+" "+ry+"?", Color.GREEN);
			if (!next) {
				prevX = rx;
				prevY = ry;
				next = true;
				return;
			}
			game.getClient().getConnection().send(new MessagePacket("/anpc "+prevX+" "+prevY+" "+rx+" "+ry, game.getSelfID(), (byte) 0));
			//game.getChunks().clearAll();
			next = false;
			return;
		}
		if (hit instanceof InventorySlot) {
			//TODO Inventory stuff
			//System.out.println("here, "+Gdx.input.isButtonPressed(Buttons.RIGHT)+", "+Gdx.input.isButtonPressed(Buttons.LEFT));
			if (Gdx.input.isButtonPressed(Buttons.RIGHT) && (System.currentTimeMillis() - actionTimer > ACTION_DELAY) && !rc) {
				System.out.println("RCAction inventory slot: "+((InventorySlot) hit).index);
				rc = true;
				actionTimer = System.currentTimeMillis();
			} else if (Gdx.input.isButtonPressed(Buttons.LEFT) && (System.currentTimeMillis() - actionTimer > ACTION_DELAY) && !lc) {
				System.out.println("LCAction inventory slot: "+((InventorySlot) hit).index);
				lc = true;
				actionTimer = System.currentTimeMillis();
			}
			return;
		} else if (hit != null && hit != chatScrollPane && hit != chatGroup && !(hit instanceof Label && !(hit instanceof TextField)))
			return;
		if (Gdx.input.isButtonPressed(Buttons.RIGHT) && (System.currentTimeMillis() - actionTimer > ACTION_DELAY)) {
			game.getClient().getConnection().send(new ActionPacket(ActionPacket.FIRE_SECONDARY, lookX, lookY));
			actionTimer = System.currentTimeMillis();
		}
		else if (Gdx.input.isButtonPressed(Buttons.LEFT) && (System.currentTimeMillis() - actionTimer > ACTION_DELAY)) {
			game.getClient().getConnection().send(new ActionPacket(ActionPacket.FIRE_PRIMARY, lookX, lookY));
			actionTimer = System.currentTimeMillis();
		}
		else if ((lastLookX != lookX || lastLookY != lookY) && (System.currentTimeMillis() - actionTimer > ACTION_DELAY)) {
			game.getClient().getConnection().send(new ActionPacket(ActionPacket.LOOK_AT, lookX, lookY));
			actionTimer = System.currentTimeMillis();
		}
	}

	@Override
	public void render(float delta) {
		ClientCharacter pc = game.getCharacters().getCharacter(game.getSelfID());
		float rX = 0;
		float rY = 0;
		if (pc != null) {
			rX = pc.getRenderX();
			rY = pc.getRenderY();
		}
		handleInput();
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		SpriteBatch batch = game.getBatch();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		game.getChunks().render(batch, rX, rY);
		game.getCharacters().render(batch, rX, rY);
		game.getProjectiles().render(batch, rX, rY);
		game.getObjects().render(batch, rX, rY);
		game.getCharacters().renderNames(batch, rX, rY);
		batch.end();
		try {
			stage.act(delta);
			stage.draw();
		} catch (Exception e) {
			System.out.println("Why does stage do this.... next time on bug tracker");
			e.printStackTrace();
			game.setGameScreen(new GameScreen(game));
			game.setScreen(game.getGameScreen());
			return;
		}
		batch.setProjectionMatrix(stage.getCamera().combined);
		batch.begin();
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		float barX = width * (CHAT_WIDTH + .02f), barWidth = width * .34f, barHeight = height * .033f;
		batch.draw(Textures.get(6), barX, 6, barWidth, barHeight);
		if (pc != null && pc.getHP() > 0) {
			batch.draw(Textures.get(7), barX, 6, barWidth * (pc.getHP() / 100.0f), barHeight, 0, 0, (int) (Textures.get(7).getWidth() * (pc.getHP() / 100.0f)), Textures.get(7).getHeight(), false, false);
			//batch.draw(Textures.get(7), barX, 6 + barHeight, 0, 0, (int) (Textures.get(7).getWidth() * (pc.getHP() / 100.0f)), Textures.get(7).getHeight());
			//batch.draw(Textures.get(7), barX, 6 + barHeight, barWidth * (pc.getHP() / 100.0f), barHeight);
		}
		batch.end();
		if (System.currentTimeMillis() - lastChunkRequest >= 250 && pc != null) {
			fpsLogger.log();
			game.getChunks().requestChunks();
			//ClientCharacter self = game.getCharacters().getCharacter(game.getSelfID());
			//System.out.println("Self at: "+self.getX()+", "+self.getY());
			//System.out.println("Client time: "+System.currentTimeMillis());
			//System.out.println("Server time: "+game.getServerTime());
			lastChunkRequest = System.currentTimeMillis();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		//camera.setToOrtho(false, width, height);
		chatField.setSize(width * CHAT_WIDTH, height * CF_HEIGHT);
		chatField.setPosition(3, 3);
		chatScrollPane.setSize(width * CHAT_WIDTH, height * CHAT_HEIGHT);
		chatScrollPane.setPosition(3, 3 + (height * CF_HEIGHT));
		inventoryScrollPane.setSize(width * INVENTORY_WIDTH, height * INVENTORY_HEIGHT);
		inventoryScrollPane.setPosition((width / 2) - ((width * (INVENTORY_WIDTH + EQUIP_WIDTH)) / 2), (height / 2) - ((height * INVENTORY_HEIGHT) / 2));
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
