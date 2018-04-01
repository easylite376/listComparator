package main;

import types.ExcelSheet;
import util.ExcelUtils;

import java.util.Objects;

/**
 * @author Martin Staehr
 */
public class App {

    public static void main(String[] args) {
        ExcelSheet excelPlus = ExcelUtils.getInformationOfExcelFile("listPlus.xlsx");
        ExcelSheet excelMinus = ExcelUtils.getInformationOfExcelFile("listMinus.xlsx");
        if (!Objects.isNull(excelPlus)) {
            System.out.println(excelPlus.toExcelSheet());
        }
        if (!Objects.isNull(excelMinus)) {
            System.out.println(excelMinus.toExcelSheet());
        }


    }


}
