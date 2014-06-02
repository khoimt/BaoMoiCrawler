package newscrawler.fetcher;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;

public class CustomFetchStatus {

    public static final int PageTooBig = 1001;
    public static final int FatalTransportError = 1005;
    public static final int UnknownError = 1006;

    protected static Map<Integer, String> statDesc;

    static {
        statDesc = new HashMap<>();
        statDesc.put(HttpStatus.SC_OK, "OK");
        statDesc.put(HttpStatus.SC_CREATED, "Created");
        statDesc.put(new Integer(HttpStatus.SC_ACCEPTED), "Accepted");
        statDesc.put(new Integer(HttpStatus.SC_NO_CONTENT), "No Content");
        statDesc.put(new Integer(HttpStatus.SC_MOVED_PERMANENTLY), "Moved Permanently");
        statDesc.put(new Integer(HttpStatus.SC_MOVED_TEMPORARILY), "Moved Temporarily");
        statDesc.put(new Integer(HttpStatus.SC_NOT_MODIFIED), "Not Modified");
        statDesc.put(new Integer(HttpStatus.SC_BAD_REQUEST), "Bad Request");
        statDesc.put(new Integer(HttpStatus.SC_UNAUTHORIZED), "Unauthorized");
        statDesc.put(new Integer(HttpStatus.SC_FORBIDDEN), "Forbidden");
        statDesc.put(new Integer(HttpStatus.SC_INTERNAL_SERVER_ERROR), "Internal Server Error");
        statDesc.put(new Integer(HttpStatus.SC_NOT_IMPLEMENTED), "Not Implemented");
        statDesc.put(new Integer(HttpStatus.SC_BAD_GATEWAY), "Bad Gateway");
        statDesc.put(new Integer(HttpStatus.SC_SERVICE_UNAVAILABLE), "Service Unavailable");
        statDesc.put(new Integer(HttpStatus.SC_CONTINUE), "Continue");
        statDesc.put(new Integer(HttpStatus.SC_TEMPORARY_REDIRECT), "Temporary Redirect");
        statDesc.put(new Integer(HttpStatus.SC_METHOD_NOT_ALLOWED), "Method Not Allowed");
        statDesc.put(new Integer(HttpStatus.SC_CONFLICT), "Conflict");
        statDesc.put(new Integer(HttpStatus.SC_PRECONDITION_FAILED), "Precondition Failed");
        statDesc.put(new Integer(HttpStatus.SC_REQUEST_TOO_LONG), "Request Too Long");
        statDesc.put(new Integer(HttpStatus.SC_REQUEST_URI_TOO_LONG), "Request-URI Too Long");
        statDesc.put(new Integer(HttpStatus.SC_MULTIPLE_CHOICES), "Multiple Choices");
        statDesc.put(new Integer(HttpStatus.SC_SEE_OTHER), "See Other");
        statDesc.put(new Integer(HttpStatus.SC_USE_PROXY), "Use Proxy");
        statDesc.put(new Integer(HttpStatus.SC_PAYMENT_REQUIRED), "Payment Required");
        statDesc.put(new Integer(HttpStatus.SC_NOT_ACCEPTABLE), "Not Acceptable");
        statDesc.put(new Integer(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED), "Proxy Authentication Required");
        statDesc.put(new Integer(HttpStatus.SC_REQUEST_TIMEOUT), "Request Timeout");
        statDesc.put(new Integer(PageTooBig), "Page size was too big");
        statDesc.put(new Integer(FatalTransportError), "Fatal transport error");
        statDesc.put(new Integer(UnknownError), "Unknown error");
    }

    ;

    public static String getStatusDescription(int code) {
        Integer key = code;
        if (statDesc.containsKey(key)) {
            return (String) statDesc.get(key);
        }
        return "(" + code + ")";
    }

}
