package com.ezimgur.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.Upload;
import com.ezimgur.file.FileManager;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.GetImageTask;
import com.ezimgur.task.SubmitGalleryImageTask;
import com.ezimgur.task.UploadImageTask;
import com.ezimgur.view.utils.ViewUtils;
import roboguice.inject.InjectView;

import java.util.UUID;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 7:59 PM
 */

public class UploadActivity extends BaseActivity {

    @InjectView (R.id.uploadTxtCaption)TextView mUploadCaption;
    @InjectView (R.id.uploadTxtTitle) TextView mUploadTitle;
    @InjectView (R.id.btnUpload) Button mButtonUpload;
    @InjectView (R.id.imgUploadImgView) ImageView mUploadImageView;
    @InjectView (R.id.uploadCheckSubmitGallery) CheckBox mCheckSubmitToGallery;

    private Uri mSelectedImageUri;
    private boolean deleteImageAfterUpload;

    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PHOTO = 101;
    private static final String TAG = "EzImgur.UploadActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminateVisibility(false);

        if (!EzApplication.isAuthenticated())
            mCheckSubmitToGallery.setEnabled(false);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_upload;
    }

    @Override
    public void finish() {
        super.finish();
        if (deleteImageAfterUpload)
            getContentResolver().delete(mSelectedImageUri, null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    this.processImageSelectionFromActivityResult(data.getData());
                    mButtonUpload.setEnabled(true);
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Log.d(TAG, "image taken: "+ mSelectedImageUri);
                    FileManager.broadcastImagesUpdatedAlert(this);
                    mButtonUpload.setEnabled(true);
                    setUriToImageView(mSelectedImageUri);
                }
                break;
        }
    }

    private boolean imageIsNotMediaExternal(Uri image) {
        if (!image.getHost().equals("media")){
            Log.d(TAG, "host that requires temp file="+image.getHost());
            Toast.makeText(this, "the image you selected does not live on this phone, it is not yet supported", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void clickSelectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void clickTakePicture(View view) {
        String randomId = UUID.randomUUID().toString();
        String fileName = "EzImgur_" + randomId.substring(0, 10) + ".jpg";

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mSelectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        catch (Exception e) {

        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectedImageUri);

        startActivityForResult(takePictureIntent, TAKE_PHOTO);

    }

    public void uploadImageToImgur(View view) {

        new UploadImageTask(this,
              String.valueOf(mUploadTitle.getText()),
              String.valueOf(mUploadCaption.getText()),
              getRealPathFromURI(mSelectedImageUri))
        {

            @Override
            protected void onSuccess(final Upload upload) throws Exception {
                super.onSuccess(upload);
                //do we need to clear the cache of image?
                if (deleteImageAfterUpload)
                    getContentResolver().delete(mSelectedImageUri, null, null);
                //do we need to submit to gallery?
                if (mCheckSubmitToGallery.isChecked()){
                    new SubmitGalleryImageTask(getContext(), upload.id, String.valueOf(mUploadTitle.getText().toString())){
                        @Override
                        protected void onSuccess(Boolean submitted) throws Exception {
                            super.onSuccess(submitted);
                            Toast.makeText(getContext(), "submitted to gallery FTW!", Toast.LENGTH_SHORT).show();

                            getImageFromNetworkAndSaveToUploadsAlbum(upload.id);
                        }

                        @Override
                        protected void onException(Exception e) throws RuntimeException {
                            super.onException(e);
                            Toast.makeText(getContext(), "Unable to submit to gallery "+ e.getMessage(), Toast.LENGTH_LONG).show();
                            getImageFromNetworkAndSaveToUploadsAlbum(upload.id);

                        }
                    }.execute();
                } else {
                    getImageFromNetworkAndSaveToUploadsAlbum(upload.id);
                }

            }
        }.execute();

        mButtonUpload.setEnabled(false);
    }

    private void getImageFromNetworkAndSaveToUploadsAlbum(String imageId) {
        new GetImageTask(this, imageId){

            @Override
            protected void onSuccess(Image image) throws Exception {
                super.onSuccess(image);
                saveImageToUploads(image);
            }
        }.execute();
    }

    private void saveImageToUploads(Image image) {
        AlbumsManager manager = new AlbumsManager(this);
        manager.addImageToUploadsAlbum(image);

        Intent intent = new Intent(this, MyImagesActivity.class);
        startActivity(intent);
    }

    private void processImageSelectionFromActivityResult(Uri imageUri) {

        if (imageIsNotMediaExternal(imageUri)) {
            // this is most likely picasa or flikr
            Log.d(TAG, "loading image from web=" +imageUri);
            mSelectedImageUri = FileManager.loadExternalImageIntoLocalUri(this, imageUri);
            Log.d(TAG, "converted external URI to local URI @ " + mSelectedImageUri);
            setUriToImageView(mSelectedImageUri);
            deleteImageAfterUpload = true;
        } else {
            //load image normally from local URI....
            Log.d(TAG, "loading local image="+imageUri);
            mSelectedImageUri = imageUri;
            setUriToImageView(mSelectedImageUri);
        }
    }

    private void setUriToImageView(Uri imageUri) {
        String selectedImagePath = getRealPathFromURI(imageUri);
        Bitmap bitmap = ViewUtils.decodeSampledBitmapFromFile(selectedImagePath, 400, 400);
        //Bitmap bt=Bitmap.createScaledBitmap(bitmap, 400, 400, false);
        mUploadImageView.setImageBitmap(bitmap);
    }

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String realPath = cursor.getString(column_index);
        Log.d(TAG, "Found real path from URI="+realPath);
        return realPath;
    }
}
