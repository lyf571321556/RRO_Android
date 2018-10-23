package ai.ones.network.response;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by heqingbao on 2016/8/1.
 */
public final class NetworkResponse {

    private final int mStatusCode;
    private final InputStream mInputStream;
    private final Map<String, String> mHeaders;

    public NetworkResponse(int statusCode, InputStream inputStream, Map<String, String> headers) {
        mStatusCode = statusCode;
        mInputStream = inputStream;
        mHeaders = headers;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public byte[] getData() throws IOException {
        try {
            return IOUtils.toByteArray(mInputStream);
        } finally {
            IOUtils.closeQuietly(mInputStream);
        }
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }
}
