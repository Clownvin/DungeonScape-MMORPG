package com.git.clownvin.dsserver.world;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public final class Instances {
	
	public static final int OVERWORLD = 0;
	
	private static final Hashtable<Integer, Instance> instanceMap = new Hashtable<>();
	
	public synchronized static Instance get(int instanceNumber) {
		return instanceMap.get(instanceNumber);
	}
	
	public synchronized static Instance put(Instance instance) {
		instanceMap.put(instance.instanceNumber, instance);
		return instance;
	}
	
	public synchronized static Instance remove(Instance instance) {
		return remove(instance.instanceNumber); 
	}
	
	public synchronized static Instance remove(int instanceNumber) {
		return instanceMap.remove(instanceNumber);
	}
	
	public synchronized static void update() {
		Set<Integer> instances = instanceMap.keySet();
		for (Integer i : instances) {
			instanceMap.get(i).update();
		}
	}

}
