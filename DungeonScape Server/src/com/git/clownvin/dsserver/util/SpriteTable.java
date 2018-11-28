package com.git.clownvin.dsserver.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class SpriteTable {
	private final Hashtable<String, Integer> spriteTable = new Hashtable<>();
	
	public SpriteTable() {
		loadSpriteTable();
	}
	
	private void loadSpriteTable() {
		System.out.println("Loading sprite lookup table...");
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/cfg/sprites.cfg"))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				spriteTable.put(tokens[0], Integer.parseInt(tokens[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished loading sprite lookup table");
	}
	
	public Integer getSprite(String name) {
		return spriteTable.get(name);
	}
}
