package org.aqua.parse.cli;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

public class CliParser {
    private Options             options;
    private Map<String, String> values;
    private String              tips;

    public CliParser(String tips) {
        this.tips = null == tips ? "nothing!" : tips;
        this.values = new HashMap<String, String>();
        this.options = new Options();
    }

    public CliParser addOption(String shortOpt, boolean withArg, String info) {
        return addOption(shortOpt, null, withArg, info);
    }

    public CliParser addOption(String shortOpt, String longOpt, boolean withArg, String info) {
        options.addOption(shortOpt, longOpt, withArg, info);
        return this;
    }

    public Map<String, String> parse(String[] args) {
        try {
            CommandLine line = new BasicParser().parse(options, args);
            for (Option option : line.getOptions()) {
                values.put(option.getOpt(), iterate(option.getOpt().charAt(0), option.getLongOpt(), option.getValue()));
            }
            return values;
        } catch (UnrecognizedOptionException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printHelp() {
        HelpFormatter helper = new HelpFormatter();
        helper.printHelp(tips, options);
    }

    public String iterate(char shortOpt, String longOpt, String value) {
        return value;
    }
}
