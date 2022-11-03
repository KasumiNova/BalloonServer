package github.kasuminova.balloonserver.configurations;

import com.alibaba.fastjson2.annotation.JSONField;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Kasumi_Nova
 */
public class IntegratedServerConfig extends Configuration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_IP = "127.0.0.1";
    public static final String DEFAULT_MAIN_DIR_PATH = "/res";
    public static final boolean DEFAULT_FILE_CHANGE_LISTENER = true;
    public static final boolean DEFAULT_COMPATIBLE_MODE = false;

    @JSONField(ordinal = 1)
    private String ip = DEFAULT_IP;
    @JSONField(ordinal = 2)
    private int port = DEFAULT_PORT;
    @JSONField(ordinal = 3)
    private String mainDirPath = DEFAULT_MAIN_DIR_PATH;
    @JSONField(ordinal = 4)
    private boolean fileChangeListener = DEFAULT_FILE_CHANGE_LISTENER;
    @JSONField(ordinal = 5)
    private boolean compatibleMode = DEFAULT_COMPATIBLE_MODE;
    @JSONField(ordinal = 6)
    private String jksFilePath = "";
    @JSONField(ordinal = 7)
    private String jksSslPassword = "";
    @JSONField(ordinal = 8)
    private String[] commonMode = new String[0];
    @JSONField(ordinal = 9)
    private String[] onceMode = new String[0];

    public IntegratedServerConfig() {
        configVersion = 1;
    }

    /**
     * 重置配置文件
     */
    public IntegratedServerConfig reset() {
        configVersion = 1;
        ip = "127.0.0.1";
        port = DEFAULT_PORT;
        mainDirPath = "/res";
        fileChangeListener = true;
        jksFilePath = "";
        jksSslPassword = "";
        commonMode = new String[0];
        onceMode = new String[0];
        return this;
    }

    @Override
    public IntegratedServerConfig setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public IntegratedServerConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public IntegratedServerConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getJksFilePath() {
        return jksFilePath;
    }

    public IntegratedServerConfig setJksFilePath(String jksFilePath) {
        this.jksFilePath = jksFilePath;
        return this;
    }

    public String getJksSslPassword() {
        return jksSslPassword;
    }

    public IntegratedServerConfig setJksSslPassword(String jksSslPassword) {
        this.jksSslPassword = jksSslPassword;
        return this;
    }

    public String[] getCommonMode() {
        return commonMode;
    }

    public IntegratedServerConfig setCommonMode(String[] commonMode) {
        this.commonMode = commonMode;
        return this;
    }

    public String[] getOnceMode() {
        return onceMode;
    }

    public IntegratedServerConfig setOnceMode(String[] onceMode) {
        this.onceMode = onceMode;
        return this;
    }

    public String getMainDirPath() {
        return mainDirPath;
    }

    public IntegratedServerConfig setMainDirPath(String mainDirPath) {
        this.mainDirPath = mainDirPath;
        return this;
    }

    public boolean isFileChangeListener() {
        return fileChangeListener;
    }

    public IntegratedServerConfig setFileChangeListener(boolean fileChangeListener) {
        this.fileChangeListener = fileChangeListener;
        return this;
    }

    public boolean isCompatibleMode() {
        return compatibleMode;
    }

    public IntegratedServerConfig setCompatibleMode(boolean compatibleMode) {
        this.compatibleMode = compatibleMode;
        return this;
    }
}
