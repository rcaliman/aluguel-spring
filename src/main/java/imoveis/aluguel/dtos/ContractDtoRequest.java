package imoveis.aluguel.dtos;

public record ContractDtoRequest(

                Long propertyId,
                Long landlordId,
                String nationality,
                String startMonth,
                String startYear,
                String endMonth,
                String endYear) {
                    
}
