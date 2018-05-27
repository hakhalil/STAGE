package TestDriver.VCR;

public class VcrLogger {
	String logger;
	
	public VcrLogger() {
		logger = new String();
	}
	
	public void println(String input) {
		//System.out.println(input);
		try {
			if(logger!=null)
				logger = logger.concat(input + "\n");
//		logger = logger.concat(" " + input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return logger;
	}
}
