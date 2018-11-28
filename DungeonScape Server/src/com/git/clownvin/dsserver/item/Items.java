package com.git.clownvin.dsserver.item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import com.git.clownvin.dsapi.item.Item;

public class Items {
	private static Hashtable<Integer, ItemDefinition> definitions = new Hashtable<>();
	public static final ItemDefinition NULL_DEFINITION = new ItemDefinition("", "", Item.NULL_IID, false, 1, false, 0);
	public static final ServerItem NULL_ITEM = new ServerItem(Item.NULL_IID, 1);
	
	public static void loadItemDefinitions() {
		System.out.println("Loading item definitions...");
		int count = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/cfg/item.cfg"))) {
			String line;
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				lineCount++;
				if (line.startsWith("//"))
					continue;
				String[] tokens = line.split(" ");
				if (tokens.length < 7) {
					System.err.println("Unusual token length on line "+lineCount+": "+line);
					continue;
				}
				String name = tokens[0].replaceAll("_", " ");
				String plural = tokens[1].replaceAll("_", " ");
				int iid = Integer.parseInt(tokens[2]);
				boolean stackable = Boolean.parseBoolean(tokens[3]);
				long maxStack = tokens[4].equalsIgnoreCase("max") ? 999_999_999_999_999L : Long.parseLong(tokens[4]);
				boolean equipable = Boolean.parseBoolean(tokens[5]);
				int equipSlot = Integer.parseInt(tokens[6]);
				definitions.put(iid, new ItemDefinition(name, plural, iid, stackable, maxStack, equipable, equipSlot));
				count++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished loading "+count+" item definitions");
	}
	
	public static ItemDefinition getItemDefinition(int iid) {
		if (iid <= Item.NULL_IID)
			return NULL_DEFINITION;
		return definitions.get(iid);
	}
}
