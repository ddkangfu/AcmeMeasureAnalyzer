package com.acme.com.acme.io;


import com.acme.com.acme.measure.ErrorCountItem;
import com.acme.com.acme.measure.MeasureEntryResultCell;

import java.io.*;
import java.util.*;

public class CSVFileOperation {
    public static final String[] stepList = {"Assembly", "Soldering", "Lathing", "Milling", "Grinding", "Forging", "Casting", "Clamping", "Punching", "Riveting"};
    public static final String[] measureList = {"Temp", "Humidity", "STemp", "Duration"};

    public static Map<String, MeasureEntryResultCell> readCSVFile(String fileName) throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("No fileName.");
        }

        File sourceFile = new File(fileName);
        if (!sourceFile.isFile()) {
            throw new Exception("invalid fileName");//FileNotFoundException, IOException
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(sourceFile));

            String line = "";
            Map<String, MeasureEntryResultCell> cellMap = new HashMap<String, MeasureEntryResultCell>();

            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                String stepName = st.nextToken().trim();
                String measureName = st.nextToken().trim();
                String value = st.nextToken().trim();

                String cellName = stepName + '-' + measureName;
                if (!cellMap.containsKey(cellName))
                    cellMap.put(cellName, new MeasureEntryResultCell(stepName, measureName));

                cellMap.get(cellName).add(value);
            }

            return cellMap;
        } finally {
            if (br != null)
                br.close();
        }
    }

    public static String[] getOrderedColumns(Map<String, MeasureEntryResultCell> cellMap) {
        Map<String, ErrorCountItem> error_count = new HashMap<String, ErrorCountItem>();
        for (Map.Entry<String, MeasureEntryResultCell> cell : cellMap.entrySet()) {
            //统计每列的错误数，以决定各列的排列顺序
            for (String columnName : measureList) {
                if (cell.getKey().endsWith(columnName)) {
                    int counter = cell.getValue().getErrorCount();
                    ErrorCountItem errorCountItem;
                    if (error_count.containsKey(columnName))
                        errorCountItem = error_count.get(columnName);
                    else{
                        errorCountItem = new ErrorCountItem(columnName);
                        error_count.put(columnName, errorCountItem);
                    }
                    errorCountItem.add(counter);
                }
            }
        }

        ErrorCountItem[] errorCountArray = new ErrorCountItem[error_count.size()];
        error_count.values().toArray(errorCountArray);

        Arrays.sort(errorCountArray);

        String[] orderedColumns = new String[errorCountArray.length];
        for (int i = errorCountArray.length - 1; i >= 0; i--) {
            //System.out.println(errorCountArray[i]);
            orderedColumns[errorCountArray.length - 1 - i] = errorCountArray[i].getMeasureName();
        }

        return orderedColumns;
    }

    public static void writeCSVFile(String fileName, Map<String, MeasureEntryResultCell> result) throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("No fileName.");
        }

        File sourceFile = new File(fileName);

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(sourceFile, false));
            String[] orderedColumnNames = getOrderedColumns(result);
            System.out.print("Production Step,\t");
            for (String columnName : orderedColumnNames) {
                System.out.print("Avg." + columnName + ",\t" + "Err." + columnName + ",\t");
            }
            System.out.print("\n");

            for (String stepName : stepList) {
                bw.newLine();
                StringBuilder sb = new StringBuilder();
                System.out.print(stepName + ",\t");
                for (String measureName : orderedColumnNames) {
                    //System.out.println(stepName + "-" + measureName);
                    String cellName = stepName + "-" + measureName;
                    if (result.containsKey(cellName)) {
                        String formatValue = String.format("%1$.4f", result.get(cellName).getAvgValue());
                        sb.append(formatValue + ",\t" + result.get(cellName).getErrorCount() + ",\t");
                    }
                    else
                        sb.append("0.0000,");
                }
                String csvLine = sb.toString();
                //bw.write(csvLine.substring(csvLine.length() - 1));
                System.out.println(csvLine);
            }
        } finally {
            if (bw != null)
                bw.close();
        }
    }
}
