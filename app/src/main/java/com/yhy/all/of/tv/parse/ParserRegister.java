package com.yhy.all.of.tv.parse;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.of.CKParser;
import com.yhy.all.of.tv.parse.of.Hold8Parser;
import com.yhy.all.of.tv.parse.of.JYParser;
import com.yhy.all.of.tv.parse.of.JsonPlayerParser;
import com.yhy.all.of.tv.parse.of.M3U8TVParser;
import com.yhy.all.of.tv.parse.of.NuoXunParser;
import com.yhy.all.of.tv.parse.of.OKParser;
import com.yhy.all.of.tv.parse.of.PanGuParser;
import com.yhy.all.of.tv.parse.of.Yun17Parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on 2023-04-13 22:08
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParserRegister {
    public final static ParserRegister instance = new ParserRegister();

    private final List<Parser> parserList;

    private ParserRegister() {
        parserList = Lists.of(
            new JsonPlayerParser(),
            new CKParser(),
            new NuoXunParser(),
            new Hold8Parser(),
            new PanGuParser(),
            new Yun17Parser(),
            new OKParser(),
            new JYParser(),
            new M3U8TVParser()
        );
    }

    public int indexOfSupported(Parser parser, String chanName) {
        for (int i = 0; i < parserList.size(); i++) {
            if (Objects.equals(parser.name(), parserList.get(i).name())) {
                return i;
            }
        }
        return -1;
    }

    public List<Parser> allOfParserList() {
        return parserList;
    }

    public List<Parser> supportedParserList(Chan chan) {
        return supportedParserList(chan.name());
    }

    public List<Parser> supportedParserList(String chanName) {
        return parserList.stream().filter(it -> it.supportedChanList().stream().anyMatch(c -> Objects.equals(chanName, c.name()))).collect(Collectors.toList());
    }

    public Parser findSupported(String parserName, String chanName) {
        return supportedParserList(chanName).stream().filter(it -> Objects.equals(it.name(), parserName)).findFirst().orElse(parserList.get(0));
    }
}
