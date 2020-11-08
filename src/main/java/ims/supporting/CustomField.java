package ims.supporting;

import ims.enums.State;

public class CustomField {
    private State state;
    private String fieldValue;
    private String message;
    private String style;

    public CustomField() {
        this.fieldValue = "";
        this.state = State.VALID;
        this.message = "";
    }

    public CustomField(String fieldValue){
        this.fieldValue = fieldValue;
        this.state = State.VALID;
        this.message = "";
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}