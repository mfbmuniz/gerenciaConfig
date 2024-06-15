package gerenciaconfigsrc.util;

import gerenciaconfigsrc.enums.RolesEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Secure {
    RolesEnum[] value();
}
