package com.alatai.mini.web;

/**
 * 对应 minisMVC-servlet.xml 中标签的属性
 *
 * @author saihou
 * @version 1.0
 * @date 2023/10/04 23:07
 */
public class MappingValue {

    private String uri;

    private String clz;

    private String method;

    public MappingValue() {
    }

    /**
     * 含参构造器
     *
     * @param uri    请求uri
     * @param clz    对应class
     * @param method 请求方法
     */
    public MappingValue(String uri, String clz, String method) {
        this.uri = uri;
        this.clz = clz;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
