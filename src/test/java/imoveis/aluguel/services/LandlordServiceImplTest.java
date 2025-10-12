package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.repositories.LandlordRepository;

@ExtendWith(MockitoExtension.class)
@WithMockUser
class LandlordServiceImplTest {

    @Mock
    private LandlordRepository landlordRepository;

    @Mock
    private LandlordMapper landlordMapper;

    @InjectMocks
    private LandlordServiceImpl landlordService;

    private Landlord landlord;

    @BeforeEach
    void setUp() {

        landlord = new Landlord();

        landlord.setId(1L);
        landlord.setName("João da Silva");
        landlord.setCpfCnpj("123.456.789-00");
        landlord.setDateOfBirth(LocalDate.of(1980, 1, 1));
        landlord.setMain(false);

        Contact contact1 = new Contact();

        contact1.setId(10L);
        contact1.setContact("99999-1111");

        landlord.setContacts(new ArrayList<>(List.of(contact1)));
    }

    @Test
    @DisplayName("Deve criar um locador com sucesso")
    void create_ShouldSaveLandlord() {

        landlord.setMain(false);
        when(landlordRepository.save(any(Landlord.class))).thenReturn(landlord);

        Landlord createdLandlord = landlordService.create(landlord);

        assertNotNull(createdLandlord);
        assertEquals("João da Silva", createdLandlord.getName());
        verify(landlordRepository, times(1)).save(landlord);
        verify(landlordRepository, never()).setAllMainToFalse();

    }

    @Test
    @DisplayName("Deve definir outros locadores como não principais ao criar um novo locador principal")
    void create_WhenMainIsTrue_ShouldSetOthersToFalse() {

        landlord.setMain(true);
        when(landlordRepository.save(any(Landlord.class))).thenReturn(landlord);

        landlordService.create(landlord);

        verify(landlordRepository, times(1)).setAllMainToFalse(); // Verifica que o método foi chamado
        verify(landlordRepository, times(1)).save(landlord);

    }

    @Test
    @DisplayName("Deve atualizar um locador existente")
    void update_WhenLandlordExists_ShouldUpdateAndSave() {

        Landlord updatedInfo = new Landlord();
        updatedInfo.setName("João da Silva Santos");
        updatedInfo.setMain(true);
        updatedInfo.setContacts(new ArrayList<>());

        when(landlordRepository.findById(anyLong())).thenReturn(Optional.of(landlord));
        when(landlordRepository.saveAndFlush(any(Landlord.class))).thenReturn(landlord);

        Landlord result = landlordService.update(1L, updatedInfo);

        assertNotNull(result);
        verify(landlordMapper, times(1)).updateEntity(updatedInfo, landlord);
        verify(landlordRepository, times(1)).setAllMainToFalse();
        verify(landlordRepository, times(1)).saveAndFlush(landlord);

    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar atualizar locador inexistente")
    void update_WhenLandlordNotFound_ShouldThrowException() {

        when(landlordRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            landlordService.update(99L, new Landlord());
        });

        verify(landlordRepository, never()).saveAndFlush(any(Landlord.class));
    }

    @Test
    @DisplayName("Deve encontrar um locador pelo ID")
    void findById_WhenLandlordExists_ShouldReturnLandlord() {

        when(landlordRepository.findById(1L)).thenReturn(Optional.of(landlord));

        Landlord found = landlordService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());

    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar ID inexistente")
    void findById_WhenLandlordNotFound_ShouldThrowException() {

        when(landlordRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            landlordService.findById(99L);
        });

        assertEquals("Locador de id 99 não encontrado", exception.getMessage());

    }

    @Test
    @DisplayName("Deve retornar uma lista de locadores")
    void list_ShouldReturnListOfLandlords() {

        List<Landlord> landlords = List.of(landlord, new Landlord());
        Sort sort = Sort.by("name");
        when(landlordRepository.findAll(sort)).thenReturn(landlords);

        List<Landlord> result = landlordService.list(sort);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(landlordRepository, times(1)).findAll(sort);

    }

    @Test
    @DisplayName("Deve deletar um locador pelo ID")
    void deleteById_WhenLandlordExists_ShouldDeleteLandlord() {

        when(landlordRepository.findById(1L)).thenReturn(Optional.of(landlord));
        doNothing().when(landlordRepository).delete(landlord);

        landlordService.deleteById(1L);

        verify(landlordRepository, times(1)).delete(landlord);

    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar deletar locador inexistente")
    void deleteById_WhenLandlordNotFound_ShouldThrowException() {

        when(landlordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            landlordService.deleteById(99L);
        });

        verify(landlordRepository, never()).delete(any(Landlord.class));
    }
}