package TestDriver.VCR;

public class PauseButton extends VcrEvent {
	private static PauseButton _instance;

	private PauseButton(String anID)
	{
		super(anID);
	}

	public static PauseButton Instance() {
		if (_instance == null) {
			_instance = new PauseButton("PauseButton");
		}

		return _instance;
	}
}