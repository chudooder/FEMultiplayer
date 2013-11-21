package chu.engine;

import java.util.Comparator;

public class SortByUpdate implements Comparator<Entity> {

	public SortByUpdate() {

	}

	@Override
	public int compare(Entity arg0, Entity arg1) {
		if(arg0.updatePriority == arg1.updatePriority){ 
			return arg0.hashCode()-arg1.hashCode();
		}
		return arg0.updatePriority - arg1.updatePriority;
	}

}