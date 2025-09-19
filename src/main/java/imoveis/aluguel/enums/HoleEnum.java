package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HoleEnum {
    ADMIN("Administrador"),
    OPERATOR("Operador");

    private final String description;

    HoleEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
