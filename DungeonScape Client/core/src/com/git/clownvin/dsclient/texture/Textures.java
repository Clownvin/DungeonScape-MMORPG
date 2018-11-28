package com.git.clownvin.dsclient.texture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public final class Textures {
	
	private static final ObjectMap<Integer, String> texturePaths = new ObjectMap<>();
	private static final ObjectMap<Integer, TextureRegion> texRegions = new ObjectMap<>();
	private static final ObjectMap<String, Texture> textures = new ObjectMap<>();
	private static final ObjectMap<Integer, TextureRegion> itemTextures = new ObjectMap<>();
	
	public static Texture get(final Integer key) {
		return textures.get(texturePaths.get(key));
	}
	
	public static void add(final Integer key) {
		add(key, new Texture(Gdx.files.internal(texturePaths.get(key))));
	}
	
	public static void add(final Integer key, final Texture texture) {
		if (textures.get(texturePaths.get(key)) != null)
			return;
		textures.put(texturePaths.get(key), texture);
	}
	
	public static void disposeAll() {
		for (String key : textures.keys()) {
			textures.get(key).dispose();
		}
		textures.clear();
	}
	
	public static void loadAll() {
		for (Integer key : texturePaths.keys()) {
			add(key);
		}
	}
	
	public static TextureRegion getItemTexture(int id) {
		return itemTextures.get(id);
	}
	
	public static TextureRegion getTextureRegion(int id) {
		return texRegions.get(id);
	}
	
	public static void loadTexturePaths() {
		File file = new File("textures.textures");
		String line = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				if (tokens.length == 2) {
					texturePaths.put(Integer.parseInt(tokens[0]), tokens[1]);
				}
				if (tokens.length == 6) {
					int key = Integer.parseInt(tokens[0]);
					texturePaths.put(key, tokens[1]);
					add(key);
					texRegions.put(key, new TextureRegion(get(key), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file = new File("items.textures");
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				if (tokens.length == 2) {
					itemTextures.put(Integer.parseInt(tokens[0]), texRegions.get(Integer.parseInt(tokens[1])));
				} else {
					System.err.println("Probable error on line: "+line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
