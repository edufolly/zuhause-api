package zuhause;

import zuhause.util.InvokableException;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import zuhause.annotations.BodyParam;
import zuhause.annotations.PathParam;
import zuhause.annotations.QueryStringParam;
import zuhause.util.Request;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zuhause.annotations.ReturnType;
import zuhause.util.Config;
import zuhause.util.HttpStatus;
import zuhause.util.Response;

/**
 *
 * @author Eduardo Folly
 */
public class Invokable {

    private static final transient Pattern PATTERN_PARAM
            = Pattern.compile(":([^/]+?)/");
    //--
    private final Class clazz;
    private final Method method;
    private final Pattern pattern;
    private String[] params;

    /**
     *
     * @param clazz
     * @param method
     * @param endpoint
     * @param pattern
     */
    public Invokable(Class clazz, Method method, String endpoint,
            Pattern pattern) {

        this.clazz = clazz;
        this.method = method;
        this.pattern = pattern;

        Matcher matcher = PATTERN_PARAM.matcher(endpoint);
        List<String> p = new ArrayList();
        while (matcher.find()) {
            p.add(matcher.group(1));
        }

        params = new String[p.size()];

        params = p.toArray(params);
    }

    /**
     *
     * @return
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     *
     * @return
     */
    public Method getMethod() {
        return method;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * @throws InvokableException
     */
    public Response invoke(Request request, Response response)
            throws InvokableException {

        try {
            Map<String, String> map = new HashMap();

            Matcher matcher = pattern.matcher(request.getHttpMethod() + " "
                    + request.getPath());

            if (matcher.find()) {
                for (int i = 0; i < params.length; i++) {
                    map.put(params[i], matcher.group(i + 1));
                }
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            Object objects[] = new Object[parameterTypes.length];

            int i = 0;

            MediaType mediaType = null;

            String body;

            for (Annotation annotation : method.getAnnotations()) {
                Class<? extends Annotation> annotationType
                        = annotation.annotationType();

                if (annotationType.equals(PathParam.class)) {
                    for (String key : ((PathParam) annotation).value()) {
                        if (map.containsKey(key)) {
                            objects[i] = cast(parameterTypes[i], map.get(key));
                        } else {
                            objects[i] = null;
                        }
                        i++;
                    }
                } else if (annotationType.equals(QueryStringParam.class)) {
                    for (String key : ((QueryStringParam) annotation).value()) {
                        if (request.containsQueryString(key)) {
                            objects[i] = cast(parameterTypes[i],
                                    request.getQueryString(key));
                        } else {
                            objects[i] = null;
                        }
                        i++;
                    }
                } else if (annotationType.equals(BodyParam.class)) {
                    objects[i] = Config.getGson().fromJson(String.valueOf(
                            request.getBody()).trim(), parameterTypes[i]);
                    i++;
                } else if (annotationType.equals(ReturnType.class)) {
                    mediaType = ((ReturnType) annotation).value().mediaType;
                }
            }

            Object invoked = method.invoke(clazz.newInstance(), objects);

            if (mediaType == null) {
                mediaType = MediaType.JSON_UTF_8;
            }

            if (mediaType == MediaType.JSON_UTF_8) {
                response.addHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.JSON_UTF_8.toString());

                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "*");

                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                        "origin, content-type, accept, authorization");

                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                        "true");

                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");

                body = Config.getGson().toJson(invoked);
            } else {
                response.addHeader(HttpHeaders.CONTENT_TYPE,
                        mediaType.toString());

                body = invoked.toString();
            }

            if (Strings.isNullOrEmpty(body)) {
                response.clearHeaders();
                response.setHttpStatus(HttpStatus.SC_NO_CONTENT);
            } else {
                response.setHttpStatus(HttpStatus.SC_OK);
                response.setBody(body);
            }
        } catch (InvocationTargetException e) {
            throw new InvokableException(e.getTargetException()
                    .getMessage(), e);
        } catch (Exception ex) {
            throw new InvokableException(ex.getMessage(), ex);
        }

        return response;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return clazz.getName() + " - " + method.getName();
    }

    /**
     *
     * @param type
     * @param value
     * @return
     */
    private Object cast(Class type, String value)
            throws UnsupportedEncodingException {

        String decoded = URLDecoder.decode(value, "UTF-8");

        if (type.equals(Integer.class)) {
            return Integer.parseInt(decoded);
        } else if (type.equals(int.class)) {
            return Integer.parseInt(decoded);
        } else if (type.equals(Long.class)) {
            return Long.parseLong(decoded);
        } else if (type.equals(long.class)) {
            return Long.parseLong(decoded);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(decoded);
        } else if (type.equals(Float.class)) {
            return Float.parseFloat(decoded);
        } else if (type.equals(boolean.class)) {
            return Boolean.parseBoolean(decoded);
        } else if (type.equals(Boolean.class)) {
            return Boolean.parseBoolean(decoded);
        } else if (type.equals(String.class)) {
            return decoded;
        } else {
            return type.cast(decoded);
        }
    }
}
