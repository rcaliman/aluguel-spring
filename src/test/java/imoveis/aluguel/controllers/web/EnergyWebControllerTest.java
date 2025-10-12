package imoveis.aluguel.controllers.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoResponseList;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.services.EnergyService;
import imoveis.aluguel.services.EnergyTitleService;

@WebMvcTest(EnergyWebController.class)
@WithMockUser
class EnergyWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {

        @Bean
        public EnergyService energyService() {
            return mock(EnergyService.class);
        }

        @Bean
        public EnergyTitleService energyTitleService() {
            return mock(EnergyTitleService.class);
        }

        @Bean
        public EnergyMapper energyMapper() {
            return mock(EnergyMapper.class);
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EnergyService energyService;
    @Autowired
    private EnergyTitleService energyTitleService;
    @Autowired
    private EnergyMapper energyMapper;

    @BeforeEach
    void setup() {

        reset(energyService, energyTitleService, energyMapper);

    }

    @Test
    @DisplayName("GET /energies - Deve exibir a lista de leituras com títulos padrão")
    void listEnergyReadings_WhenTitlesAreNull_ShouldUseDefaultTitles() throws Exception {

        Energy energy1 = new Energy();
        energy1.setId(1L);
        Energy energy2 = new Energy();
        energy2.setId(2L);

        when(energyService.listLasts()).thenReturn(List.of(energy1, energy2));
        when(energyTitleService.findLast()).thenReturn(null);

        when(energyMapper.toDtoResponseList(energy1, false))
                .thenReturn(new EnergyDtoResponseList(1L, 0L, 0L, 0L, null, null, null, null, null, null, false));
        when(energyMapper.toDtoResponseList(energy2, true))
                .thenReturn(new EnergyDtoResponseList(2L, 0L, 0L, 0L, null, null, null, null, null, null, true));

        mockMvc.perform(get("/energies")).andExpect(status().isOk()).andExpect(view().name("energy/list"))
                .andExpect(model().attributeExists("energyReadings", "energyTitles", "currentPage"))
                .andExpect(model().attribute("energyReadings", hasSize(2)))
                .andExpect(model().attribute("energyTitles", hasProperty("titleAmount1", is("conta 1"))));

    }

    @Test
    @DisplayName("POST /save (Create) - Deve chamar energyService.calculate e redirecionar")
    void saveEnergy_ForCreate_ShouldCallCalculateAndRedirect() throws Exception {

        Energy energySemId = new Energy();
        energySemId.setId(null);

        when(energyMapper.toEnergy(any(EnergyDtoRequest.class))).thenReturn(energySemId);

        mockMvc.perform(post("/energies/save").with(csrf()).param("counter1", "100").param("counter2", "200")
                .param("counter3", "300").param("billAmount", "500.0")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(energyService, times(1)).calculate(energySemId);
        verify(energyService, never()).edit(any(), any());

    }

    @Test
    @DisplayName("POST /save (Update) - Deve chamar energyService.edit e redirecionar")
    void saveEnergy_ForUpdate_ShouldCallEditAndRedirect() throws Exception {

        Energy energyComId = new Energy();
        energyComId.setId(1L);

        when(energyMapper.toEnergy(any(EnergyDtoRequest.class))).thenReturn(energyComId);

        mockMvc.perform(post("/energies/save").with(csrf()).param("id", "1").param("counter1", "110")
                .param("counter2", "220").param("counter3", "330").param("billAmount", "600.0"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/energies"));

        verify(energyService, times(1)).edit(energyComId, 1L);
        verify(energyService, never()).calculate(any());

    }

    @Test
    @DisplayName("POST /savetitle - Deve salvar os títulos e redirecionar")
    void saveEnergyTitle_ShouldCallServiceAndRedirect() throws Exception {

        EnergyTitle title = new EnergyTitle();
        title.setTitleAmount1("Ap 101");

        mockMvc.perform(post("/energies/savetitle").with(csrf()).flashAttr("energyTitle", title))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/energies"));

        verify(energyTitleService, times(1)).save(title);

    }
}
