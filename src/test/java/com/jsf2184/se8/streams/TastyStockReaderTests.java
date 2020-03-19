package com.jsf2184.se8.streams;

import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

public class TastyStockReaderTests {
    public static final List<String> FILES = Arrays.asList("amexCompanyList.csv",
                                                           "nasdaqCompanyList.csv",
                                                           "nyseCompanyList.csv");

    @Test
    public void readFiles() throws Exception {
        Map<String, Stock> stockMap = new TreeMap<>();
        for (String fname : FILES) {
            processFile(fname, stockMap);
        }
        System.out.printf("Collected %d unique symbols\n", stockMap.size());
        printStocks(stockMap);
    }

    public void printStocks(Map<String, Stock> stockMap ) {
        boolean first = true;
        System.out.println("            stocks: [");
        for (Stock s : stockMap.values()) {
            if (!first) {
                System.out.println(",");
            }
            System.out.printf("                %s", s.toJson());

            first = false;
        }
        System.out.println("\n            ]");
    }

    public static void processFile(String fileName,
                                   Map<String, Stock> stockMap) throws Exception
    {
        Stream<String> fileStream = FileStreamTests.getFileStream(fileName);
        fileStream
                .filter(line -> !line.startsWith("\"Symbol\",\"Name\""))
                .forEach(l -> processLine(l, fileName, stockMap));
    }

    public static class Stock {
        String symbol;
        String name;
        private String _fileName;

        public Stock(String symbol, String name, String fileName) {
            this.symbol = symbol;
            this.name = name;
            _fileName = fileName;
        }
        public String getSymbol() {
            return symbol;
        }
        public String getName() {
            return name;
        }

        public String getFileName() {
            return _fileName;
        }
        public String toJson() {
            String result = String.format("{symbol: '%s', name: '%s'}", symbol, name);
            return result;
        }
    }

    public static void processLine(final String line,
                                   final String fileName,
                                   Map<String, Stock> map )
    {
        String[] parts = line.split(",");
        String symbol = removeQuotes(parts[0]);
        Stock stock = new Stock(symbol,
                                 removeQuotes(parts[1]),
                                 fileName);
        Stock entry = map.get(symbol);
        if (entry != null)
        {
            System.out.printf("Stock collision on symbol: '%s' between files: %s(%s) and %s(%s)\n",
                              symbol, entry.getFileName(), entry.getName(), fileName, stock.getName() );
            return;
        }
        map.put(symbol, stock);
    }

    public static String removeQuotes(final String part) {
        String result = part.replaceAll("\"", "");
        return result;
    }

}
