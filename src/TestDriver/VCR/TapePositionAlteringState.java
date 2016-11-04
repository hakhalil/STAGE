package TestDriver.VCR;
import TestDriver.Vcr;
import java.util.Timer;
import java.text.DecimalFormat;

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
		setTimer(new Timer());
		setEventStartTime(System.currentTimeMillis());
		setVcr(inVcr);
		setTape(inTape);
	}

	public VcrState EjectButton(Vcr vcr) {
		calculateNewTapePosition();
		vcr.ejectTape();
		return TapeAbsent.Instance();
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		if (tape.isTapePulledToDrum()) {
			tape.putBackCassette();
			vcr.inceaseNumTimesTapePulled();
		}		
		return Off.Instance();
	}

	public VcrState StopButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		if (tape.isTapePulledToDrum()) {
			tape.putBackCassette();
			vcr.inceaseNumTimesTapePulled();
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
		timer = inTimer;
	}

	protected TimedTask getTask() {
		return task;
	}

	protected void setTask(TimedTask inTask) {
		task = inTask;
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
