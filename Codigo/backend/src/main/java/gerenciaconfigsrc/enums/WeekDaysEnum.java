package gerenciaconfigsrc.enums;

import java.io.Serializable;


public enum WeekDaysEnum implements Serializable {
    SEGUNDA("SEGUNDA"),
    TERCA("TERÇA"),
    QUARTA("QUARTA"),
    QUINTA("QUINTA"),
    SEXTA("SEXTA"),
    SABADO("SÁBADO"),
    DOMINGO("DOMINGO");

    private final String code;

    WeekDaysEnum(String code) {
        this.code = code;
    }

    public static WeekDaysEnum getByCd(String cd) {
        for(WeekDaysEnum e : values()) {
            if(e.code.equals(cd)) return e;
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}

