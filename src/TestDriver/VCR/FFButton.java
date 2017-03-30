package TestDriver.VCR;

public class FFButton extends VcrEvent {
	private static FFButton _instance;

	private FFButton(String anID)
	{
		super(anID);
	}

	public static FFButton Instance() {
		if (_instance == null) {
			_instance = new FFButton("FFButton");
		}

		return _instance;
	}
}