import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadCSVTest {

    ReadCSV reader = new ReadCSV();

    @org.junit.jupiter.api.Test()
    void CompareValidSamples() {
        String[] arguments = {"sample_file_1.csv", "sample_file_3.csv", "--columns", "Customer ID#","Account No."};
        assertDoesNotThrow(() -> Main.main(arguments));
    }

    @org.junit.jupiter.api.Test()
    void SwapFilenamesForValidSamples() {
        String[] arguments = {"sample_file_3.csv", "sample_file_1.csv", "--columns", "Customer ID#","Account No."};
        assertDoesNotThrow(() -> Main.main(arguments));
    }

    @org.junit.jupiter.api.Test()
    void SwapColumnsForValidSamples() {
        String[] arguments = {"sample_file_3.csv", "sample_file_1.csv", "--columns", "Account No.", "Customer ID#"};
        assertDoesNotThrow(() -> Main.main(arguments));
    }

    @org.junit.jupiter.api.Test()
    void ExecuteValidSample() {
        File file
                = new File("./exception/output.csv");

        if (file.delete()) {
            System.out.println("File deleted successfully");
        }

        String[] arguments = {"sample_file_3.csv", "sample_file_1.csv", "--columns", "Account No.", "Customer ID#"};
        assertDoesNotThrow(() -> Main.main(arguments));

        assertEquals(true, file.exists());
    }

    @org.junit.jupiter.api.Test()
    void EmptyFilename() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            reader.openFile("");
        });

        String expectedMessage = "File is not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void MalformedCSV() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            reader.openFile("sample/malformed_sample.csv");
        });

        String expectedMessage = "Your csv file is malformed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void CSVContainsOnlyCommas() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            reader.openFile("sample/malformed_sample_2.csv");
        });

        String expectedMessage = "You have a blank header or content";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void HeadersDoNotMatch() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            CSV file1 = reader.openFile("sample/sample_file_1.csv");
            CSV file2 = reader.openFile("sample/header_sample.csv");

            String[] test = {"Customer ID#","Account No.","Currency","Type","Balance"};
            reader.compareFiles(file1, file2, test);
        });

        String expectedMessage = "Headers of files do not match and is thus incomparable";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void ArgumentsNotSpecified() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            String[] arguments = {};
            List<String> cliarguments = Arrays.asList(arguments);
            Main.validateArguments(cliarguments);
        });

        String expectedMessage = "There is no argument stated.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void FilenamesNotSpecified() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            String[] arguments = {"--columns", "col1", "col2"};
            List<String> cliarguments = Arrays.asList(arguments);
            Main.validateArguments(cliarguments);
        });

        String expectedMessage = "Filenames must be specified first";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void ColumnNotSpecified() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            String[] arguments = {"filename1", "filename2", "col1", "col2"};
            List<String> cliarguments = Arrays.asList(arguments);
            Main.validateArguments(cliarguments);
        });

        String expectedMessage = "--columns not specified";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void ColumnsNotGiven() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            String[] arguments = {"filename1", "filename2", "--columns"};
            List<String> cliarguments = Arrays.asList(arguments);
            Main.validateArguments(cliarguments);
        });

        String expectedMessage = "No columns selected.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void FilenamesNotCSV() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            String[] arguments = {"filename1.docx", "filename2.pdf", "--columns", "col1", "col2"};
            List<String> cliarguments = Arrays.asList(arguments);
            Main.validateArguments(cliarguments);
        });

        String expectedMessage = "Files must be of type: csv";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void FilenamesNoContent() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            reader.openFile(".csv");
        });

        String expectedMessage = "File is not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @org.junit.jupiter.api.Test()
    void FilenamesContainInvalidChars() {
        Exception exception = assertThrows(ReadCSVException.class, () -> {
            reader.openFile("%^!&)_./.csv");
        });

        String expectedMessage = "File is not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}