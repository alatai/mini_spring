package com.alatai.mini.core.env;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 20:44
 */
public interface Environment extends PropertyResolver {

	String[] getActiveProfiles();

	String[] getDefaultProfiles();

	boolean acceptsProfiles(String... profiles);
}
