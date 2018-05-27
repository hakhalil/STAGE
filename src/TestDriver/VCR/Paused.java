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
		try {
			if(tape!=null)
				tape.putBackCassette();
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
		return Stopped.Instance();
	}		
}
