package org.cohortbackup.persistence;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jboss.seam.solder.core.ExtensionManaged;

public class PersistenceFactory {
    @ExtensionManaged
    @Produces
    @PersistenceUnit(unitName = "cohort")
    @RequestScoped
    EntityManagerFactory producerField;
}
