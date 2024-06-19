package xyz.brassgoggledcoders.shadyskies.dataregistering.util;

import org.codehaus.plexus.util.StringUtils;

public class StringHelper {
    public static String createName(String registryPath) {
        return StringUtils.capitaliseAllWords(registryPath.replace("/", " ")
                .replace("_", " ")
        );
    }
}
