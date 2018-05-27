package TestDriver.VCR;
import TestDriver.Vcr;
public abstract class TapePresentMode extends VcrState {
	protected TapePresentMode(String inID) {
		super(inID);
	}

	public VcrState EjectButton(Vcr vcr) {
		try {
			if( vcr!=null )
				vcr.ejectTape();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TapeAbsent.Instance();
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		try {
			if (tape!=null && tape.isTapePulledToDrum()) {
				tape.putBackCassette();
				vcr.inceaseNumTimesTapePulled();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Off.Instance();
	}
}
