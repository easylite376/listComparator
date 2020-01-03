import excel.ExcelParser;
import excel.ExcelRow;
import excel.ExcelSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Martin Staehr
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ExcelParser excelParser = new ExcelParser();
        ExcelSheet excelPlus = excelParser.getInformationOfExcelFile("listPlus.xlsx");
        ExcelSheet excelMinus = excelParser.getInformationOfExcelFile("listMinus.xlsx");
        if (!Objects.isNull(excelPlus)) {
            LOG.debug("Excel-Table is:{}{}", System.lineSeparator(), excelPlus);
        }
        if (!Objects.isNull(excelMinus)) {
            LOG.debug("Excel-Table is:{}{}", System.lineSeparator(), excelMinus);
        }
        Map<String, List<ExcelRow>> excelMinusMonthSummary = collectMonth(excelMinus);
        Map<String, List<ExcelRow>> excelPlusMonthSummary = collectMonth(excelPlus);
        LOG.debug("Excel-Minus-Sum: {}", excelMinusMonthSummary);
        LOG.debug("Excel-Plus-Sum: {}", excelPlusMonthSummary);
        LOG.debug("Excel-Minus-Data: {}", excelMinusMonthSummary);
        LOG.debug("Excel-Plus-Data: {}", excelPlusMonthSummary);
        List<List<ExcelRow>> result = compareLists(excelMinusMonthSummary, excelPlusMonthSummary);
        LOG.debug("Result-left: {}, result-right: {}", result.get(0), result.get(1));

        createResultFile(result.get(0), "table1");
        createResultFile(result.get(1), "table2");
    }

    /**
     * Collects all values appearing in a month of the year with the dates they are appearing on
     */
    private static Map<String, List<ExcelRow>> collectMonth(ExcelSheet excelSheet) {
        ConcurrentHashMap<String, List<ExcelRow>> valuesByRowId = new ConcurrentHashMap<>();
        excelSheet.getRows().forEach(row -> {
            String rowId = row.getId();
            List<ExcelRow> rowValues = valuesByRowId.getOrDefault(rowId, new ArrayList<>());
            rowValues.add(row);
            valuesByRowId.put(rowId, rowValues);
        });
        return valuesByRowId;
    }

    private static List<List<ExcelRow>> compareLists(Map<String, List<ExcelRow>> mapLeft, Map<String, List<ExcelRow>> mapRight) {
        List<ExcelRow> extraEntriesLeftList = new CopyOnWriteArrayList<>();
        List<ExcelRow> extraEntriesRightList = new CopyOnWriteArrayList<>();

        mapLeft.forEach((rowId, excelRowsLeft) -> {
            List<ExcelRow> excelRowsRight = mapRight.getOrDefault(rowId, new ArrayList<>());
            if (excelRowsRight.isEmpty()) {
                extraEntriesLeftList.addAll(excelRowsLeft);
                return;
            }

            List<BigDecimal> excelRowsValuesLeft = excelRowsLeft.stream().map(row -> row.getAmount().abs()).collect(Collectors.toList());
            BigDecimal sumLeftValues = excelRowsValuesLeft.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            List<BigDecimal> excelRowsValuesRight = excelRowsRight.stream().map(row -> row.getAmount().abs()).collect(Collectors.toList());
            BigDecimal sumRightValues = excelRowsValuesRight.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            if (sumLeftValues.equals(sumRightValues)) {
                return;
            }

            extraEntriesLeftList.addAll(excelRowsLeft.stream().filter(row -> {
                BigDecimal rowValue = row.getAmount().abs();
                return !excelRowsValuesRight.contains(rowValue);
            }).collect(Collectors.toList()));
            extraEntriesRightList.addAll(excelRowsRight.stream().filter(row -> {
                BigDecimal rowValue = row.getAmount().abs();
                return !excelRowsValuesLeft.contains(rowValue);
            }).collect(Collectors.toList()));
        });

        extraEntriesRightList.addAll(mapRight.entrySet().stream().filter(entry -> !mapLeft.containsKey(entry.getKey())).map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList()));

        List<List<ExcelRow>> result = new ArrayList<>();
        result.add(extraEntriesLeftList);
        result.add(extraEntriesRightList);
        return result;
    }

    private static void createResultFile(List<ExcelRow> excelRows, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();

        Sheet sheet = workbook.createSheet(fileName);

        int rowNumber = 0;
        Row headerRow = sheet.createRow(rowNumber++);
        for (int column = 0; column < ExcelParser.COLUMNS.size(); column++) {
            Cell cell = headerRow.createCell(column);
            cell.setCellValue(ExcelParser.COLUMNS.get(column));
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd.MM.yyyy"));


        for (ExcelRow excelRow : excelRows) {
            Row row = sheet.createRow(rowNumber++);
            int columnNum = 0;
            row.createCell(columnNum++).setCellValue(excelRow.getId());

            Cell dateCell = row.createCell(columnNum++);
            dateCell.setCellValue(Date.from(excelRow.getDate().toInstant()));
            dateCell.setCellStyle(dateCellStyle);

            row.createCell(columnNum++).setCellValue(excelRow.getAmount().doubleValue());
        }

        for (int column = 0; column < ExcelParser.COLUMNS.size(); column++) {
            sheet.autoSizeColumn(column);
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileName + ".xlsx")) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
