package Utils;
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
	
	public TimerHandler(){
		timers = new LinkedHashMap<>();
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
		timers.put(key, System.nanoTime()/1000000 - last);
		last = System.nanoTime()/1000000;
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
		
		for (Entry<String, Long> entry : timers.entrySet()) {
		    String key = entry.getKey();
		    long value = entry.getValue();
		    sum += value;
		    
		    print += key + " : " + value + "\n";
		}
		
		print += "Benchmark : " + sum;
		return print;
	}
}
