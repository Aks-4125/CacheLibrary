package com.aks4125.cachelibrary.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.aks4125.cachelibrary.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class Utils {

    public static final String IMAGE_URL = "INTENT_IMAGE_URL";
    private Context mContext;

    public Utils() {
        /*required*/
    }

    public Utils(Context context) {

        this.mContext = context;
    }

    public boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    public String projectInfo() {
        return fromHtml("<h4>Language: <b>Java, Kotlin</b></h4><br>" +
                "Features: <b>Cache</b>" +
                "<br><br>Libraries Used: " +
                "<font color=\"blue\"><br>Dagger 2.20" +
                "<br>RxJava 2.2.5" +
                "<br>RxAndroid 2+" +
                "<br>Retrofit 2.3" +
                "<br>OkHttp" +
                "<br>Butterknife" +
                "<br>javax annotation" +
                "<br>Unit tests" +
                "<br>& Android Jetpack components</font>" +
                "<br>logcat tag name= 'CacheLibrary'").toString();
    }

    @SuppressWarnings("deprecation")
    public Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            // This method is deprecated but need to use for old os devices
            return Html.fromHtml(html);
        }
        return result;
    }

    public void showSnackbarLong(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.white));
        snackbar.show();
    }

    public void showSnackbarIndefinite(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.white));
        snackbar.show();
    }

    public void showSnackbarShort(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }


    public Snackbar showSnackbar(View view, @StringRes int errorMessage, String error,
                                 @StringRes int actionLabel, View.OnClickListener clickListener) {
        String errorMessageString = view.getContext().getString(errorMessage);
        String message = String.format("%s: %s", errorMessageString, error);
        return showSnackbar(view, message, actionLabel, clickListener);
    }

    public Snackbar showSnackbar(View view, @StringRes int message,
                                 @StringRes int actionLabel,
                                 View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(actionLabel, clickListener);
        snackbar.show();
        return snackbar;
    }

    public Snackbar showSnackbar(View view, String message,
                                 @StringRes int actionLabel,
                                 View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(view, message.trim(), Snackbar.LENGTH_INDEFINITE);
        if (clickListener != null) {
            snackbar.setAction(actionLabel, clickListener);
        }
        snackbar.show();
        return snackbar;
    }

    public void showAlertDialog(Context mActivity, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        Dialog dialog;
        if (!isEmpty(negativeButtonText)) {
            dialog = new AlertDialog.Builder(mActivity)
                    .setIcon(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, okListener)
                    .setNegativeButton(negativeButtonText, cancelListener)
                    .setCancelable(cancelable)
                    .create();
        } else {
            dialog = new AlertDialog.Builder(mActivity)
                    .setIcon(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, okListener)
                    .setCancelable(cancelable)
                    .create();
        }
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();
    }


    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || str.toString().equals("null");
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    // Helper method to interpolate colors
    public int interpolateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;
        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
