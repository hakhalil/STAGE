package TestDriver.VCR;
import TestDriver.Vcr;
public class Paused extends TapePresentMode {
	private static Paused _instance;

	private Paused(String inID) {
		super(inID);
	}

	public static VcrState Instance() {
		if (_instance == null) {
			_instance = new Paused("Paused");
		}

		return _instance;
	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		return SlowRew.Instance(vcr, tape);
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		return SlowFF.Instance(vcr, tape);
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		return Playing.Instance(vcr, tape);
	}
	
	public VcrState StopButton(Vcr vcr, Tape tape) {
		tape.putBackCassette();
		vcr.inceaseNumTimesTapePulled();
		return Stopped.Instance();
	}		
}
