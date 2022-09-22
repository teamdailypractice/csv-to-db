package info.dailypractice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvFileReader {
    private final String filepath;
    private TableConfiguration tableConfiguration;
    private final String fieldSeparator;
    private final boolean withHeader;
    private final boolean withTrailer;
    public static final String CSV_FIELD_SEPARATOR_COMMA = ",";
    List<String> fileContent = null;
    List<String> data = null;
    List<Boolean> dataValidStatus = new ArrayList<>();

    private static Logger LOG = LoggerFactory.getLogger(CsvFileReader.class);

    public CsvFileReader(String filepath, TableConfiguration tableConfiguration, String fieldSeparator, boolean withHeader, boolean withTrailer) throws IOException {

        this.filepath = filepath;
        this.tableConfiguration = tableConfiguration;
        this.fieldSeparator = fieldSeparator;
        this.withHeader = withHeader;
        this.withTrailer = withTrailer;
        init();

    }

    public CsvFileReader(String filepath, TableConfiguration tableConfiguration) throws IOException {
        this(filepath, tableConfiguration, CSV_FIELD_SEPARATOR_COMMA, true, false);
    }

    private void init() throws IOException {
        fileContent = Files.readAllLines(Paths.get(filepath));

        if (withHeader && withTrailer) {
            data = fileContent.subList(1, fileContent.size() - 1);
        }
        if (withHeader && (!withTrailer)) {
            data = fileContent.subList(1, fileContent.size());
        }
        //No header - but trailer present
        if ((!withHeader) && withTrailer) {
            data = fileContent.subList(0, fileContent.size() - 1);
        }
        //No header and no trailer
        if ((!withHeader) && (!withTrailer)) {
            data = fileContent;
        }
        validateEveryRowDataWithSchema();
    }


    public List<String> getColumnNames() {
        var columnNames = new ArrayList<String>();
        if (withHeader) {
            String[] columnNamesInHeader = fileContent.get(0).split(fieldSeparator);
            Collections.addAll(columnNames, columnNamesInHeader);
        } else {
            //Generate column names - c1 c2 c3 c4 c5 c6 c7
            //From the first column
            String[] firstRow = fileContent.get(0).split(fieldSeparator);

            for (int i = 0; i < firstRow.length; i++) {
                columnNames.add("c" + Integer.valueOf(i + 1).toString());
            }
        }
        return columnNames;
    }

    public List<String> getTrailerInformation() {
        var columnNames = new ArrayList<String>();
        if (withTrailer) {
            String[] trailerRowColumns = fileContent.get(fileContent.size() - 1).split(fieldSeparator);
            Collections.addAll(columnNames, trailerRowColumns);
        }
        return columnNames;
    }

    private boolean validateInteger(String fieldValue) {
        boolean isValid = true;
        try {
            int actualValue = Integer.parseInt(fieldValue, 10);
        } catch (NumberFormatException ex) {
            isValid = false;
        }
        return isValid;
    }

    private boolean validateVarchar(String fieldValue, String fieldLength) {
        boolean isValid = true;
        try {
            int length = Integer.parseInt(fieldLength, 10);
            if (fieldValue.length() > length) {
                throw new Exception("String length is more than the schema definition");
            }
        } catch (NumberFormatException ex) {
            isValid = false;
        } catch (Exception ex) {
            isValid = false;
        }
        return isValid;
    }

    private boolean validateField(String fieldValue, Map<String, String> fieldSpecification) {
        String fieldName = fieldSpecification.get("name");
        String fieldType = fieldSpecification.get("type");
        String fieldLength = fieldSpecification.get("length");

        boolean isValid = false;
        //INTEGER - java.lang.Integer
        if (fieldType.equals("INTEGER")) {
            isValid = validateInteger(fieldValue);
        }
        //VARCHAR - java.lang.String
        if (fieldType.equals("VARCHAR")) {
            isValid = validateVarchar(fieldValue, fieldLength);
        }
        return isValid;
    }

    //Mark valid rows and invalid rows as per table configuration
    //TODO: validateFileDataWithFileSchema - report invalid rows
    private void validateEveryRowDataWithSchema() {
        List<Map<String, String>> fieldSpecifications = tableConfiguration.getFields();
        for (int i = 0; i < data.size(); i++) {
            boolean isValidRow = false;
            String[] fieldValues = data.get(i).split(fieldSeparator);
            if (fieldSpecifications.size() != fieldValues.length) {
                //Mark the row as invalid
                dataValidStatus.add(i, isValidRow);
            }
            //Each field type, validate with data - if any mismatch - invalid
            BitSet bitSet = new BitSet(fieldSpecifications.size());

            for (int j = 0; j < fieldSpecifications.size(); j++) {
                bitSet.set(j, validateField(fieldValues[j], fieldSpecifications.get(j)));
            }
            //TODO: Log which fields are invalid
            isValidRow = 	bitSet.length() == bitSet.cardinality();
            dataValidStatus.add(i, isValidRow);
        }
    }

    public List<String[]> getValidRows() {
        ArrayList<String[]> rows = new ArrayList<>();
        for (int i = 0; i < dataValidStatus.size(); i++) {
            if (dataValidStatus.get(i)) {
                String[] row = data.get(i).split(fieldSeparator);
                rows.add(i, row);
            }
        }
        return rows;
    }

    public void getInValidRows() throws Exception {
        throw new Exception("Not implemented");
    }


}
