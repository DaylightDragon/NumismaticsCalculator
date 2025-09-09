package org.daylight.coinscalculator.config;

import net.minecraftforge.fml.loading.FMLPaths;
//import org.yaml.snakeyaml.DumperOptions;
//import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*public class ConfigHandler2 {
    private static final File configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "numismatics_calculator/main_config.yml");
    private static final Yaml yaml;

    static {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
    }

    public static void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
//                config = yaml.load()
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
//            yaml.dump(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
