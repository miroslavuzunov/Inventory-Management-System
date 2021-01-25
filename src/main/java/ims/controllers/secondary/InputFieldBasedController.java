package ims.controllers.secondary;

import ims.enums.State;
import ims.supporting.CustomField;

import java.util.HashMap;
import java.util.Map;

public abstract class InputFieldBasedController {
    public static final String EMPTY_FIELD_MSG = "Empty field forbidden!";
    public static final String INVALID_INFO_MSG = "Invalid information entered!";
    public static final String CLEAN_MSG = "";

    protected Map<String, CustomField> customFieldsByName = new HashMap<>();

    protected boolean handleEmptyFields() {
        initializeCustomFieldMap();
        boolean handlingResult = true;

        for (CustomField field : customFieldsByName.values()) {
            if ((field.getFieldValue() == null || field.getFieldValue().isEmpty()) && !field.isNullable()) {
                editField(field, State.INVALID, EMPTY_FIELD_MSG);
                handlingResult = false;
            } else {
                editField(field, State.VALID, CLEAN_MSG);
            }
        }
        displayMessages(customFieldsByName);

        return handlingResult;
    }

    protected void editField(CustomField inputField, State state, String message) {
        if (state.equals(State.VALID)) {
            inputField.setStyle("-fx-border-width: 1px;" + "-fx-border-color: lightgrey;" + "-fx-border-radius: 3px");
        } else {
            inputField.setStyle("-fx-border-width: 1px;" + "-fx-border-color: red;" + "-fx-border-radius: 3px");
        }

        inputField.setState(state);
        inputField.setMessage(message);
    }

    protected abstract void initializeCustomFieldMap();

    protected abstract void displayMessages(Map<String, CustomField> fieldsByName);
}
