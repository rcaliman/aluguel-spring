package imoveis.aluguel.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.PropertyDtoRequest;
import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final TenantService tenantService;
    private final PropertyMapper propertyMapper;

    @PostMapping
    public ResponseEntity<PropertyDtoResponse> create(@RequestBody PropertyDtoRequest dtoRequest) {

        var tenant = tenantService.findById(dtoRequest.tenantId());
        var property = propertyMapper.toProperty(dtoRequest);
        property.setTenant(tenant);
        var newProperty = propertyService.create(property);
        var dtoResponse = propertyMapper.toDtoResponse(newProperty);

        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDtoResponse> findById(@PathVariable Long id) {

        var entity = propertyService.findById(id);
        var toDtoResponse = propertyMapper.toDtoResponse(entity);

        return new ResponseEntity<>(toDtoResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDtoResponse> update(@PathVariable Long id,
            @RequestBody PropertyDtoRequest dtoRequest) {

        var tenant = tenantService.findById(dtoRequest.tenantId());
        var property = propertyMapper.toProperty(dtoRequest);
        property.setTenant(tenant);
        var updatedProperty = propertyService.update(id, property);
        var dtoResponse = propertyMapper.toDtoResponse(updatedProperty);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PropertyDtoResponse>> list(Sort sort) {

        var properties = propertyService.list(sort);

        var listDto = properties.stream()
                .map(propertyMapper::toDtoResponse)
                .toList();

        return new ResponseEntity<>(listDto, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        propertyService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
