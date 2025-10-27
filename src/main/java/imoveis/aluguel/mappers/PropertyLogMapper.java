package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;

@Mapper(componentModel = "spring", uses = {})
public interface PropertyLogMapper {

    @Mapping(target = "property", source = "property")
    @Mapping(target = "tenantCpfCnpj", source = "tenant.cpfCnpj")
    @Mapping(target = "tenantName", source = "tenant.name")
    @Mapping(target = "id", ignore = true)
    PropertyLog toPropertyLog(Property property);

}
