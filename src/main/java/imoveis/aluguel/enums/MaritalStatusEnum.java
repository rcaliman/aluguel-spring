package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatusEnum {
    
    SOLTEIRO("solteiro(a)"),
    CASADO("casado(a)"),
    DIVORCIADO("divorciado(a)"),
    VIUVO("viúvo(a)"),
    UNIAO_ESTAVEL("união estável");

    private final String displayName;

    MaritalStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
