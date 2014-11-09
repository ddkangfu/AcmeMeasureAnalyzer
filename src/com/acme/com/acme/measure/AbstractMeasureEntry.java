package com.acme.com.acme.measure;

public abstract class AbstractMeasureEntry {
    protected String stepName;
    protected String measureName;
    protected float value;
    protected boolean isError;

    public AbstractMeasureEntry(String stepName, String measureName, float value, boolean isError) {
        this.stepName = stepName;
        this.measureName = measureName;
        this.value = value;
        this.isError = isError;
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public abstract float getValue();

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public abstract int getCount();

    @Override
    public String toString() {
        return stepName + "-" + measureName;
    }
}
