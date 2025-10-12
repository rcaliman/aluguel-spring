package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyTypeEnum {

    LOJA("loja"), SALA_COMERCIAL("sala comercial"), QUITINETE("quitinete"), APARTAMENTO("apartamento"), CONDOMINIO("condomínio");

    private final String displayName;

    PropertyTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }
}
