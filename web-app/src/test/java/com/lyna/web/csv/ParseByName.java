package com.lyna.web.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class ParseByName {
    public void readByName(String csvPath) {
        try (
                Reader fileReader = Files.newBufferedReader(Paths.get(csvPath))
        ) {
            CsvToBean csvBean = new CsvToBeanBuilder(fileReader)
                    .withType(ProductCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<ProductCsv> productsItor = csvBean.iterator();
            while(productsItor.hasNext()) {
                ProductCsv product = productsItor.next();
                System.out.println(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
