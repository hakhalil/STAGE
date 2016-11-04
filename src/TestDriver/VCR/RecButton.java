package TestDriver.VCR;

public class RecButton extends VcrEvent {
	private static RecButton _instance;

	private RecButton(String anID)
	{
		super(anID);
	}

	public static RecButton Instance() {
		if (_instance == null) {
			_instance = new RecButton("RecButton");
		}

		return _instance;
	}
}