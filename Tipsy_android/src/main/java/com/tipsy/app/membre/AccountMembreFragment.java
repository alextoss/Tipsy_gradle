package com.tipsy.app.membre;

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

import com.stackmob.sdk.api.StackMobFile;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.commun.Membre;

/**
 * Created by Alexandre on 23/12/13.
 */
public class AccountMembreFragment extends Fragment implements TextWatcher {

    private MembreListener callback;
    protected EditText Nom;
    protected EditText Prenom;
    protected EditText Email;
    protected ImageButton Avatar;
    protected boolean change = false;
    private static int RESULT_LOAD_IMAGE = 1;
    protected static ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected TipsyApp app;
    protected Membre membre;
    protected static Bitmap bitmap_avatar = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.frag_membre_account, container, false);
        app = (TipsyApp) getActivity().getApplication();
        membre = app.getMembre();

        Nom = (EditText) fragmentView.findViewById(R.id.input_nom);
        Prenom = (EditText) fragmentView.findViewById(R.id.input_prenom);
        Email = (EditText) fragmentView.findViewById(R.id.input_mail);
        Avatar = (ImageButton) fragmentView.findViewById(R.id.avatar);

        Nom.setText(membre.getNom());
        Prenom.setText(membre.getPrenom());
        Email.setHint(membre.getEmail());
        /*URL url = null;
        try {
            url = new URL(membre.getAvatar().getS3Url());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            bitmap_avatar = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Avatar.setImageBitmap(bitmap_avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        Nom.addTextChangedListener(this);
        Prenom.addTextChangedListener(this);
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
        menu.removeItem(R.id.search);
        menu.removeItem(R.id.search_date);
        inflater.inflate(R.menu.menu_orga_edit_event, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard(getActivity());
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_validate_event:
                if (change) {
                    membre.setNom(Nom.getText().toString());
                    membre.setPrenom(Prenom.getText().toString());
                    membre.setAvatar(new StackMobFile("image/jpeg", "avatar.jpg", baos.toByteArray()));
                    membre.save(new StackMobModelCallback() {
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
        callback.setMenuTitle(MenuMembre.MON_COMPTE);
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
            // --> C'est QFLC, connard !
        }
        bitmap_avatar = bitmap;
        bitmap_avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
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
