package imoveis.aluguel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PersonTypeEnum {
    LANDLORD("Locador(a)"),
    TENANT("Locatário(a)");

    private final String description;

    PersonTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

}
