package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    ADMIN("Administrador"),
    OPERATOR("Operador");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
