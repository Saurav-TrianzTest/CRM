package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV test utility that reads from Amazon S3 instead of local file system.
 */
public class CSVTest {

    public static void main(String[] args) {
        // In cloud environment, specify S3 key instead of using file dialog
        String s3Key = System.getenv().getOrDefault("CSV_S3_KEY", "data/sample.csv");
        
        try (InputStream inputStream = ReadDataUtils.readFileFromS3(s3Key)) {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }
            }
            reader.close();
            
            System.out.println("Successfully processed CSV from S3: " + s3Key);
        } catch (IOException e) {
            System.err.println("Failed to read CSV from S3: " + s3Key);
            e.printStackTrace();
        }
    }

}
