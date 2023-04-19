package com.yhy.all.of.tv.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Fir 版本信息
 * <p>
 * Created on 2023-01-26 01:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class FirVersionInfo implements Serializable {
    /**
     * name
     */
    @SerializedName("name")
    public String name;
    /**
     * version
     */
    @SerializedName("version")
    public Integer version;
    /**
     * changelog
     */
    @SerializedName("changelog")
    public String changeLog;
    /**
     * updatedAt
     */
    @SerializedName("updated_at")
    public int updatedAt;
    /**
     * versionShort
     */
    @SerializedName("versionShort")
    public String versionShort;
    /**
     * build
     */
    @SerializedName("build")
    public String build;
    /**
     * installUrl
     */
    @SerializedName("install_url")
    public String installUrl;
    /**
     * directInstallUrl
     */
    @SerializedName("direct_install_url")
    public String directInstallUrl;
    /**
     * updateUrl
     */
    @SerializedName("update_url")
    public String updateUrl;
    /**
     * binary
     */
    @SerializedName("binary")
    public BinaryDTO binary;

    public static class BinaryDTO {
        /**
         * fsize
         */
        @SerializedName("fsize")
        public int fSize;
    }
}
