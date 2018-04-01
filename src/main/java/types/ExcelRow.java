package types;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Martin Staehr
 */
public class ExcelRow {

    private ZonedDateTime date;
    private double amount;

    public ExcelRow() {
    }

    public ExcelRow(ZonedDateTime date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\t" + amount;
    }
}
