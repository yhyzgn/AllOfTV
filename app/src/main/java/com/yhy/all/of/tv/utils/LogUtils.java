package com.yhy.all.of.tv.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.IntRange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2017-12-23 14:15
 * version: 1.0.0
 * desc   : 日志工具
 */
public abstract class LogUtils {

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;

    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};

    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML = 0x30;

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final String TOP_CORNER = "┌";
    private static final String MIDDLE_CORNER = "├";
    private static final String LEFT_BORDER = "│ ";
    private static final String BOTTOM_CORNER = "└";
    private static final String SIDE_DIVIDER =
            "────────────────────────────────────────────────────────";
    private static final String MIDDLE_DIVIDER =
            "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + MIDDLE_DIVIDER + MIDDLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_CORNER + SIDE_DIVIDER + SIDE_DIVIDER;
    private static final int MAX_LEN = 3000;
    @SuppressLint("SimpleDateFormat")
    private static final Format FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");
    private static final String NOTHING = "log nothing";
    private static final String NULL = "null";
    private static final String ARGS = "args";
    private static final String PLACEHOLDER = " ";
    private static final Config CONFIG = new Config();
    private static ExecutorService sExecutor;

    /**
     * 获取当前配置
     *
     * @return 当前配置
     */
    public static Config getConfig() {
        return CONFIG;
    }

    /**
     * 打印Verbose
     *
     * @param contents 内容
     */
    public static void v(final Object... contents) {
        log(V, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Verbose
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void vTag(final String tag, final Object... contents) {
        log(V, tag, contents);
    }

    /**
     * 打印Debug
     *
     * @param contents 内容
     */
    public static void d(final Object... contents) {
        log(D, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Debug
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void dTag(final String tag, final Object... contents) {
        log(D, tag, contents);
    }

    /**
     * 打印Info
     *
     * @param contents 内容
     */
    public static void i(final Object... contents) {
        log(I, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Info
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void iTag(final String tag, final Object... contents) {
        log(I, tag, contents);
    }

    /**
     * 打印Warn
     *
     * @param contents 内容
     */
    public static void w(final Object... contents) {
        log(W, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Warn
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void wTag(final String tag, final Object... contents) {
        log(W, tag, contents);
    }

    /**
     * 打印Error
     *
     * @param contents 内容
     */
    public static void e(final Object... contents) {
        log(E, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Error
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void eTag(final String tag, final Object... contents) {
        log(E, tag, contents);
    }

    /**
     * 打印Assert
     *
     * @param contents 内容
     */
    public static void a(final Object... contents) {
        log(A, CONFIG.mGlobalTag, contents);
    }

    /**
     * 打印Assert
     *
     * @param tag      标签
     * @param contents 内容
     */
    public static void aTag(final String tag, final Object... contents) {
        log(A, tag, contents);
    }

    /**
     * 打印到文件
     *
     * @param content 内容
     */
    public static void file(final Object content) {
        log(FILE | D, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印到文件
     *
     * @param type    类型
     * @param content 内容
     */
    public static void file(final int type, final Object content) {
        log(FILE | type, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印到文件
     *
     * @param tag     标签
     * @param content 内容
     */
    public static void file(final String tag, final Object content) {
        log(FILE | D, tag, content);
    }

    /**
     * 打印到文件
     *
     * @param type    类型
     * @param tag     标签
     * @param content 内容
     */
    public static void file(final int type, final String tag, final Object content) {
        log(FILE | type, tag, content);
    }

    /**
     * 打印json
     *
     * @param content 内容
     */
    public static void json(final String content) {
        log(JSON | D, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印json
     *
     * @param type    类型
     * @param content 内容
     */
    public static void json(final int type, final String content) {
        log(JSON | type, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印json
     *
     * @param tag     标签
     * @param content 内容
     */
    public static void json(final String tag, final String content) {
        log(JSON | D, tag, content);
    }

    /**
     * 打印json
     *
     * @param type    类型
     * @param tag     标签
     * @param content 内容
     */
    public static void json(final int type, final String tag, final String content) {
        log(JSON | type, tag, content);
    }

    /**
     * 打印xml
     *
     * @param content 内容
     */
    public static void xml(final String content) {
        log(XML | D, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印xml
     *
     * @param type    类型
     * @param content 内容
     */
    public static void xml(final int type, final String content) {
        log(XML | type, CONFIG.mGlobalTag, content);
    }

    /**
     * 打印xml
     *
     * @param tag     标签
     * @param content 内容
     */
    public static void xml(final String tag, final String content) {
        log(XML | D, tag, content);
    }

    /**
     * 打印xml
     *
     * @param type    类型
     * @param tag     标签
     * @param content 内容
     */
    public static void xml(final int type, final String tag, final String content) {
        log(XML | type, tag, content);
    }

    /**
     * 打印日志
     *
     * @param type     类型
     * @param tag      标签
     * @param contents 内容
     */
    public static void log(final int type, final String tag, final Object... contents) {
        if (!CONFIG.mLogSwitch || (!CONFIG.mLog2ConsoleSwitch && !CONFIG.mLog2FileSwitch)) return;
        int type_low = type & 0x0f, type_high = type & 0xf0;
        if (type_low < CONFIG.mConsoleFilter && type_low < CONFIG.mFileFilter) return;
        final TagHead tagHead = processTagAndHead(tag);
        String body = processBody(type_high, contents);
        if (CONFIG.mLog2ConsoleSwitch && type_low >= CONFIG.mConsoleFilter && type_high != FILE) {
            print2Console(type_low, tagHead.tag, tagHead.consoleHead, body);
        }
        if ((CONFIG.mLog2FileSwitch || type_high == FILE) && type_low >= CONFIG.mFileFilter) {
            print2File(type_low, tagHead.tag, tagHead.fileHead + body);
        }
    }

    /**
     * 解析标签和日志头
     *
     * @param tag 标签
     * @return 解析结果
     */
    private static TagHead processTagAndHead(String tag) {
        if (!CONFIG.mTagIsSpace && !CONFIG.mLogHeadSwitch) {
            tag = CONFIG.mGlobalTag;
        } else {
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            final int stackIndex = 3 + CONFIG.mStackOffset;
            if (stackIndex >= stackTrace.length) {
                StackTraceElement targetElement = stackTrace[3];
                final String fileName = getFileName(targetElement);
                if (CONFIG.mTagIsSpace && isSpace(tag)) {
                    int index = fileName.indexOf('.');// Use proguard may not find '.'.
                    tag = index == -1 ? fileName : fileName.substring(0, index);
                }
                return new TagHead(tag, null, ": ");
            }
            StackTraceElement targetElement = stackTrace[stackIndex];
            final String fileName = getFileName(targetElement);
            if (CONFIG.mTagIsSpace && isSpace(tag)) {
                int index = fileName.indexOf('.');// Use proguard may not find '.'.
                tag = index == -1 ? fileName : fileName.substring(0, index);
            }
            if (CONFIG.mLogHeadSwitch) {
                String tName = Thread.currentThread().getName();
                final String head = new Formatter()
                        .format("%s, %s.%s(%s:%d)",
                                tName,
                                targetElement.getClassName(),
                                targetElement.getMethodName(),
                                fileName,
                                targetElement.getLineNumber())
                        .toString();
                final String fileHead = " [" + head + "]: ";
                if (CONFIG.mStackDeep <= 1) {
                    return new TagHead(tag, new String[]{head}, fileHead);
                } else {
                    final String[] consoleHead =
                            new String[Math.min(
                                    CONFIG.mStackDeep,
                                    stackTrace.length - stackIndex
                            )];
                    consoleHead[0] = head;
                    int spaceLen = tName.length() + 2;
                    String space = new Formatter().format("%" + spaceLen + "s", "").toString();
                    for (int i = 1, len = consoleHead.length; i < len; ++i) {
                        targetElement = stackTrace[i + stackIndex];
                        consoleHead[i] = new Formatter()
                                .format("%s%s.%s(%s:%d)",
                                        space,
                                        targetElement.getClassName(),
                                        targetElement.getMethodName(),
                                        getFileName(targetElement),
                                        targetElement.getLineNumber())
                                .toString();
                    }
                    return new TagHead(tag, consoleHead, fileHead);
                }
            }
        }
        return new TagHead(tag, null, ": ");
    }

    /**
     * 按日志栈获取文件名
     *
     * @param targetElement 栈节点
     * @return 文件名
     */
    private static String getFileName(final StackTraceElement targetElement) {
        String fileName = targetElement.getFileName();
        if (fileName != null) return fileName;
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }
        int index = className.indexOf('$');
        if (index != -1) {
            className = className.substring(0, index);
        }
        return className + ".java";
    }

    /**
     * 解析日志内容
     *
     * @param type     类型
     * @param contents 内容
     * @return 日志内容
     */
    private static String processBody(final int type, final Object... contents) {
        String body = NULL;
        if (contents != null) {
            if (contents.length == 1) {
                Object object = contents[0];
                if (object != null) body = object.toString();
                if (type == JSON) {
                    body = formatJson(body);
                } else if (type == XML) {
                    body = formatXml(body);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, len = contents.length; i < len; ++i) {
                    Object content = contents[i];
                    sb.append(ARGS)
                            .append("[")
                            .append(i)
                            .append("]")
                            .append(" = ")
                            .append(content == null ? NULL : content.toString())
                            .append(LINE_SEP);
                }
                body = sb.toString();
            }
        }
        return body.length() == 0 ? NOTHING : body;
    }

    /**
     * 格式化json
     *
     * @param json 原始json
     * @return 格式化后的json
     */
    private static String formatJson(String json) {
        try {
            if (json.startsWith("{")) {
                json = new JSONObject(json).toString(4);
            } else if (json.startsWith("[")) {
                json = new JSONArray(json).toString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 格式化xml
     *
     * @param xml 原始xml
     * @return 格式化后的xml
     */
    private static String formatXml(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * 打印到控制台
     *
     * @param type 类型
     * @param tag  标签
     * @param head 头部
     * @param msg  内容
     */
    private static void print2Console(final int type, final String tag, final String[] head, final String msg) {
        if (CONFIG.mSingleTagSwitch) {
            StringBuilder sb = new StringBuilder();
            sb.append(PLACEHOLDER).append(LINE_SEP);
            if (CONFIG.mLogBorderSwitch) {
                sb.append(TOP_BORDER).append(LINE_SEP);
                if (head != null) {
                    for (String aHead : head) {
                        sb.append(LEFT_BORDER).append(aHead).append(LINE_SEP);
                    }
                    sb.append(MIDDLE_BORDER).append(LINE_SEP);
                }
                for (String line : msg.split(LINE_SEP)) {
                    sb.append(LEFT_BORDER).append(line).append(LINE_SEP);
                }
                sb.append(BOTTOM_BORDER);
            } else {
                if (head != null) {
                    for (String aHead : head) {
                        sb.append(aHead).append(LINE_SEP);
                    }
                }
                sb.append(msg);
            }
            printMsgSingleTag(type, tag, sb.toString());
        } else {
            printBorder(type, tag, true);
            printHead(type, tag, head);
            printMsg(type, tag, msg);
            printBorder(type, tag, false);
        }
    }

    /**
     * 打印边框
     *
     * @param type  类型
     * @param tag   标签
     * @param isTop 是否是顶部
     */
    private static void printBorder(final int type, final String tag, boolean isTop) {
        if (CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, isTop ? TOP_BORDER : BOTTOM_BORDER);
        }
    }

    /**
     * 打印头部
     *
     * @param type 类型
     * @param tag  标签
     * @param head 头部
     */
    private static void printHead(final int type, final String tag, final String[] head) {
        if (head != null) {
            for (String aHead : head) {
                Log.println(type, tag, CONFIG.mLogBorderSwitch ? LEFT_BORDER + aHead : aHead);
            }
            if (CONFIG.mLogBorderSwitch) Log.println(type, tag, MIDDLE_BORDER);
        }
    }

    /**
     * 打印具体内容
     *
     * @param type 类型
     * @param tag  标签
     * @param msg  内容
     */
    private static void printMsg(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                printSubMsg(type, tag, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                printSubMsg(type, tag, msg.substring(index, len));
            }
        } else {
            printSubMsg(type, tag, msg);
        }
    }

    /**
     * 单标签打印
     *
     * @param type 类型
     * @param tag  标签
     * @param msg  内容
     */
    private static void printMsgSingleTag(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            if (CONFIG.mLogBorderSwitch) {
                Log.println(type, tag, msg.substring(0, MAX_LEN) + LINE_SEP + BOTTOM_BORDER);
                int index = MAX_LEN;
                for (int i = 1; i < countOfSub; i++) {
                    Log.println(type, tag, PLACEHOLDER + LINE_SEP + TOP_BORDER + LINE_SEP + LEFT_BORDER + msg.substring(index, index + MAX_LEN) + LINE_SEP + BOTTOM_BORDER);
                    index += MAX_LEN;
                }
                if (index != len) {
                    Log.println(type, tag, PLACEHOLDER + LINE_SEP + TOP_BORDER + LINE_SEP + LEFT_BORDER + msg.substring(index, len));
                }
            } else {
                int index = 0;
                for (int i = 0; i < countOfSub; i++) {
                    Log.println(type, tag, msg.substring(index, index + MAX_LEN));
                    index += MAX_LEN;
                }
                if (index != len) {
                    Log.println(type, tag, msg.substring(index, len));
                }
            }
        } else {
            Log.println(type, tag, msg);
        }
    }

    /**
     * 打印子内容
     *
     * @param type 类型
     * @param tag  标签
     * @param msg  内容
     */
    private static void printSubMsg(final int type, final String tag, final String msg) {
        if (!CONFIG.mLogBorderSwitch) {
            Log.println(type, tag, msg);
            return;
        }
        StringBuilder sb = new StringBuilder();
        String[] lines = msg.split(LINE_SEP);
        for (String line : lines) {
            Log.println(type, tag, LEFT_BORDER + line);
        }
    }

    /**
     * 打印到文件
     *
     * @param type 类型
     * @param tag  标签
     * @param msg  内容
     */
    private static void print2File(final int type, final String tag, final String msg) {
        Date now = new Date(System.currentTimeMillis());
        String format = FORMAT.format(now);
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath = (CONFIG.mDir == null ? CONFIG.mDefaultDir : CONFIG.mDir) + CONFIG.mFilePrefix + "-" + date + ".txt";
        if (!createOrExistsFile(fullPath)) {
            Log.e("LogUtils", "create " + fullPath + " failed!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(time)
                .append(T[type - V])
                .append("/")
                .append(tag)
                .append(msg)
                .append(LINE_SEP);
        final String content = sb.toString();
        input2File(content, fullPath);
    }

    /**
     * 检查文件是否存在，不存在则创建
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    private static boolean createOrExistsFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            boolean isCreate = file.createNewFile();
            if (isCreate) printDeviceInfo(filePath);
            return isCreate;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打印设备信息
     *
     * @param filePath 文件路径
     */
    private static void printDeviceInfo(final String filePath) {
        String versionName = "";
        int versionCode = 0;
        try {
            PackageInfo pi = CONFIG.mApp.getPackageManager().getPackageInfo(CONFIG.mApp.getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String time = filePath.substring(filePath.length() - 9, filePath.length() - 4);
        final String head = "************* Log Head ****************" +
                "\nDate of Log        : " + time +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +
                "\nDevice Model       : " + Build.MODEL +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Log Head ****************\n\n";
        input2File(head, filePath);
    }

    /**
     * 检查目录是否存在，不存在则创建
     *
     * @param file 目录路径
     * @return 是否存在
     */
    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 检查字符串是否是空格字符串
     *
     * @param s 原始字符串
     * @return 是否是空格字符串
     */
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 输入到文件
     *
     * @param input    输入内容
     * @param filePath 文件路径
     */
    private static void input2File(final String input, final String filePath) {
        if (sExecutor == null) {
            sExecutor = Executors.newSingleThreadExecutor();
        }
        Future<Boolean> submit = sExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(filePath, true));
                    bw.write(input);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            if (submit.get()) return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.e("LogUtils", "log to " + filePath + " failed!");
    }

    /**
     * 配置
     */
    public static class Config {
        private Application mApp;
        private String mDefaultDir;
        private String mDir;
        private String mFilePrefix = "util";
        private boolean mLogSwitch = true;
        private boolean mLog2ConsoleSwitch = true;
        private String mGlobalTag = null;
        private boolean mTagIsSpace = true;
        private boolean mLogHeadSwitch = true;
        private boolean mLog2FileSwitch = false;
        private boolean mLogBorderSwitch = true;
        private boolean mSingleTagSwitch = true;
        private int mConsoleFilter = V;
        private int mFileFilter = V;
        private int mStackDeep = 1;
        private int mStackOffset = 0;

        private Config() {
        }

        /**
         * 设置当前Application
         *
         * @param app Application
         * @return 当前配置
         */
        public Config setApp(Application app) {
            mApp = app;
            if (mDefaultDir != null) return this;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && mApp.getExternalCacheDir() != null)
                mDefaultDir = mApp.getExternalCacheDir() + FILE_SEP + "log" + FILE_SEP;
            else {
                mDefaultDir = mApp.getCacheDir() + FILE_SEP + "log" + FILE_SEP;
            }
            return this;
        }

        /**
         * 设置日志开关
         *
         * @param logSwitch 是否打印
         * @return 当前对象
         */
        public Config setLogSwitch(final boolean logSwitch) {
            mLogSwitch = logSwitch;
            return this;
        }

        /**
         * 设置答应到控制台开关
         *
         * @param consoleSwitch 是否打印
         * @return 当前对象
         */
        public Config setConsoleSwitch(final boolean consoleSwitch) {
            mLog2ConsoleSwitch = consoleSwitch;
            return this;
        }

        /**
         * 设置全局标签
         *
         * @param tag 标签
         * @return 当前对象
         */
        public Config setGlobalTag(final String tag) {
            if (isSpace(tag)) {
                mGlobalTag = "";
                mTagIsSpace = true;
            } else {
                mGlobalTag = tag;
                mTagIsSpace = false;
            }
            return this;
        }

        /**
         * 设置打印头部的开关
         *
         * @param logHeadSwitch 是否打印
         * @return 当前对象
         */
        public Config setLogHeadSwitch(final boolean logHeadSwitch) {
            mLogHeadSwitch = logHeadSwitch;
            return this;
        }

        /**
         * 设置打印到文件开关
         *
         * @param log2FileSwitch 是否打印
         * @return 当前对象
         */
        public Config setLog2FileSwitch(final boolean log2FileSwitch) {
            mLog2FileSwitch = log2FileSwitch;
            return this;
        }

        /**
         * 设置目录
         *
         * @param dir 目录路径
         * @return 当前对象
         */
        public Config setDir(final String dir) {
            if (isSpace(dir)) {
                mDir = null;
            } else {
                mDir = dir.endsWith(FILE_SEP) ? dir : dir + FILE_SEP;
            }
            return this;
        }

        /**
         * 设置目录
         *
         * @param dir 目录对象
         * @return 当前对象
         */
        public Config setDir(final File dir) {
            mDir = dir == null ? null : dir.getAbsolutePath() + FILE_SEP;
            return this;
        }

        /**
         * 设置目录前缀
         *
         * @param filePrefix 前缀
         * @return 当前对象
         */
        public Config setFilePrefix(final String filePrefix) {
            if (isSpace(filePrefix)) {
                mFilePrefix = "util";
            } else {
                mFilePrefix = filePrefix;
            }
            return this;
        }

        /**
         * 设置打印边框的开关
         *
         * @param borderSwitch 是否打印
         * @return 当前对象
         */
        public Config setBorderSwitch(final boolean borderSwitch) {
            mLogBorderSwitch = borderSwitch;
            return this;
        }

        /**
         * 设置打印标签的开关
         *
         * @param singleTagSwitch 是否打印
         * @return 当前对象
         */
        public Config setSingleTagSwitch(final boolean singleTagSwitch) {
            mSingleTagSwitch = singleTagSwitch;
            return this;
        }

        /**
         * 设置控制台过滤器
         *
         * @param consoleFilter 过滤器
         * @return 当前对象
         */
        public Config setConsoleFilter(final int consoleFilter) {
            mConsoleFilter = consoleFilter;
            return this;
        }

        /**
         * 设置文件过滤器
         *
         * @param fileFilter 过滤器
         * @return 当前对象
         */
        public Config setFileFilter(final int fileFilter) {
            mFileFilter = fileFilter;
            return this;
        }

        /**
         * 设置日志栈深度
         *
         * @param stackDeep 深度
         * @return 当前对象
         */
        public Config setStackDeep(@IntRange(from = 1) final int stackDeep) {
            mStackDeep = stackDeep;
            return this;
        }

        /**
         * 设置日志层级
         *
         * @param stackOffset 层级
         * @return 当前对象
         */
        public Config setStackOffset(@IntRange(from = 0) final int stackOffset) {
            mStackOffset = stackOffset;
            return this;
        }

        @Override
        public String toString() {
            return "switch: " + mLogSwitch
                    + LINE_SEP + "console: " + mLog2ConsoleSwitch
                    + LINE_SEP + "tag: " + (mTagIsSpace ? "null" : mGlobalTag)
                    + LINE_SEP + "head: " + mLogHeadSwitch
                    + LINE_SEP + "file: " + mLog2FileSwitch
                    + LINE_SEP + "dir: " + (mDir == null ? mDefaultDir : mDir)
                    + LINE_SEP + "filePrefix: " + mFilePrefix
                    + LINE_SEP + "border: " + mLogBorderSwitch
                    + LINE_SEP + "singleTag: " + mSingleTagSwitch
                    + LINE_SEP + "consoleFilter: " + T[mConsoleFilter - V]
                    + LINE_SEP + "fileFilter: " + T[mFileFilter - V]
                    + LINE_SEP + "stackDeep: " + mStackDeep
                    + LINE_SEP + "mStackOffset: " + mStackOffset;
        }
    }

    /**
     * 日志头部
     */
    private static class TagHead {
        String tag;
        String[] consoleHead;
        String fileHead;

        /**
         * 构造函数
         *
         * @param tag         标签
         * @param consoleHead 控制台头部
         * @param fileHead    文件头部
         */
        TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = tag;
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }
    }
}
