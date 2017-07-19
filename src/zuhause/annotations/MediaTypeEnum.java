package zuhause.annotations;

import com.google.common.net.MediaType;

/**
 *
 * @author Eduardo Folly
 */
public enum MediaTypeEnum {
    JSON(MediaType.JSON_UTF_8),
    TEXT(MediaType.PLAIN_TEXT_UTF_8),
    HTML(MediaType.HTML_UTF_8);

    public MediaType mediaType;

    MediaTypeEnum(MediaType mt) {
        mediaType = mt;
    }
}
