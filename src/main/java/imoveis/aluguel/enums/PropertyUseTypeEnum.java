package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyUseTypeEnum {
    
    RESIDENCIAL("residencial"), COMERCIAL("comercial");

    private final String displayName;

    PropertyUseTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

}
