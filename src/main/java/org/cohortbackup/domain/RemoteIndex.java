package org.cohortbackup.domain;


/**
 * provides a view into a backed up filesystem
 * @author Skjonsby
 *
 */
public class RemoteIndex
{
//    private final EncryptionService encryptionService; 
//    private Map<String, RemotePath> roots;
//    
//    public RemoteIndex(Node remoteNode, EncryptionService encryptionService)
//    {
//        this.encryptionService = encryptionService;
//        roots = buildIndex(remoteNode.getBackupItems());
//    }
//
//    private Map<String, RemotePath> buildIndex(Set<BackupItem> backupItems)
//    {
//        Map<String, RemotePath> roots = new HashMap<String, RemotePath>();
//        for (BackupItem backupItem : backupItems)
//        {
//            String path = encryptionService.decrypt(backupItem.getEncryptedPath());
//            String name = StringUtils.substringBefore(path, "/");
//            RemotePath root = getRoot(roots, name);
//            root.addBackupItem(path.substring(name.length() + 1), backupItem);
//        }
//        
//        return roots;
//    }
//
//    public Set<RemotePath> getRoots()
//    {
//        return new HashSet<RemotePath>(roots.values());
//    }
//    
//    private RemotePath getRoot(Map<String, RemotePath> roots, String name)
//    {
//        if (!roots.containsKey(name))
//        {
//            roots.put(name, new RemotePath(name));
//        }
//        return roots.get(name);
//    }
}