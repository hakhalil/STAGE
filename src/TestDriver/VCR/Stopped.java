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
		try {
			if (tape!=null && !tape.isProtected()) {
				tape.pullToDrum();
				if( vcr!= null)
					vcr.inceaseNumTimesTapePulled();			
				return Recording.Instance(vcr, tape);
			}
			else {
				Vcr.log.println("*** Ignoring event! (Tape is write protected.)");
				return this;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public VcrState RewButton(Vcr vcr, Tape tape) {
		return FastRew.Instance(vcr, tape);
	}

	public VcrState FFButton(Vcr vcr, Tape tape) {
		return FastFF.Instance(vcr, tape);
	}

	public VcrState PlayButton(Vcr vcr, Tape tape) {
		try {
			if( tape!= null)
				tape.pullToDrum();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if( vcr!=null)
				vcr.inceaseNumTimesTapePulled();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println("In class: " + this.getClass().getName().toString()+ " in PlayButton before creating an instance of playing");
	
		try {
			return Playing.Instance(vcr, tape);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
