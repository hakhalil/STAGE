package TestDriver.VCR;

import java.util.Date;

import TestDriver.Vcr;

public class FastFF extends TapePositionAlteringState {
	private FastFF(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);


		int tapeToMove = 0;
		try {
			if(tape!=null )
			tapeToMove = tape.getMaxPosition() - tape.getPosition();
		}catch (Throwable e) {
			System.out.println(e);
		}
		int msecRemaining = 0;
		try {
			if( FastestSpeed>0 )
				msecRemaining = msecToSec * tapeToMove / FastestSpeed;
		} catch (Throwable e) {
			System.out.println(e);
		}


		try {
			TimedTask task = new TimedTask( this);
			setTask(task);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		if( formatter!=null)
		Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		try {
			if(getTimer() != null && getTask() != null) getTimer().schedule(getTask(), Math.abs(msecRemaining));

		} catch (Throwable e) {
			System.out.println(e);
		}
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new FastFF("FastFF", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		try {
			if(getTask()!=null)
				getTask().cancel();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long finalTime = (new Date()).getTime();
		int delta = 0;
		try { 
			if(msecToSec!=0)
				delta = (int) (FastestSpeed * (finalTime - getEventStartTime()) / msecToSec);
		} catch (Throwable e) {
			System.out.println(e);
		}

		
		int pos = 0;
		try {
			if( getTape()!=null)
				getTape().getPosition();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if( formatter!=null)
		Vcr.log.println("Tape delta: " + formatter.format(delta));

		int z = 0;
		try { 
			z = pos + delta ;
		} catch (Throwable e) {
			System.out.println(e);
		}

			try {
				if(getTape()!=null)
				{
					if ((z) > getTape().getMaxPosition()) {
						getTape().setMaxPosition();
					} else {
						getTape().setPosition(pos + delta);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	// End of tape reached.
	public void tapeExtremeReached() {
		try {
			if(getVcr()!=null)
				getVcr().processEvent(EndOfTapeEvent.Instance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try {
			if(tape!=null)
				tape.pullToDrum();
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
		return Playing.Instance(vcr, tape);
	}
	public VcrState PauseButton() {
		calculateNewTapePosition();
		//	System.out.println("In class: " + this.getClass().getName().toString()+ " in PauseButton");
		return Paused.Instance();
	}
}
