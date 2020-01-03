package excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static excel.ExcelRow.*;

/**
 * @author Martin Staehr
 */
public class ExcelParser {

    public final static List<String> COLUMNS = Arrays.asList(ID_FIELD, DATE_FIELD, AMOUNT_FIELD);

    public ExcelParser() {
    }


    private static ExcelSheet getRelevantInformationOfSheet(Sheet sheet, List<String> columns) {
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
                        if (column.equals(ID_FIELD)) {
                            int id = (int) cell.getNumericCellValue();
                            excelRow.setId(String.valueOf(id));
                            return;
                        }
                        ZonedDateTime germanTime = getDateOrNull(cell);
                        if (Objects.isNull(germanTime)) {
                            double amount = cell.getNumericCellValue();
                            String stringAmount = String.valueOf(amount);
                            excelRow.setAmount(new BigDecimal(stringAmount));
                        } else {
                            excelRow.setDate(germanTime);
                        }
                        break;
                    case STRING:
                        if (column.equals(ID_FIELD)) {
                            String id = cell.getStringCellValue();
                            excelRow.setId(id);
                        }
                        break;
                }
            });
            excelSheet.addRow(excelRow);
        }
        return excelSheet;
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

    public ExcelSheet getInformationOfExcelFile(String filename) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filename));
            Workbook workbook = getRelevantWorkbook(inputStream, filename);
            Sheet firstSheet = workbook.getSheetAt(0);
            ExcelSheet excelSheet = getRelevantInformationOfSheet(firstSheet, COLUMNS);
            workbook.close();
            inputStream.close();
            return excelSheet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Workbook getRelevantWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
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
