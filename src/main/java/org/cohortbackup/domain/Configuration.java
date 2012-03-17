package org.cohortbackup.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuration {
    private String encryptionKey;

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }
}