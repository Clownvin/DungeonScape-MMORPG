package com.git.clownvin.dsclient.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.git.clownvin.dsclient.texture.Textures;

public class InventorySlot extends Actor {
	public static final int EMPTY_INVENTORY_BOX = 16;
	
	private TextureRegion empty;
	
	
	public final int index;
	private int id = -1;
	//private String name;
	//private long amount;
	private Label nameLabel;
	private Label amountLabel;
	
	public InventorySlot(int index, Skin skin) {
		empty = Textures.getTextureRegion(EMPTY_INVENTORY_BOX);
		setWidth(64);
		setHeight(64);
		this.index = index;
		nameLabel = new Label("", skin);
		amountLabel = new Label("", skin);
		amountLabel.setFontScale(1.5f);
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		nameLabel.setText(name);
	}
	
	public void setAmount(long amount) {
		System.out.println("Amount: "+amount);
		if (amount >= 1_000_000_000_000L) {
			amountLabel.setText((amount / 1_000_000_000_000L)+"T");
			amountLabel.setColor(Color.PURPLE);
		} else if (amount >= 1_000_000_000L) {
			amountLabel.setText((amount / 1_000_000_000L)+"B");
			amountLabel.setColor(Color.GOLD);
		} else if (amount >= 1_000_000L) {
			amountLabel.setText((amount / 1_000_000L)+"M");
			amountLabel.setColor(Color.LIME);
		} else if (amount >= 1_000L) {
			amountLabel.setText((amount / 1_000L)+"K");
			amountLabel.setColor(Color.GREEN);
		} else if (amount > 1) {
			amountLabel.setText(amount+"");
			amountLabel.setColor(Color.WHITE);
		} else {
			amountLabel.setText("");
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//System.out.println("Drawing at: "+getX()+", "+getY()+", w = "+getWidth()+", "+getHeight());
		batch.draw(empty, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		if (id == -1)
			return;
		batch.draw(Textures.getItemTexture(id), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		amountLabel.setPosition(getX(), getY() + amountLabel.getGlyphLayout().height);
		amountLabel.draw(batch, parentAlpha);
	}
}
