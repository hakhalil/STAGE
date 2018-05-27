package TestDriver.VCR;

public class PlayButton extends VcrEvent {
	private static PlayButton _instance;

	private PlayButton(String anID)
	{
		super(anID);
	}

	public static PlayButton Instance() {
		if (_instance == null) {
			_instance = new PlayButton("PlayButton");
		}

		return _instance;
	}
}