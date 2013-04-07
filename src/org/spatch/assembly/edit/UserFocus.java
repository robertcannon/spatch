package org.spatch.assembly.edit;
 
public class UserFocus {

	
	static UserFocus instance;
	
	
	public static UserFocus get() {
		if (instance == null) {
			instance = new UserFocus();
		}
		return instance;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
