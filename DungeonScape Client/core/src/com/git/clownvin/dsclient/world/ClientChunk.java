package com.git.clownvin.dsclient.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.git.clownvin.dsapi.world.Chunk;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.texture.Textures;

public class ClientChunk extends Chunk {

	private final DSGame game;
	
	public ClientChunk(DSGame game, int x, int y, ArrayList<Tile> tiles) {
		super(x, y, tiles);
		this.game = game;
	}
	
	public void render(SpriteBatch batch, float rX, float rY) {
		for (Tile t : tiles) {
			float tX = (t.x * Tile.WIDTH) - rX;
			float tY = (t.y * Tile.HEIGHT) - rY;
			//TODO Maybe this code, if fps becomes issue. Culls things off screen.
			//if ((tX < -Gdx.graphics.getWidth() / 2 || tX > Gdx.graphics.getWidth() / 2) && (tY < -Gdx.graphics.getHeight() / 2 || tY > Gdx.graphics.getHeight() / 2))
			//	continue;
			if (Textures.getTextureRegion(t.texture) != null) {
				batch.draw(Textures.getTextureRegion(t.getSprite()), tX, tY, Tile.WIDTH + 1, Tile.HEIGHT + 1);
			} else {
				batch.draw(Textures.get(t.texture), tX, tY, Tile.WIDTH + 1, Tile.HEIGHT + 1);
			}
			//Label l = new Label("("+t.x+","+t.y+")", game.getSkin());
			//l.setFontScale((float) .66);
			//l.setColor(Color.BLACK);
			///l.setPosition(tX, tY);
			//l.draw(batch, 1);
		}
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
	}

}
