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
import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.dtos.EnergyTitleDtoResponse;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.mappers.CommercialEnergyMapper;
import imoveis.aluguel.mappers.EnergyMapper;
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
        public EnergyMapper energyMapper() {
            return mock(EnergyMapper.class);
        }

        @Bean
        public CommercialEnergyService commercialEnergyService() {
            return mock(CommercialEnergyService.class);
        }

        @Bean
        public CommercialEnergyMapper commercialEnergyMapper() {
            return mock(CommercialEnergyMapper.class);
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
    @Autowired
    private CommercialEnergyService commercialEnergyService;
    @Autowired
    private CommercialEnergyMapper commercialEnergyMapper;

    @BeforeEach
    void setup() {

        reset(energyService, energyTitleService, energyMapper, commercialEnergyService, commercialEnergyMapper);

    }

    @Test
    @DisplayName("GET /energies - Deve exibir a lista de leituras com títulos padrão")
    void listEnergyReadings_WhenTitlesAreNull_ShouldUseDefaultTitles() throws Exception {

        EnergyDtoResponse energy1 = new EnergyDtoResponse(1L, 0.0, 0.0, 0.0, null, null, null, null, null, null, false);
        EnergyDtoResponse energy2 = new EnergyDtoResponse(2L, 0.0, 0.0, 0.0, null, null, null, null, null, null, true);

        when(energyService.listLasts()).thenReturn(List.of(energy1, energy2));
        
        EnergyTitleDtoResponse energyTitle = new EnergyTitleDtoResponse(1L, "conta 1", "conta 2", "conta 3", "ponto 4", "ponto 5");
        when(energyTitleService.findLast()).thenReturn(energyTitle);

        mockMvc.perform(get("/energies")).andExpect(status().isOk()).andExpect(view().name("energy/list"))
                .andExpect(model().attributeExists("energyReadings", "energyTitles", "currentPage"))
                .andExpect(model().attribute("energyReadings", hasSize(2)))
                .andExpect(model().attribute("energyTitles", is(energyTitle)));

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

        EnergyDtoResponse energyDto = new EnergyDtoResponse(1L, 100.0, 200.0, 300.0, null, null, null, null, null, null, false);

        when(energyService.findById(1L)).thenReturn(energyDto);

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

        imoveis.aluguel.dtos.CommercialEnergyDtoResponse commercialEnergyDto =
            new imoveis.aluguel.dtos.CommercialEnergyDtoResponse(1L, null, 100.0, 200.0, 300.0, 50.0, 150.0, null, null, false);

        when(commercialEnergyService.findById(1L)).thenReturn(commercialEnergyDto);

        mockMvc.perform(get("/energies/edit-commercial/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("energy/commercial-form"))
                .andExpect(model().attributeExists("commercialEnergy", "currentPage"));

        verify(commercialEnergyService, times(1)).findById(1L);

    }

    @Test
    @DisplayName("POST /energies/save-commercial (Create) - Deve criar energia comercial e redirecionar")
    void saveCommercialEnergy_ForCreate_ShouldCallCalculateAndRedirect() throws Exception {

        imoveis.aluguel.entities.CommercialEnergy commercialEnergySemId = new imoveis.aluguel.entities.CommercialEnergy();
        commercialEnergySemId.setId(null);

        when(commercialEnergyMapper.toEntity(any(imoveis.aluguel.dtos.CommercialEnergyDtoRequest.class)))
                .thenReturn(commercialEnergySemId);

        mockMvc.perform(post("/energies/save-commercial").with(csrf())
                .param("amount1", "100.0")
                .param("amount2", "200.0")
                .param("internalCounter", "300.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(commercialEnergyService, times(1)).calculate(commercialEnergySemId);
        verify(commercialEnergyService, never()).edit(any(), any());

    }

    @Test
    @DisplayName("POST /energies/save-commercial (Update) - Deve editar energia comercial e redirecionar")
    void saveCommercialEnergy_ForUpdate_ShouldCallEditAndRedirect() throws Exception {

        imoveis.aluguel.entities.CommercialEnergy commercialEnergyComId = new imoveis.aluguel.entities.CommercialEnergy();
        commercialEnergyComId.setId(1L);

        when(commercialEnergyMapper.toEntity(any(imoveis.aluguel.dtos.CommercialEnergyDtoRequest.class)))
                .thenReturn(commercialEnergyComId);

        mockMvc.perform(post("/energies/save-commercial").with(csrf())
                .param("id", "1")
                .param("amount1", "110.0")
                .param("amount2", "220.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/energies"));

        verify(commercialEnergyService, times(1)).edit(commercialEnergyComId, 1L);
        verify(commercialEnergyService, never()).calculate(any());

    }
}