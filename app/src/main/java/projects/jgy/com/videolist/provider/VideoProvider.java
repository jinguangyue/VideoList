package projects.jgy.com.videolist.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import projects.jgy.com.videolist.bean.Video;
import projects.jgy.com.videolist.inteface.AbstructProvider;

/**
 * Created by yue on 2016/11/18.
 */

public class VideoProvider implements AbstructProvider {
    private Activity context;

    public VideoProvider(Activity context) {
        this.context = context;
    }

    public static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};
    public static String[] mediaColumns = {MediaStore.Video.Media._ID};

    public static String getThumbnailPathForLocalFile(Activity context,
                                                      Uri fileUri) {

        long fileId = getFileId(context, fileUri);

        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
        }

        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }

    @Override
    public List<?> getList() {
        List<Video> list = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        ContentResolver contentResolver = context.getContentResolver();
        if (context != null) {
            Cursor cursor = contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);

            if (cursor != null) {
                list = new ArrayList<>();

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));

                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));

                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));

                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));

                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                    int tempid = cursor
                            .getColumnIndex(MediaStore.Video.Thumbnails._ID);

                    String temp = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Video.Thumbnails._ID));

//                    LogUtils.i("temp===" + temp);

                  /*  long thumbNailsId = cursor.getLong(cursor.getColumnIndex("_ID"));
                    //若为视频则为
                    Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver,
                          thumbNailsId, MediaStore.Video.Thumbnails.MICRO_KIND, null);*/

                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                    Video video = new Video(id, title, album, artist, displayName, mimeType, path, size, duration);
                    list.add(video);
                }

                cursor.close();
            }
        }
        return list;
    }
}
