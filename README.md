# SmartConfiguration
It's a simple class for create *JSON* configurations files Minecraft.
## How to use

### Installation

**Manual**

Copy `SmartConfiguration.java` in your Plugin.

## Use SmartConfiguration
#### Setup.
Register SmartConfiguration in your Minecraft plugin.

Put `SmartConfiguration.register(this)` in your `onEnable()`.

```java
@Override
public void onEnable() {
    SmartConfiguration.register(this);
}
```

#### Create serializable Java class.
Create Java class than extends of SmartConfiguration.

In constructor initialize default values when *JSON* file is created.

Use `update(this)` method in setters for automatically update *JSON* file. 

```java
public class ConfigurationExample extends SmartConfiguration<ConfigurationExample>{
    private boolean pvpMode;
    private String mapName;
    private List<String> questList;

    public ConfigurationExample() {
        super("config.json", ConfigurationExample.class);
        this.pvpMode = true;
        this.mapName = "default-world";
        this.questList = new ArrayList<>();
    }

    public boolean isPvpMode() {
        return pvpMode;
    }

    public String getMapName() {
        return mapName;
    }

    public List<String> getQuestList() {
        return questList;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
        update(this);
    }
}
```
#### Load configuration.
Put in variable `new ConfigurationExample().load()` in your `onEnable()`.

And now you can use simply your configuration with your getters and setters.

```java
private ConfigurationExample configuration;

@Override
public void onEnable() {
    SmartConfiguration.register(this);
    this.configuration = new ConfigurationExample().load();
}
```

#### Result in *JSON*.
```json
{
  "pvpMode": true,
  "mapName": "default-world",
  "questList": []
}
```



