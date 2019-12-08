package excel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Martin Staehr
 */
public class ExcelRow {

    private ZonedDateTime date;
    private BigDecimal amount;

    ExcelRow() {
    }

    public ExcelRow(ZonedDateTime date, BigDecimal amount) {
        this.date = date;
        this.amount = amount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\t" + amount;
    }
}
