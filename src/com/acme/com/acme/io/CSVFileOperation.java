package com.acme.com.acme.io;


import com.acme.com.acme.measure.AbstractMeasureEntry;
import com.acme.com.acme.measure.ErrorMeasureEntry;
import com.acme.com.acme.measure.MeasureEntryResultCell;
import com.acme.com.acme.measure.NormalMeasureEntry;

import java.io.*;
import java.util.*;

public class CSVFileOperation {
    public static final String[] stepList = {"Assembly", "Soldering", "Lathing", "Milling", "Grinding", "Forging", "Casting", "Clamping", "Punching", "Riveting"};
    public static final String[] measureList = {"Temp", "STemp", "Humidity", "Duration"};

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

            for (Map.Entry<String, MeasureEntryResultCell> cell : cellMap.entrySet()) {
                System.out.println(cell.getValue().toString());
            }

            return cellMap;
        } finally {
            if (br != null)
                br.close();
        }
    }

    public static void writeCSVFile(String fileName, Map<String, MeasureEntryResultCell> result) throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("No fileName.");
        }

        File sourceFile = new File(fileName);

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(sourceFile, false));
            for (String stepName : stepList) {
                bw.newLine();
                StringBuffer sb = new StringBuffer();
                for (String measureName : measureList) {
                    //System.out.println(stepName + "-" + measureName);
                    String cellName = stepName + "-" + measureName;
                    if (result.containsKey(cellName)) {
                        String formatValue = String.format("%1$.4f", result.get(cellName).getAvgValue());
                        sb.append(formatValue + "," + result.get(cellName).getErrorCount() + ",");
                    }
                    else
                        sb.append("0,");
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
