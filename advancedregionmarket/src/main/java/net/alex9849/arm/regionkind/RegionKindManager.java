package net.alex9849.arm.regionkind;

import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionKindManager extends YamlFileManager<RegionKind> {

    public RegionKindManager(File savepath) {
        super(savepath);
    }

    @Override
    public List<RegionKind> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<RegionKind> regionKindList = new ArrayList<>();

        yamlConfiguration.options().copyDefaults(true);

        if(yamlConfiguration.get("DefaultRegionKind") != null) {
            ConfigurationSection defaultRkConfig = yamlConfiguration.getConfigurationSection("DefaultRegionKind");
            updateDefaults(defaultRkConfig);
            RegionKind defaultRk = RegionKind.parse(defaultRkConfig, "Default");
            RegionKind.DEFAULT.setName(defaultRk.getRawDisplayName());
            RegionKind.DEFAULT.setMaterial(defaultRk.getMaterial());
            RegionKind.DEFAULT.setDisplayInGUI(defaultRk.isDisplayInGUI());
            RegionKind.DEFAULT.setDisplayInLimits(defaultRk.isDisplayInLimits());
            RegionKind.DEFAULT.setPaybackPercentage(defaultRk.getPaybackPercentage());
            RegionKind.DEFAULT.setLore(defaultRk.getRawLore());
        }

        if(yamlConfiguration.get("SubregionRegionKind") != null) {
            ConfigurationSection subregionRkConfig = yamlConfiguration.getConfigurationSection("SubregionRegionKind");
            updateDefaults(subregionRkConfig);
            RegionKind subregionRk = RegionKind.parse(subregionRkConfig, "Subregion");
            RegionKind.SUBREGION.setName(subregionRk.getRawDisplayName());
            RegionKind.SUBREGION.setMaterial(subregionRk.getMaterial());
            RegionKind.SUBREGION.setDisplayInGUI(subregionRk.isDisplayInGUI());
            RegionKind.SUBREGION.setDisplayInLimits(subregionRk.isDisplayInLimits());
            RegionKind.SUBREGION.setPaybackPercentage(subregionRk.getPaybackPercentage());
            RegionKind.SUBREGION.setLore(subregionRk.getRawLore());
        }

        if(yamlConfiguration.get("RegionKinds") != null) {
            ConfigurationSection regionKindsSection = yamlConfiguration.getConfigurationSection("RegionKinds");
            List<String> regionKinds = new ArrayList<>(regionKindsSection.getKeys(false));
            if(regionKinds != null) {
                for(String regionKindID : regionKinds) {
                    if(regionKindsSection.get(regionKindID) != null) {
                        ConfigurationSection rkConfSection = regionKindsSection.getConfigurationSection(regionKindID);
                        if(rkConfSection != null) {
                            updateDefaults(rkConfSection);
                            regionKindList.add(RegionKind.parse(rkConfSection, regionKindID));
                        }
                    }
                }
            }
        }
        this.saveFile();
        yamlConfiguration.options().copyDefaults(false);
        return regionKindList;
    }

    @Override
    public void saveObjectToYamlObject(RegionKind regionKind, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("RegionKinds." + regionKind.getName(), regionKind.toConfigureationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultRegionKind", RegionKind.DEFAULT.toConfigureationSection());
        yamlConfiguration.set("SubregionRegionKind", RegionKind.SUBREGION.toConfigureationSection());
    }

    public List<String> completeTabRegionKinds(String arg, String prefix) {
        List<String> returnme = new ArrayList<>();

        List<RegionKind> regionKinds = this.getObjectListCopy();

        for (RegionKind regionkind : regionKinds) {
            if ((prefix + regionkind.getName()).toLowerCase().startsWith(arg)) {
                returnme.add(prefix + regionkind.getName());
            }
        }
        if ((prefix + "default").startsWith(arg)) {
            returnme.add(prefix + "default");
        }
        if ((prefix + "subregion").startsWith(arg)) {
            returnme.add(prefix + "subregion");
        }

        return returnme;
    }

    public boolean kindExists(String kind){

        List<RegionKind> regionKinds = this.getObjectListCopy();
        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(kind)){
                return true;
            }
        }

        if(kind.equalsIgnoreCase("default")) {
            return true;
        }
        if(kind.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())){
            return true;
        }
        if(kind.equalsIgnoreCase("subregion")) {
            return true;
        }
        if(kind.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())){
            return true;
        }
        return false;
    }

    public RegionKind getRegionKind(String name){

        List<RegionKind> regionKinds = this.getObjectListCopy();
        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(name)){
                return regionKind;
            }
        }

        if(name.equalsIgnoreCase("default") || name.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())){
            return RegionKind.DEFAULT;
        }
        if(name.equalsIgnoreCase("subregion") || name.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())){
            return RegionKind.SUBREGION;
        }
        return null;
    }

    private void updateDefaults(ConfigurationSection section) {
        section.addDefault("item", "RED_BED");
        section.addDefault("displayName", "Default Displayname");
        section.addDefault("displayInLimits", true);
        section.addDefault("displayInGUI", true);
        section.addDefault("paypackPercentage", 0d);
        section.addDefault("lore", new ArrayList<String>());
    }

    public List<String> tabCompleteRegionKind(String arg) {
        List<RegionKind> regionKinds = this.getObjectListCopy();
        List<String> returnme = new ArrayList<>();

        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(arg)) {
                returnme.add(regionKind.getName());
            }
        }
        return returnme;
    }
}
