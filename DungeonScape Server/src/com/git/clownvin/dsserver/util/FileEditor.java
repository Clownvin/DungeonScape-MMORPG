package com.git.clownvin.dsserver.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class FileEditor {
	private LinkedList<String> lines = new LinkedList<>();
	
	private int index = 0;
	private String filePath;
	
	public FileEditor(String filePath) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		}
		this.filePath = filePath;
	}
	
	public void save() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (int i = 0; i < lines.size(); i++) {
				writer.write(lines.get(i));
				if (i + 1 < lines.size())
					writer.newLine();
			}
			System.out.println("finished");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getLength() {
		return lines.size();
	}
	
	public String getLine() {
		return lines.get(index);
	}
	
	public void insertLine(String line) {
		lines.add(index, line);
	}
	
	public void addLine(String line) {
		lines.add(line);
	}
	
	public void reset() {
		index = 0;
	}
	
	public String nextLine() {
		if (index + 1 == lines.size())
			return null;
		return lines.get(++index);
	}
	
	public void replaceLine(String replacement) {
		lines.set(index, replacement);
	}
	
	public void deleteLine() {
		lines.remove(index);
	}
}
