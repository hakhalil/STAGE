package TestDriver.VCR;
import TestDriver.Vcr;
import java.util.Date;

public class SlowFF extends TapePositionAlteringState {
	private SlowFF(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int tapeToMove = tape.getMaxPosition() - tape.getPosition();
		int msecRemaining = msecToSec * tapeToMove / FasterSpeed;
		setTask(new TimedTask(this)); Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		getTimer().schedule(getTask(), msecRemaining);
	
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new SlowFF("SlowFF", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = (int)(FasterSpeed * (finalTime - getEventStartTime()) / msecToSec);
		int pos = getTape().getPosition(); Vcr.log.println("Tape delta: " + formatter.format(delta));

		if ( (pos + delta) > getTape().getMaxPosition() ) {
			getTape().setMaxPosition();
		}
		else
		{
			getTape().setPosition(pos + delta);
		}
	}

	// End of tape reached.
	public void tapeExtremeReached() {
		getVcr().processEvent(EndOfTapeEvent.Instance());
//		System.out.println("In class: " + this.getClass().getName().toString()+ " in tapeExtremeReached");
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		tape.putBackCassette ();
		vcr.inceaseNumTimesTapePulled ();		
//		System.out.println("In class: " + this.getClass().getName().toString()+ " in EndOFTapeEvent");
		return FastRew.Instance(vcr, tape);


	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return SlowRew.Instance(vcr, tape);
		
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return Playing.Instance(vcr, tape);
	}

	public VcrState PauseButton() {
		calculateNewTapePosition();
	//	System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
		return Paused.Instance();
	}
}