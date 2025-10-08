package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UseTypeEnum {

    RESIDENCIAL("residencial"),
    COMERCIAL("comercial");

    private final String displayName;

    UseTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
