package imoveis.aluguel.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropertyEntityTest {

    @Test
    @DisplayName("addPropertyLog - Deve adicionar um log à lista e setar a referência inversa")
    void addPropertyLog_ShouldAddLogToListAndSetBackReference() {

        Property property = new Property();
        PropertyLog log = new PropertyLog();

        property.addPropertyLog(log);

        assertEquals(1, property.getPropertyLogs().size(), "A lista de logs deveria conter 1 item.");
        assertTrue(property.getPropertyLogs().contains(log), "A lista deveria conter o log adicionado.");
        assertEquals(property, log.getProperty(), "A referência 'property' no log deveria ser o próprio imóvel.");

    }
}