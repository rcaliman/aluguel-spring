package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.repositories.LandlordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LandlordServiceImpl implements LandlordService {

    private final LandlordRepository landlordRepository;
    private final LandlordMapper landlordMapper;

    @Override
    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public LandlordDtoResponse create(Landlord landlord) {

        if (landlord.getMain()) {
            landlordRepository.setAllMainToFalse();
        }

        landlord.getContacts().forEach(contact -> {
            contact.setId(null);
            contact.setTenant(null);
            contact.setLandlord(landlord);
        });

        var savedLandlord = landlordRepository.save(landlord);

        return landlordMapper.toDtoResponse(savedLandlord);

    }

    @Override
    @Cacheable(value = "landlords", key = "#cpfCnpj")
    public LandlordDtoResponse findByCpfCnpj(String cpfCnpj) {

        var landlord = landlordRepository.findByCpfCnpj(cpfCnpj).orElseThrow(
                () -> new NotFoundException(String.format("Locador de cpf %s não encontrado", cpfCnpj)));

        return landlordMapper.toDtoResponse(landlord);

    }

    @Override
    @Cacheable(value = "landlords", key = "#id")
    public LandlordDtoResponse findById(Long id) {

        var landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

        return landlordMapper.toDtoResponse(landlord);

    }

    @Override
    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public LandlordDtoResponse update(Long id, Landlord updatedLandlord) {

        var landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

        if (updatedLandlord.getMain()) {
            landlordRepository.setAllMainToFalse();
        }

        landlord.getContacts().clear();
        landlordRepository.flush();

        if (updatedLandlord.getContacts() != null) {
            updatedLandlord.getContacts().forEach(contact -> {
                contact.setId(null);
                contact.setTenant(null);

                contact.setLandlord(landlord);
                landlord.getContacts().add(contact);
            });
        }

        landlordMapper.updateEntity(updatedLandlord, landlord);

        var savedLandlord = landlordRepository.saveAndFlush(landlord);

        return landlordMapper.toDtoResponse(savedLandlord);

    }

    @Override
    @Cacheable(value = "landlords", key = "'list-' + #sort.toString()")
    public List<LandlordDtoResponse> list(Sort sort) {

        var orderedList = landlordRepository.findAll(sort);

        return orderedList.stream()
                    .map(landlordMapper::toDtoResponse)
                    .toList();

    }

    @Override
    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public void deleteById(Long id) {

        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

        landlordRepository.delete(landlord);

    }

}
