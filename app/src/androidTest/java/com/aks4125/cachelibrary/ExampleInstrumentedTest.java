package com.aks4125.cachelibrary;

import android.app.Instrumentation;
import android.content.Context;
import android.widget.ImageView;

import com.aks4125.cachex.DownloadUtils;
import com.aks4125.cachex.interfaces.IDataBridge;
import com.aks4125.cachex.model.DataBridge;
import com.aks4125.cachex.model.FileDataBridge;
import com.aks4125.cachex.model.ImageDataBridge;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest extends Instrumentation {
    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.aks4125.cachelibrary", appContext.getPackageName());
    }*/

    ImageView view;
    @Test
    public void testCallMultiRequests() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        final DownloadUtils mProvider = DownloadUtils.getInstance();

        final DataBridge mDataTypeImage1 =
                new ImageDataBridge(null,"https://picsum.photos/600/400?image=451",
                        new InterfaceForDataType("Image-1"));
        final DataBridge mDataTypeImage2 = new ImageDataBridge(view,"https://picsum.photos/600/400?image=451",
                new InterfaceForDataType("Image-2"));
        final DataBridge mDataTypeImage3 = new ImageDataBridge(view,"https://picsum.photos/600/400?image=222",
                new InterfaceForDataType("Image-3"));
        final DataBridge mDataTypeImage4 = new ImageDataBridge(view,"https://picsum.photos/600/400?image=25",
                new InterfaceForDataType("Image-4"));
        final DataBridge mDataTypeImage5 = new ImageDataBridge(view,"https://picsum.photos/600/400?image=25",
                new InterfaceForDataType("Image-5"));
        final DataBridge mDataTypeImage6 = new ImageDataBridge(view,"https://picsum.photos/600/400?image=45",
                new InterfaceForDataType("Image-6"));

        final DataBridge mDataTypeJson1 = new FileDataBridge("http://pastebin.com/raw/wgkJgazE", new InterfaceForDataType("JSON-1"));
        final DataBridge mDataTypeJson2 = new FileDataBridge("http://pastebin.com/raw/wgkJgazE", new InterfaceForDataType("JSON-2"));
        DataBridge mDataTypeJson3 = new FileDataBridge("http://pastebin.com/raw/wgkJgazE", new InterfaceForDataType("JSON-3"));
        System.out.println("********** Call runTestOnUiThread **********");

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.e("Test", "Test Hear");
                System.out.println("********** Test Start **********");
                // Get Images
                mProvider.getRequest(mDataTypeImage1);
                mProvider.getRequest(mDataTypeImage2);
                mProvider.getRequest(mDataTypeImage3);
                mProvider.getRequest(mDataTypeImage4);
                mProvider.getRequest(mDataTypeImage5);

                // Get JSON
                mProvider.getRequest(mDataTypeJson1);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.print(e);
                }

                mProvider.getRequest(mDataTypeJson2);

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.print(e);
                }
               /* while (!mProvider.isRequestDone()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                signal.countDown();
            }
        }).start();



        try {
            signal.await(30, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert mDataTypeImage1.getDataArray() != null && mDataTypeImage1.getDataArray().length > 0;
        assert mDataTypeImage2.getDataArray() != null && mDataTypeImage2.getDataArray().length > 0;
        assert mDataTypeImage3.getDataArray() != null && mDataTypeImage3.getDataArray().length > 0;

        assert mDataTypeJson1.getDataArray() != null && mDataTypeJson1.getDataArray().length > 0;
        assert mDataTypeJson2.getDataArray() != null && mDataTypeJson2.getDataArray().length > 0;
        if(mDataTypeImage1.getDataArray().length>0)
            System.out.print("success");
    }

    public class InterfaceForDataType implements IDataBridge {
        private String name;

        public InterfaceForDataType(String name) {
            this.name = name;
        }

        @Override
        public void onStart(DataBridge mDownloadDataType) {
            System.out.println("********** OnStart Call **********");
            System.out.println("********** Request: " + name + " **********");
            System.out.println("Come From: " + mDownloadDataType.source);
            System.out.println("Data Type: " + mDownloadDataType.getFileType().toString());
            System.out.println("Data Length: " + mDownloadDataType.getDataArray().length);
            System.out.println("Url: " + mDownloadDataType.getImageURL());
            System.out.println("********** End Request **********");
        }

        @Override
        public void onSuccess(DataBridge mDownloadDataType) {
            System.out.println("********** onSuccess Call **********");
            System.out.println("********** Request: " + name + " **********");
            System.out.println("Come From: " + mDownloadDataType.source);
            System.out.println("Data Type: " + mDownloadDataType.getFileType().toString());
            System.out.println("Data Length: " + mDownloadDataType.getDataArray().length);
            System.out.println("Url: " + mDownloadDataType.getImageURL());
            System.out.println("********** End Request **********");
        }

        @Override
        public void onFailure(DataBridge mDownloadDataType, int statusCode, byte[] errorResponse, Throwable e) {
            System.out.println("********** onFailure Call **********");
            System.out.println("Come From: " + mDownloadDataType.source);
            System.out.println("Data Type: " + mDownloadDataType.getFileType().toString());
            System.out.println("Data Length: " + mDownloadDataType.getDataArray().length);
            System.out.println("Url: " + mDownloadDataType.getImageURL());
            System.out.println("********** End Request **********");
        }

        @Override
        public void onRetry(DataBridge mDownloadDataType, int retryNo) {
            System.out.println("********** onRetry Call **********");
            System.out.println("Come From: " + mDownloadDataType.source);
            System.out.println("Data Type: " + mDownloadDataType.getFileType().toString());
            System.out.println("Data Length: " + mDownloadDataType.getDataArray().length);
            System.out.println("Url: " + mDownloadDataType.getImageURL());
            System.out.println("********** End Request **********");
        }
    }



}
