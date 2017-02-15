package TestDriver;

import java.lang.reflect.Method;

public class TrafficLight extends CaseStudy {

	final long SIXTY_SECONDS = 20;
	final long TWENTY_SECONDS = 7;
	final long FIVE_SECONDS = 5;
	final long TWO_SECONDS = 2;

	enum trafficlight_states {
		EW_GREEN, NS_Pedestrian_Waiting, EW_YELLOW, EW_RED, NS_GREEN, EW_Pedestrian_Waiting, NS_YELLOW, NS_RED
	};

	private trafficlight_states trafficLight_state;

	TrafficLightThread trafficLightTimer = null;

	public TrafficLight() {
		gotoEW_Green();
	}

	protected void walkButton() {

		if (trafficLight_state == trafficlight_states.NS_GREEN) {
			this.trafficLight_state = trafficlight_states.EW_Pedestrian_Waiting;
			if (null != trafficLightTimer) {
				trafficLightTimer.interrupt();
				trafficLightTimer = null;
			}
			logMe();
			runMethodAfterWaiting("wait_20s", TWENTY_SECONDS);
		} else if (trafficLight_state == trafficlight_states.EW_GREEN) {
			this.trafficLight_state = trafficlight_states.NS_Pedestrian_Waiting;
			if (null != trafficLightTimer) {
				trafficLightTimer.interrupt();
				trafficLightTimer = null;
			}
			logMe();
			runMethodAfterWaiting("wait_20s", TWENTY_SECONDS);
		}
	}

	@Override
	public String getCurrentState() {
		return trafficLight_state.toString();
	}

	@Override
	public void setCurrentState() {
		this.currentState = trafficLight_state.toString();

	}

	public void wait_60s() {
		if (trafficLight_state == trafficlight_states.EW_GREEN) {
			gototEW_Yellow();
		} else if (trafficLight_state == trafficlight_states.NS_GREEN) {
			gotoNS_Yellow();
		}
	}

	public void wait_20s() {
		if (trafficLight_state == trafficlight_states.NS_Pedestrian_Waiting) {
			gototEW_Yellow();
		} else if (trafficLight_state == trafficlight_states.EW_Pedestrian_Waiting) {
			gotoNS_Yellow();
		}
	}

	private void wait_5s() {
		if (trafficLight_state == trafficlight_states.EW_YELLOW) {
			trafficLight_state = trafficlight_states.EW_RED;
			logMe();
			simpleWait(TWO_SECONDS);

			wait_2s();
		} else if (trafficLight_state == trafficlight_states.NS_YELLOW) {
			trafficLight_state = trafficlight_states.NS_RED;
			logMe();
			simpleWait(TWO_SECONDS);

			wait_2s();
		}
	}

	private void wait_2s() {
		if (trafficLight_state == trafficlight_states.EW_RED) {
			gotoNS_Green();
		} else if (trafficLight_state == trafficlight_states.NS_RED) {
			gotoEW_Green();
		}
	}

	private void gototEW_Yellow() {
		trafficLight_state = trafficlight_states.EW_YELLOW;
		logMe();
		simpleWait(FIVE_SECONDS);
		wait_5s();

	}

	private void gotoNS_Green() {
		trafficLight_state = trafficlight_states.NS_GREEN;
		logMe();
		runMethodAfterWaiting("wait_60s", SIXTY_SECONDS);
	}

	private void gotoNS_Yellow() {
		trafficLight_state = trafficlight_states.NS_YELLOW;
		logMe();
		simpleWait(FIVE_SECONDS);
		wait_5s();

	}

	private void gotoEW_Green() {
		trafficLight_state = trafficlight_states.EW_GREEN;
		logMe();
		runMethodAfterWaiting("wait_60s", SIXTY_SECONDS);
	}

	private void simpleWait(long seconds) {
		long currentmillis = System.currentTimeMillis();
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Waited for " + (System.currentTimeMillis() - currentmillis) / 1000 + " seconcds");
	}

	protected void logMe() {
		System.out.println(this.trafficLight_state.toString());
	}

	private void runMethodAfterWaiting(String methodName, long seconds) {

		trafficLightTimer = new TrafficLightThread();
		trafficLightTimer.methodName = methodName;
		trafficLightTimer.parentClass = this;
		trafficLightTimer.time = seconds;
		trafficLightTimer.start();

	}

	public static void main(String[] args) {
		TrafficLight t = new TrafficLight();
		for (int i=0; i < 10; i ++)
			t.walkButton();
	}

	class TrafficLightThread extends Thread {
		public String methodName = null;
		Object parentClass = null;
		long time = 0;

		public void run() {
			long currentmillis = System.currentTimeMillis();
			try {
				trafficLightTimer.sleep(time * 1000);
				
				System.out.println("Waited for " + (System.currentTimeMillis() - currentmillis) / 1000 + " seconcds");
				
				if (null != parentClass) {
					Method method = parentClass.getClass().getMethod(methodName);
					method.invoke(parentClass);
				}
			} catch (InterruptedException interrupt) {
				System.out.println("stopped");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
