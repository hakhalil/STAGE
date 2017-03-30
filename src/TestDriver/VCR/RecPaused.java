package TestDriver.VCR;
import TestDriver.Vcr;
public class RecPaused extends TapePresentMode {
	private static RecPaused _instance;

	private RecPaused(String inID) {
		super(inID);
	}

	public static VcrState Instance() {
		if (_instance == null) {
			_instance = new RecPaused("RecPaused");
		}

		return _instance;
	}

	public VcrState RecButton(Vcr vcr, Tape tape) {
		return Recording.Instance(vcr, tape);
	}
	
	public VcrState StopButton(Vcr vcr, Tape tape) {
		try {
			if( tape!=null)
				tape.putBackCassette ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if( vcr!= null)
				vcr.inceaseNumTimesTapePulled ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Stopped.Instance();
	}	
}