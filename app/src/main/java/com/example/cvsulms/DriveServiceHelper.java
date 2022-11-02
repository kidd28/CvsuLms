package com.example.cvsulms;

import static com.example.cvsulms.CreateTask.folderId;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private static final String TAG = "GoogleDriveService";

    private final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    String secCode;


    private final String FOLDER_NAME = "CvsuLms";
    public DriveServiceHelper(Drive driveService, String secCode) {
        mDriveService = driveService;
        this.secCode = secCode;
    }
    /**
     * Upload the file to the user's My Drive Folder.
     */
    public Task<Boolean> uploadFileToGoogleDrive(String path) {

        if (folderId.isEmpty()){
            Log.e(TAG, "uploadFileToGoogleDrive: folder id not present" );
            isFolderPresent().addOnSuccessListener(id -> folderId=id)
                    .addOnFailureListener(exception -> Log.e(TAG, "Couldn't create file.", exception));
        }
        return (Task<Boolean>) Tasks.call(mExecutor, () -> {
            String mimetype = getMimeType(path);
            Log.e(TAG, "uploadFileToGoogleDrive: path: "+path );
            java.io.File filePath = new java.io.File(path);
            FileList result = mDriveService.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false").execute();
            for (File files : result.getFiles()) {
                if (files.getName().equals(secCode)){
                    File upFile = new File();
                    upFile.setName(filePath.getName());
                    upFile.setParents(Collections.singletonList(files.getId()));
                    upFile.setMimeType(mimetype);

                    FileContent mediaContent = new FileContent(mimetype, filePath);
                    File file = mDriveService.files().create(upFile, mediaContent)
                            .setFields("id,parents")
                            .execute();
                    System.out.println("File ID: " + file.getId());
                    break;
                }else {
                    File metadata = new File()
                            .setParents(Collections.singletonList(folderId))
                            .setMimeType(FOLDER_MIME_TYPE)
                            .setName(secCode);
                    File googleFolder = mDriveService.files().create(metadata).execute();
                    File upFile = new File();
                    upFile.setName(filePath.getName());
                    upFile.setParents(Collections.singletonList(googleFolder.getId()));
                    upFile.setMimeType(mimetype);

                    FileContent mediaContent = new FileContent(mimetype, filePath);
                    File file = mDriveService.files().create(upFile, mediaContent)
                            .setFields("id,parents")
                            .execute();
                    System.out.println("File ID: " + file.getId());
                    break;
                }
            }
            return false;
        });
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    /**
     * Check Folder present or not in the user's My Drive.
     */
    public Task<String> isFolderPresent() {
        return Tasks.call(mExecutor, () -> {
            FileList result = mDriveService.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false").execute();
            for (File file : result.getFiles()) {
                if (file.getName().equals(FOLDER_NAME))
                    return file.getId();
            }
            return "";
        });
    }
    public Task<String> createFolder() {
        return Tasks.call(mExecutor, () -> {
            File metadata = new File()
                    .setParents(Collections.singletonList("root"))
                    .setMimeType(FOLDER_MIME_TYPE)
                    .setName(FOLDER_NAME);


            File googleFolder = mDriveService.files().create(metadata).execute();
            if (googleFolder == null) {
                throw new IOException("Null result when requesting Folder creation.");
            }

            return googleFolder.getId();
        });
    }

}
