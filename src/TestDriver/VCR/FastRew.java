package TestDriver.VCR;

import java.util.Date;

import TestDriver.Vcr;

public class FastRew extends TapePositionAlteringState {
	private FastRew(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int msecRemaining = msecToSec * tape.getPosition() / FastestSpeed;
		setTask(new TimedTask(this)); 
		Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		getTimer().schedule(getTask(), msecRemaining);
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new FastRew("FastRew", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = (int)(FastestSpeed * (finalTime - getEventStartTime()) / msecToSec);
		int pos = getTape().getPosition(); Vcr.log.println("Tape delta: " + formatter.format(delta));
		Tape tape = getTape();	
		if (tape!= null)
		{
			if (((pos - delta) < Tape.getMinPosition()) ) {

				tape.setMinPosition();
			}
			else
			{
				getTape().setPosition(pos - delta);
			}
		}
		else{
			System.out.println("Tape is null");
		}
	}
	// Start of tape reached.
	public void tapeExtremeReached() {
		getVcr().processEvent(EndOfTapeEvent.Instance());
		//	System.out.println("In class: " + this.getClass().getName().toString()+ " in tapeExtreame Reached");
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		VcrState state= null;
		System.out.println("In class: " + this.getClass().getName().toString()+ " in EndOFTapeEvent");
		try {
			calculateNewTapePosition();
			state = Stopped.Instance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		//		System.out.println("In class: " + this.getClass().getName().toString()+ " in FFButton");
		VcrState state= null;
		try {
			state = FastFF.Instance(vcr, tape);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		VcrState state= null;
		try {
			calculateNewTapePosition();
			tape.putBackCassette();
			vcr.inceaseNumTimesTapePulled();	
			state= Playing.Instance(vcr, tape);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public VcrState PauseButton() {
		VcrState state= null;
		try {
			calculateNewTapePosition();
			//		System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
			state = Paused.Instance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}
}
