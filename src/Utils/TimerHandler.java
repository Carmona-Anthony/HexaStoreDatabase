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
	double start;
	/**
	 * Long that contains the end of the timer
	 */
	double end;
	/**
	 * Last timer added as a tour, start or stop
	 */
	double last;
	/**
	 * list of all difference as <key , difference since last>
	 */
	LinkedHashMap<String,Double> timers;
	
	/**
	 * list of all timers to show
	 */
	ArrayList<Boolean> showValues;
	
	public TimerHandler(){
		timers = new LinkedHashMap<>();
		showValues = new ArrayList<>();
		start = 0;
	}
	
	/**
	 * Start the timer and store the start value
	 */
	public void start(){
		start = ((double)System.currentTimeMillis());
		last = start;
	}
	
	/**
	 * Stop the timer and store the end value
	 */
	public void stop(){
		end =  ((double)System.currentTimeMillis());
		last = end;
	}
	
	/**
	 * Create a new tour stored in the list of timers as <Key, difference since last tour>
	 * @param key A key for retrieve value
	 */
	public void tour(String key){
		
		double currentDiff =  ((double)System.currentTimeMillis())- last;
		if(currentDiff < 1 ) currentDiff = 1;
		timers.put(key, currentDiff);
		last =  ((double)System.currentTimeMillis());
		showValues.add(true);
	}
	
	/**
	 * Create a new tour stored in the list of timers as <Key, difference since last tour> and ask if the value is to display in toString
	 * @param key A key for retrieve value
	 * @param showValue boolean value, true if the value is needed in toString false else
	 */
	public void tour(String key, boolean showValue){
		
		double currentDiff =  ((double)System.currentTimeMillis()) - last;
		if (currentDiff < 1) currentDiff = 1;
		timers.put(key, currentDiff);
		last =  ((double)System.currentTimeMillis());
		showValues.add(showValue);
	}
	
	/**
	 * Return the difference time for a key value
	 * @param key
	 * @return the difference time
	 */
	public double getTime(String key){
		return timers.get(key);
	}
	
	/**
	 * Get the all time between start and end
	 * @return the time between start and end
	 */
	public double getTime(){
		return end - start;
	}
	/**
	 * Check if timer has already been started
	 * @return true if timer already started else false
	 */
	public boolean isTimerStarted() {
		if(start == 0) {
			return false;
		}
		else return true;
	}
	
	public String toString(){
		
		String print = "";
		double sum = 0;
		int counter = 0;
		
		for (Entry<String, Double> entry : timers.entrySet()) {
		    String key = entry.getKey();
		    double value = entry.getValue();
		    
		    if(showValues.get(counter)) {
		    	  print += key + " : " + value + "\n";	
		    	  sum += value;
		    }
		    else print += key + "\n";
		    
		    counter++;
		}
		
		print += "Benchmark : " + sum;
		return print;
	}
}
