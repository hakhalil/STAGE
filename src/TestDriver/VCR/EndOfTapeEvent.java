package TestDriver.VCR;

public class EndOfTapeEvent extends VcrEvent {
	private static EndOfTapeEvent _instance;

	private EndOfTapeEvent(String anID)
	{
		super(anID);
	}

	public static EndOfTapeEvent Instance() {
		if (_instance == null) {
			_instance = new EndOfTapeEvent("EndOfTapeEvent");
		}

		return _instance;
	}
}