package TestDriver.VCR;
import java.util.Date;

import TestDriver.Vcr;

public class Recording extends TapePositionAlteringState {
	private Recording(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int tapeToMove = 0;
		try {
			tapeToMove = Tape.getMaxPosition() - tape.getPosition();
		}catch (Throwable e) {
			System.out.println(e);
		}
		int msecRemaining = 0;
		try {
			if( NormalSpeed!=0)
				msecRemaining = msecToSec * tapeToMove / NormalSpeed;
		} catch (Throwable e) {
			System.out.println(e);
		}
		try {
			TimedTask t = new TimedTask(this);
			setTask(t);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			if( formatter!=null)
			Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(getTimer() != null && getTask() != null)  getTimer().schedule(getTask(), Math.abs(msecRemaining));
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new Recording("Recording", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		if( getTask()!=null)
			getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = 0;

		try {
			if(msecToSec!=0)
				delta = (int)(NormalSpeed * (finalTime - getEventStartTime()) / msecToSec);
		} catch (Throwable e) {
			System.out.println(e);
		}
		int pos = 0;
		try {
			if(getTape()!=null)
				getTape().getPosition();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			if(formatter!=null)
				Vcr.log.println("Tape delta: " + formatter.format(delta));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int z = 0;
		try {
			z = pos + delta;
		} catch (Throwable e) {
			System.out.println(e);
		}
		if( getTape()!=null)
		{
			try {
				if ( (z) > getTape().getMaxPosition() ) {
					getTape().setMaxPosition();
				}
				else
				{
					getTape().setPosition(pos + delta);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// End of tape reached.
	public void tapeExtremeReached() {
		getVcr().processEvent(EndOfTapeEvent.Instance());
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		try {
			if(tape!=null)
				tape.putBackCassette();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(vcr!=null)
				vcr.inceaseNumTimesTapePulled();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FastRew.Instance(vcr, tape);
	}

	public VcrState PauseButton() {
		calculateNewTapePosition();
		return RecPaused.Instance();
	}
}
