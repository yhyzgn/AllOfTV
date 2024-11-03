package com.yhy.all.of.tv.parse;

import com.yhy.all.of.tv.chan.Chan;
import com.yhy.all.of.tv.internal.Lists;
import com.yhy.all.of.tv.parse.of.CKParser;
import com.yhy.all.of.tv.parse.of.XiaMiParser;
import com.yhy.all.of.tv.parse.of.PMParser;
import com.yhy.all.of.tv.parse.of.YeMuParser;
import com.yhy.all.of.tv.parse.of.M3U8TVParser;
import com.yhy.all.of.tv.parse.of.G8090Parser;
import com.yhy.all.of.tv.parse.of.NnxvParser;
import com.yhy.all.of.tv.parse.of.PanGuParser;
import com.yhy.all.of.tv.parse.of.BingDouParser;

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
            new YeMuParser(),
            new CKParser(),
            new G8090Parser(),
            new XiaMiParser(),
            new PanGuParser(),
            new BingDouParser(),
            new NnxvParser(),
            new PMParser(),
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
