package TestDriver.VCR;
import TestDriver.Vcr;
public abstract class VcrState {
	protected String id;

	protected VcrState() {
		;
	}
	
	protected VcrState(String inID) {
		setId(inID);
	}
	
	public VcrState EjectButton(Vcr vcr) {
		displayDefaultMsg();
		return this;
	}
	public VcrState PowerButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState InsertingTape(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState PauseButton() {
		displayDefaultMsg();
		return this;
	}
	public VcrState RecButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState StopButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState RewButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState FFButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState PlayButton(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
	}
	public VcrState EndOfTapeEvent(Vcr vcr, Tape tape) {
		displayDefaultMsg();
		return this;
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
	
	private void displayDefaultMsg() {
		Vcr.log.println("*** Ignoring event!");
	}
	
	public VcrState processEvent(VcrEvent event, Vcr vcr, Tape tape) {
		VcrState state = null;

		if( null != event ) {
			if (event instanceof PowerButton) {
				state = PowerButton(vcr, tape);
			}
			else if (event instanceof EjectButton) {
				state = EjectButton(vcr);
			}
			else if (event instanceof InsertingTape) {
				state = InsertingTape( vcr, ((InsertingTape)event).getTape());
			}
			else if (event instanceof PlayButton) {
				state = PlayButton(vcr, tape);
			}
			else if (event instanceof RewButton) {
				state = RewButton(vcr, tape);
			}
			else if (event instanceof FFButton) {
				state = FFButton(vcr, tape);
			}
			else if (event instanceof StopButton) {
				state = StopButton(vcr, tape);
			}
			else if (event instanceof RecButton) {
				state = RecButton(vcr, tape);
			}
			else if (event instanceof PauseButton) {
				state = PauseButton();
			}
			else if (event instanceof EndOfTapeEvent) {
				state = EndOfTapeEvent(vcr, tape);
			}
			else
			{
				state = this;
			}
		} else {
			state = this;
		}

		return state;
	}
}
