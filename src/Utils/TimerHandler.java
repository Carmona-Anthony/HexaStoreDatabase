package Utils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Class that handle time management
 * @author
 *
 */
public class TimerHandler {
	/**
	 * Long that contains the start of the timer
	 */
	long start;
	/**
	 * Long that contains the end of the timer
	 */
	long end;
	/**
	 * Last timer added as a tour, start or stop
	 */
	long last;
	/**
	 * list of all difference as <key , difference since last>
	 */
	LinkedHashMap<String,Long> timers;
	
	/**
	 * list of all timers to show
	 */
	ArrayList<Boolean> showValues;
	
	public TimerHandler(){
		timers = new LinkedHashMap<>();
		showValues = new ArrayList<>();
	}
	
	/**
	 * Start the timer and store the start value
	 */
	public void start(){
		start = System.nanoTime()/1000000;
		last = start;
	}
	
	/**
	 * Stop the timer and store the end value
	 */
	public void stop(){
		end = System.nanoTime()/1000000;
		last = end;
	}
	
	/**
	 * Create a new tour stored in the list of timers as <Key, difference since last tour>
	 * @param key A key for retrieve value
	 */
	public void tour(String key){
		
		long currentDiff = System.nanoTime()/1000000 - last;
		timers.put(key, currentDiff);
		last = System.nanoTime()/1000000;
		showValues.add(true);
	}
	
	/**
	 * Create a new tour stored in the list of timers as <Key, difference since last tour> and ask if the value is to display in toString
	 * @param key A key for retrieve value
	 * @param showValue boolean value, true if the value is needed in toString false else
	 */
	public void tour(String key, boolean showValue){
		
		long currentDiff = System.nanoTime()/1000000 - last;
		timers.put(key, currentDiff);
		last = System.nanoTime()/1000000;
		showValues.add(showValue);
	}
	
	/**
	 * Return the difference time for a key value
	 * @param key
	 * @return the difference time
	 */
	public long getTime(String key){
		return timers.get(key);
	}
	
	/**
	 * Get the all time between start and end
	 * @return the time between start and end
	 */
	public long getTime(){
		return end - start;
	}
	
	public String toString(){
		
		String print = "";
		long sum = 0;
		int counter = 0;
		
		for (Entry<String, Long> entry : timers.entrySet()) {
		    String key = entry.getKey();
		    long value = entry.getValue();
		    sum += value;
		    
		    if(showValues.get(counter)) {
		    	  print += key + " : " + value + "\n";	
		    }
		    else print += key + "\n";
		    
		    counter++;
		}
		
		print += "Benchmark : " + sum;
		return print;
	}
}
