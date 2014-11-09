package com.acme;

import com.acme.com.acme.io.CSVFileOperation;
import com.acme.com.acme.measure.MeasureEntryResultCell;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("OK..." + args.length);
	    for (String arg : args){
            System.out.println(arg);
            try {
                Map<String, MeasureEntryResultCell> cellMap = CSVFileOperation.readCSVFile(args[0]);
                CSVFileOperation.writeCSVFile(args[1], cellMap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
