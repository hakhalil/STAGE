package TestDriver.VCR;
import TestDriver.Vcr;
public abstract class TapePresentMode extends VcrState {
	protected TapePresentMode(String inID) {
		super(inID);
	}

	public VcrState EjectButton(Vcr vcr) {
		vcr.ejectTape();
		return TapeAbsent.Instance();
	}

	public VcrState PowerButton(Vcr vcr, Tape tape) {
		if (tape.isTapePulledToDrum()) {
			tape.putBackCassette();
			vcr.inceaseNumTimesTapePulled();
		}
		return Off.Instance();
	}
}
