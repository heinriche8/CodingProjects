/*
 * Course: SE 2030 021
 * Spring 2021-2022
 * Lab 5 - Classes and Git
 * Name: AfternoonGroup3
 * Created: 4/7/2022
 */

/**
 * + attach(observer) void
 * + detach(observer) void
 * + notify(observer) void
 * @author beattyr
 * @version 1.0
 */
public interface Subject {

	/**
	 * 
	 * @param observer
	 */
	public void attach(Observer observer);

	/**
	 * 
	 * @param observer
	 */
	public void detach(Observer observer);

	public void notifyObservers();

}