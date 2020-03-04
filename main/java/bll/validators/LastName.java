package bll.validators;

import java.util.regex.Pattern;

import model.Customer;

public class LastName implements Validator<Customer>{
	
	private static final String LastName_PATTERN = "[a-zA-z]+([ '-][a-zA-Z]+)*";

	public void validate(Customer t) {
		Pattern pattern = Pattern.compile(LastName_PATTERN);
		if (!pattern.matcher(t.getLast_name()).matches()) {
			throw new IllegalArgumentException("Last names is not a valid email!");
		}
	}

}
