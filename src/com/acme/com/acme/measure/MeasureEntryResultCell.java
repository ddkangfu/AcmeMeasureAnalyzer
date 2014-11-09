package com.acme.com.acme.measure;


public class MeasureEntryResultCell {
    protected String stepName;
    protected String measureName;
    //protected String cellName;
    protected float sum;
    protected int errorCount;
    protected int normalCount;

    public MeasureEntryResultCell(String stepName, String measureName) {
        this.stepName = stepName;
        this.measureName = measureName;
        this.sum = 0;
        this.errorCount = 0;
        this.normalCount = 0;
    }

    public void add(String value) throws NumberFormatException {
        if (value.equalsIgnoreCase("ERROR"))
            errorCount++;
        else {
            float floatValue = Float.parseFloat(value);
            sum += floatValue;
            normalCount++;
        }
    }

    public float getAvgValue() {
        if (sum > 0 && normalCount > 0)
            return sum / normalCount;
        else
            return 0;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    @Override
    public String toString() {
        return stepName + "-" + measureName + "[" + this.getAvgValue() + ", " + this.getErrorCount() + "]";
    }
}
