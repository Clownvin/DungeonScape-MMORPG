package com.git.clownvin.dsserver.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.git.clownvin.math.MathUtil;

public final class Generator {
	
	//public static final int MAX_VALUE = 100;
	
	protected static char[] toChars(float[][] floats) {
		char[] chars = new char[floats.length * floats[0].length * 4];
		int i = 0;
		int l;
		for (int j = 0; j < floats.length; j++) {
			for (int k = 0; k < floats[j].length; k++) {
				l = Float.floatToIntBits(floats[j][k]);
				chars[i++] = (char) ((l >> 24) & 0xFF);
				chars[i++] = (char) ((l >> 16) & 0xFF);
				chars[i++] = (char) ((l >> 8) & 0xFF);
				chars[i++] = (char) (l & 0xFF);
			}
		}
		return chars;
	}
	
	protected static float[][] fromChars(char[] chars) {
		int width = (int) Math.sqrt(chars.length / 4.0f);
		float[][] floats = new float[width][width];
		int i = 0;
		for (int x = 0; x < floats.length; x++) {
			for (int y = 0; y < floats[x].length; y++) {
				floats[x][y] = Float.intBitsToFloat(((chars[i++] & 0xFF) << 24) | ((chars[i++] & 0xFF) << 16) | ((chars[i++] & 0xFF) << 8) | (chars[i++] & 0xFF));
			}
		}
		return floats;
	}
	
	public static void generateMap(int width, int height, String name) {
		long id = System.currentTimeMillis();
		new File("./data/maps/"+name+"/").mkdirs();
		//float[][] normalized =Generator.normalize( Generator.normalize(Generator.normalize(Generator.generateHeightMap(10, 1000, (int) (Math.random() * 50000)))));
		//try (BufferedWriter writer = new BufferedWriter(new FileWriter("./data/maps/"+id+"/"+id+".normalized"))) {
		//	writer.write(toChars(normalized));
		//	writer.close();
		//} catch (IOException e1) {
		//	e1.printStackTrace();
		//}
		ArrayList<Character> characters = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/maps/"+name+"/"+name+".normalized"))){
			char c;
			while ((c = (char) reader.read()) != (char) -1)
				characters.add(c);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		char[] chars = new char[characters.size()];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = characters.get(i);
		}
		float[][] normalized = fromChars(chars);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./data/maps/"+name+"/"+name+".map"))) {
			writer.write("#x "+(-(width / 2)));
			writer.newLine();
			writer.write("#y "+(-(height / 2)));
			writer.newLine();
			writer.write("#entx 0");
			writer.newLine();
			writer.write("#enty 0");
			writer.newLine();
			writer.write("#width "+width);
			writer.newLine();
			writer.write("#height "+height);
			writer.newLine();
			writer.write("#def water 3");
			writer.newLine();
			writer.write("#def sand 24");
			writer.newLine();
			writer.write("#def dirt 2");
			writer.newLine();
			writer.write("#def grass 1");
			writer.newLine();
			writer.write("#def gravel 25\n");
			writer.write("#def tgrass 21\n");
			writer.write("#def flowers1 22\n");
			float grassRes = .05f;
			float dirtRes = 0.0f;
			float waterRes = 1.0f;
			int offX = (normalized.length - (width)) / 2;
			int offY = (normalized.length - (height)) / 2;
			boolean[][] hastree = new boolean[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					hastree[i][j] = false;
				}
			}
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int x = i - (width / 2);
					int y = j - (height / 2);
					if (MathUtil.distance(0, 0, x, y) < 20) {
						writer.write("grass "+x+" "+y+" "+grassRes+"\n");
						continue;
					}
						
					int v = (int) (normalized[i + offX][j + offY] * 10);
					if (v < 0)
						v = -v;
					outer: switch (v) {
					case 0:
					case 1:
						writer.write("water "+x+" "+y+" "+waterRes+"\n");
						break;
					case 2:
						writer.write("water "+x+" "+y+" "+waterRes+"\n");
						break;
					case 3:
						writer.write("sand "+x+" "+y+" "+0.25f+"\n");
						break;
					case 4:
					case 5:
						writer.write("grass "+x+" "+y+" "+grassRes+"\n");
						for (int k = i - 2; k < i + 3; k++) {
							for (int l = j - 2; l < j + 3; l++) {
								if (l < 0 || l >= height || k < 0 || k >= width)
									continue;
								if (hastree[k][l])
									break outer;
							}
						}
						if (Math.random() * 1000 < 25) {
							writer.write("#object "+(int)(Math.random() * 7)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						}
						break;
					case 6:
					case 7:
						writer.write("grass "+x+" "+y+" "+grassRes+"\n");
						for (int k = i - 2; k < i + 3; k++) {
							for (int l = j - 2; l < j + 3; l++) {
								if (l < 0 || l >= height || k < 0 || k >= width)
									continue;
								if (hastree[k][l])
									break outer;
							}
						}
						if (Math.random() * 1000 < 15) {
							writer.write("#object "+(int)(Math.random() * 7)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						} else if (Math.random() * 1000 < 45) {
							writer.write("#object "+((int)(Math.random() * 4)+11)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						} else if (Math.random() * 1000 < 2) {
							writer.write("#object "+((int)(Math.random() * 2)+8)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						}
						break;
					case 8:
					case 9:
						writer.write("grass "+x+" "+y+" "+grassRes+"\n");
						for (int k = i - 2; k < i + 3; k++) {
							for (int l = j - 2; l < j + 3; l++) {
								if (l < 0 || l >= height || k < 0 || k >= width)
									continue;
								if (hastree[k][l])
									break outer;
							}
						}
						if (Math.random() * 1000 < 5) {
							writer.write("#object "+(int)(Math.random() * 7)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						} else if (Math.random() * 1000 < 50) {
							writer.write("#object "+((int)(Math.random() * 4)+11)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						} else if (Math.random() * 1000 < 1) {
							writer.write("#object "+((int)(Math.random() * 2)+8)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						}
						break;
					case 10:
						writer.write("grass "+x+" "+y+" "+grassRes+"\n");
						for (int k = i - 2; k < i + 3; k++) {
							for (int l = j - 2; l < j + 3; l++) {
								if (l < 0 || l >= height || k < 0 || k >= width)
									continue;
								if (hastree[k][l])
									break outer;
							}
						}
						if (Math.random() * 1000 < 75) {
							writer.write("#object "+((int)(Math.random() * 4)+11)+" "+x+" "+y+"\n");
							hastree[i][j] = true;
						}
						break;
					default:
						System.out.println("No case for "+v);
					}
					//writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage image = new BufferedImage(normalized.length, normalized.length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < normalized.length; x++) {
			for (int y = 0; y < normalized.length; y++) {
				float val = normalized[x][y];
				
				//image.setRGB(x, y, ((int)(val * 255) << 16) | ((int)(val * 255) << 8) | ((int)(val * 255)));
				int c = (int) (val * 10);
				Color color = Color.WHITE;
				switch (c) {
				case 0:
					color = Color.BLUE.darker();
					break;
				case 1:
					color = Color.BLUE;
					break;
				case 2:
					color = Color.BLUE.brighter();
					break;
				case 3:
					color = Color.GREEN.darker();
					break;
				case 4:
					color = Color.GREEN;
					break;
				case 5:
					color = Color.GREEN.brighter();
					break;
				case 6:
					color = Color.YELLOW;
					break;
				case 7:
					color = Color.ORANGE;
					break;
				case 8:
					color = Color.RED;
					break;
				case 9:
					color = Color.MAGENTA;
					break;
				}
				image.setRGB(x, y, color.getRGB());
				//System.out.print("{"+(int)(heightMap[x][y] * 9)+"}");
			}
			//System.out.println("");
		}
		try {
			ImageIO.write(image, "png", new File("./data/maps/"+name+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[][] generateHeightmap(final int size, final int maxValue, final double randomInsertChance, final float roughness, final double mutationChance, boolean islands) {
		if (roughness > 1.0f || roughness < 0.0f) {
			throw new IllegalArgumentException("Roughness must be a floating point value between 0.0f and 1.0f");
		}
		if (randomInsertChance > 1.0f || randomInsertChance < 0.0f) {
			throw new IllegalArgumentException("Random Insert Chance must be a floating point value between 0.0f and 1.0f");
		}
		int w = (int) (Math.pow(2, size) + 1); //W for width
		int[][] map = new int[w][w];
		if (randomInsertChance >= 0.0f)
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < w; y++) {
				if (Math.random() < randomInsertChance) {
					map[x][y] = (int) (Math.random() * maxValue);
				} else {
					map[x][y] = 0;
				}
			}
		}
		if (!islands) { //Set up "borders"
			map[0][0] += Math.random() * maxValue;
			map[0][w - 1] += Math.random() * maxValue;
			map[w - 1][0] += Math.random() * maxValue;
			map[w - 1][w - 1] += Math.random() * maxValue;
		} else {
			int v = w/4;
			map[w/2][w/2] += (Math.random() * maxValue / 2) + (maxValue / 2);
			map[v][v] += Math.random() * maxValue / 2;
			map[v*3][v] += Math.random() * maxValue / 2;
			map[v][v*3] += Math.random() * maxValue / 2;
			map[v*3][v*3] += Math.random() * maxValue / 2;
		}
		diamondStep(map, w / 2, (int) (roughness * maxValue), mutationChance);
		return map;
	}
	
	//TOTALLY not ripped straight from stack overflow.
	//Credit to M. Jessup @ https://stackoverflow.com/questions/2755750/diamond-square-algorithm
	//I tried, cmon. It wasn't bad, but this one is beautiful. No recursion, how about that.
	public static float[][] generateHeightMap(final int size, final int seed, final int range) {
		//size of grid to generate, note this must be a
		//value 2^n+1
		final int DATA_SIZE = (int) (Math.pow(2, size) + 1);
		float[][] data = new float[DATA_SIZE][DATA_SIZE];
		//seed the data
		data[0][0] = data[0][DATA_SIZE-1] = data[DATA_SIZE-1][0] = 
		  data[DATA_SIZE-1][DATA_SIZE-1] = seed;

		double h = range;//the range (-h -> +h) for the average offset
		Random r = new Random();//for the new value in range of h
		//side length is distance of a single square side
		//or distance of diagonal in diamond
		for(int sideLength = DATA_SIZE-1;
		    //side length must be >= 2 so we always have
		    //a new value (if its 1 we overwrite existing values
		    //on the last iteration)
		    sideLength >= 2;
		    //each iteration we are looking at smaller squares
		    //diamonds, and we decrease the variation of the offset
		    sideLength /=2, h/= 2.0){
		  //half the length of the side of a square
		  //or distance from diamond center to one corner
		  //(just to make calcs below a little clearer)
		  int halfSide = sideLength/2;

		  //generate the new square values
		  for(int x=0;x<DATA_SIZE-1;x+=sideLength){
		    for(int y=0;y<DATA_SIZE-1;y+=sideLength){
		      //x, y is upper left corner of square
		      //calculate average of existing corners
		      double avg = data[x][y] + //top left
		      data[x+sideLength][y] +//top right
		      data[x][y+sideLength] + //lower left
		      data[x+sideLength][y+sideLength];//lower right
		      avg /= 4.0;

		      //center is average plus random offset
		      data[x+halfSide][y+halfSide] = 
		    (float) (//We calculate random value in range of 2h
		    //and then subtract h so the end value is
		    //in the range (-h, +h)
		    avg + (r.nextDouble()*2*h) - h);
		    }
		  }

		  //generate the diamond values
		  //since the diamonds are staggered we only move x
		  //by half side
		  //NOTE: if the data shouldn't wrap then x < DATA_SIZE
		  //to generate the far edge values
		  for(int x=0;x<DATA_SIZE-1;x+=halfSide){
		    //and y is x offset by half a side, but moved by
		    //the full side length
		    //NOTE: if the data shouldn't wrap then y < DATA_SIZE
		    //to generate the far edge values
		    for(int y=(x+halfSide)%sideLength;y<DATA_SIZE-1;y+=sideLength){
		      //x, y is center of diamond
		      //note we must use mod  and add DATA_SIZE for subtraction 
		      //so that we can wrap around the array to find the corners
		      double avg = 
		        data[(x-halfSide+DATA_SIZE)%DATA_SIZE][y] + //left of center
		        data[(x+halfSide)%DATA_SIZE][y] + //right of center
		        data[x][(y+halfSide)%DATA_SIZE] + //below center
		        data[x][(y-halfSide+DATA_SIZE)%DATA_SIZE]; //above center
		      avg /= 4.0;

		      //new value = average plus random offset
		      //We calculate random value in range of 2h
		      //and then subtract h so the end value is
		      //in the range (-h, +h)
		      avg = avg + (r.nextDouble()*2*h) - h;
		      //update value for center of diamond
		      data[x][y] = (float) avg;

		      //wrap values on the edges, remove
		      //this and adjust loop condition above
		      //for non-wrapping values.
		      if(x == 0)  data[DATA_SIZE-1][y] = (float) avg;
		      if(y == 0)  data[x][DATA_SIZE-1] = (float) avg;
		    }
		  }
		}
		return data;
	}
	
	private static int[][] diamondStep(int[][] map, final int step, final int roughness, final double mutationChance) {
		for (int x = 0; x < map.length; x += step) {
			if (x - step < 0 || x + step >= map.length)
				continue;
			for (int y = 0; y < map.length; y += step) {
				if (y - step < 0 || y + step >= map.length || map[x][y] != 0)
					continue;
				map[x][y] = (map[x - step][y - step] + map[x + step][y - step] + map[x - step][y + step] + map[x + step][y + step]) / 4;
				if (Math.random() > mutationChance)
					continue;
				if (Math.random() < .5) {
					map[x][y] += (int) (Math.random() * roughness);
				} else {
					map[x][y] -= (int) (Math.random() * roughness);
				}
			}
		}
		return squareStep(map, step, roughness, mutationChance);
	}
	
	private static int[][] squareStep(int[][] map, final int step, final int roughness, final double mutationChance) {
		for (int x = 0; x < map.length; x += step) {
			for (int y = 0; y < map.length; y += step) {
				if (map[x][y] != 0)
					continue;
				int i = 0;
				if (x - step >= 0) {
					i++;
					map[x][y] += map[x - step][y];
				}
				if (x + step < map.length) {
					i++;
					map[x][y] += map[x + step][y];
				}
				if (y - step >= 0) {
					i++;
					map[x][y] += map[x][y - step];
				}
				if (y + step < map.length) {
					i++;
					map[x][y] += map[x][y + step];
				}
				map[x][y] /= i;
				if (Math.random() > mutationChance)
					continue;
				if (Math.random() < .5) {
					map[x][y] += (int) (Math.random() * roughness);
				} else {
					map[x][y] -= (int) (Math.random() * roughness);
				}
			}
		}
		if (step == 1) {
			return map;
		} else {
			return diamondStep(map, step / 2, roughness, mutationChance);
		}
	}
	
	public static int[][] simulateRain(int[][] map, int raindrops) {
		int x = 0;// (int) (Math.random() * (map.length - 1));
		int y = 0;//(int) (Math.random() * (map.length - 1));
		class Coords {
			int x, y;
			Coords(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}
		ArrayList<Coords> coords = new ArrayList<>(8);
		ArrayList<Coords> toCarve = new ArrayList<>(10000);
		int lowest = 0;
		for (int i = 0; i < raindrops; i++) {
			x = (int) (Math.random() * (map.length - 1));
			y = (int) (Math.random() * (map.length - 1));
			lowest = map[x][y];
			for (int j = 0; j < 10000; j++) {
				coords.clear();
				for (int x2 = x - 1; x2 < x + 2; x2++) {
					if (x2 < 0 || x2 >= map.length)
						continue;
					for (int y2 = y - 1; y2 < y + 2; y2++) {
						if (y2 < 0 || y2 >= map.length || (x2 == x && y2 == y))
							continue;
						if (map[x2][y2] < lowest) {
							lowest = map[x2][y2];
							coords.clear();
							coords.add(new Coords(x2, y2));
						} else if (map[x2][y2] == lowest) {
							coords.add(new Coords(x2, y2));
						}
					}
				}
				if (coords.isEmpty())
					break;
				toCarve.add(new Coords(x, y));
				Coords next = coords.get((int) (Math.random() * (coords.size() - 1)));
				x = next.x;
				y = next.y;
			}
			for (Coords c : toCarve) {
				map[c.x][c.y] -= 1;
				if (map[c.x][c.y] < 0)
					map[c.x][c.y] = 0;
			}
			toCarve.clear();
		}
		return map;
	}
	
	public static float[][] normalize(int[][] map) {
		float largestValue = 0;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				if (map[x][y] <= largestValue)
					continue;
				largestValue = map[x][y];
			}
		}
		float[][] normalized = new float[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				normalized[x][y] = map[x][y] / largestValue;
			}
		}
		return normalized;
	}
	
	public static int[][] normalizeToInts(int[][] map, final int maxValue) {
		float[][] normalized = normalize(map);
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				map[x][y] = (int) (normalized[x][y] * maxValue);
			}
		}
		return map;
	}
	
	
	
	public static float[][] normalize(float[][] map) {
		float largestValue = 0;
		float bump = 0;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				if (map[x][y] < 0 && -map[x][y] > bump)
					bump = -map[x][y];
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				if (map[x][y] + bump <= largestValue)
					continue;
				largestValue = map[x][y] + bump;
			}
		}
		float[][] normalized = new float[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				normalized[x][y] = (bump + map[x][y]) / largestValue;
			}
		}
		return normalized;
	}
	
	public static float[][] randomSmooth(float[][] map) {
		float[][] original = new float[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				original[x][y] = map[x][y];
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				int i = 0;
				double val = 0.0f;
				while (i < 9) {
					int x2 = (int) (x + (Math.random() < .5 ? Math.random() * 2 : Math.random() * -2));
					int y2 = (int) (y + (Math.random() < .5 ? Math.random() * 2 : Math.random() * -2));
					if (x2 < 0 || y2 < 0 || x2 >= map.length || y2 >= map.length)
						continue;
					val += original[x2][y2];
					i++;
				}
				map[x][y] = (float) (val / i);
			}
		}
		return map;
	}
	
	public static float[][] smooth(float[][] map) {
		float[][] original = new float[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				original[x][y] = map[x][y];
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				int i = 0;
				float val = 0;
				for (int x2 = x - 1; x2 < x + 2; x2++) {
					if (x2 < 0 || x2 >= map.length)
						continue;
					for (int y2 = y - 1; y2 < y + 2; y2++) {
						if (y2 < 0 || y2 >= map.length || (x2 == x && y2 == y))
							continue;
						val += original[x2][y2];
						i++;
					}
				}
				map[x][y] = val / i;
			}
		}
		return map;
	}
	
	public static int[][] randomSmooth(int[][] map) {
		int[][] original = new int[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				original[x][y] = map[x][y];
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				int i = 0;
				int val = 0;
				while (i < 9) {
					int x2 = (int) (x + (Math.random() < .5 ? Math.random() * 2 : Math.random() * -2));
					int y2 = (int) (y + (Math.random() < .5 ? Math.random() * 2 : Math.random() * -2));
					if (x2 < 0 || y2 < 0 || x2 >= map.length || y2 >= map.length)
						continue;
					val += original[x2][y2];
					i++;
				}
				map[x][y] = val / i;
			}
		}
		return map;
	}
	
	public static int[][] smooth(int[][] map) {
		int[][] original = new int[map.length][map.length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				original[x][y] = map[x][y];
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map.length; y++) {
				int i = 0;
				int val = 0;
				for (int x2 = x - 1; x2 < x + 2; x2++) {
					if (x2 < 0 || x2 >= map.length)
						continue;
					for (int y2 = y - 1; y2 < y + 2; y2++) {
						if (y2 < 0 || y2 >= map.length || (x2 == x && y2 == y))
							continue;
						val += original[x2][y2];
						i++;
					}
				}
				map[x][y] = val / i;
			}
		}
		return map;
	}
	
	public static float[][] simulateRain(float[][] map, int raindrops) {
		int x = 0;// (int) (Math.random() * (map.length - 1));
		int y = 0;//(int) (Math.random() * (map.length - 1));
		class Coords {
			int x, y;
			Coords(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}
		ArrayList<Coords> coords = new ArrayList<>(9);
		float lowest = 0.0f;
		for (int i = 0; i < raindrops; i++) {
			x = (int) (Math.random() * (map.length - 1));
			y = (int) (Math.random() * (map.length - 1));
			for (int j = 0; j < 1000; j++) {
				lowest = map[x][y];
				map[x][y] *= .95f;
				coords.clear();
				for (int x2 = x - 1; x2 < x + 2; x2++) {
					if (x2 < 0 || x2 >= map.length)
						continue;
					for (int y2 = y - 1; y2 < y + 2; y2++) {
						if (y2 < 0 || y2 >= map.length || (x2 == x && y2 == y))
							continue;
						if (map[x2][y2] < lowest) {
							lowest = map[x2][y2];
							coords.clear();
							coords.add(new Coords(x2, y2));
						} else if (map[x2][y2] == lowest) {
							coords.add(new Coords(x2, y2));
						}
					}
				}
				if (lowest <= 0.0 || coords.isEmpty())
					break;
				Coords next = coords.get((int) (Math.random() * (coords.size() - 1)));
				x = next.x;
				y = next.y;
			}
		}
		return map;
	}
}
