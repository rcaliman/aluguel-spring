package imoveis.aluguel.controllers.api;

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

import imoveis.aluguel.dtos.TenantDtoRequest;
import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    @PostMapping
    public ResponseEntity<TenantDtoResponse> create(@RequestBody TenantDtoRequest dtoRequest) {

        var tenant = tenantMapper.toTenant(dtoRequest);

        var newTenant = tenantService.create(tenant);

        var dtoResponse = tenantMapper.toDtoResponse(newTenant);

        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);

    }

    @GetMapping("/cpfCnpj/{cpfCnpj}")
    public ResponseEntity<TenantDtoResponse> findbyCpfCnpj(@PathVariable String cpfCnpj) {

        var tenant = tenantService.findByCpfCnpj(cpfCnpj);

        var dtoResponse = tenantMapper.toDtoResponse(tenant);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TenantDtoResponse> findById(@PathVariable Long id) {

        var tenant = tenantService.findById(id);

        var dtoResponse = tenantMapper.toDtoResponse(tenant);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDtoResponse> updateTenant(@PathVariable Long id,
            @RequestBody TenantDtoRequest tenantDto) {

        var tenant = tenantMapper.toTenant(tenantDto);

        var updateTenant = tenantService.update(id, tenant);

        var dtoResponse = tenantMapper.toDtoResponse(updateTenant);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<List<TenantDtoResponse>> list(Sort sort) {

        var listTenant = tenantService.list(sort);

        var listDto = listTenant.stream()
                .map(tenantMapper::toDtoResponse)
                .toList();

        return new ResponseEntity<>(listDto, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        tenantService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
