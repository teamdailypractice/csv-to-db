package info.dailypractice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvFileReader {
    private String filepath;
    private String fieldSeparator;
    private boolean withHeader;
    private boolean withTrailer;
    public static final String CSV_FIELD_SEPARATOR = ",";
    List<String> fileContent = null;
    List<String> data = null;


    public CsvFileReader(String filepath, String fieldSeparator, boolean withHeader, boolean withTrailer) throws IOException {

        this.filepath = filepath;
        this.fieldSeparator = fieldSeparator;
        this.withHeader = withHeader;
        this.withTrailer = withTrailer;
        init();

    }

    public CsvFileReader(String filepath) throws IOException {
        this(filepath, CSV_FIELD_SEPARATOR, true, false);
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
}
