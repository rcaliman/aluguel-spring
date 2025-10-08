package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    ADMIN("administrador"),
    OPERATOR("operador");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
