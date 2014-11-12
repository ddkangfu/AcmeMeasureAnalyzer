//package com.acme;

import java.io.*;
import java.util.*;


public class Acem {
    //设定检测步骤，共10步
    static final String[] steps = {"Assembly", "Soldering", "Lathing", "Milling", "Grinding",
            "Forging", "Casting", "Clamping", "Punching", "Riveting"};
    //定义每一步的检测内容，共4项
    static final String[] measures = {"Temp", "Humidity", "STemp", "Duration"};

    public static void main(String[] args) {
        //System.out.println(args.length);
        if (args.length == 0) {
            System.out.println("usage: Acem <CSV File Name> [--html | --csv].");
            return;
        }
        try {
            Map<String, MeasureCell> result = readCSVFile(args[0]);
            String[] orderedColumns = getOrderedColumnNames(result);
            if (args.length > 1 && args[1].equalsIgnoreCase("--html"))
                outputToHTML(result, orderedColumns);
            else
                outputToCSV(result, orderedColumns);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map<String, MeasureCell> readCSVFile(String fileName) throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("FileName is null or blank string.");
        }

        File csvFile = new File(fileName);
        if (!csvFile.isFile()) {
            throw new Exception("Can not find file " + fileName + ".");
        }

        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(csvFile));
            Map<String, MeasureCell> result = new HashMap<String, MeasureCell>();
            String line = null;
            while((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                String cellName = columns[0].trim() + "-" + columns[1].trim();
                MeasureCell cell = null;
                if (result.containsKey(cellName)) {
                    cell = result.get(cellName);
                } else {
                    cell = new MeasureCell(columns[0].trim(), columns[1].trim());
                    result.put(cellName, cell);
                }
                cell.add(columns[2].trim());
            }
            return result;
        } catch(Exception ex) {
            throw new Exception("Read CSV File Error:" + ex.getMessage(), ex);
        } finally {
            if (br != null)
                br.close();
        }
    }

    public static String[] getOrderedColumnNames(Map<String, MeasureCell> cells) {
        Map<String, ColumnErrorCounter> counter = new HashMap<String, ColumnErrorCounter>();
        for (Map.Entry<String, MeasureCell> cell : cells.entrySet()) {
            for (String columnName : measures) {
                if (cell.getKey().endsWith(columnName)) {
                    ColumnErrorCounter errorCounter = null;
                    if (counter.containsKey(columnName)) {
                        errorCounter = counter.get(columnName);
                    } else {
                        errorCounter = new ColumnErrorCounter(columnName);
                        counter.put(columnName, errorCounter);
                    }
                    errorCounter.add(cell.getValue().getErrorCount());
                }
            }
        }

        ColumnErrorCounter[] columnArray = counter.values().toArray(new ColumnErrorCounter[0]);
        Arrays.sort(columnArray);

        String[] orderedColumns = new String[columnArray.length];
        for (int i = columnArray.length - 1; i >= 0; i--) {
            orderedColumns[i] = columnArray[columnArray.length - 1 - i].getColumnName();
            //System.out.println(orderedColumns[i]);
        }
        return orderedColumns;
    }

    public static void outputToCSV(Map<String, MeasureCell> result, String[] columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("Production Step");
        for (String column : columns) {
            sb.append("Avg." + column + "," + "Err." + column + ",");
        }
        System.out.println(Utils.formatStr(sb.toString()));

        for (String step : steps) {
            sb = new StringBuilder();
            sb.append(step + ",");
            for (String column : columns) {
                MeasureCell cell = result.get(step + "-" + column);
                if (cell != null)
                    sb.append(cell.getAvgValue() + "," + cell.getErrorCount() + ",");
                else
                    sb.append("0,0,");
            }
            System.out.println(Utils.formatStr(sb.toString()));
        }
    }

    public static void outputToHTML(Map<String, MeasureCell> result, String[] columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n<head>\n<title>Result</title>\n");
        sb.append("<style>th{background:#f1fedd;} tr{text-align:center;}</style>");
        sb.append("</head>\n<body>\n<table width=\"100%\" border=1>\n");
        sb.append("<thead>\n<tr>\n<th>Production Step</th>");
        for (String column : columns) {
            sb.append("<th>Avg." + column + "</th>\n<th>" + "Err." + column + "</th>\n");
        }
        sb.append("</tr>\n</thead>\n");

        for (String step : steps) {
            //sb = new StringBuilder();
            sb.append("<tr>\n<th>" + step + "</th>\n");
            for (String column : columns) {
                MeasureCell cell = result.get(step + "-" + column);
                if (cell != null) {
                    String formatedValue = String.format("%1$.4f", cell.getAvgValue());
                    sb.append("<td>" + formatedValue + "</td>\n<td>" + cell.getErrorCount() + "</td>\n");
                }
                else
                    sb.append("<td>0</td>\n<td>0</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n</body>\n</html>");
        System.out.println(sb.toString());
    }

}

class Utils {
    public static String formatStr(String str) {
        if (str.endsWith(","))
            return str.substring(0, str.length() - 1);
        return str;
    }
}


class MeasureCell {
    private String stepName;
    private String measureName;
    private float valueSum;
    private int normalCount;
    private int errorCount;

    public MeasureCell(String stepName, String measureName) {
        this.stepName = stepName;
        this.measureName = measureName;
        this.errorCount = 0;
        this.normalCount = 0;
        this.valueSum = 0;
    }

    public void add(String value) throws NumberFormatException{
        if (value.equalsIgnoreCase("ERROR")) {
            errorCount++;
        } else {
            float floatValue = Float.parseFloat(value);
            this.valueSum += floatValue;
            this.normalCount++;
        }
    }

    public float getAvgValue() {
        if (normalCount == 0 || valueSum == 0) {
            return 0;
        } else {
            return valueSum / normalCount;
        }
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    @Override
    public String toString() {
        return this.stepName + "-" + this.measureName + "=>" + this.getAvgValue() + "[" + this.errorCount + "]";
    }
}


class ColumnErrorCounter implements Comparable<ColumnErrorCounter> {
    private String columnName;
    private int count;

    public ColumnErrorCounter(String columnName) {
        this.columnName = columnName;
    }

    public void add(int errorCount) {
        this.count += errorCount;
    }

    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public int compareTo(ColumnErrorCounter other) {
        if (this.count > other.count)
            return 1;
        else if (this.count < other.count)
            return -1;
        else
            return 0;
    }
}