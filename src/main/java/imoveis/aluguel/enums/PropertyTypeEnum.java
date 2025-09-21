package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyTypeEnum {
    RETAIL_SPACE("Loja"),
    COMERCIAL_SPACE("Sala Comercial"),
    STUDIO("Quitinete"),
    APARTMENT("Apartamento");

    private final String description;

    PropertyTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
