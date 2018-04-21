package zuhause.sunrise;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author Eduardo Folly
 */
public class SunriseResult implements Serializable {

    @SerializedName("sunrise")
    private String sunrise;

    @SerializedName("sunset")
    private String sunset;

    @SerializedName("solar_noon")
    private String solarNoon;

    @SerializedName("day_length")
    private int dayLength;

    @SerializedName("civil_twilight_begin")
    private String civilTwilightBegin;

    @SerializedName("civil_twilight_end")
    private String civilTwilightEnd;

    @SerializedName("nautical_twilight_begin")
    private String nauticalTwilightBegin;

    @SerializedName("nautical_twilight_end")
    private String nauticalTwilightEnd;

    @SerializedName("astronomical_twilight_begin")
    private String astronomicalTwilightBegin;

    @SerializedName("astronomical_twilight_end")
    private String astronomicalTwilightEnd;

    /**
     *
     * @return
     */
    public String getSunrise() {
        return sunrise;
    }

    /**
     *
     * @param sunrise
     */
    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    /**
     *
     * @return
     */
    public String getSunset() {
        return sunset;
    }

    /**
     *
     * @param sunset
     */
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    /**
     *
     * @return
     */
    public String getSolarNoon() {
        return solarNoon;
    }

    /**
     *
     * @param solarNoon
     */
    public void setSolarNoon(String solarNoon) {
        this.solarNoon = solarNoon;
    }

    /**
     *
     * @return
     */
    public int getDayLength() {
        return dayLength;
    }

    /**
     *
     * @param dayLength
     */
    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
    }

    /**
     *
     * @return
     */
    public String getCivilTwilightBegin() {
        return civilTwilightBegin;
    }

    /**
     *
     * @param civilTwilightBegin
     */
    public void setCivilTwilightBegin(String civilTwilightBegin) {
        this.civilTwilightBegin = civilTwilightBegin;
    }

    /**
     *
     * @return
     */
    public String getCivilTwilightEnd() {
        return civilTwilightEnd;
    }

    /**
     *
     * @param civilTwilightEnd
     */
    public void setCivilTwilightEnd(String civilTwilightEnd) {
        this.civilTwilightEnd = civilTwilightEnd;
    }

    /**
     *
     * @return
     */
    public String getNauticalTwilightBegin() {
        return nauticalTwilightBegin;
    }

    /**
     *
     * @param nauticalTwilightBegin
     */
    public void setNauticalTwilightBegin(String nauticalTwilightBegin) {
        this.nauticalTwilightBegin = nauticalTwilightBegin;
    }

    /**
     *
     * @return
     */
    public String getNauticalTwilightEnd() {
        return nauticalTwilightEnd;
    }

    /**
     *
     * @param nauticalTwilightEnd
     */
    public void setNauticalTwilightEnd(String nauticalTwilightEnd) {
        this.nauticalTwilightEnd = nauticalTwilightEnd;
    }

    /**
     *
     * @return
     */
    public String getAstronomicalTwilightBegin() {
        return astronomicalTwilightBegin;
    }

    /**
     *
     * @param astronomicalTwilightBegin
     */
    public void setAstronomicalTwilightBegin(String astronomicalTwilightBegin) {
        this.astronomicalTwilightBegin = astronomicalTwilightBegin;
    }

    /**
     *
     * @return
     */
    public String getAstronomicalTwilightEnd() {
        return astronomicalTwilightEnd;
    }

    /**
     *
     * @param astronomicalTwilightEnd
     */
    public void setAstronomicalTwilightEnd(String astronomicalTwilightEnd) {
        this.astronomicalTwilightEnd = astronomicalTwilightEnd;
    }

}
