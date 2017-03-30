package TestDriver;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AtmContext.java
import statemap.FSMContext;
import statemap.State;
import statemap.StateUndefinedException;

public class AtmContext extends FSMContext
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static class MainMap_Default extends AtmState
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final class MainMap_Quit extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Reset(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.resetAttempts();
					atmcontext.setState(MainMap.Idle);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_Quit(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_ProcessS extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Receipt(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.print((new StringBuilder()).append("Balance = ").append(atm.getBalance()).toString());
					atmcontext.setState(MainMap.SelectSAction);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_ProcessS(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_ProcessC extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Receipt(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.print((new StringBuilder()).append("Balance = ").append(atm.getBalance()).toString());
					atmcontext.setState(MainMap.SelectCAction);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_ProcessC(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_SelectSAction extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Balance(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.setBalance(atm.getBalanceS());
					atmcontext.setState(MainMap.ProcessS);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Deposit(AtmContext atmcontext, Integer i)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.updateS(i);
					atmcontext.setState(MainMap.ProcessS);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Done(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.write("Savings/Checking");
					atmcontext.setState(MainMap.SelectAccount);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Withdrawal(AtmContext atmcontext, Integer i)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.updateS(-i);
					atmcontext.setState(MainMap.ProcessS);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_SelectSAction(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_SelectCAction extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Balance(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.setBalance(atm.getBalanceC());
					atmcontext.setState(MainMap.ProcessC);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Deposit(AtmContext atmcontext, Integer i)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.updateC(i);
					atmcontext.setState(MainMap.ProcessC);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Done(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.write("Savings/Checking");
					atmcontext.setState(MainMap.SelectAccount);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			protected void Withdrawal(AtmContext atmcontext, Integer i)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.updateC(-i);
					atmcontext.setState(MainMap.ProcessC);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_SelectCAction(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_SelectAccount extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Checking(AtmContext atmcontext)
			{
				try {
					atmcontext.getState().Exit(atmcontext);
					atmcontext.setState(MainMap.SelectCAction);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			protected void Savings(AtmContext atmcontext)
			{
				try {
					atmcontext.getState().Exit(atmcontext);
					atmcontext.setState(MainMap.SelectSAction);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			protected void Stop(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.write("Ejecting Card");
					atmcontext.setState(MainMap.Quit);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private MainMap_SelectAccount(String s, int i)
			{
				super(s, i);
			}

		}

		private static final class MainMap_Validate extends MainMap_Default
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void Pin( AtmContext context, java.lang.String p )
			{
				Atm ctxt = context.getOwner();
				if (ctxt.hasRightPin( p )) {
					context.getState().Exit( context );
					context.clearState();
					try {
						ctxt.write( "Savings/Checking" );
					} finally 
					{
						context.setState( MainMap.SelectAccount );
						context.getState().Entry( context );
					}
				} else {

					//only one attempt, because otherwhise the path will be infeasible
						//if (ctxt.getAttempts() < 0 && !ctxt.hasRightPin( p )) {
						if (ctxt.getAttempts() < 2 && !ctxt.hasRightPin( p )) {
						AtmContext.AtmState endState = context.getState();
						context.clearState();
						try {
							ctxt.write( "Wrong Pin, ReEnter" );
							ctxt.incrementAttempts();
						} finally 
						{
							context.setState( endState );
						}
					} else {
						context.getState().Exit( context );
						context.clearState();
						try {
							ctxt.write( "Wrong Pin, Ejecting Card" );
						} finally 
						{
							context.setState( MainMap.Quit );
							context.getState().Entry( context );
						}
					}
				}
				return;
			}

			private MainMap_Validate(String s, int i)
			{
				super(s, i);
			}

			protected void Stop(AtmContext atmcontext)
			{
				try {
					Atm atm;
					atm = atmcontext.getOwner();
					atmcontext.getState().Exit(atmcontext);
					atmcontext.clearState();
					atm.write("Ejecting Card");
					atmcontext.setState(MainMap.Quit);
					atmcontext.getState().Entry(atmcontext);
				} catch (StateUndefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


		}

		private static final class MainMap_Idle extends MainMap_Default
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private MainMap_Idle(String s, int i)
			{
				super(s, i);
			}
			protected void Card( AtmContext context, java.lang.String pin, int sb, int cb )
			{
				Atm ctxt = context.getOwner();
				context.getState().Exit( context );
				context.clearState();
				try {
					ctxt.write( "Enter pin" );
					ctxt.makeCard( pin, sb, cb );
				} finally 
				{
					context.setState( MainMap.Validate );
					context.getState().Entry( context );
				}
				return;
			}

		}
		protected MainMap_Default(String s, int i)
		{
			super(s, i);
		}
	}

	static abstract class MainMap
	{

		public static final MainMap_Default.MainMap_Idle Idle = new MainMap_Default.MainMap_Idle("MainMap.Idle", 0);
		public static final MainMap_Default.MainMap_Validate Validate = new MainMap_Default.MainMap_Validate("MainMap.Validate", 1);
		public static final MainMap_Default.MainMap_SelectAccount SelectAccount = new MainMap_Default.MainMap_SelectAccount("MainMap.SelectAccount", 2);
		public static final MainMap_Default.MainMap_SelectCAction SelectCAction = new MainMap_Default.MainMap_SelectCAction("MainMap.SelectCAction", 3);
		public static final MainMap_Default.MainMap_SelectSAction SelectSAction = new MainMap_Default.MainMap_SelectSAction("MainMap.SelectSAction", 4);
		public static final MainMap_Default.MainMap_ProcessC ProcessC = new MainMap_Default.MainMap_ProcessC("MainMap.ProcessC", 5);
		public static final MainMap_Default.MainMap_ProcessS ProcessS = new MainMap_Default.MainMap_ProcessS("MainMap.ProcessS", 6);
		public static final MainMap_Default.MainMap_Quit Quit = new MainMap_Default.MainMap_Quit("MainMap.Quit", 7);
		private static final MainMap_Default Default = new MainMap_Default("MainMap.Default", -1);


		MainMap()
		{
		}
	}

	public static abstract class AtmState extends State
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected void Entry(AtmContext atmcontext)
		{
		}

		protected void Exit(AtmContext atmcontext)
		{
		}

		protected void Balance(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Card(AtmContext atmcontext, String s, int i, int j)
		{
			Default(atmcontext);
		}

		protected void Checking(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Deposit(AtmContext atmcontext, Integer i)
		{
			Default(atmcontext);
		}

		protected void Done(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Pin(AtmContext atmcontext, String s)
		{
			Default(atmcontext);
		}

		protected void Receipt(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Reset(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Savings(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Stop(AtmContext atmcontext)
		{
			Default(atmcontext);
		}

		protected void Withdrawal(AtmContext atmcontext, Integer i)
		{
			Default(atmcontext);
		}

		protected void Default(AtmContext atmcontext)
		{
			atmcontext.setState(MainMap.Default);
			//throw new TransitionUndefinedException((new StringBuilder()).append("State: ").append(atmcontext.getState().getName()).append(", Transition: ").append(atmcontext.getTransition()).toString());
		}

		protected AtmState(String s, int i)
		{
			super(s, i);
		}
	}


	public AtmContext(Atm atm)
	{
		super(MainMap.Idle);
		_owner = atm;
	}

	public AtmContext(Atm atm, AtmState atmstate)
	{
		super(atmstate);
		_owner = atm;
	}

	public void enterStartState()
	{
		getState().Entry(this);
	}

	public void Balance()
	{
		_transition = "Balance";
		getState().Balance(this);
		_transition = "";
	}

	public void Card(String s, int i, int j)
	{
		_transition = "Card";
		getState().Card(this, s, i, j);
		_transition = "";
	}

	public void Checking()
	{
		_transition = "Checking";
		getState().Checking(this);
		_transition = "";
	}

	public void Deposit(Integer i)
	{
		_transition = "Deposit";
		getState().Deposit(this, i);
		_transition = "";
	}

	public void Done()
	{
		_transition = "Done";
		getState().Done(this);
		_transition = "";
	}

	public void Pin(String s)
	{
		_transition = "Pin";
		getState().Pin(this, s);
		_transition = "";
	}

	public void Receipt()
	{
		_transition = "Receipt";
		getState().Receipt(this);
		_transition = "";
	}

	public void Reset()
	{
		_transition = "Reset";
		getState().Reset(this);
		_transition = "";
	}

	public void Savings()
	{
		_transition = "Savings";
		getState().Savings(this);
		_transition = "";
	}

	public void Stop()
	{
		_transition = "Stop";
		getState().Stop(this);
		_transition = "";
	}

	public void Withdrawal(Integer i)
	{
		_transition = "Withdrawal";
		getState().Withdrawal(this, i);
		_transition = "";
	}

	public AtmState getState()
			throws StateUndefinedException
	{
		if(_state == null)
			throw new StateUndefinedException();
		else
			return (AtmState)_state;
	}

	protected Atm getOwner()
	{
		return _owner;
	}

	public void setOwner(Atm atm)
	{
		if(atm == null)
		{
			throw new NullPointerException("null owner");
		} else
		{
			_owner = atm;
			return;
		}
	}

	private transient Atm _owner;
}
