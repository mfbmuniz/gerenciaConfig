package gerenciaconfigsrc.enums;

import java.io.Serializable;


public enum SexEnum implements Serializable {
    MALE("M"),
    FEMALE("F");

    private final String code;

    SexEnum(String code) {
        this.code = code;
    }

    public static SexEnum getByCd(String cd) {
        for(SexEnum e : values()) {
            if(e.code.equals(cd)) return e;
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}

