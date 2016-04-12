package com.esoft.core.jsf.validator;

import java.util.HashSet;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.component.validator.ValidateMultipleFields;

@FacesComponent(ValidateEqual.COMPONENT_TYPE)
public class ValidateEqual extends ValidateMultipleFields {

	// Public constants
	// -----------------------------------------------------------------------------------------------

	/** The standard component type. */
	public static final String COMPONENT_TYPE = "com.esoft.core.validator.ValidateEqual";

	// Private constants
	// ----------------------------------------------------------------------------------------------

	private static final String DEFAULT_MESSAGE = "{0}: Please fill out the same value for all of those fields";

	// Constructors
	// ---------------------------------------------------------------------------------------------------

	private boolean validationFailed;

	/**
	 * The default constructor sets the default message.
	 */
	public ValidateEqual() {
		super(DEFAULT_MESSAGE);
	}

	// Actions
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Validate if all values are equal.
	 */
	@Override
	public boolean validateValues(FacesContext context, List<UIInput> inputs,
			List<Object> values) {
		return (new HashSet<Object>(values).size() == 1);
	}

	@Override
	protected void validateComponents(FacesContext context) {
		if (isDisabled()) {
			return;
		}

		List<UIInput> inputs = collectComponents();

		if (inputs.isEmpty()) {
			return;
		}

		List<Object> values = collectValues(inputs);

		for (int i = 0; i < inputs.size(); i++) {
			UIInput input = inputs.get(i);
			if (input.isRequired()) {
				Object value = values.get(i);
				if (value == null || StringUtils.isEmpty(value.toString())) {
					return;
				}
			}
		}

		if (!validateValues(context, inputs, values)) {
			int i = 0;

			for (UIInput input : inputs) {
				input.setValid(!(isInvalidateAll() || shouldInvalidateInput(
						context, input, values.get(i))));
				i++;
			}

			validationFailed = true;
			context.validationFailed();
			showMessage(context, inputs);
		}

	}

	public boolean isValidationFailed() {
		return validationFailed;
	}

}