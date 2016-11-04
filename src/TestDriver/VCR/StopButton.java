package TestDriver.VCR;
import TestDriver.Vcr;
public class StopButton extends VcrEvent {
	private static StopButton _instance;

	private StopButton(String anID)
	{
		super(anID);
	}

	public static StopButton Instance() {
		if (_instance == null) {
			_instance = new StopButton("StopButton");
		}

		return _instance;
	}
}