package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.EnergyTitle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class EnergyTitleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnergyTitleRepository energyTitleRepository;

    @Test
    @DisplayName("Deve persistir e recuperar um EnergyTitle com sucesso")
    void saveAndFind_ShouldPersistEnergyTitle() {

        EnergyTitle energyTitle = new EnergyTitle();
        energyTitle.setTitleAmount1("Apartamento 101");
        energyTitle.setTitleAmount2("Apartamento 102");
        energyTitle.setTitleAmount3("Área Comum");

        EnergyTitle savedEnergyTitle = energyTitleRepository.save(energyTitle);
        entityManager.flush();
        entityManager.clear();

        EnergyTitle foundEnergyTitle = energyTitleRepository.findById(savedEnergyTitle.getId()).orElse(null);

        assertNotNull(foundEnergyTitle, "O título de energia não deveria ser nulo após ser buscado.");
        assertNotNull(foundEnergyTitle.getId(), "O ID deveria ter sido gerado pelo banco de dados.");
        assertEquals("Apartamento 101", foundEnergyTitle.getTitleAmount1());
        assertEquals("Apartamento 102", foundEnergyTitle.getTitleAmount2());
        assertEquals("Área Comum", foundEnergyTitle.getTitleAmount3());

    }
}