package com.git.clownvin.dsclient.entity.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.git.clownvin.dsapi.entity.object.GameObject;
import com.git.clownvin.dsclient.DSGame;
import com.git.clownvin.dsclient.entity.ClientEntity;
import com.git.clownvin.dsclient.texture.Textures;
import com.git.clownvin.math.MathUtil;

public class ClientGameObject extends ClientEntity implements GameObject {

	private final float spriteWidth, spriteHeight;
	
	public ClientGameObject(DSGame game, int eid, float x, float y, float width, float height, int sprite, float spriteWidth, float spriteHeight) {
		super(game, eid, x, y, width, height, sprite);
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}

	@Override
	public Integer getIX() {
		return MathUtil.ard(getX());
	}

	@Override
	public Integer getIY() {
		return MathUtil.ard(getY());
	}

	@Override
	public void update() {
		//Do nothing?
	}

	@Override
	public void render(SpriteBatch batch, float selfX, float selfY) {
		float x = 0, y = 0;
		x = (getRenderX() - selfX);
		y = (getRenderY() - selfY);
		if (Textures.getTextureRegion(getSprite()) != null) {
			TextureRegion reg = Textures.getTextureRegion(getSprite());
			//batch.setColor(Color.PURPLE);
			batch.draw(reg, x - (spriteWidth / 2) + (getWidth() / 2), y - (spriteHeight / 2) + (getHeight() / 2), spriteWidth, spriteHeight);
			//batch.setColor(Color.WHITE);
			//batch.draw(reg, oX, oY, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1f, 1f, (float) Math.toDegrees(getAngle() + Math.toRadians(90)), 0, 0, (int) Textures.get(getSprite()).getWidth(), (int) Textures.get(getSprite()).getHeight(), false, false);
		} else {
			//System.out.println("Drawing at "+oX+", "+oY);
			batch.draw(Textures.get(getSprite()),  x - (spriteWidth / 2) + (getWidth() / 2), y - (spriteHeight / 2) + (getHeight() / 2), spriteWidth, spriteHeight);
		}
	}

	@Override
	public float getSpriteWidth() {
		return spriteWidth;
	}

	@Override
	public float getSpriteHeight() {
		return spriteHeight;
	}

}
