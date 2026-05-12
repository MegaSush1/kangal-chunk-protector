package kangal.chunk_protector;

import kangal.chunk_protector.protection.ProtectionEvents;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kangal_chunk_protector implements ModInitializer {

	public static final String MOD_ID = "kangalchunkprotector";
	public static final Logger MOD_LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ProtectionEvents.register();

	}
}