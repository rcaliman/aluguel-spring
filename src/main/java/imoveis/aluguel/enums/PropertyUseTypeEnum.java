package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyUseTypeEnum {
    RESIDENTIAL("residencial"),
    COMMERCIAL("comercial");

    private final String description;

    PropertyUseTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }
}
