package com.dylangolan3sys.heavycoredetector;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeavyCoreDetectorMod implements ModInitializer {
    public static final String MOD_ID = "heavycoredetector";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Heavy Core Detector initialized!");
    }
}