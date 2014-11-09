package com.acme.com.acme.measure;


public class ErrorMeasureEntry extends AbstractMeasureEntry {
    public ErrorMeasureEntry(String stepName, String measureName) {
        super(stepName, measureName, 0, true);
    }

    @Override
    public float getValue() {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
