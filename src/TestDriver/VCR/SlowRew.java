package TestDriver.VCR;
import java.util.Date;

import TestDriver.Vcr;

public class SlowRew extends TapePositionAlteringState {
	private SlowRew(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
			int msecRemaining = 0;
			try { 
			if( FastestSpeed>0 && tape!=null)
				msecRemaining = msecToSec * tape.getPosition()  / FastestSpeed;
			} catch (Throwable e) {
				System.out.println(e);
			}
			
			if ( msecRemaining < 0 )
				msecRemaining = 0;	
			try {
				TimedTask t = new TimedTask(this);
				if(t!=null)
					setTask(t); 
				if( formatter!=null)
				Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
				if(getTimer() != null && getTask() != null) getTimer().schedule(getTask(), Math.abs(msecRemaining));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new SlowRew("SlowRew", vcr, tape);
	}

	protected void calculateNewTapePosition() {
			if( getTask()!= null)
				getTask().cancel();
			long finalTime = (new Date()).getTime();
			int delta = 0;
			try {
				delta = (int)(FasterSpeed * (finalTime - getEventStartTime()) / msecToSec);
			} catch (Throwable e) {
				System.out.println(e);
			}
			int pos = 0;
			try { 
				if(getTape()!=null)
					pos = getTape().getPosition(); 
				if( formatter!=null)
					Vcr.log.println("Tape delta: " + formatter.format(delta));
			}catch (Throwable e) {
				System.out.println(e);
			}
	
			int z = 0;
			try {
				z = pos - delta;
			} catch (Throwable e) {
				System.out.println(e);
			}
			try {
				if(getTape()!=null)
				{
				if ( (z) < getTape().getMinPosition() ) {
					getTape().setMinPosition();
				}
				else
				{
					getTape().setPosition(pos - delta);
				}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Start of tape reached.
	public void tapeExtremeReached() {
		if(getVcr()!=null)
			try {
				getVcr().processEvent(EndOfTapeEvent.Instance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		try {
			if( tape!=null)
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
		return Stopped.Instance();
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return SlowFF.Instance(vcr, tape);
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return Playing.Instance(vcr, tape);
	}

	public VcrState PauseButton() {
		calculateNewTapePosition();
		System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
		return Paused.Instance();
	}
}
