package comparator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dataset {

    private HashMap<BigDecimal, List<ZonedDateTime>> valuesOnDateMap = new HashMap<>();

    public HashMap<BigDecimal, List<ZonedDateTime>> getValuesOnDateMap() {
        return valuesOnDateMap;
    }

    public void setValuesOnDateMap(HashMap<BigDecimal, List<ZonedDateTime>> valuesOnDateMap) {
        this.valuesOnDateMap = valuesOnDateMap;
    }

    public void addNewEntry(BigDecimal key, ZonedDateTime dateTime) {
        List<ZonedDateTime> dates = valuesOnDateMap.getOrDefault(key, new ArrayList<>());
        dates.add(dateTime);
        valuesOnDateMap.put(key, dates);
    }

    private BigDecimal sumOfValues() {
        BigDecimal result = new BigDecimal(0);
        for (BigDecimal entry : valuesOnDateMap.keySet()) {
            List<ZonedDateTime> dateTimeList = valuesOnDateMap.get(entry);
            BigDecimal valueToAdd = entry.multiply(BigDecimal.valueOf(dateTimeList.size()));
            result = result.add(valueToAdd);
        }
        return result;
    }

    @Override
    public String toString() {
        return sumOfValues().toString();


    }
}
