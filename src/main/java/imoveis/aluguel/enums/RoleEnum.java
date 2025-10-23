package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    ADMIN("administrador"), OPERADOR("operador"), LEITOR("leitor");

    private final String displayName;

    RoleEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return this.displayName;
    }

}
