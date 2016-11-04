package TestDriver.VCR;

import java.util.TimerTask;

public class TimedTask extends TimerTask {
	TapePositionAlteringState state;

	public TimedTask(TapePositionAlteringState inState) {
		state = inState;
	}

	public void run() {
		// Invoke the tapeExtremeReached() method on the
		// state that created this TimedTask.
		
		try {
			state.tapeExtremeReached();
		} catch (Exception e) {
			System.out.println("Statet is null");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
