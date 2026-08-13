package net.thanachot.autoiteminactionbar;

import net.fabricmc.api.ModInitializer;
import net.thanachot.autoiteminactionbar.refill.RefillCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AutoItemInActionbar implements ModInitializer {
    public static final String MOD_ID = "autoiteminactionbar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        RefillCoordinator.initialize();
        LOGGER.info("AutoItemInActionbar initialized");
    }
}
