package TestDriver.VCR;

public abstract class VcrEvent {
	protected String id;

	private VcrEvent() {
		;
	}

	protected VcrEvent(String inID) {
		setId(inID);
	}

	public String toString() {
		return getId();
	}

	protected void setId(String newId) {
		id = newId;
	}

	protected String getId() {
		return id;
	}
}