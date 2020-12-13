package com.jsf2184.dnb;

import com.jeff.fischman.spex.OutputField;
import com.jeff.fischman.spex.utility.StreamUtility;

import java.util.*;
import java.util.stream.Stream;

public class ArgParser {

    public static final String DEFAULT_INPUT_FILE = "applicaton.log";
    public static final String DEFAULT_OUTPUT_FILE = "errors.json";
    public static final int DEFAULT_LINE_OFFSET = 0;
    private String outputFileName;
    private String inputFileName;
    private int lineOffset;
    String[] args;


    public ArgParser(String[] args) {
        this.args = args;
        outputFileName = DEFAULT_OUTPUT_FILE;
        inputFileName = DEFAULT_INPUT_FILE;
        lineOffset = DEFAULT_LINE_OFFSET;
    }

    public boolean process() {
        int numArgs = args.length;
        for (int i=0; i< numArgs; i++) {
            String arg = args[i];
            switch (arg) {
                case "-h":
                    printUsage();
                    return false;

                case "-o":
                    if (i+1 >= numArgs) {
                        System.err.println("Missing -o argument");
                        printUsage();
                        return false;
                    }
                    outputFileName = args[++i];
                    break;

                case "-i":
                    if (i+1 >= numArgs) {
                        System.err.println("Missing -i argument");
                        printUsage();
                        return false;
                    }
                    inputFileName = args[++i];
                    break;

                case "-n":
                    if (i+1 >= numArgs) {
                        System.err.println("Missing -n argument");
                        printUsage();
                        return false;
                    }

                default:
                    if (inputFile != null) {
                        System.err.println("Extra command line arguments.");
                        printUsage();
                        return false;
                    }
                    inputFile = arg;
                    break;
            }
        }

        if (canned && inputFile != null) {
            System.err.println("Cannot specify both an inputFile and the -canned option.");
            printUsage();
            return false;
        }

        try {
            _outputFields = parseOutputFields(outputFieldAbbrevs);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printUsage();
            return false;
        }

        if (canned) {
            System.err.println("Will be using canned input data");
            _inputStream = _streamUtility.getCannedSampleInputStream();
        } else {
            if (inputFile == null) {
                inputFile = DEFAULT_INPUT_FILE;
            }
            System.out.printf("Will be using input from file: '%s'\n", inputFile);
            _inputStream = _streamUtility.getFileStream(inputFile);
        }
        if (_inputStream == null) {
            printUsage();
            return false;
        }
        return true;
    }

    List<OutputField> parseOutputFields(String outputFieldAbbrevs) throws Exception {
        List<OutputField> res = new ArrayList<>();
        for (int i=0; i<outputFieldAbbrevs.length(); i++) {
            char o = outputFieldAbbrevs.charAt(i);
            OutputField outputField = OutputField.abbrevToEnum(o);
            if (outputField == null) {
                throw new Exception(String.format("char '%c' is an invalid output field abbreviation", o));
            }
            res.add(outputField);
        }
        Collection<OutputField> set = new HashSet<>(res);
        if (set.size() != res.size()) {
            throw new Exception(String.format("duplicate output format fields in '%s' are prohibited", outputFieldAbbrevs));
        }

        return res;
    }
    public Stream<String> getInputStream() {
        return _inputStream;
    }

    public String getOutputFileName() {
        // for now, we don't give the user to control the outputFileName, maybe later.
        return outputFileName;
    }

    public List<OutputField> getOutputFields() {
        return _outputFields;
    }

    private void printUsage() {
        System.err.println("Usage: |-i inputFileName| |-o outputFileName| |-n lineOffset| |-h|");
        System.err.printf ("  inputFileName:     default = %s\n", DEFAULT_INPUT_FILE);
        System.err.printf ("  outputFileName:    default = %s\n", DEFAULT_OUTPUT_FILE);
        System.err.printf ("  lineOffset:        default = %d\n", DEFAULT_LINE_OFFSET);
        System.err.println("  in:                      to run the program off of canned data rather than file input");
        System.err.println("  -output outputAbbrevChars:    to customize output, See legal chars below");
        System.err.println("  fileName:                     to explicitly name an input file\n");
        System.err.println("Here are the legal characters that can appear in 'outputAbbrevChars'");
        System.err.println(OutputField.getAbbrevHelp());

    }

}
