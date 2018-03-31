package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ExcelReader {

    public static void getObjectOfFile(String filename) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filename));
            System.out.println(filename);
            Workbook workbook = getRelevantWorkbook(inputStream, filename);

            Sheet firstSheet = workbook.getSheetAt(0);


            for (Row nextRow : firstSheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellTypeEnum()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                ZonedDateTime germanTime = ZonedDateTime.ofInstant(cell.getDateCellValue().toInstant(),
                                        ZoneId.of("Europe/Berlin"));
                                System.out.print(germanTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                            } else {
                                System.out.print(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue());
                            break;
                        default:
                            break;
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }

            workbook.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Workbook getRelevantWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook;

        if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("Incorrect file format");
        }

        return workbook;
    }
}
