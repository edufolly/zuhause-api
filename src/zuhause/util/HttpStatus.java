package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public interface HttpStatus {

    /* 1xx */
    public static final String SC_CONTINUE = "100 Continue";
    public static final String SC_SWITCHING_PROTOCOLS = "101 Switching Protocols";

    /* 2xx */
    public static final String SC_OK = "200 OK";
    public static final String SC_CREATED = "201 Created";
    public static final String SC_ACCEPTED = "202 Accepted";
    public static final String SC_NON_AUTHORITATIVE_INFORMATION = "203 Non-Authoritative Information";
    public static final String SC_NO_CONTENT = "204 No Content";
    public static final String SC_RESET_CONTENT = "205 Reset Content";
    public static final String SC_PARTIAL_CONTENT = "206 Partial Content";

    /* 3xx */
    public static final String SC_MULTIPLE_CHOICES = "300 Multiple Choices";
    public static final String SC_MOVED_PERMANENTLY = "301 Moved Permanently";
    public static final String SC_FOUND = "302 Found";
    public static final String SC_SEE_OTHER = "303 See Other";
    public static final String SC_NOT_MODIFIED = "304 Not Modified";
    public static final String SC_USE_PROXY = "305 Use Proxy";
    public static final String SC_TEMPORALY_REDIRECT = "307 Temporary Redirect";

    /* 4xx */
    public static final String SC_BAD_REQUEST = "400 Bad Request";
    public static final String SC_UNAUTHORIZED = "401 Unauthorized";
    public static final String SC_PAYMENT_REQUIRED = "402 Payment Required";
    public static final String SC_FORBIDDEN = "403 Forbidden";
    public static final String SC_NOT_FOUND = "404 Not Found";
    public static final String SC_METHOD_NOT_ALLOWED = "405 Method Not Allowed";
    public static final String SC_NOT_ACCEPTABLE = "406 Not Acceptable";
    public static final String SC_PROXY_AUTHENTICATION_REQUIRED = "407 Proxy Authentication Required";
    public static final String SC_REQUEST_TIME_OUT = "408 Request Time-out";
    public static final String SC_CONFLICT = "409 Conflict";
    public static final String SC_GONE = "410 Gone";
    public static final String SC_LENGTH_REQUIRED = "411 Length Required";
    public static final String SC_PRECONDITION_FAILED = "412 Precondition Failed";
    public static final String SC_REQUEST_ENTITY_TOO_LARGE = "413 Request Entity Too Large";
    public static final String SC_REQUEST_URI_TOO_LARGE = "414 Request-URI Too Large";
    public static final String SC_UNSUPPORTED_MEDIA_TYPE = "415 Unsupported Media Type";
    public static final String SC_REQUESTED_RANGE_NOT_SATISFIABLE = "416 Requested range not satisfiable";
    public static final String SC_EXPECTATION_FAILED = "417 Expectation Failed";

    /* 5xx */
    public static final String SC_INTERNAL_SERVER_ERROR = "500 Internal Server Error";
    public static final String SC_NOT_IMPLEMENTED = "501 Not Implemented";
    public static final String SC_BAD_GATEWAY = "502 Bad Gateway";
    public static final String SC_SERVICE_UNAVAILABLE = "503 Service Unavailable";
    public static final String SC_GATEWAY_TIME_OUT = "504 Gateway Time-out";
    public static final String SC_HTTP_VERSION_NOT_SUPPORTED = "505 HTTP Version not supported";

}
