package TestDriver.VCR;
import TestDriver.Vcr;
public class TapeAbsent extends VcrState {
	private static TapeAbsent _instance;

	private TapeAbsent(String inID) {
		super(inID);
	}

	public static VcrState Instance() {
		if (_instance == null) {
			_instance = new TapeAbsent("TapeAbsent");
		}

		return _instance;
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		return Off.Instance();
	}

	public VcrState InsertingTape(Vcr vcr, Tape tape) {
		VcrState nextState;

		if (vcr.isTapePresent()) {
			Vcr.log.println("*** Ignoring event! (Tape already present.)");
			nextState = this;
		}
		else {
			vcr.insertTape(tape);
	
			if (tape.getPosition() == tape.getMaxPosition()) {
				nextState = FastRew.Instance(vcr, tape);
			}
			else if (tape.isProtected()) {
    			tape.pullToDrum();
	    		vcr.inceaseNumTimesTapePulled();							
				nextState = Playing.Instance(vcr, tape);
			}
			else {
				nextState = Stopped.Instance();
			}
		}
		
		return nextState;
	}
}
