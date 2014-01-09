package com.tipsy.app.orga;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.commun.Organisateur;

/**
 * Created by Alexandre on 23/12/13.
 */
public class AccountOrgaFragment extends Fragment implements TextWatcher {

    private OrgaListener callback;
    protected EditText Orga;
    protected EditText Email;
    protected ImageButton Avatar;
    protected boolean change = false;
    private static int RESULT_LOAD_IMAGE = 1;
    protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected TipsyApp app;
    protected Organisateur orga;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.frag_orga_account, container, false);
        app = (TipsyApp) getActivity().getApplication();
        orga = app.getOrga();

        Orga = (EditText) fragmentView.findViewById(R.id.input_orga);
        Email = (EditText) fragmentView.findViewById(R.id.input_mail);
        Avatar = (ImageButton) fragmentView.findViewById(R.id.avatar);

        Orga.setText(orga.getNom());
        Email.setHint(orga.getEmail());
        /*URL url = null;
        Bitmap avatar = null;
        try {
            url = new URL(orga.getAvatar().getS3Url());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            avatar = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Avatar.setImageBitmap(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Orga.addTextChangedListener(this);
        Email.setFocusable(false);
        Email.setEnabled(false);

        Avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_orga_edit_event, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        hideKeyboard(getActivity());
        switch (item.getItemId()) {
            case R.id.action_validate_event:
                if (change) {
                    orga.setNom(Orga.getText().toString());
                    //orga.setAvatar(new StackMobFile("image/jpeg", "avatar.jpg", baos.toByteArray()));
                    orga.save(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            callback.goToTableauDeBord(false);
                        }

                        // En cas d'échec
                        @Override
                        public void failure(StackMobException e) {
                            Toast.makeText(getActivity(), "Sauvegarde échouée", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    callback.goToTableauDeBord(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void afterTextChanged(Editable s) {
        change = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuOrga.MON_COMPTE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            File imgFile = new File(picturePath);
            cursor.close();

            Avatar.setImageBitmap(rotate(imgFile));

        }
    }

    public static Bitmap rotate(File f) {

        Bitmap bitmap = resize(f);

        try {
            ExifInterface exif = new ExifInterface(f.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                matrix.postRotate(90);
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                matrix.postRotate(180);
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
            Log.d("QLFCONNARD", "Rotate" + e.getMessage());
        }
        return bitmap;
    }

    //decodes image and scales it to reduce memory consumption
    public static Bitmap resize(File f) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
