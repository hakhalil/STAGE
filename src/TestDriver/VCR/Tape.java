package TestDriver.VCR;
import TestDriver.Vcr;
import java.text.DecimalFormat;
public class Tape {
	// Measured in percentages.
	private int tapePosition;
	DecimalFormat formatter = new DecimalFormat("00000");
	// Is the write protection seal broken off on the tape?
	private boolean tapeWriteProtected;
	// Right now the tape position is between 0 and 100 percent.
	private static final int minTapePosition = 0;
	private static final int maxTapePosition = 100;
	// is the tape pulled to drum
	private boolean isTapePulledToDrum = false;

	public Tape() {
		setPosition(defaultPosition());
		setProtection(defaultProtection());
	}
	
	// Assuming the position will be between 0 and 100.
	public Tape(int position, boolean protection) {
		setPosition(position);
		setProtection(protection);
	}

	public void setPosition(int position) {
		Vcr.log.println("(Tape) Last position: " + formatter.format(tapePosition));
		Vcr.log.println("(Tape) New  position: " + formatter.format(position));
		tapePosition = position;
	}
	
	public int getPosition() {
		return tapePosition;
	}

	public boolean isTapePulledToDrum () {
		return isTapePulledToDrum;
	}
	
	public void pullToDrum () {
		Vcr.log.println("Tape is being pulled to Drum");
		isTapePulledToDrum = true;
	}

	public void putBackCassette () {
		Vcr.log.println("Tape is being put back to cassette");
		isTapePulledToDrum = false;
	}
	
	public boolean isProtected() {
		return tapeWriteProtected;
	}
	
	private void setProtection(boolean protection) {
		tapeWriteProtected = protection;
	}
	
	public void setMaxPosition() {
		setPosition(maxTapePosition);
	}
	
	static public int getMaxPosition() {
		return maxTapePosition;
	
	}
	
	public void setMinPosition() {
		setPosition(minTapePosition);
	}
	
	static public int getMinPosition() {
		return minTapePosition;
	}

	private boolean defaultProtection() {
		return false;
	}
	
	private int defaultPosition() {
		return 0;
	}
}
