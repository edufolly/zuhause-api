package zuhause;

import zuhause.annotations.*;
import zuhause.util.Config;
import zuhause.util.Request;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Eduardo Folly
 */
public class EndpointCache {

    private static EndpointCache INSTANCE;

    private final Map<Pattern, Invokable> cache = new HashMap<>();

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void init() throws IOException, ClassNotFoundException {
        INSTANCE = new EndpointCache();
    }

    /**
     *
     * @param request
     * @return
     */
    public static Invokable find(Request request) {
        if (INSTANCE != null) {
            for (Pattern pattern : INSTANCE.cache.keySet()) {
                if (pattern.matcher(request.getHttpMethod() + " "
                        + request.getPath()).matches()) {

                    return INSTANCE.cache.get(pattern);
                }
            }
        }

        return null;
    }

    /**
     *
     * @param find
     * @return
     */
    public static Invokable find(String find) {
        if (INSTANCE != null) {
            for (Pattern pattern : INSTANCE.cache.keySet()) {
                if (pattern.matcher(find).matches()) {
                    return INSTANCE.cache.get(pattern);
                }
            }
        }

        return null;
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private EndpointCache() throws IOException, ClassNotFoundException {

        Config config = Config.getInstance();

        String packageName = config.getAppPackage();

        final ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();

        ClassPath classpath = ClassPath.from(loader);

        for (ClassPath.ClassInfo classInfo : classpath
                .getTopLevelClasses(packageName)) {

            Class clazz = classInfo.load();

            String classEndpoint = "";

            if (clazz.isAnnotationPresent(WS.class)) {
                WS annotation = (WS) clazz.getAnnotation(WS.class);
                classEndpoint = annotation.value();

                if (!classEndpoint.startsWith("/")) {
                    classEndpoint = "/" + classEndpoint;
                }

                if (classEndpoint.endsWith("/")) {
                    classEndpoint = classEndpoint
                            .substring(0, classEndpoint.length() - 1);
                }

                String patternEndpoint = classEndpoint
                        .replaceAll(":([^/]+?)/", "([^/]+?)/");

                String prefix = "WS";

                Pattern compile = Pattern
                        .compile(prefix + " " + patternEndpoint + "$");

                cache.put(compile,
                        new Invokable(clazz, null, patternEndpoint, compile));
            }

            if (clazz.isAnnotationPresent(Path.class)) {
                Path annotation = (Path) clazz.getAnnotation(Path.class);
                classEndpoint = annotation.value();

                if (!classEndpoint.startsWith("/")) {
                    classEndpoint = "/" + classEndpoint;
                }

                if (classEndpoint.endsWith("/")) {
                    classEndpoint = classEndpoint
                            .substring(0, classEndpoint.length() - 1);
                }
            }

            for (Method method : clazz.getMethods()) {

                String finalEndpoint = classEndpoint;

                if (method.isAnnotationPresent(Path.class)) {

                    String methodEndpoint = method
                            .getAnnotation(Path.class).value();

                    if (!methodEndpoint.startsWith("/")) {
                        methodEndpoint = "/" + methodEndpoint;
                    }

                    finalEndpoint += methodEndpoint;
                }

                if (!finalEndpoint.isEmpty()) {

                    if (!finalEndpoint.endsWith("/")) {
                        finalEndpoint += "/";
                    }

                    // Busca parâmetros
                    String patternEndpoint = finalEndpoint
                            .replaceAll(":([^/]+?)/", "([^/]+?)/");

                    // Método
                    String prefix = null;

                    if (method.isAnnotationPresent(GET.class)) {
                        prefix = "GET";
                    } else if (method.isAnnotationPresent(POST.class)) {
                        prefix = "POST";
                    } else if (method.isAnnotationPresent(PUT.class)) {
                        prefix = "PUT";
                    } else if (method.isAnnotationPresent(DELETE.class)) {
                        prefix = "DELETE";
                    } else if (method.isAnnotationPresent(PATCH.class)) {
                        prefix = "PATCH";
                    } else if (method.isAnnotationPresent(COPY.class)) {
                        prefix = "COPY";
                    } else if (method.isAnnotationPresent(HEAD.class)) {
                        prefix = "HEAD";
                    }

                    if (prefix != null) {
                        Pattern compile = Pattern
                                .compile(prefix + " " + patternEndpoint + "$");

                        cache.put(compile, new Invokable(clazz, method,
                                finalEndpoint, compile));
                    }
                }
            }
        }
    }
}
