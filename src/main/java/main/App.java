package main;

import types.ExcelSheet;
import util.ExcelUtils;

public class App {

    public static void main(String[] args) {
        ExcelSheet excelSheet = ExcelUtils.getInformationOfExcelFile("listPlus.xlsx");
        ExcelUtils.getInformationOfExcelFile("listMinus.xlsx");
    }


}
