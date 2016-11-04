package TestDriver.VCR;

import TestDriver.Vcr;

public class Off extends VcrState {
	private static Off _instance;

	private Off(String inID) {
		super(inID);
	}

	public static VcrState Instance() {
		if (_instance == null) {
			_instance = new Off("Off");
		}

		return _instance;
	}

	public VcrState EjectButton(Vcr vcr) {
		if (!vcr.isTapePresent()) {
			Vcr.log.println("*** Ignoring event! (No tape.)");
		}
		else {
			vcr.ejectTape();
		}
		
		return this;
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		VcrState nextState;

		if (tape == null) {
			nextState = TapeAbsent.Instance();
		}
		else {
			nextState = Stopped.Instance();
		}

		return nextState;
	}

	public VcrState InsertingTape(Vcr vcr, Tape tape) {
		VcrState nextState;

		if (vcr.isTapePresent()) {
			Vcr.log.println("*** Ignoring event! (Tape already present.)");
			nextState = this;
		}
		else {
			vcr.insertTape(tape);
	
			if (tape.getPosition() == Tape.getMaxPosition()) {
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
