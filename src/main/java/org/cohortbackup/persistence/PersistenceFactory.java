package org.cohortbackup.persistence;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jboss.seam.persistence.SeamManaged;

public class PersistenceFactory {
    @SeamManaged
    @Produces
    @PersistenceUnit
    @ConversationScoped
    EntityManagerFactory producerField;
}
