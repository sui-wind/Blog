package com.tlc.blog.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.ext.gfm.tables.TablesExtension;

import java.util.*;

/**
 * 实现 Markdown 转换为 HTML
 */
public class MarkdownUtils {

    /**
     * markdown 格式装换为HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
    }

    /**
     * 增加扩展（标题描点，表格生成）
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {

        // h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        // 装换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    public static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            // 改变a标签的target属性为_blank
            if (node instanceof Link) {
                map.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                map.put("class", "ui cell table");
            }
        }
    }
}
