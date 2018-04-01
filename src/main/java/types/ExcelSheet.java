package types;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Staehr
 */
public class ExcelSheet {

    private List<ExcelRow> rows;

    public ExcelSheet() {
        rows = new ArrayList<>();
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    public void setRows(List<ExcelRow> rows) {
        this.rows = rows;
    }

    public ExcelRow getRow(int index) {
        return rows.get(index);
    }

    public void addRow(ExcelRow excelRow) {
        rows.add(excelRow);
    }

    @Override
    public String toString() {
        return "ExcelSheet{" +
                "rows=" + rows +
                '}';
    }

    public String toExcelSheet() {
        String excelSheetString = "";
        for (ExcelRow excelRow : rows) {
            excelSheetString = excelSheetString.concat(excelRow.toExcelRow() + System.lineSeparator());
        }
        return excelSheetString;
    }
}
