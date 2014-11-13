package com.acme;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Acem2 {
    public static void Main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Acem2 <CSV File> [--html|--csv]");
            return;
        }

        try {
            readCSVFile(args[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void readCSVFile(String fileName) throws Exception {
        if (fileName == null || fileName.isEmpty())
            throw new Exception("File Name is null or blank");

        File csvFile = new File(fileName);
        if (!csvFile.exists())
            throw new Exception("File is not exist!");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));

            String line = null;
            while((line = br.readLine()) != null) {
                String[] columns = line.split(",");

            }
        } catch (Exception ex) {
            throw new Exception("Read CSV file error: " + ex.getMessage());
        } finally {
            if (br != null)
                br.close();
        }
    }
}
