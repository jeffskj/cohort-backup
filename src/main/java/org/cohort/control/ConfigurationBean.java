package org.cohort.control;

import javax.enterprise.inject.Model;

import org.cohortbackup.domain.Configuration;
import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;

@Model
public class ConfigurationBean {

//    @Inject
//    ConfigurationService configService;
    Configuration config;

    private String reposLocation;

    public Configuration getCurrentConfiguration() {
        if (config == null) {
//            config = configService.getCurrentConfiguration();
        }
        return config;
    }

    @Transactional(TransactionPropagation.REQUIRED)
    public void saveConfiguration() {
//        configService.save(config);
    }

    public void setReposLocation(String reposLocation) {
        this.reposLocation = reposLocation;
    }

    public String getReposLocation() {
        return reposLocation;
    }
}
