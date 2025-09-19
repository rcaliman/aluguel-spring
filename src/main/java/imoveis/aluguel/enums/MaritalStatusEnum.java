package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatusEnum {
    SINGLE("Solteiro(a)"),
    MARRIED("Casado(a)"),
    DIVORCED("Divorciado(a)"),
    WIDOWED("Viuvo(a)"),
    SEPARATED("Separado(a)");

    private final String description;

    MaritalStatusEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
