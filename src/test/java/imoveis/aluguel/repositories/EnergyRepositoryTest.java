package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Energy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class EnergyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnergyRepository energyRepository;

    @Test
    @DisplayName("Deve persistir uma leitura de energia com sucesso quando todos os campos obrigatórios são preenchidos")
    void save_ShouldPersistEnergyReading_WhenDataIsValid() {

        Energy energy = new Energy();
        energy.setCounter1(1000L);
        energy.setCounter2(2000L);
        energy.setCounter3(3000L);

        Energy savedEnergy = energyRepository.save(energy);
        entityManager.flush();
        entityManager.clear();

        Energy foundEnergy = energyRepository.findById(savedEnergy.getId()).orElse(null);

        assertNotNull(foundEnergy);
        assertEquals(1000L, foundEnergy.getCounter1());

    }

    @Test
    @DisplayName("Deve falhar ao salvar uma leitura de energia com 'counter1' nulo")
    void save_WhenCounter1IsNull_ShouldThrowException() {

        Energy energy = new Energy();
        energy.setCounter2(2000L);
        energy.setCounter3(3000L);

        assertThrows(DataIntegrityViolationException.class, () -> {
            energyRepository.saveAndFlush(energy);
        }, "Deveria falhar ao salvar com 'counter1' nulo.");

    }

    @Test
    @DisplayName("Deve falhar ao salvar uma leitura de energia com 'counter2' nulo")
    void save_WhenCounter2IsNull_ShouldThrowException() {

        Energy energy = new Energy();
        energy.setCounter1(1000L);
        energy.setCounter3(3000L);

        assertThrows(DataIntegrityViolationException.class, () -> {
            energyRepository.saveAndFlush(energy);
        }, "Deveria falhar ao salvar com 'counter2' nulo.");

    }

    @Test
    @DisplayName("Deve falhar ao salvar uma leitura de energia com 'counter3' nulo")
    void save_WhenCounter3IsNull_ShouldThrowException() {

        Energy energy = new Energy();
        energy.setCounter1(1000L);
        energy.setCounter2(2000L);

        assertThrows(DataIntegrityViolationException.class, () -> {
            energyRepository.saveAndFlush(energy);
        }, "Deveria falhar ao salvar com 'counter3' nulo.");
    }
}