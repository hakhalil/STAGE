package TestDriver.VCR;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

import TestDriver.Vcr;
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

	static ArrayList<Timer> myTimers ;
	
	public Tape() {
		purgeTimers();
		setPosition(defaultPosition());
		setProtection(defaultProtection());
	
	}
	
	// Assuming the position will be between 0 and 100.
	public Tape(int position, boolean protection) {
		purgeTimers();
		myTimers = new ArrayList<Timer>();
		setPosition(position);
		setProtection(protection);
	}

	public void purgeTimers()
	{
		try {
			if( myTimers!=null && myTimers.size()!=0)
			{
				for (int i=0; i<myTimers.size();i++)
				{
					if( myTimers.get(i)!=null)
					{
						myTimers.get(i).cancel();
						myTimers.get(i).purge();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setPosition(int position) {
		if( formatter!=null)
		{
		Vcr.log.println("(Tape) Last position: " + formatter.format(tapePosition));
		Vcr.log.println("(Tape) New  position: " + formatter.format(position));
		}
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
