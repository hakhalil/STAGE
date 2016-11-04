package TestDriver;


/**
 * OrdSet
 * 
 * This class implements an ordered set of integers; it supports 
 * the basic sets operation: union as well as search, removal and 
 * insertion.
 */

public class OrdSet extends CaseStudy {
	
	// ---------
	// Constants
	// ---------
	public static final int min_set_size = 2;
	public static final int max_set_size = 6;
	public static final int max_accepted_resizes = 2;
	
	//The maximum current set size; in other words, the maximum allowed number 
	// of elements before a resize
	private int _set_size;
	
	//The number of resizes made on the set
	private int _resized_times;
	
	//The index of the last element
	private int _last;
	
	//representation of the OrdSet as an array; note that _last + 1 = 
	// _set.length
	private int[] _set;
	
	//Flag that becomes true when trying to add new elements to the Ordered set 
	//after reaching the maximum set size and the maximum accepted resizes
	private boolean _overflow;
	
	// ---------
	// Private methods
	// ---------
	/**
	 * Determines the size to set the array to from any given size. This
	 * method ensures that the size of the array is at least equal to and
	 * always the closest multiple of min_set_size to the proposed size n.
	 * If the proposed size is greater than the max_set_size, the returned
	 * size is max_set_size. 
	 * Note that no changes are made to the instance of OrdSet.
	 * 
	 * @param n - The proposed size to set the array to, n is expected to be >=0
	 * @return The new size for the array, as a multiple of min_set_size.
	 */
	private int defSetSize(int n) {
		try {
			int mod;
			if (n <= min_set_size) return min_set_size;
			if (n >= max_set_size) return max_set_size;
			mod = n / min_set_size;
			if (mod * min_set_size == n) return n;
			else return (mod+1)*min_set_size;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			currentState = "Failed";
		} 
		return -1;
	}
	
	/**
	 * Initializes the array. Sets all of the variables to their default.
	 * It also copies the provided values into the ordered set up to the
	 * set size. 
	 * 
	 * @param v - The array of values to copy from into the ordered set.
	 * @param n - The size proposed for the ordered set.
	 */
	private void initSetArray (Integer[] v) {
		try {
			int k;
					
			for (k = 0; k < v.length; k++) {
				try {
					if (! _overflow) this.add(v[k]);
				} catch (OverflowException e) {
					currentState = "Failed";
					System.out.println("Array is too big.");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			currentState = "Failed";
			e.printStackTrace();
		}
	}
	
	/**
	 * Increments the size of the array, if possible, by min_set_size.
	 * If the maximum size or the maximum resizes is reached, _overflow
	 * is set to true.
	 */
	private void resizeArray() {
		try {
			int new_size = _set_size + min_set_size;
			
			if (new_size <= max_set_size && _resized_times < max_accepted_resizes) {
				int[] _new_set = new int[new_size];
				for (int k = 0; k < _last+1; k++) {
					_new_set[k] = _set[k];
				}
				
				_set_size = new_size;
				_set = _new_set;
				_resized_times++;
			} else {
				_overflow = true;
			}
			setCurrentState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			currentState = "Failed";
			e.printStackTrace();
		}
	}
	
	/**
	 * Performs a binary search on the provided array for the element x.
	 * A binary search is a search algorithm for finding a particular value 
	 * in a list of data. Binary search assumes the data is sorted. The 
	 * search begins by examining the value in the center of the list; 
	 * because the values are sorted, it then knows whether the value occurs 
	 * before or after the center value, and searches through the correct 
	 * half in the same way.
	 * 
	 * @param a - The array to search.
	 * @param nElts - The number of elements to search in array (first nElts).
	 * @param x - The value to search the array for.
	 * @return The index of the value if it is found, and -1 otherwise.
	 */
	private int binSearch(int[] a, int nElts, int x) {
		try {
			int i=0;
			int j=nElts-1;
			int m=0;
			
//		The array is of size 0
			if (j<0) return -1; 
			
//		All elements in the array are of smaller value than the searched value x
			if (a[j] < x)  return -1; 

// 		Applying the search algorithm by first examining the value of the center
			while(i<j){
				m= (i+j)/2;
				
//		Changing the indexes of search to choose the appropriate half of the 
//		array to search in
				if (x>a[m]) {
					i=m+1;
				} else { 
					j=m;
				}
			}

// 		if the value is found, return its index
			if (x == a[i]) return i;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			currentState = "Failed";
			//e.printStackTrace();
		}
		
//		if the value is not found, return -1
		return -1;
	}
	
	// ---------
	// Constructors
	// ---------
	/**
	 * Create an empty instance of OrdSet with default values and
	 * a size based on the proposed size.
	 *  
	 * @param size - The proposed size
	 */
	public OrdSet (Integer size) {
		try {
			_set_size = defSetSize(size);
			_set = new int[_set_size];
			_last = -1;
			_resized_times = 0;
			_overflow = false;
			setCurrentState();
		} catch (Exception e) {
			currentState = "Failed";;
		}
	}
	
	/**
	 * Create by copy from an array of values. The ordered set size
	 * will be defined based on the length of the parameter array v; 
	 * if the length of v exceeds max_set_size, only the first values in
	 * the array up to max_set_size will be considered and the set  
	 * would have a size of max_set_size 
	 * 
	 * @param v - The array of values to copy from into the ordered set.
	 */
	public OrdSet(Integer[] v) {
		this(v.length);
		try {
			initSetArray(v);
			setCurrentState();
		} catch (Exception e) {
			currentState = "Failed";
		}
	}
	
	// ----------
	// Get methods
	// ----------
	/**
	 * @return The times the array has been resized
	 */
	public int getResizedTimes() {
		return _resized_times;
	}
	/**
	 * @return The actual size of the ordered set (the actual number of elements
	 * in the ordered set)
	 */
	public int getActualSize() {
		return _last +1;
	}
	
	/**
	 * @return The current maximum size of the ordered set (the current allowed 
	 * number of elements before resizing).
	 */
	public int getSetSize() {
		return _set_size;
	}
	
	/**
	 * @return The index of the last element in the ordered set.
	 */
	public int getSetLast() {
		return _last;
	}
	
	/**
	 * @return True if the ordered set is empty and false otherwise.
	 */
	public boolean isEmpty() {
		if (_last < 0) return true;
		return false;
	}
	
	/**
	 * @return The ordered set as an array (note that it may include zeros for the empty slots)
	 */
	private int[] getSetArray() {
		return _set;
	}
	
	/**
	 * @return The elements of the ordered set as an array
	 */
	public int[] getSetElements() {
		try {
			int[] elts = new int[_last+1];
			for (int i =0; i<=_last; i++) {
				elts[i] = _set[i];
			}
			return elts;
		} catch (Exception e) {
			currentState = "Failed";
			return null;
		}
	}	
	
	/**
	 * @return The overflow flag
	 */
	public boolean isOverflow() {
		return _overflow;
	}
	
	// ---------
	// Operators.
	// ---------
	
	/**
	 * Compares two ordered sets for equality.
	 * @param x - The ordered set to check for equality.
	 * @return 0 if the sets are equal (have the same elements, 
	 * 		   1 if they are the same size, but not equal,
	 * 		   and the difference in size otherwise.
	 */
	public int equals(OrdSet x) {
		int k,n;
		
		try {
			if (x.getSetLast() != _last) {
				return (_last - x.getSetLast());
			}
			
			if (isEmpty() && x.isEmpty()) return 0;
			
			for(k = 0, n = 0; n < (x.getSetLast() + 1); n++) {
				k = x.elementAt(n);
				if (k != _set[n]) {
					return 1;
				}
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
		
		return 0;
	}

	/**
	 * Checks if the ordered set contains the indicated value.
	 * @param n - The value to check for in the ordered set.
	 * @return True if a match is found and false otherwise.
	 */
	public boolean contains(int n) {
		return binSearch(_set, _last+1, n) >= 0;
	}
	
	/**
	 * Checks if this ordered set contains the ordered set given by x,
	 * all elements in x are elements in this.
	 * @param x - The ordered set to check for.
	 * @return true if x is a subset of this ordered set, and false otherwise.
	 */
	public boolean contains (OrdSet x) {
		try {
			int k, n;
			
			for(n = 0, k = 0; n < (x.getSetLast() + 1); n++) {
				k = x.elementAt(n);
				if (binSearch(_set, _last+1, k) < 0) {
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			currentState = "Failed";
			return false;
		}
	}
	
	/**
	 * Removes an element from the set. The element to remove is given 
	 * by the val parameter.
	 * 
	 * @param val - The value of the element to remove from the set.
	 * @return true if the value was found and removed, and false otherwise.
	 */
	public boolean remove (Integer val) throws OverflowException{
		try {
			if (_overflow) return false;
			int where = binSearch(_set, _last + 1, val);
			if (where >= 0) {
				int k;
				for (k = where; k < _last; k++) {
					_set[k] = _set[k+1];
				}
				_last --;
			
				_set_size = (_last/min_set_size)*min_set_size+min_set_size;
				
				setCurrentState();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			currentState = "Failed";
		}
		return _overflow;
		
	}
	
	/**
	 * Adds an item to the set. Since this is a set, the element is only 
	 * added if it is unique. An attempt to add a new element while the set
	 * is full causes a set resize. When no resizes are allowed, an 
	 * overflow exception is raised.
	 * 
	 * @param n - The element to add to the ordered set. 
	 */
	public void add(Integer n) throws OverflowException{
	//	if (_overflow) throw new OverflowException();
		// if it is already in the set we return.
		try {
			if (this.contains(n)) return;
			if(!_overflow){
				if (_last+1 >= _set_size) {
					this.resizeArray();
					setCurrentState();
					/*if (_overflow) {
						throw new OverflowException();
					}*/
				}
				if(! _overflow)
				{
					int i = make_a_free_slot(n);
					_set[i] = n;
					_last++;
					setCurrentState();
				}
			}
		} catch (Exception e) {
			currentState = "Failed";
		}
	}
	 
	/**
	 * Returns the element at the provided index.
	 * 
	 * @param where - The position of the element to return or -1 if the item is
	 * out of bound.
	 * @return The ordered set element at the given position.
	 */
	public int elementAt(int where) {
		if ((where < 0) || (where > _last)) {
			System.err.println("Out of bound element: " + where);
			return -1;
		}
		return _set[where];
	}
	
	/**
	 * Makes a free slot in the array, to place the value n into. 
	 * This free slot will be made where it is appropriate to store
	 * the value n and maintain the ordering of the set.
	 * 
	 * @param n - The value to be stored in the array, for which the free slot 
	 * is being made.
	 * @return The index of the new slot in which to place n.
	 */
	private int make_a_free_slot(int n) {
		int where = _last + 1;
		
		while ((where-1>=0) && (_set[where-1] > n)) {
			_set[where] = _set[where-1];
			where--;
		}
		return where;
	}
	 
	/**
	 * Union of two sets. It returns an instance of OrdSet that is the union of 
	 * "this" instance and s2. If the size of the union is greater than 
	 * max_set_size an overflow exception is thrown
	 * 
	 * @param s2 The second set to perform the union on.
	 * @return The union of the two sets, "this" and s2.
	 */
	public OrdSet union(OrdSet s2) {
		int[] set1 = this.getSetArray();
		int[] set2 = s2.getSetArray();
		
		int size1 = this.getSetLast() + 1;
		int size2 = s2.getSetLast() + 1;
		
		OrdSet set = new OrdSet(size1 + size2);
		
		int lb1 = 0, lb2 = 0, pos = 0;
		try {
			while (lb1 < size1 && lb2 < size2) {
				if (set1[lb1] < set2[lb2]) {
					set.add(set1[lb1]);
					pos = pos + 1;
					lb1 = lb1 + 1;
				} else if (set2[lb2] < set1[lb1]) {
					set.add(set2[lb2]);
					pos = pos + 1;
					lb2 = lb2 + 1;
				} else if (set1[lb1] == set2[lb2]) {
					set.add(set2[lb2]);
					pos = pos + 1;
					lb1 = lb1 + 1;
					lb2 = lb2 + 1;
				} else {
					System.exit(1);
				}
			}
		
			while (lb1 < size1) {
				set.add(set1[lb1]);
				pos = pos + 1;
				lb1 = lb1 + 1;
			}
		
			while (lb2 < size2) {
				set.add(set2[lb2]);
				pos = pos + 1;
				lb2 = lb2 + 1;
			}
		
			// set the last element index.
			set._last = pos - 1;
		} catch (OverflowException e) {
			currentState = "Failed";
			System.out.println ("The union caused an overflow; the union size exceeds the maximum set size");
		}
		return set;
	}

	/**
	 * 
	 * @return The string representation of this ordered set, where each integer
	 * in the set is separated from the next one by a space character.
	 */
	public String toString() {
		try {
			String output = "";
			int k = 0;
			
			for (k = 0; k <= _last; k++)
				output += "" + _set[k] + " ";
			
			return output;
		} catch (Exception e) {
			currentState = "Failed";
			return null;
		}
	}

	//getting the current state of the object
	@Override
	public String getCurrentState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	//setting the current state of the object
	@Override
	public void setCurrentState() {
		
		//Empty state
		if (getResizedTimes() <= max_accepted_resizes &&
				getSetLast() == -1 &&
				getActualSize() == 0 &&
				getSetElements().length == 0 &&
				!isOverflow() )
		
		{
			currentState = "Empty";
			return;
		}
		//PartiallyFilled state
		if (getResizedTimes() <= max_accepted_resizes &&
				getSetLast() >= 0 &&
				getSetLast() < (getSetSize() - 1)&&
				getActualSize() <  getSetSize() &&
				getSetSize() > 0 &&
				!isOverflow() ) {
				if(getSetLast() == 0)
					currentState = "PartiallyFilled1";
				else if(getSetLast() == 2)
					currentState = "PartiallyFilled3";
				else if(getSetLast() == 4)
					currentState = "PartiallyFilled5";
			return;
		}
		//Filled state
		if (getResizedTimes() <= OrdSet.max_accepted_resizes &&
				getSetLast() == getSetSize() - 1 &&
				getActualSize() ==  getSetSize() &&
				!isOverflow() ) {
				if(getActualSize() == 2)
					currentState = "Filled2";
				else if(getActualSize() == 4)
					currentState = "Filled4";
				else if(getActualSize() == 6)
					currentState = "Filled6";
			return;
		}
		//Overflow state
		if ((getResizedTimes() == OrdSet.max_accepted_resizes ||
				getSetSize() == OrdSet.max_set_size) &&
				getSetLast() == getSetSize() - 1 &&
				getActualSize() ==  getSetSize() &&
				isOverflow() ) {
			currentState = "OverFlow";
		
		}
		//


	}
}