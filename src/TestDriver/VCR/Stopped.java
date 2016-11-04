package TestDriver.VCR;
import TestDriver.Vcr;
public class Stopped extends TapePresentMode {
	private static Stopped _instance;

	private Stopped(String inID) {
		super(inID);
	}

	public static VcrState Instance() {
		if (_instance == null) {
			_instance = new Stopped("Stopped");
		}

		return _instance;
	}

	public VcrState RecButton(Vcr vcr, Tape tape) {
		if (!tape.isProtected()) {
			tape.pullToDrum();
			vcr.inceaseNumTimesTapePulled();			
			return Recording.Instance(vcr, tape);
		}
		else {
			Vcr.log.println("*** Ignoring event! (Tape is write protected.)");
			return this;
		}
	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		return FastRew.Instance(vcr, tape);
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		return FastFF.Instance(vcr, tape);
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		tape.pullToDrum();
		vcr.inceaseNumTimesTapePulled();			
		return Playing.Instance(vcr, tape);
	}
}
