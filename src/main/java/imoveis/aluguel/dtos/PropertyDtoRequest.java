package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;

public record PropertyDtoRequest(Long id, PropertyTypeEnum propertyType, PropertyUseTypeEnum useType, String address,
        String location, String city, String state, String number, Long tenantId, Double value, String complement,
        String observation, String paymentDay) {

}
