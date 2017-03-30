package TestDriver.VCR;
import java.util.Date;

import TestDriver.Vcr;

public class Playing extends TapePositionAlteringState {
	private Playing(String inID, Vcr vcr, Tape tape) {
		super(inID, vcr, tape);
		int tapeToMove = Tape.getMaxPosition(); 
		try {
			if( tape!=null)
				tapeToMove = tapeToMove- tape.getPosition();
		} catch (Exception e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("In class: " + this.getClass().getName().toString()+ " in constructor");
		int msecRemaining = 0;
		try {
			if(NormalSpeed!=0)
				msecRemaining = msecToSec * tapeToMove / NormalSpeed;
		} catch (Throwable e) {
			System.out.println("Dividing by zero since FastestSpeed " + FastestSpeed);
		}
		try {
			TimedTask t= new TimedTask(this);
			if(t!=null)
				setTask(t); 
			if(formatter!=null)
			Vcr.log.println("Time to end of tape event: " + formatter.format(msecRemaining));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("In class: " + this.getClass().getName().toString()+ " in constructor just before get timer");
		try {
			if( (getTimer()!= null) && (getTask()!=null))
			{
				if(msecRemaining>=0)
				{
					getTimer().schedule(getTask(), msecRemaining);
				}
				System.out.println("In class: " + this.getClass().getName().toString()+ " in constructor just after get timer");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static VcrState Instance(Vcr vcr, Tape tape) {
		return new Playing("Playing", vcr, tape);
	}

	protected void calculateNewTapePosition() {
		try {
			if( getTask()!=null)
				getTask().cancel();
			long finalTime = (new Date()).getTime();
			int delta = 0;
			if (msecToSec!= 0)
				delta = (int)(NormalSpeed * (finalTime - getEventStartTime()) / msecToSec);
			int pos = 0;
			if( getTape()!=null)
				pos = getTape().getPosition(); 
			if(formatter!=null)
				Vcr.log.println("Tape delta: " + formatter.format(delta));

				if ( (getTape()!= null) &&(pos + delta) > Tape.getMaxPosition() ) {
					getTape().setMaxPosition();
				}
				else
				{
					if(getTape()!=null)
						getTape().setPosition(pos + delta);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// End of tape reached.
	public void tapeExtremeReached() {
		if(getVcr()!=null)
			getVcr().processEvent(EndOfTapeEvent.Instance());
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
			if( vcr != null)
				vcr.inceaseNumTimesTapePulled();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FastRew.Instance(vcr, tape);
	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return SlowRew.Instance(vcr, tape);
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		calculateNewTapePosition();
		return SlowFF.Instance(vcr, tape);
	}

	public VcrState PauseButton() {
		calculateNewTapePosition();
		return Paused.Instance();
	}
}
