package fr.unilim.counter;

/**
 * Class represent counter.
 */
public class Counter {
	
	private Integer value;
	
	public Counter(){
		this.reset();
	}
	
	/**
	 * Set Counter = 0
	 */
	public void reset(){
		this.value = 0;
	}
	
	/**
	 * @return Value of Cuunter and increments it;
	 */
	public Integer nextInteger(){
		return this.value++;
	}
	
	/**
	 * @return Value of Cuunter and increments it.
	 */
	public int nextInt(){
		return this.value++;
	}
	
	/**
	 * @return Value of Cuunter and increments it.
	 */
	public String nextString(){
		String str = this.value.toString();
		this.value++;
		return str;
	}

}
