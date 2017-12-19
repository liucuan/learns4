package com.tone.ls4.mytag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyTagNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("mytag", new MyTagBeanDefinitionPaser());
    }
}
