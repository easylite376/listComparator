package excel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author Martin Staehr
 */
public class ExcelRow {

    public static final String ID_FIELD = "Belegnr.";
    public static final String DATE_FIELD = "Datum";
    public static final String AMOUNT_FIELD = "Betrag";

    private ZonedDateTime date;
    private String id;
    private BigDecimal amount;

    ExcelRow() {
    }

    public ExcelRow(ZonedDateTime date, String id, BigDecimal amount) {
        this.date = date;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExcelRow{" +
                "date=" + date +
                ", id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }

}
