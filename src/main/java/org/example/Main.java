package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String[] arr1 = "1,John,Smith,USA,25".split(",");
        String[] arr2 ="2,Ivan,Petrov,RU,23".split(",");
        List<String[]> employers = Arrays.asList(arr1,arr2);
        createAndWriteDATA(fileName,employers);
        writeString(listToJson(parseCSV(columnMapping,fileName)),fileName);


    }
    static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader csv = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvToBean= new CsvToBeanBuilder<Employee>(csv)
                    .withMappingStrategy(strategy)
                    .build();
            list = csvToBean.parse();
            csv.close();
            Files.delete(Path.of(fileName));
            return list;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    public static String listToJson(List<Employee> list){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();

        String json=gson.toJson(list,listType);
        return json;
    }
    public static void writeString(String json,String fileName) {
        String path = fileName.replaceAll("csv", "json");
        try (FileWriter fw = new FileWriter(new File(path))) {
            fw.write(json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createAndWriteDATA(String path, List<String[]> list) {
        try (CSVWriter scvw = new CSVWriter(new FileWriter(path,true))){
            scvw.writeAll(list);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}