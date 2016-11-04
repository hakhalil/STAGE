package TestDriver.VCR;

public class VcrLogger {
	String logger;
	
	public VcrLogger() {
		logger = new String();
	}
	
	public void println(String input) {
		//System.out.println(input);
		logger = logger.concat(input + "\n");
//		logger = logger.concat(" " + input);
	}
	
	public String toString() {
		return logger;
	}
}
