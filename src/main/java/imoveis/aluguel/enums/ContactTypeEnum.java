package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactTypeEnum {
    
    PHONE("Telefone"),
    EMAIL("Email");

    private final String description;

    ContactTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

}
