package org.cohort.control;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@RequestScoped
@FacesConverter("uuid")
public class UUIDConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
        return UUID.fromString(value);
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

}
