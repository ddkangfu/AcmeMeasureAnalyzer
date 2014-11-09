package com.acme.com.acme.io;


import com.acme.com.acme.measure.NormalMeasureEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

public class CSVFileOperation {
    public static void readCSVFile(String fileName) throws Exception {
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
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                //while (st.hasMoreTokens()) {
                    //NormalMeasureEntry me = new NormalMeasureEntry()
                    //System.out.print(st.nextToken() + "\t");
                //}
                NormalMeasureEntry me = new NormalMeasureEntry(st.nextToken().trim(), st.nextToken().trim(), 0);
                System.out.println(me);
            }
        } finally {
            if (br != null)
                br.close();
        }
    }

}
