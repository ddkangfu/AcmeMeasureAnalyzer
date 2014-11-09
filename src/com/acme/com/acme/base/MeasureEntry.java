package com.acme.com.acme.base;


public class MeasureEntry {
    private String stepName;
    private float value;
    private boolean isError;

    public MeasureEntry(String stepName, float value, boolean isError) {
        this.stepName = stepName;
        this.value = value;
        this.isError = isError;
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }
}
