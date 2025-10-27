package imoveis.aluguel.controllers.web;

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

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.services.CommercialEnergyService;
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
        public CommercialEnergyService commercialEnergyService() {
            return mock(CommercialEnergyService.class);
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EnergyService energyService;
    @Autowired
    private EnergyTitleService energyTitleService;
    @Autowired
    private CommercialEnergyService commercialEnergyService;

    @BeforeEach
    void setup() {

        reset(energyService, energyTitleService, commercialEnergyService);

    }

    @Test
    @DisplayName("GET /energies - Deve exibir a lista de leituras com títulos padrão")
    void listEnergyReadings_WhenTitlesAreNull_ShouldUseDefaultTitles() throws Exception {

        EnergyDtoResponse energy1 = new EnergyDtoResponse(1L, 0.0, 0.0, 0.0, null, null, null, null, null, null, false);
        EnergyDtoResponse energy2 = new EnergyDtoResponse(2L, 0.0, 0.0, 0.0, null, null, null, null, null, null, true);

        when(energyService.listLasts()).thenReturn(List.of(energy1, energy2));

        EnergyTitle energyTitle = new EnergyTitle();
        energyTitle.setId(1L);
        energyTitle.setTitleAmount1("conta 1");
        energyTitle.setTitleAmount2("conta 2");
        energyTitle.setTitleAmount3("conta 3");
        energyTitle.setTitleAmount4("ponto 4");
        energyTitle.setTitleAmount5("ponto 5");
        when(energyTitleService.findLast()).thenReturn(energyTitle);

        mockMvc.perform(get("/energies")).andExpect(status().isOk()).andExpect(view().name("energy/list"))
                .andExpect(model().attributeExists("energyReadings", "energyTitles", "currentPage"))
                .andExpect(model().attribute("energyReadings", hasSize(2)))
                .andExpect(model().attribute("energyTitles", is(energyTitle)));

    }

    @Test
    @DisplayName("POST /save (Create) - Deve chamar energyService.calculate e redirecionar")
    void saveEnergy_ForCreate_ShouldCallCalculateAndRedirect() throws Exception {

        mockMvc.perform(post("/energies/save").with(csrf()).param("counter1", "100").param("counter2", "200")
                .param("counter3", "300").param("billAmount", "500.0")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(energyService, times(1)).calculate(any(Energy.class));
        verify(energyService, never()).edit(any(), any());

    }

    @Test
    @DisplayName("POST /save (Update) - Deve chamar energyService.edit e redirecionar")
    void saveEnergy_ForUpdate_ShouldCallEditAndRedirect() throws Exception {

        mockMvc.perform(post("/energies/save").with(csrf()).param("id", "1").param("counter1", "110")
                .param("counter2", "220").param("counter3", "330").param("billAmount", "600.0"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/energies"));

        verify(energyService, times(1)).edit(any(Energy.class), any());
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

    @Test
    @DisplayName("GET /energies/new - Deve exibir o formulário de nova leitura")
    void showCreateForm_ShouldReturnFormView() throws Exception {

        mockMvc.perform(get("/energies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/form"))
                .andExpect(model().attributeExists("energy", "currentPage"))
                .andExpect(model().attribute("currentPage", "energies"));

    }

    @Test
    @DisplayName("GET /energies/edit/{id} - Deve exibir o formulário de edição")
    void showEditForm_ShouldReturnFormViewWithEnergy() throws Exception {

        Energy energy = new Energy();
        energy.setId(1L);
        energy.setCounter1(100.0);
        energy.setCounter2(200.0);
        energy.setCounter3(300.0);

        when(energyService.findById(1L)).thenReturn(energy);

        mockMvc.perform(get("/energies/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/form"))
                .andExpect(model().attributeExists("energy", "currentPage"));

        verify(energyService, times(1)).findById(1L);

    }

    @Test
    @DisplayName("GET /energies/titleform - Deve exibir o formulário de títulos")
    void titleForm_ShouldReturnTitleFormView() throws Exception {

        mockMvc.perform(get("/energies/titleform"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/titleform"))
                .andExpect(model().attributeExists("energyTitle", "currentPage"))
                .andExpect(model().attribute("currentPage", "energies"));

    }

    @Test
    @DisplayName("GET /energies/new-commercial - Deve exibir o formulário de energia comercial")
    void showCreateFormCommercial_ShouldReturnCommercialFormView() throws Exception {

        mockMvc.perform(get("/energies/new-commercial"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/commercial-form"))
                .andExpect(model().attributeExists("commercialEnergy", "currentPage"))
                .andExpect(model().attribute("currentPage", "energies"));

    }

    @Test
    @DisplayName("GET /energies/edit-commercial/{id} - Deve exibir o formulário de edição comercial")
    void showEditFormCommercial_ShouldReturnCommercialFormWithEnergy() throws Exception {

        imoveis.aluguel.entities.CommercialEnergy commercialEnergy = new imoveis.aluguel.entities.CommercialEnergy();
        commercialEnergy.setId(1L);
        commercialEnergy.setAmount1(100.0);
        commercialEnergy.setAmount2(200.0);
        commercialEnergy.setInternalCounter(300.0);
        commercialEnergy.setAccountConsumption(50.0);
        commercialEnergy.setAccountValue(150.0);

        when(commercialEnergyService.findById(1L)).thenReturn(commercialEnergy);

        mockMvc.perform(get("/energies/edit-commercial/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/commercial-form"))
                .andExpect(model().attributeExists("commercialEnergy", "currentPage"));

        verify(commercialEnergyService, times(1)).findById(1L);

    }

    @Test
    @DisplayName("POST /energies/save-commercial (Create) - Deve criar energia comercial e redirecionar")
    void saveCommercialEnergy_ForCreate_ShouldCallCalculateAndRedirect() throws Exception {

        mockMvc.perform(post("/energies/save-commercial").with(csrf())
                .param("amount1", "100.0")
                .param("amount2", "200.0")
                .param("internalCounter", "300.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(commercialEnergyService, times(1)).calculate(any(imoveis.aluguel.entities.CommercialEnergy.class));
        verify(commercialEnergyService, never()).edit(any(), any());

    }

    @Test
    @DisplayName("POST /energies/save-commercial (Update) - Deve editar energia comercial e redirecionar")
    void saveCommercialEnergy_ForUpdate_ShouldCallEditAndRedirect() throws Exception {

        mockMvc.perform(post("/energies/save-commercial").with(csrf())
                .param("id", "1")
                .param("amount1", "110.0")
                .param("amount2", "220.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(commercialEnergyService, times(1)).edit(any(imoveis.aluguel.entities.CommercialEnergy.class), any());
        verify(commercialEnergyService, never()).calculate(any());

    }
}