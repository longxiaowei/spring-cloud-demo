package com.longxw.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

public class FastDFSClient {

    private static StorageClient1 client;

    static {
        try {
            ClientGlobal.init("classpath:config/fdfs_client.conf");
            System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            if(trackerServer == null){
                throw new IllegalStateException("init trackerServer failed");
            }

            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            }

            client = new StorageClient1(trackerServer, storageServer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static byte[] download(String fileId) throws IOException, MyException {
        return client.download_file1(fileId);
    }
}
