package com.ezimgur.file;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

public class FileManager {
	
	public static final String TAG = "EzImgur.FileManager";
	
	@SuppressLint("NewApi")
	@TargetApi(8)
	public static String saveImageToSd(Context context, Bitmap bitmap, String imageName, String imageExtension, boolean showToast) {
		boolean imageSaved = false;
		//Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        String filePath = null;
		try {
            Bitmap.CompressFormat format;
			File storagePath = getPicturesFileDirectory(context);
			
			if (bitmap != null && !bitmap.isRecycled()) {
				 storagePath.mkdirs();
				 FileOutputStream out = null;
				 File imageFile = new File(storagePath, String.format("%s%s", imageName, imageExtension));
                 imageFile.delete();
                 filePath = imageFile.getPath();
				  try { 
				      out = new FileOutputStream(imageFile);
				      format = Bitmap.CompressFormat.JPEG;
				      if (imageExtension.equals(".png"))
				    	  format = Bitmap.CompressFormat.PNG;
				      
				      imageSaved = bitmap.compress(format, 90, out);
				  } catch (Exception e) { 
				      Log.e("saveimage", "error " + e.getMessage());
				  } finally {
				      if (out != null) {
				          try {
							out.flush();
							out.close(); 
						} catch (IOException e) {
							e.printStackTrace();
						}    
				          
				      }
				  }

                    if (showToast) {
                          ContentValues values = new ContentValues(3);
                          values.put(Images.Media.TITLE, imageName);
                          values.put(Images.Media.MIME_TYPE, imageExtension.equals(".png")? "image/png":"image/jpeg");
                          values.put("_data", imageFile.getAbsolutePath()) ;
                          values.put(Media.DATA, imageFile.getAbsolutePath());
                          values.put(Media.DATE_MODIFIED, System.currentTimeMillis()/1000);
                          context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);

                          Toast.makeText(context, String.format("Image %ssaved in "+ storagePath.getName(), imageSaved ? "" : "NOT "), Toast.LENGTH_SHORT).show();

                          if (Build.VERSION.SDK_INT < 19)
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ getPicturesFileDirectory(context))));
                    }
				} 
			}catch(Exception ex) {
				Log.e(TAG, "Unable to save image to SD", ex);
				Toast.makeText(context, "unable to save image...saving without SD is not supported.", Toast.LENGTH_LONG).show();
			}
            return filePath;
		}
	
	public static Bitmap decodeUri(Context context, Uri selectedImage, int requiredSize) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < requiredSize
               || height_tmp / 2 < requiredSize) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

    }

    public static void broadcastImagesUpdatedAlert(Context context, String fileUri) {
        MediaScannerConnection.scanFile(context,
                new String[]{fileUri}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }
	
	@SuppressLint("NewApi")
	@TargetApi(8)
	public static File getPicturesFileDirectory(Context context) {
		if (android.os.Build.VERSION.SDK_INT > 7) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); 
		}
		else {
			return Environment.getExternalStorageDirectory();
		}
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@TargetApi(11)
	public static void sendTextToClipboard(Context context, String textToSend) {
		
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(textToSend);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); 
		    android.content.ClipData clip = android.content.ClipData.newPlainText("EzImgur",textToSend);
		    clipboard.setPrimaryClip(clip);
		}
		Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public static Uri loadExternalImageIntoLocalUri(Context context, Uri imageUri) {
        com.ezimgur.instrumentation.Log.d(TAG, "loading image from web=" + imageUri);
        final String[] columns = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        final Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        //columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        if (columnIndex != -1) {
            InputStream is = null;
            try {

                is = context.getContentResolver().openInputStream(imageUri);
                final File file = new File(getPicturesFileDirectory(context), "temp_upload.png");
                final OutputStream stream = new FileOutputStream(file);

                com.ezimgur.instrumentation.Log.d(TAG, "input stream created, writing external image to temp file");
                final byte[] buffer = new byte[1024];
                int read;

                while ((read = is.read(buffer)) != -1) {
                    stream.write(buffer, 0, read);
                }
                stream.flush();

                //Uri selectedUri = Uri.fromFile(file);
                int UNIQUE_BUCKET_ID = 1337;
                ContentValues values = new ContentValues(7);
                values.put(MediaStore.Images.Media.DISPLAY_NAME,"temp_upload");
                values.put(MediaStore.Images.Media.TITLE,"Temp Upload Placeholder");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Application IMage Files");
                values.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME,"EzImgur Cache");
                values.put(MediaStore.Images.Media.BUCKET_ID,UNIQUE_BUCKET_ID);
                values.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());

                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                com.ezimgur.instrumentation.Log.d(TAG, "finished writing image to file @ " + uri);
                return uri;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {

                    }
            }
        }
        return null;
    }
}
