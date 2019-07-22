package it.unitn.disi.wp.cup.jstl.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * {@link FacesConverter Converter} for {@link LocalTime}
 *
 * @author Carlo Corradini
 */
@FacesConverter(value = LocalTimeConverter.ID)
public final class LocalTimeConverter extends DateTimeConverter {

    public static final String ID = "it.unitn.disi.wp.cup.jstl.converter.LocalTimeConverter";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object o = super.getAsObject(context, component, value);

        if (o == null)
            return null;

        if (o instanceof Date) {
            Instant instant = Instant.ofEpochMilli(((Date) o).getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
        } else {
            throw new IllegalArgumentException(String.format("Value %s could not be converted to a LocalTime, result super.getAsObject=%s", value, o));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null)
            return super.getAsString(context, component, value);

        if (value instanceof LocalTime) {
            LocalTime localTime = (LocalTime) value;
            Instant instant = localTime.atDate(
                    LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )).atZone(ZoneId.systemDefault()).toInstant();
            return super.getAsString(context, component, Date.from(instant));
        } else {
            throw new IllegalArgumentException(String.format("Value %s is not an instanceof LocalTime", value));
        }
    }
}
