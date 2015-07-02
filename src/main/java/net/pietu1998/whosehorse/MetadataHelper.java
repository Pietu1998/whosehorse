package net.pietu1998.whosehorse;

import java.util.List;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

public class MetadataHelper {

	private MetadataHelper() {}

	public static void setMetadata(Metadatable object, String key, Object value, Plugin plugin) {
		object.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static Object getMetadata(Metadatable object, String key, Plugin plugin) {
		return getMetadata(object, key, plugin, null);
	}

	public static Object getMetadata(Metadatable object, String key, Plugin plugin, Object def) {
		List<MetadataValue> values = object.getMetadata(key);
		for (MetadataValue value : values) {
			if (value.getOwningPlugin() == plugin)
				return value.value();
		}
		return def;
	}

}
