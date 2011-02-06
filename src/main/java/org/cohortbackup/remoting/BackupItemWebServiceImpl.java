package org.cohortbackup.remoting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.cohortbackup.backup.LocalRepository;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.persistence.BackupItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/backup")
public class BackupItemWebServiceImpl implements BackupItemWebService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    BackupItemService backupItemService;

    @Inject
    LocalRepository repository;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public BackupItem getBackupItem(@PathParam("id") UUID id) {
        return backupItemService.getBackupItem(id);
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Override
    public void putBackupItem(@PathParam("id") UUID id, BackupItem item) {
        backupItemService.save(item);
    }
    
    @GET
    @Path("/{id}/contents")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] get(@PathParam("id") UUID id) throws IOException {
        logger.info("getting content for {}", id);
        return IOUtils.toByteArray(repository.getRaw(id));
    }
    
    @GET
    @Path("/{id}/checksum")
    @Produces(MediaType.TEXT_PLAIN)
    public String getChecksum(@PathParam("id") UUID id) {
        logger.info("getting checksum for {}", id);
        return repository.getRawChecksum(id);
    }

    @Override
    @PUT
    @Path("/{id}/contents")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void put(@PathParam("id") UUID id, byte[] contents) throws IOException {
        logger.info("adding contents for {} to repository", id);
        BackupItem item = getBackupItem(id);
        if (item == null) {
            throw new IllegalArgumentException("backup item doesn't exist!");
        }
        
        repository.putRaw(id, new ByteArrayInputStream(contents));
    }
}
