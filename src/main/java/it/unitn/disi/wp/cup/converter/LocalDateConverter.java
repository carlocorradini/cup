package it.unitn.disi.wp.cup.converter;

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
    public static final String ID = "it.unitn.disi.wp.cup.converter.LocalDateConverter";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        Object o = super.getAsObject(facesContext, uiComponent, value);

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
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null)
            return super.getAsString(facesContext, uiComponent, value);

        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            return super.getAsString(facesContext, uiComponent, Date.from(instant));
        } else {
            throw new IllegalArgumentException(String.format("Value %s is not an instanceof LocalDate", value));
        }
    }
}