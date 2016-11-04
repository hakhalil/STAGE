package TestDriver;

public class Atm extends CaseStudy {

	private AtmContext _fsm;

	private int attempts;

	private int cur_balance;

	private java.lang.String pin;

	private int sb;

	private int cb;

	public Atm() {
		try {
			_fsm = new AtmContext(this);
			attempts = 0;

			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Atm(String pin, Integer sb, Integer cb) {
		try {
			_fsm = new AtmContext(this);
			attempts = 0;
			// startFSM();
			card(pin, sb, cb);
			// write("enter pin:");
			// makeCard(pin,sb,cb);
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
	}

	public void startFSM() {
		try {
			_fsm.enterStartState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
	}

	public void card(java.lang.String pin, int sb, int cb) {
		try {
			_fsm.Card(pin, sb, cb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
	}

	public void pin(java.lang.String p) {
		try {
			_fsm.Pin(p);
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void checking() {
		try {
			_fsm.Checking();
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			////e.printStackTrace();
		}
	}

	public void savings() {
		try {

			_fsm.Savings();
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void returnCard() {
		try {
			_fsm.Stop();
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void withdrawal(Integer w) {
		try {
			_fsm.Withdrawal(w);
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void deposit(Integer d) {
		try {
			_fsm.Deposit(d);
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public int balance() {
		try {
			_fsm.Balance();
			setCurrentState();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return cur_balance;
	}

	public void exitAccount() {
		try {
			_fsm.Done();
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void receipt() {
		_fsm.Receipt();
		setCurrentState();
	}

	public void reset() {
		_fsm.Reset();
	}

	protected void setBalance(int bal) {
		cur_balance = bal;
	}

	protected int getBalance() {
		return cur_balance;
	}

	public int getAttempts() {
		return attempts;
	}

	public java.lang.String stateName() {
		return _fsm.getState().getName().replace("MainMap.", "");
	}

	public void makeCard(java.lang.String pin, int sb, int cb) {
		this.pin = pin;
		this.sb = sb;
		this.cb = cb;
	}

	public void incrementAttempts() {
		attempts++;
	}

	public void resetAttempts() {
		attempts = 0;
	}

	public void write(java.lang.String s) {
		// System.out.println( s );
	}

	public void print(java.lang.String s) {
		try {
			for (int i = 0; i < s.length() + 4; i++) {
				System.out.print("-");
			}
			System.out.println();
			System.out.println("| " + s + " |");
			for (int i = 0; i < s.length() + 4; i++) {
				System.out.print("-");
			}
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public static void main( java.lang.String[] args ) { Atm atm = new Atm();
	 * atm.startFSM(); }
	 */

	public void updateS(int amount) {
		sb += amount;
	}

	public void updateC(int amount) {
		cb += amount;
	}

	public int getBalanceS() {
		return sb;
	}

	public int getBalanceC() {
		return cb;
	}

	public boolean hasRightPin(java.lang.String pin) {
		return this.pin.equals(pin);
	}

	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	@Override
	public void setCurrentState() {
		// TODO Auto-generated method stub
		String state = _fsm.getState().getName();
		currentState = state.substring(state.indexOf(".") + 1);
		if (currentState.contentEquals("Validate"))
			if (getAttempts() < 0 || getAttempts() > 2)
				currentState = "Invalid";
			else
				currentState += getAttempts() + 1;

	}

}
