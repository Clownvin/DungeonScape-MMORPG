package com.git.clownvin.dsclient.screen;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.git.clownvin.dsapi.packet.ClientStatusPacket;
import com.git.clownvin.dsapi.packet.LoginRequest;
import com.git.clownvin.dsapi.packet.LoginResponse;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.simplepacketframework.packet.RequestTimedOutException;
import com.git.clownvin.simplescframework.connection.KeyExchangeIncompleteException;

public class LoginScreen extends AbstractScreen {
	private Table table;
	private TextField usernameField;
	private TextField passwordField;
	private Label errorLabel;
	private TextButton submit;

	public LoginScreen(DSGame game) {
		super(game);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		table = new Table();
		Skin skin = game.getSkin();
		table.add(usernameField = new TextField("", skin)).center().top().pad(4);
		usernameField.setMessageText("Username");
		table.row();
		table.add(passwordField = new TextField("", skin)).center().bottom().pad(4);
		passwordField.setMessageText("Password");
		table.row();
		passwordField.setPasswordMode(true);
		passwordField.setPasswordCharacter('*');
		table.add(submit = new TextButton("Login", skin)).center().pad(4);
		table.row();
		table.add(errorLabel = new Label("", skin)).center();
		submit.addListener(new EventListener() {
			
			private boolean pressed = false;
			private long time = System.currentTimeMillis();
			
			@Override
			public boolean handle(Event event) {
				if (System.currentTimeMillis() - time < 100)
					return false;
				if (submit.isPressed() && !pressed) {
					System.out.println("Pressed");
					pressed = true;
					time = System.currentTimeMillis();
					LoginRequest req = new LoginRequest(usernameField.getText(), passwordField.getText());
					//System.out.println("Sending req with size: "+req.sizeOf());
					try {
						LoginResponse response = (LoginResponse) game.getClient().getConnection().getResponse(req);
						switch (response.getResult()) {
						case LoginResponse.INCORRECT:
							errorLabel.setText("Incorrect credentials");
							break;
						case LoginResponse.ALREADY_LOGGED_IN:
							errorLabel.setText("You are already logged in");
							break;
						case LoginResponse.EXCEPTION_OCCURED:
							errorLabel.setText("There was an error processing your request");
							break;
						case LoginResponse.USERNAME_TOO_SHORT:
							errorLabel.setText("Username must be at least 3 characters long");
							break;
						case LoginResponse.PASSWORD_TOO_SHORT:
							errorLabel.setText("Password must be at least 5 characters long\nIt should be at least 8, however");
							break;
						case LoginResponse.SUCCESS:
							game.setScreen(game.getGameScreen());
							game.getClient().getConnection().send(new ClientStatusPacket(ClientStatusPacket.READY));
							break;
						default:
							System.out.println("Unexpected result: "+response.getResult());
						}
					} catch (IOException | InterruptedException | KeyExchangeIncompleteException
							| RequestTimedOutException e) {
						e.printStackTrace();
					}//.send(req);
					return true;
				} else {
					pressed = false;
				}
				return false;
			}
			
		});
		table.setFillParent(true);
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		System.out.println("Dispose called");
		stage.dispose();
	}

}
