package com.myapplication;





import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.android.volley.Header;
import com.android.volley.Request.Method;
import com.myapplication.mock.TestRequest;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class HurlStackTest {

   // URL url =  new URL("http://api.acme.international/fortune");
    @Mock private HttpURLConnection mMockConnection;
    private HurlStack mHurlStack;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mMockConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        mHurlStack =
                new HurlStack() {
                    @Override
                    protected HttpURLConnection createConnection(URL url) {
                        return mMockConnection;
                    }
                };
    }

    @Test
    public void connectionForDeprecatedGetRequest() throws Exception {
        TestRequest.DeprecatedGet request = new TestRequest.DeprecatedGet();
        assertEquals(request.getMethod(), Method.DEPRECATED_GET_OR_POST);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        verify(mMockConnection, never()).setRequestMethod(anyString());
        verify(mMockConnection, never()).setDoOutput(true);
    }


    @Test
    public void connectionForGetRequest() throws Exception {
        TestRequest.Get request = new TestRequest.Get();
        assertEquals(request.getMethod(), Method.GET);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        verify(mMockConnection).setRequestMethod("GET");
        verify(mMockConnection, never()).setDoOutput(true);
    }

}
