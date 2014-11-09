package com.acme;

import com.acme.com.acme.io.CSVFileOperation;

public class Main {

    public static void main(String[] args) {
        System.out.println("OK..." + args.length);
	    for (String arg : args){
            System.out.println(arg);
            try {
                CSVFileOperation.readCSVFile(args[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
