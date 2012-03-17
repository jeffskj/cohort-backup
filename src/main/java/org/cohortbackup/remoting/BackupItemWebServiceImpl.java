package org.cohortbackup.remoting;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.cohort.repos.LocalRepository;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.persistence.BackupItemService;
import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/backup")
@Transactional(TransactionPropagation.REQUIRED)
@RequestScoped
public class BackupItemWebServiceImpl implements BackupItemWebService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    BackupItemService backupItemService;

    @Inject
    LocalRepository repository;

    public BackupItem getBackupItem(@PathParam("id") UUID id) {
        return backupItemService.getBackupItem(id);
    }

    public void putBackupItem(@PathParam("id") UUID id, BackupItem item) {
        backupItemService.save(item);
    }

    public byte[] get(@PathParam("id") UUID id) throws IOException {
        logger.info("getting content for {}", id);
//        return IOUtils.toByteArray(repository.getRaw(id));
        return null;
    }

    public String getChecksum(@PathParam("id") UUID id) {
        logger.info("getting checksum for {}", id);
        return repository.getRawChecksum(id);
    }

    public void put(@PathParam("id") UUID id, byte[] contents) throws IOException {
        logger.info("adding contents for {} to repository", id);
        BackupItem item = getBackupItem(id);
        if (item == null) {
            throw new IllegalArgumentException("backup item doesn't exist!");
        }

//        repository.putRaw(id, new ByteArrayInputStream(contents));
    }
}
