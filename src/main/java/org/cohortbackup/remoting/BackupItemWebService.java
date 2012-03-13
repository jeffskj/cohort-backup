package org.cohortbackup.remoting;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cohortbackup.domain.BackupItem;

@Path("/backup")
public interface BackupItemWebService {
    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    void putBackupItem(@PathParam("id") UUID id, BackupItem item);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    BackupItem getBackupItem(@PathParam("id") UUID id);

    @GET
    @Path("/{id}/contents")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    byte[] get(@PathParam("id") UUID id) throws IOException;

    @GET
    @Path("/{id}/checksum")
    @Produces(MediaType.TEXT_PLAIN)
    String getChecksum(@PathParam("id") UUID id) throws IOException;

    @PUT
    @Path("/{id}/contents")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void put(@PathParam("id") UUID id, byte[] contents) throws IOException;
}