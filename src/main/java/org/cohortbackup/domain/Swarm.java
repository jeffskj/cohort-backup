package org.cohortbackup.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Swarm
{
    @Id
    private UUID id;
    
    @OneToMany(cascade=CascadeType.ALL)
    @XmlElementWrapper(name="nodes")
    private Set<Node> nodes = new HashSet<Node>();

    @OneToOne(cascade=CascadeType.ALL)
    private Node coordinator;
    
    public void setNodes(Set<Node> nodes)
    {
        this.nodes = nodes;
    }

    public Set<Node> getNodes()
    {
        return nodes;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public UUID getId()
    {
        return id;
    }

    public void setCoordinator(Node coordinator) {
        this.coordinator = coordinator;
    }

    public Node getCoordinator() {
        return coordinator;
    }
}
