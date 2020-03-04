package bll.validators;

import java.util.regex.Pattern;

import model.Customer;

public class FirstName  implements Validator<Customer>{
	
	
	private static final String FirstName_PATTERN = "[A-Z][a-zA-Z]*";
	public void validate(Customer t) {
		Pattern pattern = Pattern.compile(FirstName_PATTERN);
		if (!pattern.matcher(t.getFirst_name()).matches()) {
			throw new IllegalArgumentException("First name is not a valid email!");
		}
	}
	
}
