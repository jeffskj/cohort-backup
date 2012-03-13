package org.cohortbackup.remoting;

import java.util.UUID;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.StringConverter;

@Provider
public class UUIDStringConverter implements StringConverter<UUID> {
    @Override
    public UUID fromString(String str) {
        return UUID.fromString(str);
    }

    @Override
    public String toString(UUID value) {
        return value.toString();
    }
}
