package org.cohort.control;

import java.io.File;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@RequestScoped
@FacesConverter("file")
public class FileConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
        return new File(value);
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

}
