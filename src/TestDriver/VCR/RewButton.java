package TestDriver.VCR;

public class RewButton extends VcrEvent {
	private static RewButton _instance;

	private RewButton(String anID)
	{
		super(anID);
	}

	public static RewButton Instance() {
		if (_instance == null) {
			_instance = new RewButton("RewButton");
		}

		return _instance;
	}
}