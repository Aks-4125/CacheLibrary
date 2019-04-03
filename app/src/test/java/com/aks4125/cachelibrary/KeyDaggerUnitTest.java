package com.aks4125.cachelibrary;

import android.text.TextUtils;

import com.aks4125.cachelibrary.di.component.AppComponent;
import com.aks4125.cachelibrary.di.component.DaggerAppComponent;
import com.aks4125.cachelibrary.di.module.ApplicationModule;
import com.aks4125.cachelibrary.di.module.RetrofitModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class KeyDaggerUnitTest {


    private MyApp mApp;

    private static String generateKeyHash(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public MyApp getApp() {
        return mApp;
    }

    public void setApp(MyApp mApp) {
        this.mApp = mApp;
    }

    @Before
    public void setUp() {
        // some other code, even a mocked context if needed (Mockito.mock(Context.class)) to use in the other modules

        AppComponent component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(getApp()))
                // other modules
                .build();

        System.out.print(component.utils().isValidEmail("Aks4125@gmail.com")); // DI test case sample

    }

    @Test
    public void workMD5() {
        String str_1 = "https://picsum.photos/600/400?image=451";
        String str_2 = "https://picsum.photos/600/400?image=451";
        String str_3 = "https://picsum.photos/600/400?image=451";
        String str_4 = "https://picsum.photos/600/400?image=451";
        String str_5 = "https://picsum.photos/600/400?image=451";
        String str_6 = "https://picsum.photos/600/400?image=451";


        String key = generateKeyHash(str_1);
        assertEquals(key, generateKeyHash(str_1));
        assertEquals(key, generateKeyHash(str_2));
        assertEquals(key, generateKeyHash(str_3));
        assertEquals(key, generateKeyHash(str_4));
        assertEquals(key, generateKeyHash(str_5));
        assertEquals(key, generateKeyHash(str_6));
    }

    @Singleton
    @Component(modules = {RetrofitModule.class})
    interface TestComponent {

    }
}
