package TestDriver;

public class CommentExtractorCaseStudy extends CaseStudy {


	enum States {
		ONE, TWO, THREE, FOUR
	};

	private States classState;

	private StringBuffer buffer = new StringBuffer();
	private String originalString;
	private int index  = 0;
	
	public CommentExtractorCaseStudy() {
	}

	public void start() {
		classState = States.ONE;
		if(index > 0 )logMe();
		
		char ch = getNextChar();
		if(ch != 0) {
			ignore(ch);
		}
	}
	
	public void ignore(char ch) {
		if(classState == States.ONE && (ch == '*' || ch != '/')) {
			start();
			
		} else if(classState == States.ONE && ch == '/') {
			classState = States.TWO;
			logMe();
			char newChar = getNextChar();
			if(newChar == '*') {
				emptyBuffer();
			} else if(newChar != 0){
				ignore(newChar);
			}
			
		} else if(classState == States.TWO && (ch != '*' || ch == '/')) {
			start();
		}
	}
	
	public void emptyBuffer() {
		classState = States.THREE;
		logMe();
		
		if(buffer.length() > 0)
			buffer.delete(0, buffer.length());
		
		concatBuffer(getNextChar());
	}
	
	public void concatBuffer(char ch) {
		
		if(ch == 0)
			return;
		
		 if(classState == States.THREE && (ch != '*' || ch == '/')) {
			 classState = States.THREE;
			 logMe();
			 buffer.append(ch);
			 concatBuffer(getNextChar());
		 } else  if(classState == States.THREE && (ch == '*')) {
			 classState = States.FOUR;
			 logMe();
			 buffer.append(ch);
			 concatBuffer(getNextChar());
		 } else if(classState == States.FOUR && (ch == '*')) {
			 classState = States.FOUR;
			 logMe();
			 buffer.append(ch);
			 concatBuffer(getNextChar());
		 } else if(classState == States.FOUR && (ch != '*' && ch != '/')) {
			 classState = States.THREE;
			 logMe();
			 buffer.append(ch);
			 concatBuffer(getNextChar());
		 } else if(classState == States.FOUR && ch == '/') {
			 classState = States.ONE;
			 deconcatBuffer();
		 }
	}
	
	public void deconcatBuffer() {
		if(buffer.length() > 0) 
			buffer.deleteCharAt(buffer.length()-1);
		print();
	}
	
	public void print() {
		System.out.println(buffer.toString());
		start();
	}

	private void logMe() {
		System.out.println(getCurrentState());
	}
	
	
	private char getNextChar() {
		char ch = 0;
		
		if (index < originalString.length()) {
			ch = originalString.charAt(index++);
		}
		return ch;
	}
	
	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return classState.toString();
	}


	@Override
	public void setCurrentState() {
		currentState = classState.toString();
		
	}

	static public void main(String args[]) {
		CommentExtractorCaseStudy c = new CommentExtractorCaseStudy();
		c.originalString = "amr";
		c.start();
		System.out.println("----------------");
		c.originalString = "a*r";
		c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "a/r";
		c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "a/*";
		c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "/*a";
		c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "/a*";
		c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "*/a";c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "a*/";c.index=0; 
		c.start();
		System.out.println("----------------");
		c.originalString = "*a/";c.index=0; 
		c.start();
		System.out.println("----------------");
		c.originalString = "*a//";c.index=0;
		c.start();
		System.out.println("----------------");
		c.originalString = "**a/";c.index=0; 
		c.start();
		System.out.println("----------------");
		c.originalString = "/**/amr";c.index=0; 
		c.start();
		System.out.println("----------------");
		c.originalString = "amr/**/";c.index=0; 
		c.start();
		System.out.println("----------------");
		c.originalString = "r/*amr*/b";c.index=0;
		c.start();
		System.out.println("----------------");
	}
}
