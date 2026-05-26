package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing utility using Amazon EFS for persistent file storage.
 * This class demonstrates POSIX-compliant file access patterns suitable for cloud environments.
 * 
 * Files can be accessed from:
 * 1. Amazon EFS mounted volume (for persistent, shared storage)
 * 2. Classpath resources (for bundled application resources)
 */
public class CSVTest {

    /**
     * EFS mount point for CSV file storage.
     * Configure this via environment variable: CSV_STORAGE_PATH
     * Default: /mnt/efs/csv
     */
    private static final String CSV_STORAGE_PATH = System.getenv().getOrDefault("CSV_STORAGE_PATH", "/mnt/efs/csv");

    /**
     * Reads CSV file from Amazon EFS mounted volume.
     * EFS provides POSIX-compliant shared file access across multiple container instances.
     * 
     * @param fileName the name of the CSV file to read
     * @return List of string arrays representing CSV rows
     * @throws IOException if file reading fails
     */
    public static List<String[]> readCsvFromEFS(String fileName) throws IOException {
        Path filePath = Paths.get(CSV_STORAGE_PATH, fileName);
        
        if (!Files.exists(filePath)) {
            throw new IOException("CSV file not found in EFS: " + filePath);
        }
        
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        
        System.out.println("Successfully read " + data.size() + " rows from EFS: " + filePath);
        return data;
    }
    
    /**
     * Reads CSV file from classpath resources (bundled with application).
     * This is suitable for static reference data that doesn't change.
     * 
     * @param resourcePath the classpath resource path (e.g., "data/sample.csv")
     * @return List of string arrays representing CSV rows
     * @throws IOException if resource reading fails
     */
    public static List<String[]> readCsvFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        
        if (!resource.exists()) {
            throw new IOException("CSV resource not found in classpath: " + resourcePath);
        }
        
        List<String[]> data = new ArrayList<>();
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader isr = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(isr)) {
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        
        System.out.println("Successfully read " + data.size() + " rows from classpath: " + resourcePath);
        return data;
    }
    
    /**
     * Processes CSV data with filtering logic.
     * 
     * @param data the CSV data to process
     * @param filterColumn the column index to filter on
     * @param filterValue the value to match
     */
    public static void processCsvData(List<String[]> data, int filterColumn, String filterValue) {
        System.out.println("Processing CSV data with filter: column=" + filterColumn + ", value=" + filterValue);
        
        for (String[] line : data) {
            if (line.length > filterColumn && filterValue.equals(line[filterColumn])) {
                // Print matching rows
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < line.length; i++) {
                    if (i > 0) sb.append("\t");
                    sb.append(line[i]);
                }
                System.out.println(sb.toString());
            }
        }
    }

    /**
     * Main method demonstrating cloud-ready CSV processing.
     * 
     * Usage examples:
     * 1. Read from EFS: java CSVTest efs sample.csv
     * 2. Read from classpath: java CSVTest classpath data/sample.csv
     */
    public static void main(String[] args) {
        try {
            List<String[]> data;
            
            if (args.length >= 2) {
                String source = args[0];
                String fileName = args[1];
                
                if ("efs".equalsIgnoreCase(source)) {
                    // Read from Amazon EFS mounted volume
                    data = readCsvFromEFS(fileName);
                } else if ("classpath".equalsIgnoreCase(source)) {
                    // Read from classpath resources
                    data = readCsvFromClasspath(fileName);
                } else {
                    System.err.println("Invalid source. Use 'efs' or 'classpath'");
                    System.err.println("Usage: java CSVTest <efs|classpath> <filename>");
                    return;
                }
            } else {
                // Default: try to read from classpath
                System.out.println("No arguments provided. Attempting to read from classpath...");
                System.out.println("Usage: java CSVTest <efs|classpath> <filename>");
                
                // Example: read from classpath resource
                data = readCsvFromClasspath("sample.csv");
            }
            
            // Process the data - example: filter for "QUICK SUB" in column 1
            if (!data.isEmpty()) {
                processCsvData(data, 1, "QUICK SUB");
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
