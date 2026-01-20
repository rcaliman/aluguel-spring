package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.PropertyTypeEnum;

public record ReceiptDtoResponse(

                String tenant,
                String tenantDocument,
                String value,
                PropertyTypeEnum propertyType,
                String propertyNumber,
                String observation,
                String locale,
                String day,
                String month,
                String nextMonth,
                String year,
                String nextYear,
                String landlord,
                String landlordCpf,
                String tenantContact) {
                    
}
