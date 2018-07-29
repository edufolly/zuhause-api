package zuhause.serial;

import java.io.Serializable;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Eduardo Folly
 */
public class Serial implements Serializable {

    private static final Logger LOGGER = LogManager.getRootLogger();

    private String dev;
    private int baundrate;
    private int databits;
    private int stopbits;
    private int parity;
    private long timeout;
    private long sleep;

    private transient SerialPort serialPort = null;
    private transient StringBuilder sb = new StringBuilder();
    private transient boolean running = false;

    /**
     *
     * @return
     */
    public String getDev() {
        return dev;
    }

    /**
     *
     * @param dev
     */
    public void setDev(String dev) {
        this.dev = dev;
    }

    /**
     *
     * @return
     */
    public int getBaundrate() {
        return baundrate;
    }

    /**
     *
     * @param baundrate
     */
    public void setBaundrate(int baundrate) {
        this.baundrate = baundrate;
    }

    /**
     *
     * @return
     */
    public int getDatabits() {
        return databits;
    }

    /**
     *
     * @param databits
     */
    public void setDatabits(int databits) {
        this.databits = databits;
    }

    /**
     *
     * @return
     */
    public int getStopbits() {
        return stopbits;
    }

    /**
     *
     * @param stopbits
     */
    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    /**
     *
     * @return
     */
    public int getParity() {
        return parity;
    }

    /**
     *
     * @param parity
     */
    public void setParity(int parity) {
        this.parity = parity;
    }

    /**
     *
     */
    private void clear() {
        sb = new StringBuilder();
    }

    /**
     *
     * @param message
     * @throws SerialPortException
     */
    public void safeWrite(String message) throws SerialPortException {
        open();
        long start = System.currentTimeMillis();
        while (running) {
            if (System.currentTimeMillis() - start >= timeout) {
                throw new SerialPortException(serialPort
                        .getPortName(), "safeWrite", "Timeout");
            }
            try {
                Thread.sleep(sleep);
            } catch (Exception ex) {
                LOGGER.error("Break sleep", ex);
                throw new SerialPortException(serialPort
                        .getPortName(), "safeWrite", "Break sleep");
            }
        }
        running = true;
        clear();
        serialPort.writeString(message);
    }

    /**
     *
     * @param wait
     * @return
     * @throws SerialPortException
     */
    public String waitFor(String wait) throws SerialPortException {
        long start = System.currentTimeMillis();
        while (!sb.toString().contains(wait)) {
            if (System.currentTimeMillis() - start >= timeout) {
                System.out.println(sb.toString());
                clear();
                running = false;
                throw new SerialPortException(serialPort
                        .getPortName(), "waitFor", "Timeout");
            }
            try {
                Thread.sleep(sleep);
            } catch (Exception ex) {
                LOGGER.error("Break sleep", ex);
                throw new SerialPortException(serialPort
                        .getPortName(), "waitFor", "Break sleep");
            }
        }
        running = false;
        return sb.toString();
    }

    /**
     *
     * @param time
     */
    private void block(final long time) {
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (Exception ex) {
                    LOGGER.warn("Break start", ex);
                }
                running = false;
            }
        }).start();
    }

    /**
     *
     */
    public void open() {
        try {
            if (serialPort == null) {
                serialPort = new SerialPort(dev);

                serialPort.openPort();

                serialPort.setParams(baundrate, databits, stopbits, parity);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
                        | SerialPort.FLOWCONTROL_RTSCTS_OUT);

                serialPort.addEventListener(new PortReader(),
                        SerialPort.MASK_RXCHAR);

                block(Math.min(sleep * sleep, timeout - 500l));
            }
        } catch (Exception ex) {
            LOGGER.error("Trying to open " + dev, ex);
        }
    }

    /**
     *
     */
    public void close() {
        try {
            if (serialPort != null) {
                serialPort.closePort();
            }
        } catch (Exception ex) {
            LOGGER.warn("Erro ao fechar a porta " + dev + ".", ex);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SerialConfig{" + "dev=" + dev + ", baundrate=" + baundrate
                + ", databits=" + databits + ", stopbits=" + stopbits
                + ", parity=" + parity + '}';
    }

    /**
     *
     */
    private class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort
                            .readString(event.getEventValue());
                    sb.append(receivedData);
                } catch (SerialPortException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

}
