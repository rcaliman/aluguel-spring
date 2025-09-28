package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;

import imoveis.aluguel.entities.Landlord;

public interface LandlordService {
    
    Landlord create(Landlord landlord);

    Landlord findByCpfCnpj(String cpfCnpj);

    Landlord findById(Long id);

    Landlord update(Long id, Landlord landlord);

    List<Landlord> list(Sort sort);

    void deleteById(Long id);
    
}
