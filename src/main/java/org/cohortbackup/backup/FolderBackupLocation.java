package org.cohortbackup.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.cohortbackup.domain.BackupClient;
import org.cohortbackup.domain.BackupLocation;

public class FolderBackupLocation implements BackupLocation {
	private UUID id;
	private File location;

	@Override
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public File getLocation() {
		return location;
	}
	
	public void setLocation(File location) {
		this.location = location;
	}

	@Override
	public BackupClient getBackupClient() {
		return new BackupClient() {
			@Override
			public void send(String path, InputStream input) {
				try {
					FileUtils.copyInputStreamToFile(input, new File(getLocation(), path));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
}