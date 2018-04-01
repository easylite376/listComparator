package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import types.ExcelSheet;
import util.ExcelUtils;

import java.util.Objects;

/**
 * @author Martin Staehr
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ExcelSheet excelPlus = ExcelUtils.getInformationOfExcelFile("listPlus.xlsx");
        ExcelSheet excelMinus = ExcelUtils.getInformationOfExcelFile("listMinus.xlsx");
        if (!Objects.isNull(excelPlus)) {
            log.error("Excel-Table is: {} {}", System.lineSeparator(), excelPlus.toExcelSheet());
        }
        if (!Objects.isNull(excelMinus)) {
            log.error("Excel-Table is:{}{}", System.lineSeparator(), excelMinus.toExcelSheet());
        }


    }


}
