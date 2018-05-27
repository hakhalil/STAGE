package TestDriver.VCR;

public class EjectButton extends VcrEvent {
	private static EjectButton _instance;

	private EjectButton(String anID)
	{
		super(anID);
	}

	public static EjectButton Instance() {
		if (_instance == null) {
			_instance = new EjectButton("EjectButton");
		}

		return _instance;
	}
}
