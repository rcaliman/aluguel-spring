package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactTypeEnum {

    CELULAR("celular"), EMAIL("email"), TELEFONE("telefone");

    private final String displayName;

    ContactTypeEnum(String displayName) {
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