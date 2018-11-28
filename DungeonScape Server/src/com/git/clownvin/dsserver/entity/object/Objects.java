package com.git.clownvin.dsserver.entity.object;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Objects {
	private final Hashtable<Integer, ServerGameObject> objects = new Hashtable<>();
	private final Hashtable<Integer, ObjectDefinition> definitions = new Hashtable<>();
	private volatile int count = 0;
	
	public Objects() {
		loadObjectDefinitions();
	}
	
	private void loadObjectDefinitions() {
		System.out.println("Loading object definitions...");
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/cfg/object.cfg"))) {
			String line;
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				lineCount++;
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				if (tokens.length < 8) {
					System.err.println("Unusual token length on line "+lineCount+": "+line);
					continue;
				}
				String name = tokens[0].replaceAll("_", " ");
				int oid = Integer.parseInt(tokens[1]);
				float width = Float.parseFloat(tokens[2]); 
				float height = Float.parseFloat(tokens[3]);
				float spriteWidth = Float.parseFloat(tokens[5]);
				float spriteHeight = Float.parseFloat(tokens[6]);
				float resistance = Float.parseFloat(tokens[7]);
				definitions.put(oid, new ObjectDefinition(name, oid, width, height, tokens[4], spriteWidth, spriteHeight, resistance));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished loading object definitions");
	}
	
	public ObjectDefinition getDefinition(int oid) {
		return definitions.get(oid);
	}
	
	public ObjectDefinition getDefinition(String name) {
		for (int oid : definitions.keySet()) {
			if (definitions.get(oid).name.equalsIgnoreCase(name))
				return definitions.get(oid);
		}
		return null;
	}
	
	public synchronized void add(ServerGameObject object) {
		objects.put(object.getEID(), object);
		count++;
	}
	
	public synchronized ServerGameObject get(int id) {
		return objects.get(id);
	}
	
	public int getCount() {
		return count;
	}
	
	public synchronized Set<Integer> getKeySet() {
		return objects.keySet();
	}
	
	public synchronized ServerGameObject remove(ServerGameObject object) {
		count--;
		return objects.remove(object.getEID());
	}
	
	public synchronized void removeInstanced(int instanceNumber) {
		ArrayList<ServerGameObject> toRemove = new ArrayList<>();
		for (Integer key : objects.keySet()) {
			ServerGameObject object = objects.get(key);
			if (object.getInstanceNumber() != instanceNumber)
				continue;
			toRemove.add(object);
		}
		for (ServerGameObject object : toRemove) {
			remove(object);
			object.dispose();
		}
	}
}
