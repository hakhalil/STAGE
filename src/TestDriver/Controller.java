package TestDriver;

/**
 * This class represents the cruise control. It contains a speed controller
 * which it controls it to monitor and adjust car speed when "Cruising" is 
 * enabled.
 */

class Controller extends CaseStudy{
	//	cruise controller states

  	private SpeedControl sc;			// The speed controller

	// -----------
	// Constructor
	// -----------
  	Controller(CarSimulator cs)	{
  		try {
			sc=new SpeedControl(cs);
		} catch (Exception e) {
			currentState = "Failed";
		} 
  	}

	Controller()	{
		try {
			CarSimulator cs = new CarSimulator();
			sc=new SpeedControl(cs); 
			currentState  = "Inactive"; //represents the current state of the controller
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}

	/**
	 * Braking would disable "Cruising".
	 * If the controller state is "Cruising" then it will be set to "StandBy"
	 */
   	synchronized void brake(){
    	try {
			if (currentState=="Cruising") {
				sc.disableControl(); 
				if( sc.getState() == 0)
					currentState="StandBy"; 
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
 	}

	/**
	 * Accelerating would disable "Cruising".
	 * If the controller state is "Cruising" then it will be set to "StandBy"
	 */
	synchronized void accelerator() {
    	try {
			if (currentState=="Cruising") {
				sc.disableControl();
				if( sc.getState() == 0)
					currentState="StandBy"; 
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}

	/**
	 * Turning the car engine off would disable "Cruising" and set 
	 * the controller state to "Inactive"
	 */
   	synchronized void engineOff(){
    	try {
			if (currentState!="Inactive") {
				sc.disableControl();
				if( sc.getState() == 0)
					currentState="Inactive";
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
 	}

	/**
	 * When turning the car engine on, the controller resets the target
	 * cruise control speed. The controller state would be "Active"
	 */
   	synchronized void engineOn(){
    	try {
			if (currentState=="Inactive") {
				sc.clearSpeed(); 
				if(sc.getCruiseSpeed()==0)
					currentState="Active";
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}

	/**
	 * When turning the cruise control on, "Cruising" is enabled. The speed 
	 * controller would set the target cruise control speed and continue
	 * to monitor and adjust car speed 
	 */
   	synchronized void on() {
    	try {
			if( currentState!="Inactive") {
				sc.recordSpeed(); 
				sc.enableControl();
				if( sc.getState() == 1)
					currentState="Cruising";
				else
					currentState = "Failed";
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}

	/**
	 * When turning the cruise control off, "Cruising" is disabled. The
	 * controller becomes in "StandBy" mode, allowing a possible resuming
	 * of the cruise control 
	 */
   	synchronized void off(){
    	try {
			if (currentState=="Cruising") {
				sc.disableControl(); 
				if( sc.getState() == 0)
					currentState="StandBy";
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}

	/**
	 * Resuming from a "StandBy" mode would enable "Cruising" with the 
	 * same target speed used before disabling "Cruising" 
	 */
   	synchronized void resume() {
    	try {
			if(currentState=="StandBy" && sc.getState()==0) {
				sc.enableControl();
				if( sc.getState() == 1)
					currentState="Cruising";
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
  	}


  	public SpeedControl getSpeedControl() {
  		return sc;
  	}

	@Override
	public String getCurrentState() {

		return currentState;
	}

	@Override
	public void setCurrentState() {
		if( sc.getState() == 0)
			currentState = "Inactive";
		else if( sc.getState() == 1)
			currentState = "StandBy";
			
		
			
		
	}
}