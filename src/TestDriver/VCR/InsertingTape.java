package TestDriver.VCR;

public class InsertingTape extends VcrEvent {
	private Tape tape;

	private InsertingTape(Tape inTape)
	{
		super("InsertingTape");
		setTape(inTape);
	}
	
	// Assuming the Tape object is not null.
	public static InsertingTape Instance(Tape inTape) {
		return new InsertingTape(inTape);
	}

	public Tape getTape() {
		return tape;
	}
	
	private void setTape(Tape newTape) {
		tape = newTape;
	}
}
