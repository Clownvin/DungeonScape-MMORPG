package com.git.clownvin.dsclient.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.git.clownvin.dsapi.entity.character.Character;
import com.git.clownvin.dsapi.world.Tile;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.MovableClientEntity;
import com.git.clownvin.dsclient.texture.Textures;

public class ClientCharacter extends MovableClientEntity implements Character {

	private float hp, lookX, lookY;
	private String name;
	
	private Label message;
	private Label nameLabel;
	private long messageTimer = 0L;
	private byte affiliation;
	
	public ClientCharacter(DSGame game, float x, float y, float width, float height, float lastX, float lastY, int id, float hp, int sprite, String name, byte affiliation) {
		super(game, id, x, y, width, height, sprite, lastX, lastY, 0);
		this.hp = hp;
		this.sprite = sprite;
		this.name = name;
		this.affiliation = affiliation;
		nameLabel = new Label(name, game.getSkin());
		message = new Label("", game.getSkin());
	}
	
	public byte getAffiliation() {
		return affiliation;
	}
	
	public void setMessage(String message, Color color) {
		this.message.setText(message);
		this.message.setColor(color);
		messageTimer = System.currentTimeMillis();
	}
	
	public boolean showMessage() {
		return System.currentTimeMillis() - messageTimer < 3000;
	}
	
	public Label getMessage() {
		return message;
	}
	
	public Label getNameLabel() {
		return nameLabel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getHP() {
		return hp;
	}

	@Override
	public void setHP(float hp) {
		this.hp = hp;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getLookX() {
		return lookX;
	}

	@Override
	public float getLookY() {
		return lookY;
	}

	@Override
	public void setLookX(float x) {
		this.lookX = x;
	}

	@Override
	public void setLookY(float y) {
		this.lookY = y;
	}
	
	public void renderName(SpriteBatch batch, float selfX, float selfY) {
		float x = (getRenderX() - selfX) - (getWidth() / 2);
		float y = (getRenderY() - selfY) - (getHeight() / 2);
		float textPos = 1.0f;
		if (showMessage()) {
			textPos += .25f;
			Label message = getMessage();
			message.layout();
			message.setPosition((x - (message.getGlyphLayout().width / 2)) + (Tile.WIDTH / 2), (y + (Tile.HEIGHT * textPos)));
			message.draw(batch, 1);
		}
		if (getEID() == game.getSelfID()) {
			batch.draw(Textures.get(11), x + 12f, y + 12f, 8, 8);
		} else {
			textPos += .25f;
			Label name = getNameLabel();
			name.layout();
			name.setPosition((x - (name.getGlyphLayout().width / 2)) + (getWidth() / 2), (y + (getHeight() * textPos)));
			name.draw(batch, 1);
			if (hp < 100.0f) {
				batch.draw(Textures.get(6), x, y + getHeight() + 4, getWidth(), 6);
				batch.draw(Textures.get(7), x, y + getHeight() + 4, getWidth() * (getHP() / 100.0f), 6, 0, 0, (int) (Textures.get(7).getWidth() * (getHP() / 100.0f)), Textures.get(7).getHeight(), false, false);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, float selfX, float selfY) {
		float x = (getRenderX() - selfX) - (getWidth() / 2);
		float y = (getRenderY() - selfY) - (getHeight() / 2);
		float dX;
		float dY;
		double deg;
		if (getEID() == game.getSelfID()) {
			dX = Gdx.input.getX() - (Gdx.graphics.getWidth() / 2);
			dY = Gdx.input.getY() - (Gdx.graphics.getHeight() / 2);
			if (dX == 0) {
				deg = 0;
			} else {
				deg = -Math.atan(dY/dX);
			}
		} else {
			dX = getLookX();
			dY = getLookY();
			if (dX == 0) {
				deg = 0;
			} else {
				deg = Math.atan(dY/dX);
			}
		}
		if (dX < 0)
			deg += Math.toRadians(180);
		//batch.draw(Textures.get(c.getSprite()), x, y, c.getWidth() / 2, c.getHeight() / 2, c.getWidth(), c.getHeight(), 1f, 1f, (float) Math.toDegrees(deg), 0, 0, (int) c.getWidth(), (int) c.getHeight(), false, false);
		//batch.draw(Textures.get(getSprite()), x, y, getWidth(), getHeight());
		//System.out.println("Drawing char sprite: "+x+", "+y+", "+(getWidth() / 2)+", "+(getHeight() / 2)+", "+getWidth()+", "+getHeight()+", "+(float) Math.toDegrees(deg));
		if (Textures.getTextureRegion(getSprite()) != null) {
			TextureRegion reg = Textures.getTextureRegion(getSprite());
			batch.draw(reg, x, y, (getWidth() / 2), (getHeight() / 2), getWidth(), getHeight(), 1.0f, 1.0f, (float) Math.toDegrees(deg));
		} else {
			batch.draw(Textures.get(getSprite()), x, y, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1f, 1f, (float) Math.toDegrees(deg), 0, 0, (int) getWidth(), (int) getHeight(), false, false);
		}
		
	}
	
	
	//SHOULDN'T be USED

	@Override
	public float getShootX() {
		return getLookX();
	}

	@Override
	public float getShootY() {
		return getLookY();
	}

	@Override
	public int getInstanceNumber() {
		return 0;
	}

	@Override
	public int getFireCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
