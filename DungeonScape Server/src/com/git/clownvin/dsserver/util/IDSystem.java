package com.git.clownvin.dsserver.util;

import com.git.clownvin.util.CycleQueue;

public class IDSystem {
	
	public final class ID {
		private final int id;
		private boolean used = false;
		
		private ID(final int id) {
			this.id = id;
		}
		
		public int getID() {
			if (!used)
				throw new IllegalStateException("Cannot get ID from ID that is not in use");
			return id;
		}
		
		public void dispose() {
			if (!used)
				throw new IllegalStateException("Cannot dispose an already disposed ID");
			used = false;
			ids.add(this);
		}
	}
	
	private final CycleQueue<ID> ids;
	
	public IDSystem(final int idCount) {
		System.out.println("Generating "+idCount+" ids...");
		ids = new CycleQueue<ID>(idCount);
		for (int i = 0; i < idCount; i++) {
			ids.add(new ID(i));
		}
		System.out.println("Finished generating ids");
	}
	
	public ID getNextID() {
		ID next = ids.remove();
		next.used = true;
		return next;
	}
}
