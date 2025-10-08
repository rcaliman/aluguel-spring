package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyTypeEnum {
    RETAIL_SPACE("loja"),
    COMERCIAL_SPACE("sala comercial"),
    STUDIO("quitinete"),
    APARTMENT("apartamento");

    private final String description;

    PropertyTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
