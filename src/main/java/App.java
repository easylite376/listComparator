import comparator.Dataset;
import excel.ExcelParser;
import excel.ExcelSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
        Map<String, Dataset> excelMinusMonthSummary = collectMonth(excelMinus);
        Map<String, Dataset> excelPlusMonthSummary = collectMonth(excelPlus);
        LOG.debug("Excel-Minus: {}", excelMinusMonthSummary);
        LOG.debug("Excel-Plus: {}", excelPlusMonthSummary);

    }

    private static Map<String, Dataset> collectMonth(ExcelSheet excelSheet) {
        ConcurrentHashMap<String, Dataset> valuesSumByMonth = new ConcurrentHashMap<>();
        excelSheet.getRows().forEach(row -> {
            ZonedDateTime rowDate = row.getDate();
            String monthValue = String.valueOf(rowDate.getMonthValue());
            String yearValue = String.valueOf(rowDate.getYear());
            String key = monthValue.concat(yearValue);
            BigDecimal rowAmount = row.getAmount();
            Dataset currentDataSet = valuesSumByMonth.getOrDefault(key, new Dataset());
            currentDataSet.addNewEntry(rowAmount, row.getDate());
            valuesSumByMonth.put(key, currentDataSet);
        });
        return valuesSumByMonth;
    }
}
