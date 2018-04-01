package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import types.ExcelRow;
import types.ExcelSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExcelUtils {

    private ExcelUtils() {
    }

    public static ExcelSheet getInformationOfExcelFile(String filename) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filename));
            System.out.println(filename);
            Workbook workbook = getRelevantWorkbook(inputStream, filename);
            List<String> columns = Arrays.asList("Datum", "Betrag");
            Sheet firstSheet = workbook.getSheetAt(0);
            ExcelSheet excelSheet = getDateAndMoneyOfSheet(firstSheet, columns);
            System.out.println(excelSheet.toExcelSheet());
            workbook.close();
            inputStream.close();
            return excelSheet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

    private static ZonedDateTime getDateOrNull(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            Instant date = cell.getDateCellValue().toInstant();
            return ZonedDateTime.ofInstant(date, ZoneId.of("Europe/Berlin"));
        }
        return null;
    }

    private static HashMap<String, Integer> getHeaderPositionMap(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        HashMap<String, Integer> tableHeader = new HashMap<>();
        int counter = 0;
        for (Cell cell : headerRow) {
            tableHeader.put(cell.getStringCellValue(), counter++);
        }
        return tableHeader;
    }

    private static ExcelSheet getDateAndMoneyOfSheet(Sheet sheet, List<String> columns) {
        HashMap<String, Integer> tableHeader = getHeaderPositionMap(sheet);
        ExcelSheet excelSheet = new ExcelSheet();
        boolean isFirstLine = true;
        for (Row row : sheet) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            ExcelRow excelRow = new ExcelRow();
            columns.forEach(column -> {
                Cell cell = row.getCell(tableHeader.get(column));
                switch (cell.getCellTypeEnum()) {
                    case NUMERIC:
                        ZonedDateTime germanTime = getDateOrNull(cell);
                        if (Objects.isNull(germanTime)) {
                            excelRow.setAmount(cell.getNumericCellValue());
                        } else {
                            excelRow.setDate(germanTime);
                        }
                        break;
                    default:
                        break;

                }
            });
            excelSheet.addRow(excelRow);
        }
        return excelSheet;
    }
}
