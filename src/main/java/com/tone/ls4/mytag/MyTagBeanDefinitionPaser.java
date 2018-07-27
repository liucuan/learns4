package com.tone.ls4.mytag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class MyTagBeanDefinitionPaser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MyTag.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String myTagId = element.getAttribute("id");
        String myTagName = element.getAttribute("name");
        if (StringUtils.hasText(myTagId)) {
            builder.addPropertyValue("id", myTagId);
        }
        if (StringUtils.hasText(myTagName)) {
            builder.addPropertyValue("name", myTagName);
        }
    }
}
