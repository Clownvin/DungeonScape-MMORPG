package com.git.clownvin.dsclient;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.git.clownvin.dsclient.entity.character.Characters;
import com.git.clownvin.dsclient.entity.object.Objects;
import com.git.clownvin.dsclient.entity.projectile.Projectiles;
import com.git.clownvin.dsclient.net.Client;
import com.git.clownvin.dsclient.net.packet.PacketHandler;
import com.git.clownvin.dsclient.screen.GameScreen;
import com.git.clownvin.dsclient.screen.LoadingScreen;
import com.git.clownvin.dsclient.screen.LoginScreen;
import com.git.clownvin.dsclient.texture.Textures;
import com.git.clownvin.dsclient.world.Chunks;
import com.git.clownvin.simplepacketframework.packet.Packets;

public class DSGame extends Game {
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 360;
	private Client client;
	private Characters characters;
	private Projectiles projectiles;
	private Objects objects;
	private Chunks chunks;
	private SpriteBatch batch;
	private Skin skin;
	private LoginScreen loginScreen;
	private GameScreen gameScreen;
	private int selfID = -1;
	private long serverTime = 0L;
	
	public Client getClient() {
		return client;
	}
	
	public void setGameScreen(GameScreen screen) {
		gameScreen = screen;
	}
	
	public Characters getCharacters() {
		return characters;
	}
	
	public Projectiles getProjectiles() {
		return projectiles;
	}
	
	public Objects getObjects() {
		return objects;
	}
	
	public Chunks getChunks() {
		return chunks;
	}
	
	public int getSelfID() {
		return selfID;
	}
	
	public void setSelfID(int selfID) {
		this.selfID = selfID;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public void setupPackets() {
		Packets.setPacketHandler(new PacketHandler(this));
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
	public LoginScreen getLoginScreen() {
		return loginScreen;
	}
	
	public long getServerTime() {
		return serverTime;
	}
	
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	
	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skin.json"));
		batch = new SpriteBatch();
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}
	
	private LoadingScreen loadingScreen;
	private int loadingPhase = 0;
	
	protected void load() {
		switch (loadingPhase) {
		case 1:
			Textures.loadTexturePaths();
			loadingScreen.setStatusMessage("Loading textures... this can take awhile");
			break;
		case 2:
			Textures.loadAll(); //Temporary for development.
			loadingScreen.setStatusMessage("Loading login screen...");
			break;
		case 3:
			loginScreen = new LoginScreen(this);
			loadingScreen.setStatusMessage("Loading game screen...");
			break;
		case 4:
			gameScreen = new GameScreen(this);
			loadingScreen.setStatusMessage("Loading characters...");
			break;
		case 0:
			setupPackets();
			try {
				client = new Client("localhost", 6942);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			loadingScreen.setStatusMessage("Loading TexturePaths... this can take awhile");
			break;
		case 5:
			characters = new Characters(this);
			loadingScreen.setStatusMessage("Loading projectiles...");
			break;
		case 6:
			projectiles = new Projectiles(this);
			loadingScreen.setStatusMessage("Loading objects...");
			break;
		case 7:
			objects = new Objects(this);
			loadingScreen.setStatusMessage("Loading chunks...");
			break;
		case 8:
			chunks = new Chunks(this);
			loadingScreen.setStatusMessage("Starting client...");
			break;
		case 9:
			client.start();
			loadingScreen.setStatusMessage("Time to play...");
			break;
		case 10:
			setScreen(loginScreen);
			break;
		}
		loadingPhase++;
	}

	@Override
	public void render () {
		super.render();
		if (loadingPhase < 11) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			load();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		skin.dispose();
		//img.dispose();
	}
}
