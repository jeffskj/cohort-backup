package org.cohortbackup.persistence;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriBuilder;

import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.remoting.SwarmWebService;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@ApplicationScoped
public class SwarmServiceImpl implements SwarmService {
    private static int DEFAULT_PORT = Integer.getInteger("cohort.defaults.port", 8080);
    private static String DEFAULT_IP = System.getProperty("cohort.defaults.ip", "127.0.0.1");
    private static String DEFAULT_CONTEXT = System.getProperty("cohort.defaults.context", "cohort");

    @Inject
    EntityManager em;
    @Inject
    @Current
    Configuration config;

    @PostConstruct
    public void init() {
        if (getCurrentNode() == null) {
            Node n = new Node();
            n.setIpAddress(DEFAULT_IP);
            n.setPort(DEFAULT_PORT);
            em.persist(n);
        }

        if (getCurrentSwarm() == null) {
            Swarm s = new Swarm();
//            s.setId(config.getSwarmId());
            Node currentNode = getCurrentNode();
            s.getNodes().add(currentNode);
            currentNode.setSwarm(s);
            em.persist(s);
        }
    }

    @Override
    public Swarm getSwarm(UUID id) {
        return em.find(Swarm.class, id);
    }

    @Override
    @Produces
    @Current
    public Swarm getCurrentSwarm() {
        return null;
//        return getSwarm(config.getSwarmId());
    }

    @Override
    @Produces
    @Current
    public Node getCurrentNode() {
        return null;
//        return em.find(Node.class, config.getNodeId());
    }

    public void addNode(UUID swarmId, Node node) {
        Swarm swarm = getSwarm(swarmId);
        swarm.getNodes().add(node);
        node.setSwarm(swarm);

        Node currentNode = getCurrentNode();
        if (currentNode.equals(swarm.getCoordinator())) {
            for (Node n : swarm.getNodes()) {
                if (n != currentNode) {
                    getClient(n).addSwarmMember(swarmId, node);
                }
            }
        }
    }

    public void synchronizeMembers() {
        Swarm swarm = getCurrentSwarm();
        Node currentNode = getCurrentNode();
        for (Node n : swarm.getNodes()) {
            if (n != currentNode) {
                getClient(n).setMembers(swarm.getId(), swarm.getNodes());
            }
        }
    }

    public void joinSwarm(Node node) {
        notNull(node);
        notNull(node.getSwarm());
        notNull(node.getSwarm().getId());

        UUID swarmId = node.getSwarm().getId();

        SwarmWebService client = getClient(node);
        Swarm swarm = client.getSwarm(swarmId);
        Node coordinator = swarm.getCoordinator();

        SwarmWebService coordinatorClient = getClient(coordinator);
        coordinatorClient.addSwarmMember(swarmId, getCurrentNode());
        Swarm currentSwarm = getCurrentSwarm();
        if (!currentSwarm.equals(swarm)) {
//            config.setSwarmId(swarmId);
            em.persist(swarm);

            Node currentNode = getCurrentNode();
            swarm.getNodes().add(currentNode);
            currentNode.setSwarm(swarm);
        }
    }

    SwarmWebService getClient(Node n) {
        return ProxyFactory.create(SwarmWebService.class, getBaseUri(n), new ApacheHttpClient4Executor(),
                ResteasyProviderFactory.getInstance());
    }

    private URI getBaseUri(Node n) {
        notNull(n.getIpAddress());
        isTrue(n.getPort() > 0);

        return UriBuilder.fromPath(DEFAULT_CONTEXT).host(n.getIpAddress()).port(n.getPort()).build();
    }
}
