package chu.engine;

import java.util.Comparator;

public class SortByRender implements Comparator<Entity> {

	public SortByRender() {
		
	}

	@Override
	public int compare(Entity arg0, Entity arg1) {
		if(arg0.renderDepth == arg1.renderDepth){ 
			return arg0.hashCode()-arg1.hashCode();
		} else if(arg0.renderDepth < arg1.renderDepth) {
			return 1;
		} else {
			return -1;
		}
	}

}
