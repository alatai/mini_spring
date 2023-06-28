package com.alatai.mini.context;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/06/28 23:12
 */
public class ContextRefreshEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source the object on which the Event initially occurred
	 * @throws IllegalArgumentException if source is null
	 */
	public ContextRefreshEvent(Object source) {
		super(source);
	}

	@Override
	public String toString() {
		return this.msg;
	}
}
