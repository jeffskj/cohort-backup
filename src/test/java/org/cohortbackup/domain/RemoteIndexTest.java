package org.cohortbackup.domain;

public class RemoteIndexTest {
    // @Test
    // public void testGetRoots()
    // {
    // Node n = new Node();
    // n.setBackupItems(createMockBackupItems());
    // RemoteIndex index = new RemoteIndex(n, createMockBackupService());
    // assertEquals(2, index.getRoots().size());
    // }
    //
    // private EncryptionService createMockBackupService()
    // {
    // EncryptionService service = EasyMock.createMock(EncryptionService.class);
    // EasyMock.expect(service.decrypt(EasyMock.isA(String.class)))
    // .andAnswer(new IAnswer<String>() {
    // public String answer() throws Throwable
    // {
    // return (String) EasyMock.getCurrentArguments()[0];
    // }
    // }).anyTimes();
    // EasyMock.replay(service);
    // return service;
    // }
    //
    // private Set<BackupItem> createMockBackupItems()
    // {
    // Set<BackupItem> items = new HashSet<BackupItem>();
    // BackupItem item1 = new BackupItem();
    // item1.setEncryptedPath("root1/path/to/it");
    // items.add(item1);
    //
    // BackupItem item2 = new BackupItem();
    // item2.setEncryptedPath("root2/path/to/it");
    // items.add(item2);
    //
    // BackupItem item3 = new BackupItem();
    // item3.setEncryptedPath("root2/path/it");
    // items.add(item3);
    //
    // return items;
    // }
}
