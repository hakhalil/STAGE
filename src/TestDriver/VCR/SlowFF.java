package TestDriver.VCR;
import java.util.Date;

import TestDriver.Vcr;

public class SlowFF extends TapePositionAlteringState {
	private SlowFF(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
			int tapeToMove = 0;
			try {
				tapeToMove = tape.getMaxPosition() - tape.getPosition();
			}catch (Throwable e) {
				System.out.println(e);
			}
			int msecRemaining = 0;
			try {
			if( FastestSpeed>0 && tape!=null)
				msecRemaining = msecToSec * tapeToMove  / FastestSpeed;
			} catch (Throwable e) {
				System.out.println(e);
			}
			try {
				TimedTask t = new TimedTask(this);
				if( t!=null )
					setTask(new TimedTask(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				if(formatter!=null)
				Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(getTimer() != null && getTask() != null)  getTimer().schedule(getTask(), Math.abs(msecRemaining));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new SlowFF("SlowFF", vcr, tape);
	}

	protected void calculateNewTapePosition() {
			try {
				if( getTask()!=null)
					getTask().cancel();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			long finalTime=0;
			try {
				finalTime = (new Date()).getTime();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int delta = 0;
			try {
				if(msecToSec!=0)
					delta = (int)(FasterSpeed * (finalTime - getEventStartTime()) / msecToSec);
			} catch (Throwable e) {
				System.out.println(e);
			}
			int pos=0;
			try {
				pos = 0;
				if(getTape()!=null)
					pos = getTape().getPosition();
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
			if(getTape()!=null)
			{
			if ( (z) > getTape().getMaxPosition() ) {
				getTape().setMaxPosition();
			}
			else
			{
				
				getTape().setPosition(pos + delta);
			}
			}
	}

	// End of tape reached.
	public void tapeExtremeReached() {
		try {
			if( getVcr()!=null)
				getVcr().processEvent(EndOfTapeEvent.Instance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("In class: " + this.getClass().getName().toString()+ " in tapeExtremeReached");
	}

	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		try {
			if( tape!=null )
				tape.putBackCassette ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if( vcr!=null)
				vcr.inceaseNumTimesTapePulled ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
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