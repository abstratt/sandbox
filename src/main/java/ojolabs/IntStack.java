package ojolabs;

import java.util.LinkedList;
import java.util.List;


/*

  push 5 -> [5] (min: 5)
  push 3 -> [5, 3] (min: 3)
  push 4 -> [5, 3, 4] (min: 3)
  pop -> [5, 3] (min: 3)
  pop -> [5] (min: 5)
  
  
  
  // mins: 
  push 5 -> [5]
  push 3 -> [3, 5]
  push 4 -> [3, 4, 5]
  


 */
public class IntStack {

	private List<Integer> items = new LinkedList<>();
	private List<Integer> historicalMins = new LinkedList<>();
	
	public void push(int value) {
		items.add(value);
		int minimum = items.isEmpty() ? value : Math.min(((int) items.get(items.size()-1)), value);
		historicalMins.add(minimum);
	}
	
	public Integer pop() {
		if (items.isEmpty()) {
			return null;
		}
		return items.remove(items.size()-1);
	}
	
	public Integer getMin() {
		return items.stream().reduce((a, b) -> a < b ? a : b).orElse(null);
	}
	
	public Integer peek() {
		if (items.isEmpty()) {
			return null;
		}
		return items.get(items.size()-1);
	}
}
