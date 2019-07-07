package it.unitn.disi.wp.cup.jstl.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * {@link FacesConverter Converter} for {@link LocalDate}
 *
 * @author Carlo Corradini
 */
@FacesConverter(value = LocalDateConverter.ID)
public final class LocalDateConverter extends DateTimeConverter {

    public static final String ID = "it.unitn.disi.wp.cup.jstl.converter.LocalDateConverter";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object o = super.getAsObject(context, component, value);

        if (o == null)
            return null;

        if (o instanceof Date) {
            Instant instant = Instant.ofEpochMilli(((Date) o).getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        } else {
            throw new IllegalArgumentException(String.format("Value %s could not be converted to a LocalDate, result super.getAsObject=%s", value, o));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null)
            return super.getAsString(context, component, value);

        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            return super.getAsString(context, component, Date.from(instant));
        } else {
            throw new IllegalArgumentException(String.format("Value %s is not an instanceof LocalDate", value));
        }
    }
}