package com.acme.com.acme.measure;


public class ErrorCountItem implements Comparable<ErrorCountItem> {
    public String measureName;
    public int count;

    public ErrorCountItem(String measureName) {
        this.measureName = measureName;
        this.count = 0;
    }

    public void add(int inc) {
        this.count += inc;
    }

    public String getMeasureName() {
        return this.measureName;
    }

    @Override
    public int compareTo(ErrorCountItem errorCountItem) {
        if (this.count > errorCountItem.count)
            return 1;
        else if (this.count < errorCountItem.count)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "ErrorCountItem{" +
                "measureName='" + measureName + '\'' +
                ", count=" + count +
                '}';
    }
}
