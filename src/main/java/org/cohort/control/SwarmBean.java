package org.cohort.control;

import java.util.List;
import java.util.UUID;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.persistence.SwarmService;
import org.jboss.seam.transaction.Transactional;

import com.google.common.collect.Lists;

@Model
public class SwarmBean {

    @Inject
    SwarmService swarmService;

    @Inject
    @New
    private Node node;

    public Swarm getSwarm() {
        return swarmService.getCurrentSwarm();
    }

    @Transactional
    public void addMember() {
        Swarm swarm = getSwarm();
        node.setId(UUID.randomUUID());
        swarm.getNodes().add(node);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public List<Node> getCurrentNodes() {
        return Lists.newArrayList(getSwarm().getNodes());
    }
}
