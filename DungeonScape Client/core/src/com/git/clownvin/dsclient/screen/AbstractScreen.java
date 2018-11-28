package com.git.clownvin.dsclient.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.git.clownvin.dsclient.DSGame;

public abstract class AbstractScreen implements Screen {
	
	protected final DSGame game;
	protected OrthographicCamera camera;
	protected Viewport viewport;
	protected Stage stage;
	protected OrthographicCamera stageCamera;
	protected Viewport stageViewport;
	
	public AbstractScreen(final DSGame game) {
		this.game = game;
		onCreate();
	}
	
	public void onCreate() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		stage = new Stage(stageViewport = new ScreenViewport(stageCamera = new OrthographicCamera()));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		stageViewport.update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
