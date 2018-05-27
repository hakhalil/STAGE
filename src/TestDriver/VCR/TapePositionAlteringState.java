package TestDriver.VCR;
import java.text.DecimalFormat;
import java.util.Timer;

import TestDriver.Vcr;

public abstract class TapePositionAlteringState extends VcrState {
	private Timer timer; protected DecimalFormat formatter = new DecimalFormat("00000");
	private TimedTask task;
	private long eventStartTime;
	private Tape tape;
	private Vcr vcr;

	//Tape traversal speed (in tape percentage per second)
	//These values or only necessary to the subclasses of this class.
	protected static final int NormalSpeed = 10; //250ms - 400
	protected static final int FasterSpeed = 20; //200ms - 500
	protected static final int FastestSpeed = 40; //100ms - 1000

	//1000 milliseconds in a second
	protected final int msecToSec = 1000;

	protected TapePositionAlteringState(String inID, Vcr inVcr, Tape inTape) {
		super(inID);
		try {
			System.out.println("Creating Timer");
			if( getTimer()!= null)
			{
				getTimer().cancel();
				getTimer().purge();
			}
		//	Timer createdTimer = new Timer();
		//	storeTimer(createdTimer);
		//	setTimer(createdTimer);
		} catch (Throwable e) {
			System.out.println("============> "+e.getMessage());
		}
		System.out.println(" After Creating Timer");
		setEventStartTime(System.currentTimeMillis());
		setVcr(inVcr);
		setTape(inTape);
	}

	void storeTimer( Timer storedTimer)
	{
		Tape.myTimers.add(storedTimer);
	}
	public VcrState EjectButton(Vcr vcr) {
		calculateNewTapePosition();
		try {
			if(vcr!=null)
				vcr.ejectTape();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TapeAbsent.Instance();
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		try {
			calculateNewTapePosition();
			if (tape!=null && tape.isTapePulledToDrum()) {
				tape.putBackCassette();
				vcr.inceaseNumTimesTapePulled();
			}		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Off.Instance();
	}

	public VcrState StopButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		try {
			if (tape!=null &&tape.isTapePulledToDrum()) {
				tape.putBackCassette();
				vcr.inceaseNumTimesTapePulled();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return Stopped.Instance();
	}

	/* Either the beginning or the end of the tape
	   has been reached. */
	public abstract void tapeExtremeReached();

	protected abstract void calculateNewTapePosition();

	// Getter and setter methods.

	protected Timer getTimer() {
		return timer;
	}

	protected void setTimer(Timer inTimer) {
		try {
			if( timer!= null)
			{
				try {
					//timer.cancel();
					//timer.purge();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			timer = inTimer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected TimedTask getTask() {
		return task;
	}

	protected void setTask(TimedTask inTask) {
		try {
			if (task!= null)
				task.cancel();	
			task = inTask;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}

	protected long getEventStartTime() {
		return eventStartTime;
	}

	protected void setEventStartTime(long inTime) {
		eventStartTime = inTime;
	}

	protected Tape getTape() {
		return tape;
	}

	protected void setTape(Tape inTape) {
		tape = inTape;
	}

	protected Vcr getVcr() {
		return vcr;
	}

	protected void setVcr(Vcr inVcr) {
		vcr = inVcr;
	}

	// TESTING METHODS

	public static int getNormalSpeed() {
		return NormalSpeed;
	}

	public static int getFasterSpeed() {
		return FasterSpeed;
	}

	public static int getFastestSpeed() {
		return FastestSpeed;
	}
}
