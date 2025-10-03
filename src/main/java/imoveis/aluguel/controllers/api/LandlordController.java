package imoveis.aluguel.controllers.api;

import java.util.List;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.LandlordDtoRequest;
import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.services.LandlordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/landlord")
@RequiredArgsConstructor
public class LandlordController {

    private final LandlordService landlordService;
    private final LandlordMapper landlordMapper;

    @PostMapping
    public ResponseEntity<LandlordDtoResponse> create(@RequestBody LandlordDtoRequest dtoRequest) {

        var landlord = landlordMapper.toLandlord(dtoRequest);

        var newLandlord = landlordService.create(landlord);

        var dtoResponse = landlordMapper.toDtoResponse(newLandlord);

        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);

    }

    @GetMapping("/cpfCnpj/{cpfCnpj}")
    public ResponseEntity<LandlordDtoResponse> findByCpfCnpj(@PathVariable String cpfCnpj) {
        
        var landlord = landlordService.findByCpfCnpj(cpfCnpj);
        var dtoResponse = landlordMapper.toDtoResponse(landlord);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    
    }

    @GetMapping("/{id}")
    public ResponseEntity<LandlordDtoResponse> findById(@PathVariable Long id) {

        var landlord = landlordService.findById(id);
        var dtoResponse = landlordMapper.toDtoResponse(landlord);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<LandlordDtoResponse> update(@PathVariable Long id, LandlordDtoRequest dtoRequest) {

        var updatedLandlord = landlordMapper.toLandlord(dtoRequest);
        var landlord = landlordService.update(id, updatedLandlord);
        var dtoResponse = landlordMapper.toDtoResponse(landlord);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<List<LandlordDtoResponse>> list(Sort sort) {

        var listLandlord = landlordService.list(sort);
        var listDto = listLandlord.stream()
                        .map(landlordMapper::toDtoResponse)
                        .toList();

        return new ResponseEntity<>(listDto, HttpStatus.OK);
        
    }
}