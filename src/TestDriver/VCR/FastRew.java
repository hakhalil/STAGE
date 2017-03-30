package TestDriver.VCR;

import java.util.Date;

import TestDriver.Vcr;

public class FastRew extends TapePositionAlteringState {
	private FastRew(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int msecRemaining = 0;
		try {
			if( FastestSpeed>0 && tape!=null)
				msecRemaining = msecToSec * tape.getPosition()  / FastestSpeed;
		} catch (Throwable e) {
			System.out.println("Dividing by zero since FastestSpeed " + FastestSpeed);
		}
		try {
			if( msecRemaining <0 ) msecRemaining =0;
			if( getTask()!= null)
				getTask().cancel();
			TimedTask task = new TimedTask( this);
			setTask(task); 
			if( formatter!=null)
				Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
			if( getTimer()!=null && getTask()!=null)
				getTimer().schedule(getTask(), msecRemaining);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new FastRew("FastRew", vcr, tape);
	}

	protected void calculateNewTapePosition() {

		if( getTask()!= null)
			getTask().cancel();
		long finalTime = (new Date()).getTime();
		int delta = 0;
		if( msecToSec!=0)
			delta = (int)(FastestSpeed * (finalTime - getEventStartTime()) / msecToSec);
		Tape tape = getTape();	
		if (tape!= null)
		{
			int pos=0;
			try {
				pos = tape.getPosition();
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


			System.out.println("In class: " + this.getClass().getName().toString()+ " calculateNewTapePosition before get min");

			try {
				if (((pos - delta) < Tape.getMinPosition()) ) {

					System.out.println("In class: " + this.getClass().getName().toString()+ " calculateNewTapePosition after get min");

					tape.setMinPosition();
					System.out.println("In class: " + this.getClass().getName().toString()+ " calculateNewTapePosition after set min pos");

				}
				else
				{
					System.out.println("In class: " + this.getClass().getName().toString()+ " calculateNewTapePosition in else 1");

					tape.setPosition(pos - delta);
					System.out.println("In class: " + this.getClass().getName().toString()+ " calculateNewTapePosition in else");

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			System.out.println("Tape is null");
		}
	}
	// Start of tape reached.
	public void tapeExtremeReached() {
		if(getVcr()!=null)
			getVcr().processEvent(EndOfTapeEvent.Instance());
		//	System.out.println("In class: " + this.getClass().getName().toString()+ " in tapeExtreame Reached");
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		VcrState state= null;
		System.out.println("In class: " + this.getClass().getName().toString()+ " in EndOFTapeEvent");
		try {
			calculateNewTapePosition();
			state = Stopped.Instance();
			System.out.println("In class: " + this.getClass().getName().toString()+ " in EndOFTapeEvent after stopped.instance ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		System.out.println("In class: " + this.getClass().getName().toString()+ " in FFButton");
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
			if( tape!=null)
				tape.putBackCassette();
			if(vcr!=null)
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
			System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
			state = Paused.Instance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}
}
