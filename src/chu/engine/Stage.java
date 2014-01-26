package chu.engine;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;

public abstract class Stage {
	protected TreeSet<Entity> entities;
	protected Stack<Entity> addStack;
	protected Stack<Entity> removeStack;
	public final String soundTrack;
	
	public Stage(String soundTrack) {
		entities = new TreeSet<Entity>(new SortByUpdate());
		addStack = new Stack<Entity>();
		removeStack = new Stack<Entity>();
		this.soundTrack = soundTrack;
	}
	
	public TreeSet<Entity> getAllEntities() {
		return entities;
	}
	
	public void addEntity(Entity e) {
		addStack.push(e);
		e.willBeRemoved = false;
	}
	
	
	public void removeEntity(Entity e) {
		if(e != null) {
			e.flagForRemoval();
			if(removeStack.contains(e)){
				return;
			}
			removeStack.push(e);
		}
	}
	
	public void update() {
		for(Entity e : entities) {
			e.onStep();
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	public void render() {
		SortByRender comparator = new SortByRender();
		PriorityQueue<Entity> renderQueue = new PriorityQueue<Entity>(entities.size()+1, comparator);
		renderQueue.addAll(entities);
		while(!renderQueue.isEmpty()) {
			renderQueue.poll().render();
		}
	}
	
	public Entity instanceAt(int x, int y) {
		for(Entity e : entities) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) return e;
		}
		return null;
	}
	
	public Entity[] allInstancesAt(int x, int y) {
		ArrayList<Entity> ans = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) ans.add(e);
		}
		
		for(Entity e : addStack) {
			if(e.x == x && e.y == y && !e.willBeRemoved()) ans.add(e);
		}
		
		Entity[] ret = new Entity[ans.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ans.get(i);
		}
		return ret;
	}
	
	public Collidable[] collideableAt(int x, int y) {
		ArrayList<Collidable> ans = new ArrayList<Collidable>();
		for(Entity e : entities) {
			if(e instanceof Collidable && e.x == x && e.y == y && !e.willBeRemoved()) 
				ans.add((Collidable)e);
		}
		
		for(Entity e : addStack) {
			if(e instanceof Collidable && e.x == x && e.y == y && !e.willBeRemoved()) 
				ans.add((Collidable)e);
		}
		
		Collidable[] ret = new Collidable[ans.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ans.get(i);
		}
		return ret;
	}
	
	public void processAddStack() {
		while(!addStack.isEmpty()) {
			Entity e = addStack.pop();
			entities.add(e);
			e.stage = this;
		}
	}
	
	public boolean willBeRemoved(Entity e) {
		return removeStack.contains(e);
	}
	
	public void processRemoveStack() {
		while(!removeStack.isEmpty()) {
			Entity e = removeStack.pop();
			entities.remove(e);
			addStack.remove(e);		//Otherwise some weird shit happens and entities get stuck in limbo
		}
	}

	public abstract void beginStep();

	public abstract void onStep();
	
	public abstract void endStep();
	
}
