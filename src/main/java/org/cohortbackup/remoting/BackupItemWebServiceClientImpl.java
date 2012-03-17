package org.cohortbackup.remoting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cohort.repos.LocalRepository;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BackupItemWebServiceClientImpl implements BackupItemWebServiceClient {
    private static final int MAX_ATTEMPTS = 3;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Node, BackupItemWebService> proxyCache;// = createProxyCache();

    @Inject
    LocalRepository repository;

    @Override
    public BackupItem getBackupItem(Node from, UUID id) {
        return getProxy(from).getBackupItem(id);
    }

    @Override
    public InputStream getContents(Node from, UUID id) throws IOException {
        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            try {
//                repository.putRaw(id, new ByteArrayInputStream(getProxy(from).get(id)));
                String actualChecksum = repository.getRawChecksum(id);
                String requiredChecksum = getProxy(from).getChecksum(id);

                if (actualChecksum.equals(requiredChecksum)) {
                    return repository.get(id);
                }
            } catch (Exception e) {
                throw new RuntimeException("failed to retreive contents from node!", e);
            }
        }

        throw new RuntimeException("failed to retreive contents from node!");
    }

    @Override
    public void sendBackupItem(Node to, BackupItem item) {
        getProxy(to).putBackupItem(item.getId(), item);
    }

    @Override
    public boolean sendContents(Node to, BackupItem item) {
        UUID id = item.getId();

        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            BackupItemWebService proxy = getProxy(to);
            try {
//                proxy.put(id, IOUtils.toByteArray(repository.getRaw(id)));

                String requiredChecksum = repository.getRawChecksum(id);
                String actualChecksum = getProxy(to).getChecksum(id);

                if (actualChecksum.equals(requiredChecksum)) {
                    return true;
                }
                logger.warn("checksums didn't match for {}", id);
            } catch (Exception e) {
                logger.warn("error sending backup item", e);
            }
        }

        return false;
    }

    private BackupItemWebService getProxy(Node n) {
        return proxyCache.get(n);
    }

    // private Map<Node, BackupItemWebService> createProxyCache() {
    // return new MapMaker().concurrencyLevel(1).expireAfterAccess(1L, TimeUnit.HOURS)
    // .makeComputingMap(new Function<Node, BackupItemWebService>() {
    // @Override
    // public BackupItemWebService apply(Node n) {
    // String baseUrl = "http://" + n.getIpAddress() + ":" + n.getPort() + "/"
    // + System.getProperty("cohort.contextPath", "cohort");
    // return ProxyFactory.create(BackupItemWebService.class, baseUrl, new
    // ApacheHttpClient4Executor());
    // }});
    // }
}
