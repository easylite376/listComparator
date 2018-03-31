package main;

import util.ExcelReader;

public class App {

    public static void main(String[] args) {
        ExcelReader.getObjectOfFile("listPlus.xlsx");
        ExcelReader.getObjectOfFile("listMinus.xlsx");
    }


}
