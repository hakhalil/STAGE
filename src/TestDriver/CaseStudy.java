package TestDriver;

/**
 * @author Hoda
 *CaseStudy class should be the base class for all case studies (e.g. ordSet, cruiseControl,...etc). When implementing a new
 *case study, bplease inherit from this class
 */
public abstract class CaseStudy {
	
	protected String currentState;
	
	public abstract String getCurrentState();
	
	public abstract void setCurrentState();

}
