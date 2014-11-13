//package com.acme;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Acem2 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Acem2 <CSV File> [--html|--csv]");
            return;
        }

        try {
            Map<String, MeasureCellInfo> result = readCSVFile(args[0]);
            for (Map.Entry<String, MeasureCellInfo> entry : result.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map<String, MeasureCellInfo> readCSVFile(String fileName) throws Exception {
        if (fileName == null || fileName.isEmpty())
            throw new Exception("File Name is null or blank");

        File csvFile = new File(fileName);
        if (!csvFile.exists())
            throw new Exception("File is not exist!");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));

            Map<String, MeasureCellInfo> result = new HashMap<String, MeasureCellInfo>();

            String line = null;
            while((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                String cellName = columns[0].trim() + "-" + columns[1].trim();

                MeasureCellInfo cell = result.get(cellName);
                if (cell == null) {
                    cell = new MeasureCellInfo(columns[0].trim(), columns[1].trim());
                    result.put(cellName, cell);
                }
                cell.add(columns[2].trim());
            }
            return result;
        } catch (Exception ex) {
            throw new Exception("Read CSV file error: " + ex.getMessage());
        } finally {
            if (br != null)
                br.close();
        }
    }
}

class MeasureCellInfo {
    String stepName;
    String measureName;
    int normalCount;
    int errorCount;
    float sum;

    public MeasureCellInfo(String stepName, String measureName) {
        this.stepName = "";
        this.measureName = "";
        this.normalCount = 0;
        this.errorCount = 0;
        this.sum = 0.0f;
    }

    public void add(String value) throws NumberFormatException {
        if (value.equalsIgnoreCase("ERROR")) {
            errorCount++;
        } else {
            normalCount++;
            this.sum += Float.parseFloat(value);
        }
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public float getAvgValue() {
        if (normalCount == 0 || sum == 0)
            return 0;
        else
            return sum / normalCount;
    }

    @Override
    public String toString() {
        return stepName + "-" + measureName + "=>" + getAvgValue() + "[" + getErrorCount() +  "]";
    }
}

class ColumnErrCounter implements Comparable<ColumnErrCounter> {
    String columnName;
    int errorCount;

    public ColumnErrCounter(String columnName) {
        this.columnName = columnName;
    }

    public void add(int number) {
        errorCount += number;
    }

    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int compareTo(ColumnErrCounter other) {
        if (this.errorCount > other.errorCount)
            return 1;
        else if (this.errorCount < other.errorCount)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return columnName + "[" + errorCount + "]";
    }
}