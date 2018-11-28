package com.git.clownvin.dsclient.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.git.clownvin.dsclient.DSGame;

public class LoadingScreen extends AbstractScreen {
	
	private Texture loadingTexture;
	private Label label;
	public LoadingScreen(DSGame game) {
		super(game);
		loadingTexture = new Texture(Gdx.files.internal("./textures/loading.png"));
		label = new Label("Loading...", game.getSkin());
		label.setFontScale(1);
	}
	
	public void setStatusMessage(String message) {
		label.setText(message);
	}

	@Override
	public void render(float delta) {
		label.setX((Gdx.graphics.getWidth() / 2) - (label.getGlyphLayout().width / 2));
		label.setY((Gdx.graphics.getHeight() / 2) - (label.getGlyphLayout().height / 2));
		game.getBatch().begin();
		game.getBatch().draw(loadingTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		label.draw(game.getBatch(), 1.0f);
		game.getBatch().end();
	}

	@Override
	public void dispose() {
		loadingTexture.dispose();
	}

}
