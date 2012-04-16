package org.cohortbackup.backup;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupClient;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BackupSendServiceImplTest extends EasyMockSupport {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	
	@Test
	public void shouldSendUnsentAndUpdateBackupLog() throws IOException {
		File folder = tmp.newFolder("folder");
		File file = tmp.newFile("folder/test.txt");
		FileUtils.write(file, "some text");
		
		LocalRepository repos = new LocalRepository(tmp.getRoot());
		repos.getIndex().addRoot(new Path(folder));
		
		
		UUID id = UUID.randomUUID();
		repos.put(id, FileUtils.openInputStream(file));
		BackupItem item = new BackupItem();
		
		BackupLocation backupLocation = createMock(BackupLocation.class);
		BackupClient client = createMock(BackupClient.class);
		client.send(EasyMock.same(item), EasyMock.isA(InputStream.class));
		EasyMock.expect(backupLocation.getBackupClient()).andReturn(client);
		
		replayAll();
		
		repos.getConfig().addBackupLocation(backupLocation);
		item.setId(id);
		repos.getIndex().getOutOfDatePaths().get(0).getBackupItems().add(item);
		new BackupSendServiceImpl().sendBackups(repos);
		verifyAll();
	}
}