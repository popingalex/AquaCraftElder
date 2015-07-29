package org.aqua.parse;

import org.aqua.parse.MarkupLanguage.DataObject;
import org.aqua.parse.MarkupLanguage.Language;

import test.Tester;

public class MarkupLanguageTest extends Tester{

    @Override
    public void test(String[] args) {
        // TODO Auto-generated method stub
        DataObject o = MarkupLanguage.getDataObject(null, Language.YAML);
    }

}
