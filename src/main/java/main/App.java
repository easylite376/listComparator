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

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ExcelSheet excelPlus = ExcelUtils.getInformationOfExcelFile("listPlus.xlsx");
        ExcelSheet excelMinus = ExcelUtils.getInformationOfExcelFile("listMinus.xlsx");
        if (!Objects.isNull(excelPlus)) {
            LOG.debug("Excel-Table is: {} {}", System.lineSeparator(), excelPlus);
        }
        if (!Objects.isNull(excelMinus)) {
            LOG.debug("Excel-Table is:{}{}", System.lineSeparator(), excelMinus);
        }


    }


}
