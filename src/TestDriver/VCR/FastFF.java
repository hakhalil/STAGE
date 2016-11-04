package TestDriver.VCR;

import java.util.Date;

import TestDriver.Vcr;

public class FastFF extends TapePositionAlteringState {
	private FastFF(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int tapeToMove = tape.getMaxPosition() - tape.getPosition();
		int msecRemaining = msecToSec * tapeToMove / FastestSpeed;
		setTask(new TimedTask(this));
		Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		getTimer().schedule(getTask(), msecRemaining);
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new FastFF("FastFF", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = (int) (FastestSpeed * (finalTime - getEventStartTime()) / msecToSec);
		int pos = getTape().getPosition();
		Vcr.log.println("Tape delta: " + formatter.format(delta));

		if ((pos + delta) > getTape().getMaxPosition()) {
			getTape().setMaxPosition();
		} else {
			getTape().setPosition(pos + delta);
		}
	}

	// End of tape reached.
	public void tapeExtremeReached() {
		getVcr().processEvent(EndOfTapeEvent.Instance());
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		VcrState state =  null;
		try {
			calculateNewTapePosition();
			state = FastRew.Instance(vcr, tape);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return FastRew.Instance(vcr, tape);
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		tape.pullToDrum();
		vcr.inceaseNumTimesTapePulled();
		return Playing.Instance(vcr, tape);
	}
	public VcrState PauseButton() {
		calculateNewTapePosition();
	//	System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
		return Paused.Instance();
	}
}
