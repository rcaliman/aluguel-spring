package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;

import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.entities.Landlord;

public interface LandlordService {

    LandlordDtoResponse create(Landlord landlord);

    LandlordDtoResponse findByCpfCnpj(String cpfCnpj);

    LandlordDtoResponse findById(Long id);

    LandlordDtoResponse update(Long id, Landlord landlord);

    List<LandlordDtoResponse> list(Sort sort);

    void deleteById(Long id);

}
