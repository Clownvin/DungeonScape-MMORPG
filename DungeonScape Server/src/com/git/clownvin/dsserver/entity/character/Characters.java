package com.git.clownvin.dsserver.entity.character;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.git.clownvin.dsserver.entity.character.npc.NPCDefinition;

public final class Characters {
	
	private volatile int count;
	
	public Hashtable<Integer, ServerCharacter> characters = new Hashtable<>();
	private Hashtable<Integer, NPCDefinition> npcDefinitions = new Hashtable<>();
	
	public Characters() {
		loadNPCDefinitions();
	}
	
	private void loadNPCDefinitions() {
		System.out.println("Loading character definitions...");
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/cfg/npc.cfg"))) {
			String line;
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				lineCount++;
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				if (tokens.length < 11) {
					System.err.println("Unusual token length on line "+lineCount+": "+line);
					continue;
				}
				int nid = Integer.parseInt(tokens[0]);
				String name = tokens[1].replaceAll("_", " ");
				String sprite = tokens[2];
				float width = Float.parseFloat(tokens[3]);
				float height = Float.parseFloat(tokens[4]);
				float radius = Float.parseFloat(tokens[5]);
				float damageReduction = Float.parseFloat(tokens[6]);
				int fireRate = Integer.parseInt(tokens[7]);
				byte affiliation = Byte.parseByte(tokens[8]);
				float aggressionRadius = Float.parseFloat(tokens[9]);
				String pattern = tokens[10];
				System.out.println("Loaded definition: "+name);
				npcDefinitions.put(nid, new NPCDefinition(nid, name, sprite, width, height, radius, damageReduction, fireRate, affiliation, aggressionRadius, pattern));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished loading character definitions");
	}
	
	public NPCDefinition getDefinition(Integer nid) {
		return npcDefinitions.get(nid);
	}
	
	public synchronized void add(ServerCharacter character) {
		characters.put(character.getEID(), character);
		//System.out.println("[Characters] Added character: "+character);
		count++;
		//System.out.println("Count: "+count);
	}
	
	public synchronized ServerCharacter get(int id) {
		return characters.get(id);
	}
	
	public int getCount() {
		return count;
	}
	
	public synchronized ServerCharacter remove(ServerCharacter character) {
		count--;
		return characters.remove(character.getEID());
	}
	
	public synchronized void update() {
			ArrayList<ServerCharacter> toRemove = new ArrayList<>();
			for (Integer key : characters.keySet()) {
				ServerCharacter character = characters.get(key);
				character.update();
				if (character.needsRemoval())
					toRemove.add(character);
			}
			for (ServerCharacter character : toRemove) {
				remove(character);
				character.dispose();
			}
	}
}
