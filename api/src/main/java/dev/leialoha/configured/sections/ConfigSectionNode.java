package dev.leialoha.configured.sections;

import java.util.Map;

import dev.leialoha.configured.values.ConfigValue;

public class ConfigSectionNode {

    protected String identifier;
    protected Map<String, ConfigValue<?>> values;
    protected Map<String, ConfigSectionNode> subSections;
    protected Map<String, String[]> comments;

    // TODO: void addValue(ConfigValue<?> value)
    // TODO: ConfigValue<?> getValue(String key)
    // TODO: void removeValue(String key)
    // TODO: void addSubSection(SubConfigSection section)
    // TODO: SubConfigSection getSubSection(String name)
    // TODO: boolean containsKey(String key)
    // TODO: boolean containsSubSection(String name)

    // TODO: String[] getComment(String key)
    // TODO: void setComment(String key, String[] comments)

}
