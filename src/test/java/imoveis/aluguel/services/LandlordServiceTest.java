package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.repositories.LandlordRepository;

@ExtendWith(MockitoExtension.class)
class LandlordServiceTest {

    @Mock private LandlordRepository landlordRepository;
    @Mock private LandlordMapper landlordMapper;
    @InjectMocks private LandlordService landlordService;

    @Test
    @DisplayName("Deve criar landlord")
    void create_ShouldSaveLandlord() {
        Landlord landlord = new Landlord();
        landlord.setMain(false);
        landlord.setContacts(List.of());
        when(landlordRepository.save(any(Landlord.class))).thenReturn(landlord);

        Landlord result = landlordService.create(landlord);

        assertNotNull(result);
        verify(landlordRepository).save(landlord);
    }

    @Test
    @DisplayName("Deve buscar landlord por CPF/CNPJ")
    void findByCpfCnpj_ShouldReturnLandlord() {
        Landlord landlord = new Landlord();
        when(landlordRepository.findByCpfCnpj("12345678901")).thenReturn(Optional.of(landlord));

        Landlord result = landlordService.findByCpfCnpj("12345678901");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar landlord inexistente")
    void findByCpfCnpj_WithNonExistent_ShouldThrowNotFoundException() {
        when(landlordRepository.findByCpfCnpj("99999999999")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> landlordService.findByCpfCnpj("99999999999"));
    }

    @Test
    @DisplayName("Deve buscar landlord por id")
    void findById_ShouldReturnLandlord() {
        Landlord landlord = new Landlord();
        when(landlordRepository.findById(1L)).thenReturn(Optional.of(landlord));

        Landlord result = landlordService.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve listar todos os landlords")
    void list_ShouldReturnAll() {
        when(landlordRepository.findAll(any(Sort.class))).thenReturn(List.of(new Landlord()));

        List<Landlord> result = landlordService.list(Sort.by("name"));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve deletar landlord")
    void deleteById_ShouldDelete() {
        Landlord landlord = new Landlord();
        when(landlordRepository.findById(1L)).thenReturn(Optional.of(landlord));

        landlordService.deleteById(1L);

        verify(landlordRepository).delete(landlord);
    }
}
