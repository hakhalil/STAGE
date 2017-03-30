package TestDriver;

import TestDriver.VCR.EjectButton;
import TestDriver.VCR.EndOfTapeEvent;
import TestDriver.VCR.FFButton;
import TestDriver.VCR.InsertingTape;
import TestDriver.VCR.Off;
import TestDriver.VCR.PauseButton;
import TestDriver.VCR.PlayButton;
import TestDriver.VCR.PowerButton;
import TestDriver.VCR.RecButton;
import TestDriver.VCR.RewButton;
import TestDriver.VCR.StopButton;
import TestDriver.VCR.Tape;
import TestDriver.VCR.VcrEvent;
import TestDriver.VCR.VcrLogger;
import TestDriver.VCR.VcrState;

public class Vcr extends CaseStudy{
	// Please note, the tape speeds are defined in
	// the TapePositionAlteringState class.

	private VcrState state;
	private Tape tape;
	//private Tape ejTape;
	static public VcrLogger log;
	// number of times when tapes in the VCR are
	// pulled to drum or put back to cassette

	//VCR states
	private int numTimesTapePulled = 0;
	public Vcr(){
		log = new VcrLogger();
		initialize();		
	}
	public Vcr(Integer state) {
		log = new VcrLogger();
		initialize();
		switch (state){
		case 0: 

			insertTape(new Tape(0,true));
			tape = getTape();
			//currentState = "OffProtected";
			break;
		case 1:
			//currentState = "OffNoTape";
			break;
		case 2:
			insertTape(new Tape(0,false));
			powerButton();
			playButton();
			tape = getTape();
			//currentState = "PlayingNotProtected";
			break;
		}

		setCurrentState();
	}

	public void stopButton(){
		VcrState theState = getState();
		if( theState != null)
			theState = theState.processEvent(StopButton.Instance(),this,getTape());
		setState(theState);
		setCurrentState();
	}

	public void powerButton(){
		//Vcr vcr = new Vcr();
		VcrState theState = getState();
		if( theState != null){
			theState = theState.processEvent(PowerButton.Instance(),this,getTape());
			setState(theState);
			setCurrentState();
		}
	}

	public void ejectButton(){
		VcrState theState = getState();
		if( theState != null)
			theState = theState.processEvent(EjectButton.Instance(),this,getTape());
		setState(theState);
		//tape.pullToDrum();
		setCurrentState();
	}

	public void pauseButton(){
		VcrState theState = getState();
		if( theState!= null)
			theState = theState.processEvent(PauseButton.Instance(),this,getTape());
		setState(theState);
		setCurrentState();
	}

	public void playButton(){
		VcrState theState = getState();
		if( theState!= null)
			theState = theState.processEvent(PlayButton.Instance(),this,getTape());
		setState(theState);
		setCurrentState();
	}

	public void rewButton(){
		VcrState theState = getState();
		if( theState!= null)
			theState = theState.processEvent(RewButton.Instance(),this,getTape());
		setState(theState);
		setCurrentState();
	}

	public void fFButton(){
		VcrState theState = getState();
		if( theState!= null)
			theState = theState.processEvent(FFButton.Instance(),this,getTape());
		setState(theState);
		setCurrentState();
	}

	public void recButton(){
		VcrState theState = getState();
		try {
			if( theState!= null)
				theState = theState.processEvent(RecButton.Instance(),this,getTape());
			setState(theState);
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processEvent(VcrEvent event)
	{
		if( event != null ) Vcr.log.println("Processing event: " + event.toString());
		VcrState theState = getState();
		if( theState != null)
			if( event != null ) theState = theState.processEvent(event,this,getTape());
		setState(theState);
	}

	public void insertingTape(){
		//	if(tape == null)
		//		tape = new Tape(0,false);
		Tape paramTape = new Tape(0,false);
		VcrState theState = getState();
		if( theState != null)
			theState = theState.processEvent(InsertingTape.Instance(paramTape),this,paramTape);
		setState(theState);
		insertTape (tape);
		setCurrentState();
	}

	public void endOfTapeEvent(){
		VcrState theState = getState();
		if( theState != null)
			theState = theState.processEvent(EndOfTapeEvent.Instance(),this,tape);
		setState(theState);
		setCurrentState();
	}

	public void insertTape(Tape inTape) {
		setTape(inTape);
	}

	public void ejectTape() {
		if (getTape () != null) {
			if (getTape ().isTapePulledToDrum()) {
				getTape ().putBackCassette();
			}
		}
		Vcr.log.println("Ejecting the tape...");
		//ejTape = tape;
		setTape(null);
	}

	public void inceaseNumTimesTapePulled () {
		numTimesTapePulled ++;
		Vcr.log.println("Vcr has pulled/released tapes " +
				"to/from drum for " + numTimesTapePulled + " times");
	}

	protected void initialize() {
		if (state == null) {
			setState(Off.Instance());
		}
	}

	// Getter and setter methods.

	public void setState(VcrState inState) {
		if( inState != null)
		{
			try {
				if(Vcr.log!=null)
					Vcr.log.println("Setting state to: " + inState.toString());
				state = inState;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public VcrState getState() {
		return state;
	}

	public Tape getTape() {
		return tape;
	}

	private void setTape(Tape newTape) {
		tape = newTape;
	}

	public boolean isTapePresent() {
		return (getTape() != null);
	}

	// TESTING METHODS
	public void testingSetTape(Tape newTape) {
		tape = newTape;
	}

	public Tape testingGetTape() {
		return tape;
	}

	public static void main(String[] args) {
		Vcr vcr = new Vcr();
		Tape tape1 = new Tape(0,false);
		vcr.processEvent(InsertingTape.Instance(tape1));
		System.out.println(vcr.getState());
		vcr.processEvent(PlayButton.Instance());
		System.out.println(vcr.getState());
		vcr.processEvent(RecButton.Instance());
		System.out.println(vcr.getState());
		vcr.processEvent(StopButton.Instance());
		System.out.println(vcr.getState());
	}

	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	@Override
	public void setCurrentState() {
		if(getState()!=null)
			currentState= getState().toString();
		if(currentState == null)
			currentState = "Failed";
		else{
			if( !currentState.contains("Rec")){
				if(currentState.contains("Rew"))
					currentState = "Rew"; 
				else if (currentState.contains("FastFF")||currentState.contains("SlowFF"))
					currentState = "FF";
				String suffex;
				if(currentState == "TapeAbsent")
					suffex = "";
				else if( getTape()==null)
					suffex = "NoTape";
				else if((getTape() != null) && getTape().isProtected())
					suffex = "Protected";
				else
					suffex = "NotProtected";
				currentState = currentState+suffex;
			}
		}
	}
}
