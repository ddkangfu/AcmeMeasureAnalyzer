package com.acme.com.acme.measure;


public class NormalMeasureEntry extends AbstractMeasureEntry{

    public NormalMeasureEntry(String stepName, String measureName, float value) {
        super(stepName, measureName, value, true);
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getErrorCount() {
        return 0;
    }
}
