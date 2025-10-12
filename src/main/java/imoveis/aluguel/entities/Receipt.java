package imoveis.aluguel.entities;

import imoveis.aluguel.enums.PropertyTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Receipt {

    private String tenant;
    private Double value;
    private PropertyTypeEnum propertyType;
    private String propertyNumber;
    private String locale;
    private String day;
    private String month;
    private String nextMonth;
    private String observation;
    private String year;
    private String nextYear;
    private String landlord;
    private String tenantContact;

}
