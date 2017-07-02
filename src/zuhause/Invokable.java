package zuhause;

import zuhause.annotations.BodyParam;
import zuhause.annotations.PathParam;
import zuhause.annotations.QueryStringParam;
import zuhause.util.Request;
import com.google.gson.Gson;
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

/**
 *
 * @author edufolly
 */
public class Invokable {

    private static final transient Pattern patternParam = Pattern.compile(":([^/]+?)/");
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
    public Invokable(Class clazz, Method method, String endpoint, Pattern pattern) {
        this.clazz = clazz;
        this.method = method;
        this.pattern = pattern;

        Matcher matcher = patternParam.matcher(endpoint);
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
     * @return
     * @throws InvokableException
     */
    public String invoke(Request request) throws InvokableException {
        try {
            Gson gson = new Gson();

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

            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(PathParam.class)) {
                    for (String key : ((PathParam) annotation).value()) {
                        if (map.containsKey(key)) {
                            objects[i] = cast(parameterTypes[i], map.get(key));
                        } else {
                            objects[i] = null;
                        }
                        i++;
                    }
                } else if (annotation.annotationType().equals(QueryStringParam.class)) {
                    for (String key : ((QueryStringParam) annotation).value()) {
                        if (request.containsQueryString(key)) {
                            objects[i] = cast(parameterTypes[i], request.getQueryString(key));
                        } else {
                            objects[i] = null;
                        }
                        i++;
                    }
                } else if (annotation.annotationType().equals(BodyParam.class)) {
                    objects[i] = gson.fromJson(String.valueOf(request.getBody()).trim(), parameterTypes[i]);
                    i++;
                }
            }

            Object invoked = method.invoke(clazz.newInstance(), objects);

            return gson.toJson(invoked);
        } catch (InvocationTargetException e) {
            throw new InvokableException(e.getTargetException().getMessage(), e);
        } catch (Exception ex) {
            throw new InvokableException(ex.getMessage(), ex);
        }

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
    private Object cast(Class type, String value) throws UnsupportedEncodingException {

        value = URLDecoder.decode(value, "UTF-8");

        if (type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(Float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(String.class)) {
            return value;
        } else {
            return type.cast(value);
        }
    }

}
