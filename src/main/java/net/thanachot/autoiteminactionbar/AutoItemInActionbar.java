package net.thanachot.autoiteminactionbar;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoItemInActionbar implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("autoiteminactionbar");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello from AutoItemInActionbar!");
	}
}
