package org.wulfnoth.blank;

import org.junit.Before;
import org.junit.Test;

public class WBlankFactoryTest {

	private WBlank wBlank;

	@Before
	public void init() {
		wBlank = WBlankFactory.getWBlank(WBlankFactoryTest.class);
	}

	@Test
	public void log() {
		wBlank.info("This is the second test for skt blank.");
	}

}
