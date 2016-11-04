package TestDriver.VCR;

public class PowerButton extends VcrEvent {
	private static PowerButton _instance;

	private PowerButton(String anID)
	{
		super(anID);
	}

	public static PowerButton Instance() {
		if (_instance == null) {
			_instance = new PowerButton("PowerButton");
		}

		return _instance;
	}
}
