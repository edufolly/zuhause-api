package zuhause.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eduardo Folly
 */
public class TerminalHelper {

    // TODO - Existe um bug quando o comando retorna somente 2 linhas.
    
    /**
     *
     * @param command
     * @return
     * @throws IOException
     */
    public static String rawExecute(String command) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command);
        StringBuilder sb = new StringBuilder();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = input.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<Map<String, String>> execute(String command)
            throws IOException, InterruptedException {

        return execute(command, 0);
    }

    /**
     *
     * @param command
     * @param startLine
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<Map<String, String>> execute(String command, int startLine)
            throws IOException, InterruptedException {

        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command);
        List<String> lines = new ArrayList();

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        int l = 0;

        while ((line = input.readLine()) != null) {
            if (l >= startLine) {
                lines.add(line);
            }
            l++;
        }

        String[] lns = new String[lines.size()];
        lns = lines.toArray(lns);

        List<Triple> headers = getTriples(lns);

        List<Map<String, String>> objects = getObjects(headers, lns, 1);

        return objects;
    }

    /**
     *
     * @param lines
     * @return
     */
    private static List<Triple> getTriples(String[] lines) {
        List<Triple> header = new ArrayList();

        if (lines.length > 2) {

            String line = lines[0];
            int beginIndex = 0;

            for (int i = 0; i < line.length(); i++) {

                if (line.charAt(i) == ' ') {
                    boolean spaceOnly = true;

                    for (int j = 1; j < lines.length - 1; j++) {
                        if (lines[j].length() >= i && lines[j].charAt(i) != ' ') {
                            spaceOnly = false;
                            break;
                        }
                    }

                    if (spaceOnly) {
                        String key = line.substring(beginIndex, i).trim();
                        if (!key.isEmpty()) {
                            header.add(new Triple(key, beginIndex, i));
                        }
                        beginIndex = i;
                    }
                }
            }

            String key = line.substring(beginIndex).trim();
            if (!key.isEmpty()) {
                header.add(new Triple(key, beginIndex, -1));
            }
        }

        return header;
    }

    /**
     *
     * @param header
     * @param lines
     * @param startLine
     * @return
     */
    private static List<Map<String, String>> getObjects(List<Triple> header, String[] lines, int startLine) {
        List<Map<String, String>> objects = new ArrayList();

        for (int i = startLine; i < lines.length - 1; i++) {
            String l = lines[i];
            Map<String, String> object = new HashMap();
            for (Triple triple : header) {
                String value = l.substring(triple.begin, triple.end == -1 || triple.end > l.length() ? l.length() : triple.end).trim();
                object.put(triple.key, value);
            }
            objects.add(object);
        }
        return objects;
    }

    /**
     *
     * @param list1
     * @param key1
     * @param list2
     * @param key2
     * @return
     */
    private static List<Map<String, String>> mergeMap(List<Map<String, String>> list1, String key1, List<Map<String, String>> list2, String key2) {

        for (Map object1 : list1) {

            if (object1.containsKey(key1)) {

                Object value1 = object1.get(key1);

                for (Map object2 : list2) {

                    if (object2.containsKey(key2)) {

                        Object value2 = object2.get(key2);

                        if (value1.equals(value2)) {
                            object1.putAll(object2);

                            object1.remove(key2);

                            break;
                        }
                    }
                }
            }
        }

        return list1;
    }

}
