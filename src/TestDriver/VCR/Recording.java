package TestDriver.VCR;
import TestDriver.Vcr;
import java.util.Date;

public class Recording extends TapePositionAlteringState {
	private Recording(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int tapeToMove = Tape.getMaxPosition() - tape.getPosition();
		int msecRemaining = msecToSec * tapeToMove / NormalSpeed;
		setTask(new TimedTask(this)); Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		getTimer().schedule(getTask(), msecRemaining);
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new Recording("Recording", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = (int)(NormalSpeed * (finalTime - getEventStartTime()) / msecToSec);
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
	}
	
	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		tape.putBackCassette();
		vcr.inceaseNumTimesTapePulled();
		return FastRew.Instance(vcr, tape);
	}

	public VcrState PauseButton() {
		calculateNewTapePosition();
		return RecPaused.Instance();
	}
}
